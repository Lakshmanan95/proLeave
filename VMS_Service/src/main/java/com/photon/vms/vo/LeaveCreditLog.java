package com.photon.vms.vo;

public class LeaveCreditLog {

	private String employeeCode;
	private String employeeName;
	private String leaveType;
	private String creditValue;
	private String compOffDate;
	private String creditedDate;
	private String uploadStatus;
	private String comments;
	
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public String getCreditValue() {
		return creditValue;
	}
	public void setCreditValue(String creditValue) {
		this.creditValue = creditValue;
	}
	public String getCompOffDate() {
		return compOffDate;
	}
	public void setCompOffDate(String compOffDate) {
		this.compOffDate = compOffDate;
	}
	public String getCreditedDate() {
		return creditedDate;
	}
	public void setCreditedDate(String creditedDate) {
		this.creditedDate = creditedDate;
	}
	public String getUploadStatus() {
		return uploadStatus;
	}
	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
}
