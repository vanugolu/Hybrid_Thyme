package com.aims.model;

public class TestStepReportDisplay {
	private String testStepId;
	private String testStepDescription;
	private String keyword;
	private boolean runMode;
	private boolean status;
	private String screenShotPath;
	
	public String getTestStepId() {
		return testStepId;
	}
	public void setTestStepId(String testStepId) {
		this.testStepId = testStepId;
	}
	public String getTestStepDescription() {
		return testStepDescription;
	}
	public void setTestStepDescription(String testStepDescription) {
		this.testStepDescription = testStepDescription;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public boolean isRunMode() {
		return runMode;
	}
	public void setRunMode(boolean runMode) {
		this.runMode = runMode;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getScreenShotPath() {
		return screenShotPath;
	}
	public void setScreenShotPath(String screenShotPath) {
		this.screenShotPath = screenShotPath;
	}
}
