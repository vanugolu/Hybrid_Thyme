package com.aims;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aims.constants.PublicConstants;
import com.aims.model.TestCaseData;
import com.aims.model.TestCaseReportDisplay;
import com.aims.model.TestStepData;
import com.aims.model.TestStepInputData;
import com.aims.model.TestStepReportDisplay;
import com.aims.util.ReportsUtil;
import com.aims.xls.ExcelOperations;

public class Controller extends Keywords{
	protected String moduleName;
	protected String controllerFileName;
	protected String testDataFileName;
	protected boolean captureScreenShot;
	protected int totalTestStepCount;
	protected int completedTestStepCount;
	protected TestCaseData testCaseData;
	protected Date startTime;
	protected Date endTime;
	protected int priority;
	protected Map<Integer, List<ITestResult>> executedTestStepResults = new LinkedHashMap<>();
	private String moduleReportDirectory;
	
	public Controller(TestCaseData testCaseData) {
		this.testCaseData = testCaseData;
	}
	
	@BeforeClass
	@Parameters({ "moduleName", "controllerFileName", "testDataFileName" })
	public void beforeClass(ITestContext context, String moduleName, String controllerFileName, String testDataFileName) {
		this.suiteParameters = context.getSuite().getXmlSuite().getParameters();
		this.moduleParameters = context.getCurrentXmlTest().getLocalParameters();
		startTime = new Date();
		this.moduleName = moduleName;
		this.capabilities = (Capabilities) context.getAttribute(PublicConstants.CAPABILITIES);
		this.controllerFileName = controllerFileName;
		this.testDataFileName = testDataFileName;
		log = Logger.getLogger(moduleName + "_" + capabilities.getBrowserName());
		this.moduleReportDirectory = (String) context.getAttribute(PublicConstants.MODULE_PATH);
		this.captureScreenShot= Boolean.valueOf(suiteParameters.get(PublicConstants.CAPTURE_SCREEN_SHOT));
		this.hubURL = suiteParameters.get(PublicConstants.HUB_URL);
	}

	public String getModuleName() {
		return moduleName;
	}

	public String getControllerFileName() {
		return controllerFileName;
	}

	public String getTestDataFileName() {
		return testDataFileName;
	}

	@DataProvider(name = "testStepsData")
	public Object[][] testStepsData(ITestContext context) throws URISyntaxException {
		List<TestStepData> testStepData = fetchTestStepData();
		if (CollectionUtils.isNotEmpty(testStepData)) {
			Object[][] testStepDataArray = new Object[testStepData.size()][1];
			int i = 0;
			for (TestStepData testStep : testStepData) {
				testStepDataArray[i++][0] = testStep;
			}
			return testStepDataArray;
		}
		return new Object[0][0];
	}
	
	private List<TestStepData> fetchTestStepData() throws URISyntaxException {
		File controllerFile = Paths.get(this.getClass().getResource("/modules/" + moduleName + "/" + controllerFileName).toURI()).toFile();
		
		if (!controllerFile.exists())
		{
			IllegalStateException exception = new IllegalStateException("Test case file could not be found");
			log.error(exception);
			throw exception;
		}
		ExcelOperations controllerExcelSheet = new ExcelOperations(controllerFile.getPath());
		List<TestStepData> testStepDataList = new ArrayList<>();
		if (controllerExcelSheet != null && controllerExcelSheet.isSheetExist(moduleName)) {
			if (testCaseData != null) {
				if (!testCaseData.isRunMode()) {
					return Collections.emptyList();
				}
				testStepDataList = fetchTestSteps(controllerExcelSheet);
			}
		}
		return testStepDataList;
	}

