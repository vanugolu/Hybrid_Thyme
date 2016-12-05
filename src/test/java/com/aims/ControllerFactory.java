package com.aims;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.log4testng.Logger;

import com.aims.constants.PublicConstants;
import com.aims.model.TestCaseData;
import com.aims.util.CreateLogger;
import com.aims.util.ReportsUtil;
import com.aims.xls.ExcelOperations;

public class ControllerFactory {
	private static final Logger log = Logger.getLogger(ControllerFactory.class);
	private static final File batchFileDir = Paths.get(System.getProperty("user.dir"), "/grid").toFile();
	
	@Factory(dataProvider = "testCaseData")
	public Object[] createControllerObjectForEachTestCase(List<TestCaseData> testCaseDataList) {
		List<Controller> controllerList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(testCaseDataList)) {
			int i=0;
			for (TestCaseData testCase : testCaseDataList) {
				Controller controller = new Controller(testCase);
				controller.setPriority(i++);
				controllerList.add(controller);
			}
		}
		return controllerList.toArray();
	}

	@DataProvider(name = "testCaseData")
	public Object[][] testCaseData(ITestContext context) throws URISyntaxException {
		String moduleName = context.getCurrentXmlTest().getParameter("moduleName");
		String controllerFileName = context.getCurrentXmlTest().getParameter("controllerFileName");
		
		File controllerFile = Paths.get(this.getClass().getResource("/modules/" + moduleName + "/" + controllerFileName).toURI()).toFile();

		if (!controllerFile.exists()) {
			IllegalStateException exception = new IllegalStateException("Test case file could not be found");
			throw exception;
		}
		List<TestCaseData> tcIdList = new ArrayList<TestCaseData>();
		ExcelOperations controllerExcelSheet = new ExcelOperations(controllerFile.getPath());
		if(controllerExcelSheet != null && controllerExcelSheet.isSheetExist(moduleName)) {
			int rowCount = controllerExcelSheet.getRowCount(moduleName);
			for (int i=2; i <= rowCount; i++) {
				TestCaseData testCase = populateTestCaseData(controllerExcelSheet, moduleName, i);
				if (testCase != null) {
					tcIdList.add(testCase);
				}
			}
		}
		return dataProviderObjectArray(tcIdList);
	}

	private Object[][] dataProviderObjectArray(List<TestCaseData> testCaseDataList) {
		if (CollectionUtils.isNotEmpty(testCaseDataList)) {
			Object[][] testCaseDataArray = new Object[1][1];
			testCaseDataArray[0][0] = testCaseDataList;
			return testCaseDataArray;
		}
		return new Object[0][0];
	}
	
	private TestCaseData populateTestCaseData(ExcelOperations controllerExcelSheet, String moduleName, int rowNum) {
		String tcId = controllerExcelSheet.getCellData(moduleName, "TCID", rowNum);
		if (StringUtils.isNotBlank(tcId)) {
			boolean runMode = getRunMode(controllerExcelSheet.getCellData(moduleName, "Runmode", rowNum));
			TestCaseData testCaseData = new TestCaseData();
			testCaseData.setTcId(tcId);
			testCaseData.setRunMode(runMode);
			testCaseData.setDescription(controllerExcelSheet.getCellData(moduleName, "Description", rowNum));
			testCaseData.setManualTcId(controllerExcelSheet.getCellData(moduleName, "Manual TC ID", rowNum));
			return testCaseData;
		}
		return null;
	}
	
	private boolean getRunMode(String cellData) {
		if (StringUtils.isNotBlank(cellData)) {
			return "Y".equalsIgnoreCase(cellData.trim());
		}
		return true;
	}
	
	@BeforeSuite
	public void beforeSuite(ITestContext context) throws FileNotFoundException, IOException, InterruptedException {
		Properties properties = loadPropertyFiles();
		ISuite suite = context.getSuite();
		Map<String, String> suiteParameters = suite.getXmlSuite().getParameters();
		for (Object key : properties.keySet()) {
			String value = properties.getProperty((String) key);
			suiteParameters.put((String) key, value);
		}
		String runTest = properties.getProperty("RunTest");
		String env = properties.getProperty(PublicConstants.MACHINE_TYPE);
		ReportsUtil.shutDownGrid(env);
		context.getSuite().setAttribute(PublicConstants.START_DATE, new Date());
		
		runGrid(suiteParameters);
		Thread.sleep(5000);
	}
	
	@AfterSuite
	public void afterSuite(ITestContext context) throws IOException {
		context.getSuite().setAttribute(PublicConstants.END_DATE, new Date());
		ReportsUtil.clearTempFolder();
		ReportsUtil.shutDownGrid(context.getSuite().getParameter(PublicConstants.MACHINE_TYPE));
		ReportsUtil.generateIndexReport(context);
		/*if (!testCONFIG.getProperty("Env").equals("LocalMachine")) {
			Runtime.getRuntime().exec("cmd.exe /c start grid_TEST_WIN.bat", null, batchFileDir);
		}*/

	}

	@BeforeTest
	@Parameters({ "moduleName"})
	public void beforeTest(ITestContext context, String moduleName) throws IOException {
		ISuite suite = context.getSuite();
		Map<String, String> suiteParameters;
		synchronized (suite) {
			suiteParameters = suite.getXmlSuite().getParameters();
			addCurrentContextToSuite(context);
		}
		Map<String, String> moduleParameters = context.getCurrentXmlTest().getLocalParameters();
		String testBrowser = suiteParameters.get(PublicConstants.TEST_BROWSER);
		if (StringUtils.isNotBlank(testBrowser)) {
			moduleParameters.put(PublicConstants.TEST_BROWSER, testBrowser);
		} else {
			testBrowser = moduleParameters.get(PublicConstants.TEST_BROWSER);
		}
		File moduleDirectory = Paths.get(context.getOutputDirectory(), moduleName, testBrowser).toFile();
		context.setAttribute(PublicConstants.MODULE_PATH, moduleDirectory.getAbsolutePath());
		
		ReportsUtil.mkdir(moduleDirectory);
		
		String logFileName = Paths.get(moduleDirectory.getAbsolutePath(), moduleName + PublicConstants.LOG_FILE_EXTENSION).toString();
		CreateLogger.createLogger(logFileName, moduleName + "_" + testBrowser);
		context.setAttribute(PublicConstants.CAPABILITIES, fetchCapabilities(testBrowser));

	}

	protected void addCurrentContextToSuite(ITestContext context) {
		ISuite suite = context.getSuite();
		List<ITestContext> iTestContexts = (List<ITestContext>) suite.getAttribute(PublicConstants.TEST_CONTEXT_LIST);
		if (iTestContexts == null) {
			iTestContexts = new ArrayList<>();
			context.getSuite().setAttribute(PublicConstants.TEST_CONTEXT_LIST, iTestContexts);
		}
		iTestContexts.add(context);
	}
	
	@AfterTest
	@Parameters({ "moduleName" })
	public void afterTest(ITestContext context, String moduleName) throws IOException {
		DriverFactory.getInstance().quit();
		ReportsUtil.generateModuleReport(context);
	}

	private Properties loadPropertyFiles() {
		Properties properties = new Properties();
		properties.putAll(loadConfigFile("/config/configuration.properties"));
		properties.putAll(loadConfigFile("/env.properties"));
		properties.putAll(loadConfigFile("/com/aims/objectRepo/OR.properties")); 
		properties.putAll(loadConfigFile("/com/aims/objectRepo/APPTEXT.properties"));
		return properties;
	}
	
	private void runGrid(Map<String, String> suiteParameters) throws IOException, InterruptedException {
		String machineType = suiteParameters.get(PublicConstants.MACHINE_TYPE);
		if (System.getProperty("os.name").equals("Mac OS X")) {
			String node;
			String cmd[];
			if ("LocalMachine".equalsIgnoreCase(machineType)) {
				node = "gridNode_LOCAL_MAC.sh";
				cmd = new String[] { "/usr/bin/open", "-a", "terminal.app", batchFileDir.toString() + "/gridHub_LOCAL_MAC.sh" };
			} else {
				node = "gridNode_TEST_MAC.sh";
				cmd = new String[] { "/usr/bin/open", "-a", "terminal.app", batchFileDir.toString() + "/" + node };
			}
			Runtime.getRuntime().exec(cmd);
		} else {
			if ("LocalMachine".equalsIgnoreCase(machineType)) {
				Runtime.getRuntime().exec("cmd.exe /c start grid_LOCAL_WIN.bat", null, batchFileDir);
			} else {
				Runtime.getRuntime().exec("cmd.exe /c start grid_TEST_WIN1.bat", null, batchFileDir);
			}
		}
	}	

	private String formatDate(LocalDateTime dateTime) {

		String format = "dd_MMM_yyyy_hh_mm_ss_a";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return dateTime.format(formatter);
	}
	
	private Properties loadConfigFile(String filePath) {
		try {
			Properties properties = new Properties();
			File propertyFile = Paths.get(this.getClass().getResource(filePath).toURI()).toFile();
			properties.load(new FileInputStream(propertyFile));
			return properties;
		} catch (URISyntaxException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		return null;
	}
	
	protected Capabilities fetchCapabilities(String testBrowser) {
		Capabilities capabilities = null;
		switch (testBrowser) {
		case BrowserType.FIREFOX:
			capabilities = fireFoxWebDriverCapabilities();
			break;
			
		case BrowserType.IE:
			capabilities = ieWebDriverCapabilities();
			break;
			
		case BrowserType.CHROME:
			capabilities = chromeWebDriverCapabilities();
			break;
		
		case BrowserType.SAFARI:
			capabilities = safariWebDriverCapabilities();
			break;

		default:
			break;
		}
		return capabilities;
	}

	protected DesiredCapabilities fireFoxWebDriverCapabilities() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("geo.prompt.testing", true);
		profile.setPreference("geo.prompt.testing.allow", true);
		// profile.setEnableNativeEvents(true);
		log.debug("inside navigate firefox");
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setBrowserName("firefox");
		capabilities.setCapability("nativeEvents", true);
		capabilities.setCapability(FirefoxDriver.PROFILE, profile);

		return capabilities;
	}

	protected DesiredCapabilities chromeWebDriverCapabilities() {
		log.debug("inside navigate chrome");
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setBrowserName("chrome");

		String chromeDriver;
		if (System.getProperty("os.name").equals("Mac OS X")) {
			capabilities.setPlatform(Platform.MAC);
			chromeDriver = "chromedriver";
		} else {
			capabilities.setPlatform(Platform.WINDOWS);
			chromeDriver = "chromedriver.exe";

			log.debug("webdriver.chrome.driver: " + System.getProperty("user.dir") + "/drivers/" + chromeDriver);
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/" + chromeDriver);
		}

		ChromeOptions options = new ChromeOptions();

		options.addArguments("--silent");
		options.addArguments("--disable-extensions");
		options.addArguments("test-type");
		options.addArguments("start-maximized");

		capabilities.setCapability(ChromeOptions.CAPABILITY, options);

		return capabilities;
	}

	protected DesiredCapabilities ieWebDriverCapabilities() {
		log.debug("webdriver.ie.driver: " + System.getProperty("user.dir") + "/drivers/IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "/drivers/IEDriverServer.exe");
		log.debug("inside navigate IE");
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		// cap.setBrowserName("iexplore");
		capabilities.setPlatform(Platform.WINDOWS);
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilities.setCapability("enablePersistentHover", false);
		// cap.setCapability("requireWindowFocus", true);
		capabilities.setCapability("ignoreProtectedModeSettings", true);
		capabilities.setCapability("ie.ensureCleanSession", true);

		return capabilities;
	}

	protected DesiredCapabilities safariWebDriverCapabilities() {
		DesiredCapabilities capabilities = DesiredCapabilities.safari();
		capabilities.setBrowserName("safari");
		capabilities.setPlatform(Platform.MAC);

		return capabilities;
	}
}
