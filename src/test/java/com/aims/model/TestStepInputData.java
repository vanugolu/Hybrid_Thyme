package com.aims.model;

public class TestStepInputData implements Cloneable{
	
	private String dataColumnName;
	private String dataCoulmnValue;
	public String getDataColumnName() {
		return dataColumnName;
	}
	public void setDataColumnName(String dataColumnName) {
		this.dataColumnName = dataColumnName;
	}
	public String getDataCoulmnValue() {
		return dataCoulmnValue;
	}
	public void setDataCoulmnValue(String dataCoulmnValue) {
		this.dataCoulmnValue = dataCoulmnValue;
	}
	
	@Override
	public TestStepInputData clone() {
		try {
			return (TestStepInputData) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return null;
	}
}