	private List<TestStepData> fetchTestSteps(ExcelOperations controllerExcelSheet) throws URISyntaxException {
		String testCaseId = testCaseData.getTcId();
		if (controllerExcelSheet.isSheetExist(testCaseId) && controllerExcelSheet.getRowCount(testCaseId) > 1) {
			File testDataFile = Paths.get(this.getClass().getResource("/modules/" + moduleName + "/" + testDataFileName).toURI()).toFile();
			List<TestStepData> testStepDataList = populateTestStepData(controllerExcelSheet, testCaseId);
			if (testDataFile.exists()) {
				ExcelOperations testDataExcelSheet = new ExcelOperations(testDataFile.getPath());
				if (testDataExcelSheet.isSheetExist(testCaseId) && testDataExcelSheet.getRowCount(testCaseId) > 1) {
					return populateTestStepInputData(testDataExcelSheet, testStepDataList, testCaseId);
				}
			}
			return testStepDataList;
		}
		return null;
		
	}


	
	private List<TestStepData> populateTestStepData(ExcelOperations controllerExcelSheet, String sheetName) {
		log.debug("sheet : " +  sheetName);
		List<TestStepData> testStepDataList = new ArrayList<>();
		for (int i=2; i <= controllerExcelSheet.getRowCount(sheetName); i++) {
			String testStepId = controllerExcelSheet.getCellData(sheetName, "TSID", i);
			if (StringUtils.isNotBlank(testStepId)) {
				TestStepData testStepData = new TestStepData();
				testStepData.setTestCaseId(testCaseData.getTcId());
				testStepData.setTestStepId(testStepId);
				testStepData.setRunMode(getRunMode(controllerExcelSheet.getCellData(sheetName, "Runmode", i)));
				testStepData.setDescription(controllerExcelSheet.getCellData(sheetName, "Description", i));
				testStepData.setKeyword(controllerExcelSheet.getCellData(sheetName, "Keyword", i));
				String object = controllerExcelSheet.getCellData(sheetName, "Object", i);
				testStepData.setObject(parseTestStepObject(object));
				String dataColumnName = controllerExcelSheet.getCellData(sheetName, "Data_Column_Name", i);
				List<TestStepInputData> stepDataInputList = parseStepDataInputColumnName(dataColumnName);
				testStepData.setTestStepInputData(stepDataInputList);
				testStepDataList.add(testStepData);
			}
		}
		return testStepDataList;
	}


	
	private List<TestStepData> populateTestStepInputData(ExcelOperations testDataExcelSheet, List<TestStepData> testStepDataList, String testCaseId) {
		List<TestStepData> testStepDataInputIterations = new ArrayList<>();
		int iterationCount = 1;
		for (int i=2; i <= testDataExcelSheet.getRowCount(testCaseId); i++) {
			for (TestStepData testStepData : testStepDataList) {
				TestStepData testStepDataClone = testStepData.clone();
				if (testStepDataClone != null) {
					testStepDataClone.setIterationCount(iterationCount);
					fillTestStepInputData(testStepDataClone, testDataExcelSheet, testCaseId, i);
					testStepDataInputIterations.add(testStepDataClone);
				}
			}
			iterationCount++;
		}
		return CollectionUtils.isNotEmpty(testStepDataInputIterations) ? testStepDataInputIterations : testStepDataList;
	}
	

	private void fillTestStepInputData(TestStepData testStepData, ExcelOperations testDataExcelSheet, String testCaseId, int dataSheetRow) {
		if (testStepData != null && CollectionUtils.isNotEmpty(testStepData.getTestStepInputData())) {
			for (TestStepInputData testStepInput : testStepData.getTestStepInputData()) {
				String testData = testDataExcelSheet.getCellData(testCaseId, testStepInput.getDataColumnName(), dataSheetRow);
				if (StringUtils.isNotBlank(testData)) {
					testStepInput.setDataCoulmnValue(testData);
				}
			}
		}
	}


	private List<String> parseTestStepObject(String object) {
		if (StringUtils.isNotBlank(object)) {
			String[] objectList = object.trim().split(",");
			return Arrays.asList(objectList).stream()
											.filter(element -> element != null)
											.collect(Collectors.toList());
		}
		return null;
	}

	private List<TestStepInputData> parseStepDataInputColumnName(String dataColumnName) {
		if (StringUtils.isNotBlank(dataColumnName)) {
			List<TestStepInputData> inputDataList = new ArrayList<>();
			String[] columnNames = dataColumnName.trim().split(",");
			for (String columnName : columnNames) {
				if (StringUtils.isNotBlank(columnName)) {
					TestStepInputData inputData = new TestStepInputData();
					inputData.setDataColumnName(columnName);
					inputDataList.add(inputData);
				}
			}
			return CollectionUtils.isNotEmpty(inputDataList) ? inputDataList : null;
		}
		return null;
	}

	private boolean getRunMode(String cellData) {
		if (StringUtils.isNotBlank(cellData)) {
			return "Y".equalsIgnoreCase(cellData.trim());
		}
		return true;
	}

	@Test(dataProvider = "testStepsData")
	public void beginTestCaseExecution(TestStepData testStepData) throws InterruptedException, IOException {
		boolean result = false;
		if (!testCaseData.isRunMode() || !testStepData.isRunMode()) {
			throw new SkipException("Test Case Skipped");
		}
		try {
			result = executeTest(testStepData);
		} catch (Exception e) {
			log.error(e);
		}
		Assert.assertTrue(result);
	}
	

	public boolean executeTest(TestStepData testStepData) throws InterruptedException, IOException {
		log.info(testStepData.getIterationCount());
		log.info(testStepData.getDescription());
		log.info(testStepData.getKeyword());
		String keyword = testStepData.getKeyword();
		if (StringUtils.isBlank(keyword)) {
			System.out.println("Keyword not available");
			return false;
		}
		try {

			Method method = this.getClass().getMethod(keyword, TestStepData.class);
			boolean result = (boolean) method.invoke(this, testStepData);
			if (result) {
				log.info("result is : "+ result );
				return result;
			}
			if (captureScreenShot) {
				String screenShotFolder = Paths.get(moduleReportDirectory, testCaseData.getTcId(), 
													Integer.toString(testStepData.getIterationCount())).toString();
				String screenShotFileName = testStepData.getTestStepId() + ".jpeg";
				ReportsUtil.takeScreenShot(screenShotFileName, driver, screenShotFolder, log);
				testStepData.setScreenShotPath(Paths.get(screenShotFolder, screenShotFileName).toString());
			}
		} catch (NoSuchMethodException nsme) {
			log.error(nsme);
			return false;
		} catch (Exception t) {
			log.error(t);
			return false;
		}
		return false;
	}

