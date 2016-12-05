package com.aims;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.aims.constants.PublicConstants;
import com.aims.util.CreateLogger;
import com.aims.util.ReportsUtil;

public class TestSample {
	
	@BeforeSuite
	public static void beforeSuite(ITestContext context) throws FileNotFoundException, IOException, InterruptedException {
		TestRunner runner = (TestRunner) context;

		String projectFolderPath = Paths.get(System.getProperty("user.dir")).getParent().toString();

		File reportDirectory = Paths.get(projectFolderPath,"Reports").toFile();

		ReportsUtil.mkdir(reportDirectory);
		
		runner.setOutputDirectory(reportDirectory.getAbsolutePath());
	}
	
	@Parameters({ "moduleName"})
	@BeforeTest(alwaysRun = true)
	public static void beforeTest(ITestContext context, String moduleName) throws IOException {
		System.out.println(context.getOutputDirectory());
			File moduleDirectory = Paths.get(context.getOutputDirectory(), moduleName, "browser").toFile();
			System.out.println(moduleName + "-----" + moduleDirectory.exists());
			ReportsUtil.mkdir(moduleDirectory);
	}

}
