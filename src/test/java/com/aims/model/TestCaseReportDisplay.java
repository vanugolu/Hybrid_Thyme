package com.aims.model;

import java.util.Date;

public class TestCaseReportDisplay {
	
	private String testCaseId;
	private String manualTestCaseId;
	private String testCaseDescription;
	private boolean status;
	private Date startTime;
	private Date endTime;
	private boolean runMode;
	private int sequenceCount;
	private String testCaseReportPath;
	private int iterationCount;
	
	public String getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}
	public String getTestCaseDescription() {
		return testCaseDescription;
	}
	public void setTestCaseDescription(String testCaseDescription) {
		this.testCaseDescription = testCaseDescription;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean isRunMode() {
		return runMode;
	}
	public void setRunMode(boolean runMode) {
		this.runMode = runMode;
	}
	public int getSequenceCount() {
		return sequenceCount;
	}
	public void setSequenceCount(int sequenceCount) {
		this.sequenceCount = sequenceCount;
	}
	public String getTestCaseReportPath() {
		return testCaseReportPath;
	}
	public void setTestCaseReportPath(String testCaseReportPath) {
		this.testCaseReportPath = testCaseReportPath;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getManualTestCaseId() {
		return manualTestCaseId;
	}
	public void setManualTestCaseId(String manualTestCaseId) {
		this.manualTestCaseId = manualTestCaseId;
	}
	public int getIterationCount() {
		return iterationCount;
	}
	public void setIterationCount(int iterationCount) {
		this.iterationCount = iterationCount;
	}
}