	@AfterMethod
	public void afterTestMethod(ITestResult result) {
		if (!testCaseData.isRunMode()) {
			return;
		}
		addResultIterationCountMap(result);
	}
	
	private void addResultIterationCountMap(ITestResult result) {
		TestStepData testStepData = (TestStepData) result.getParameters()[0];
		if (testStepData != null) {
			List<ITestResult> resultList = executedTestStepResults.get(testStepData.getIterationCount());
			if (CollectionUtils.isNotEmpty(resultList)) {
				resultList.add(result);
				return;
			}
			executedTestStepResults.put(testStepData.getIterationCount(), new ArrayList<>(Collections.singletonList(result)));
		}
	}

	public int getTotalTestStepCount() {
		return totalTestStepCount;
	}

	public int getCompletedTestStepCount() {
		return completedTestStepCount;
	}

	public TestCaseData getTestCaseData() {
		return testCaseData;
	}

	protected Date getStartTime() {
		return startTime;
	}

	protected Date getEndTime() {
		return endTime;
	}

	@AfterClass
	public void afterClass(ITestContext context) throws IOException {
		endTime = new Date();
		if (MapUtils.isNotEmpty(executedTestStepResults)) {
			for (int iterationCount : executedTestStepResults.keySet()) {
				generateTestStepReport(context, iterationCount);
			}
		} else {
			addTestCaseReportDisplayToContext(context, 1, false);
		}
	}

	private void generateTestStepReport(ITestContext context, int iterationCount) throws IOException {
		List<TestStepReportDisplay> testStepReportList = new ArrayList<TestStepReportDisplay>();
		Map<String, Object> map = new HashMap<>();
		boolean testCaseStatus = true;
		for (ITestResult result : executedTestStepResults.get(iterationCount)) {
			TestStepData testStepData = (TestStepData) result.getParameters()[0];
			if (testStepData != null) {
				TestStepReportDisplay stepDisplay = new TestStepReportDisplay();
				stepDisplay.setTestStepId(testStepData.getTestStepId());
				stepDisplay.setTestStepDescription(testStepData.getDescription());
				stepDisplay.setKeyword(testStepData.getKeyword());
				stepDisplay.setRunMode(testStepData.isRunMode());
				stepDisplay.setScreenShotPath(testStepData.getScreenShotPath());
				boolean stepStatus = populateStatus(result.getStatus());
				stepDisplay.setStatus(stepStatus);
				if (!stepStatus) {
					testCaseStatus = false;
				}
				testStepReportList.add(stepDisplay);
			}
		}
		map.put("stepList", testStepReportList);
		map.put("testCaseId", testCaseData.getTcId());
		addTestCaseReportDisplayToContext(context, iterationCount, testCaseStatus);
		Path path = Paths.get(moduleReportDirectory, testCaseData.getTcId(), 
											Integer.toString(iterationCount), testCaseData.getTcId() + ".html");
		String templatePath = Paths.get(PublicConstants.thymeLeafTemplateBaseDirectory,  "testSteps").toString();
		ReportsUtil.prepareThymeLeafWebReport(templatePath, map, path.toFile());
	}
	
	private void addTestCaseReportDisplayToContext(ITestContext context, int iterationCount, boolean testCaseStatus) {
		List<TestCaseReportDisplay> testCaseReportDisplayList = (List<TestCaseReportDisplay>) context.getAttribute(PublicConstants.TEST_CASE_LIST);
		if (context.getAttribute(PublicConstants.TEST_CASE_LIST) == null) {
			testCaseReportDisplayList = new ArrayList<TestCaseReportDisplay>();
			context.setAttribute(PublicConstants.TEST_CASE_LIST, testCaseReportDisplayList);
		}
		String testCaseId = testCaseData.getTcId();
		TestCaseReportDisplay testCaseReport = new TestCaseReportDisplay();
		testCaseReport.setTestCaseId(testCaseData.getTcId());
		testCaseReport.setManualTestCaseId(testCaseData.getManualTcId());
		testCaseReport.setTestCaseDescription(testCaseData.getDescription());
		testCaseReport.setStatus(testCaseStatus);
		testCaseReport.setRunMode(testCaseData.isRunMode());
		testCaseReport.setStartTime(getStartTime());
		testCaseReport.setEndTime(getEndTime());
		testCaseReport.setIterationCount(iterationCount);
		testCaseReport.setTestCaseReportPath(Paths.get(testCaseId, Integer.toString(iterationCount), testCaseId + PublicConstants.reportFileExtension).toString());
		testCaseReportDisplayList.add(testCaseReport);
	}

	private boolean populateStatus(int status) {
		boolean stepStatus = false;
		switch (status) {
			case ITestResult.FAILURE:
				stepStatus = false;
				break;
	
			case ITestResult.SKIP:
				stepStatus = false;
				break;
				
			case ITestResult.SUCCESS:
				stepStatus = true;
				break;
				
			default:
				break;
		}
		return stepStatus;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
