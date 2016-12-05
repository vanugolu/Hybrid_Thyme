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

public class TestSample1 {
	
	@Parameters({ "moduleName"})
	@BeforeTest
	public static void beforeTest(ITestContext context, String moduleName) throws IOException {
			File moduleDirectory = Paths.get(context.getOutputDirectory(), moduleName, "browser").toFile();
			ReportsUtil.mkdir(moduleDirectory);
	}

}
