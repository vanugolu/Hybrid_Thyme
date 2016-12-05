package com.aims.listeners;

import java.util.List;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class CustomReporter implements IReporter {

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		for (ISuite suite : suites) {
			String reportDirectory = suite.getOutputDirectory();
			for (XmlTest xmlTest : suite.getXmlSuite().getTests()) {
				//xmlTest.getp
			}
		}
	}

}
