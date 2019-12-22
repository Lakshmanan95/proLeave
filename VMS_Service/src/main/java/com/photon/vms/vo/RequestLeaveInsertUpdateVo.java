package com.photon.vms.vo;

public class RequestLeaveInsertUpdateVo {

	private String loginEmployeeCode;
	private String employeeNumber;
	private int leaveTypeId;
	private Double leaveOpen;
	private Double leaveCredit;
	private Double leaveUsed;
	private String comments;
	private String compoffDate;
	private String revokeCompoffDate;
	
	public String getLoginEmployeeCode() {
		return loginEmployeeCode;
	}
	public void setLoginEmployeeCode(String loginEmployeeCode) {
		this.loginEmployeeCode = loginEmployeeCode;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public int getLeaveTypeId() {
		return leaveTypeId;
	}
	public void setLeaveTypeId(int leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}
	public Double getLeaveOpen() {
		return leaveOpen;
	}
	public void setLeaveOpen(Double leaveOpen) {
		this.leaveOpen = leaveOpen;
	}
	public Double getLeaveCredit() {
		return leaveCredit;
	}
	public void setLeaveCredit(Double leaveCredit) {
		this.leaveCredit = leaveCredit;
	}
	public Double getLeaveUsed() {
		return leaveUsed;
	}
	public void setLeaveUsed(Double leaveUsed) {
		this.leaveUsed = leaveUsed;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getCompoffDate() {
		return compoffDate;
	}
	public void setCompoffDate(String compoffDate) {
		this.compoffDate = compoffDate;
	}
	public String getRevokeCompoffDate() {
		return revokeCompoffDate;
	}
	public void setRevokeCompoffDate(String revokeCompoffDate) {
		this.revokeCompoffDate = revokeCompoffDate;
	}
}
