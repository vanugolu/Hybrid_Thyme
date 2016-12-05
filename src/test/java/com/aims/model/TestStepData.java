package com.aims.model;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

public class TestStepData implements Cloneable{
	
	private String testCaseId;
	private String testStepId;
	private boolean runMode;
	private String description;
	private String keyword;
	private List<String> object;
	private boolean proceedOnFail;
	private int iterationCount = 1;
	private List<TestStepInputData> testStepInputData;
	private String screenShotPath;
	
	public String getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}
	public String getTestStepId() {
		return testStepId;
	}
	public void setTestStepId(String testStepId) {
		this.testStepId = testStepId;
	}
	public boolean isRunMode() {
		return runMode;
	}
	public void setRunMode(boolean runMode) {
		this.runMode = runMode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public boolean isProceedOnFail() {
		return proceedOnFail;
	}
	public void setProceedOnFail(boolean proceedOnFail) {
		this.proceedOnFail = proceedOnFail;
	}
	
	public List<TestStepInputData> getTestStepInputData() {
		return testStepInputData;
	}
	public void setTestStepInputData(List<TestStepInputData> testStepInputData) {
		this.testStepInputData = testStepInputData;
	}
	public List<String> getObject() {
		return object;
	}
	public void setObject(List<String> object) {
		this.object = object;
	}
	public int getIterationCount() {
		return iterationCount;
	}
	public void setIterationCount(int iterationCount) {
		this.iterationCount = iterationCount;
	}
	public String getScreenShotPath() {
		return screenShotPath;
	}
	public void setScreenShotPath(String screenShotPath) {
		this.screenShotPath = screenShotPath;
	}
	@Override
	public TestStepData clone() {
		try {
			TestStepData clonedTestStepData = (TestStepData) super.clone();
			clonedTestStepData.setTestStepInputData(cloneTestStepInput());
			return clonedTestStepData;
		} catch (CloneNotSupportedException e) {
		}
		return null;
	}
	
	private List<TestStepInputData> cloneTestStepInput() {
		if (CollectionUtils.isNotEmpty(testStepInputData)) {
			return testStepInputData.stream()
									.map(data -> data.clone())
									.collect(Collectors.toList());
		}
		return null;
	}
}
