package com.photon.vms.vo;

public class UnFreezeRecords {

	private String loginEmployeeCode;
	private String unfreezeEmployeeCode;
	private String employeeCode;
	private int locationId;
	private String fromDate;
	private String toDate;
	private String comments;
	private String flag;
	private int index;
	
	public String getLoginEmployeeCode() {
		return loginEmployeeCode;
	}
	public void setLoginEmployeeCode(String loginEmployeeCode) {
		this.loginEmployeeCode = loginEmployeeCode;
	}
	public String getUnfreezeEmployeeCode() {
		return unfreezeEmployeeCode;
	}
	public void setUnfreezeEmployeeCode(String unfreezeEmployeeCode) {
		this.unfreezeEmployeeCode = unfreezeEmployeeCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
