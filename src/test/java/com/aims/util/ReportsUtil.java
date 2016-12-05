package com.aims.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.aims.Keywords;
import com.aims.constants.PublicConstants;
import com.aims.model.IndexReportDisplay;
import com.aims.model.TestCaseReportDisplay;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class ReportsUtil extends Keywords{
	public String version;
	public Logger log;
	public static File indexHTML;
	public static String RUN_DATE;
	public static String testStartTime;
	public static String testEndTime;
	public static String ENVIRONMENT;
	public static String suite;
	
	public static Integer passCount ;
	public static Integer failCount ;
	public static Integer skipCount ;
	public static Integer grandTotal ;
	
	
	public ReportsUtil() {

	}

	// returns current date and time
	public static String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());

	}

	// store screenshots
	public static void takeScreenShot(String file, WebDriver driver, String reportFolder, Logger log) {
		try {
			driver = (RemoteWebDriver) new Augmenter().augment(driver);
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			FileUtils.moveFile(scrFile, new File(reportFolder, file));
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Throwable t) {
			// TODO: handle exception
			log.debug(t.getMessage());
		}

	}
	
	public static void prepareWebReport(String templatePath, Map<String, Object> data, File targetFile) throws IOException{
		Configuration cfg = new Configuration();
		FileWriter filestream = null;
		BufferedWriter bw = null;
	    try {
	        //Load template from source folder
	        Template template = cfg.getTemplate(templatePath);
	        // File output
	     // Create file if it doesn't exists
	  	  if (!targetFile.exists()) {
	  		targetFile.createNewFile();
	  	  }
	        filestream = new FileWriter (targetFile);
	        bw = new BufferedWriter(filestream);
	        template.process(data, bw);
	        bw.flush();
	         
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (TemplateException e) {
	        e.printStackTrace();
	    } finally {
	    	if(bw != null) {
	    		bw.close();
	    	}
	    }
		
	}
	public static void clearTempFolder() throws IOException {

        try {
        File file = new File(System.getProperty("java.io.tmpdir"));
        FileUtils.cleanDirectory(file);
        }

        catch (IOException e) {
        // Do nothing since
        }
        }

	
	public static void shutDownGrid(String env) throws IOException{
		try {
			if("LocalMachine".equalsIgnoreCase(env)) {
				Runtime.getRuntime().exec("taskkill /IM cmd.exe");
				Runtime.getRuntime().exec("taskkill /IM java.exe");
				Runtime.getRuntime().exec("taskkill /IM chromedriver.exe /f");
				Runtime.getRuntime().exec("taskkill /IM IEDriverServer.exe /f");
				Runtime.getRuntime().exec("taskkill /IM iexplore.exe /f");
			}else {
				Runtime.getRuntime().exec("taskkill /IM cmd.exe");
				Runtime.getRuntime().exec("taskkill /IM java.exe");
				Runtime.getRuntime().exec("taskkill /IM chromedriver.exe /f");
				Runtime.getRuntime().exec("taskkill /IM IEDriverServer.exe /f");
				Runtime.getRuntime().exec("taskkill /IM iexplore.exe /f");
				Runtime.getRuntime().exec("taskkill /IM chrome.exe /f");
				Runtime.getRuntime().exec("taskkill /IM firefox.exe /f");
			}
		}catch(Throwable t) {
			
		}

	}
	
	public static void prepareThymeLeafWebReport(String templatePath, Map<String, Object> data, File targetFile) throws IOException{
		mkdir(targetFile.getParentFile());
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setSuffix(".html");
		resolver.setCharacterEncoding("UTF-8");
		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(resolver);
		Context context = new Context();
		context.setVariables(data);
		engine.process(templatePath, context, new FileWriter(targetFile));
	}
	
	public static void generateIndexReport(ITestContext context) throws IOException {
		ISuite suite = context.getSuite();
		int suiteTotalCount = 0, suitePassCount = 0, suiteFailCount = 0;
		List<ITestContext> tests = (List<ITestContext>) suite.getAttribute(PublicConstants.TEST_CONTEXT_LIST);
		if (CollectionUtils.isNotEmpty(tests)) {
			List<IndexReportDisplay> moduleList = new ArrayList<>();
			for (ITestContext testContext : tests) {
				IndexReportDisplay module = new IndexReportDisplay();
				module.setModuleName(testContext.getCurrentXmlTest().getParameter("moduleName"));
				module.setStartTime(testContext.getStartDate());
				module.setEndTime(testContext.getEndDate());
				int moduleTtotalCount = Integer.valueOf(testContext.getCurrentXmlTest().getParameter(PublicConstants.TOTAL_COUNT));
				suiteTotalCount += moduleTtotalCount;
				int modulePassCount = Integer.valueOf(testContext.getCurrentXmlTest().getParameter(PublicConstants.PASS_COUNT));
				suitePassCount += modulePassCount;
				int moduleFailCount = Integer.valueOf(testContext.getCurrentXmlTest().getParameter(PublicConstants.FAIL_COUNT));
				suiteFailCount += moduleFailCount;
				module.setTotal(moduleTtotalCount);
				module.setPassCount(modulePassCount);
				module.setFailCount(moduleFailCount);
				module.setBrowserName(testContext.getCurrentXmlTest().getParameter(PublicConstants.TEST_BROWSER));
				moduleList.add(module);
			}
			Map<String, Object> map = new HashMap<>();
			Date suiteStartTime = (Date) suite.getAttribute(PublicConstants.START_DATE);
			Date suiteEndTime = (Date) suite.getAttribute(PublicConstants.END_DATE);
			map.put("moduleList", moduleList);
			map.put("startTime", suiteStartTime);
			map.put("endTime", suiteEndTime);
			map.put("suiteName", suite.getName());
			map.put("appURL", suite.getParameter(PublicConstants.MAIN_APP_URL));
			map.put("duration", fetchDuration(suiteStartTime, suiteEndTime));
			map.put("totalCount", suiteTotalCount);
			map.put("passCount", suitePassCount);
			map.put("failCount", suiteFailCount);
			File moduleFile = Paths.get(context.getOutputDirectory(), "index" + PublicConstants.reportFileExtension)
					.toFile();
			String indexTemplate = Paths.get(PublicConstants.thymeLeafTemplateBaseDirectory, "index").toString();
			ReportsUtil.prepareThymeLeafWebReport(indexTemplate, map, moduleFile);
		}
	}
	
	public static void generateModuleReport(ITestContext context) throws IOException {
		String moduleName = context.getCurrentXmlTest().getParameter("moduleName");
		Map<String, Object> map = new HashMap<>();
		List<TestCaseReportDisplay> testCaseList = new ArrayList<>();
		if (context.getAttribute(PublicConstants.TEST_CASE_LIST) != null) {
			testCaseList = (List<TestCaseReportDisplay>) context.getAttribute(PublicConstants.TEST_CASE_LIST);
			if (CollectionUtils.isEmpty(testCaseList)) {
				testCaseList = Collections.emptyList();
			}
		}
		moduleHeaderData(context, moduleName, map);
		populateTestCaseDisplay(testCaseList, map, context);
		File moduleFile = Paths.get((String)context.getAttribute(PublicConstants.MODULE_PATH), moduleName + PublicConstants.reportFileExtension)
				.toFile();
		String testCaseTemplate = Paths.get(PublicConstants.thymeLeafTemplateBaseDirectory, "testCases").toString();
		ReportsUtil.prepareThymeLeafWebReport(testCaseTemplate, map, moduleFile);
	}

	private static void moduleHeaderData(ITestContext context, String moduleName, Map<String, Object> map) {
		Date endDate = new Date();
		map.put("moduleName", moduleName);
		map.put("startTime", context.getStartDate());
		map.put("endTime", endDate);
		map.put("duration", fetchDuration(context.getStartDate(), endDate));
		map.put("browser", context.getCurrentXmlTest().getParameter("testBrowser"));
	}

	private static String fetchDuration(Date startDate, Date endDate) {
		StringBuilder durationFormat = new StringBuilder();
		LocalDateTime localStartTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
		LocalDateTime localEndTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
		LocalDateTime temp = LocalDateTime.from(localStartTime);
		long days = ChronoUnit.DAYS.between(temp, localEndTime);
		temp = temp.plusDays(days);
		long hours = ChronoUnit.HOURS.between(temp, localEndTime);
		temp = temp.plusHours(hours);
		long minutes = ChronoUnit.MINUTES.between(temp, localEndTime);
		temp = temp.plusMinutes(minutes);
		long seconds = ChronoUnit.SECONDS.between(temp, localEndTime);
		
		durationFormat.append(formatClockNeedle(days, "Day"));
		durationFormat.append(formatClockNeedle(hours, "Hour"));
		durationFormat.append(formatClockNeedle(minutes, "Min"));
		durationFormat.append(formatClockNeedle(seconds, "Sec"));
		return durationFormat.toString();
	}

	private static void populateTestCaseDisplay(List<TestCaseReportDisplay> testCaseReportList, Map<String, Object> map, ITestContext context) {
		int sequence = 1, totalCount = 0, passCount = 0, failCount = 0;
		for (TestCaseReportDisplay reportDisplay : testCaseReportList) {
			++totalCount;
			if (reportDisplay.isRunMode()) {
				if (reportDisplay.isStatus()) {
					++passCount;
				} else {
					++failCount;
				}
			}
			reportDisplay.setSequenceCount(sequence++);
		}
		map.put("testCaseList", testCaseReportList);
		map.put(PublicConstants.TOTAL_COUNT, totalCount);
		context.getCurrentXmlTest().addParameter(PublicConstants.TOTAL_COUNT, Integer.toString(totalCount));
		map.put(PublicConstants.PASS_COUNT, passCount);
		context.getCurrentXmlTest().addParameter(PublicConstants.PASS_COUNT, Integer.toString(passCount));
		map.put(PublicConstants.FAIL_COUNT, failCount);
		context.getCurrentXmlTest().addParameter(PublicConstants.FAIL_COUNT, Integer.toString(failCount));
	}
	
	public static String formatClockNeedle(long clockNeedle, String textToAppend) {
		if(clockNeedle > 1) {
			return clockNeedle +" "+textToAppend+"s"+" ";
		} else if(clockNeedle == 1) {
			return clockNeedle +" "+textToAppend+" ";
		} else {
			return "";
		}
	}
	
	public static void mkdir(File dir) throws IOException {
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
}
