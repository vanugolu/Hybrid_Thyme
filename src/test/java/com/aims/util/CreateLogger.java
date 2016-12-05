package com.aims.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class CreateLogger {

	public static void createLogger(String fileName, String loggerName) {
		RollingFileAppender rfa = new RollingFileAppender();
		PatternLayout layout = new PatternLayout("org.apache.log4j.PatternLayout");
		String conversionPattern = "%d{dd/MM/yyyy HH:mm:ss} %m%n";
		layout.setConversionPattern(conversionPattern);
		rfa.setName("FileLogger");
		rfa.setMaxBackupIndex(3);
		rfa.setMaxFileSize("5000KB");
		rfa.setFile(fileName);
		rfa.setLayout(layout);
		rfa.setAppend(false);
		rfa.activateOptions();
		Logger.getLogger(loggerName).addAppender(rfa);	  
	}
}
