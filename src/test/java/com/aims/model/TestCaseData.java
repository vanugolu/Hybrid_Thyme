package com.aims.model;

public class TestCaseData implements Cloneable{
	
	private String tcId;
	private boolean runMode;
	private String description;
	private String manualTcId;
	private String userAgent;
	private String comments;
	public String getTcId() {
		return tcId;
	}
	public void setTcId(String tcId) {
		this.tcId = tcId;
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
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getManualTcId() {
		return manualTcId;
	}
	public void setManualTcId(String manualTcId) {
		this.manualTcId = manualTcId;
	}
	
	@Override
	public TestCaseData clone() throws CloneNotSupportedException {
		return (TestCaseData) super.clone();
	}
}
