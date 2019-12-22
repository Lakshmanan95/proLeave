package com.photon.vms.vo;

import java.util.List;

public class RequestUnfreezeVO {

	List<UnFreezeRecords> unfreezeRecords;
	private String employeeNumber;
	UnFreezeRecords record;

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public List<UnFreezeRecords> getUnfreezeRecords() {
		return unfreezeRecords;
	}

	public void setUnfreezeRecords(List<UnFreezeRecords> unfreezeRecords) {
		this.unfreezeRecords = unfreezeRecords;
	}

	public UnFreezeRecords getRecord() {
		return record;
	}

	public void setRecord(UnFreezeRecords record) {
		this.record = record;
	}
}
