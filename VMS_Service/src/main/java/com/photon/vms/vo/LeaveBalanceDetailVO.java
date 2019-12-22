package com.photon.vms.vo;

public class LeaveBalanceDetailVO {
	private String employeeId;
	private String year;
	private String leaveTypeId;
	private String leaveCode;
	private String leaveTypeName;
	private double openLeave;
	private String creditedLeave;
	private String usedLeave;
	private String balanceLeave;
	private String pendingLeave;
	private String locationCode;
	private String approvedAndPendingBalance;
	
	public String getApprovedAndPendingBalance() {
		return approvedAndPendingBalance;
	}
	public void setApprovedAndPendingBalance(String approvedAndPendingBalance) {
		this.approvedAndPendingBalance = approvedAndPendingBalance;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getLeaveTypeId() {
		return leaveTypeId;
	}
	public void setLeaveTypeId(String leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}
	public String getLeaveCode() {
		return leaveCode;
	}
	public void setLeaveCode(String leaveCode) {
		this.leaveCode = leaveCode;
	}
	public String getLeaveTypeName() {
		return leaveTypeName;
	}
	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
	}
	public double getOpenLeave() {
		return openLeave;
	}
	public void setOpenLeave(double openLeave) {
		this.openLeave = openLeave;
	}
	public String getCreditedLeave() {
		return creditedLeave;
	}
	public void setCreditedLeave(String creditedLeave) {
		this.creditedLeave = creditedLeave;
	}
	public String getUsedLeave() {
		return usedLeave;
	}
	public void setUsedLeave(String usedLeave) {
		this.usedLeave = usedLeave;
	}
	public String getBalanceLeave() {
		return balanceLeave;
	}
	public void setBalanceLeave(String balanceLeave) {
		this.balanceLeave = balanceLeave;
	}
	public String getPendingLeave() {
		return pendingLeave;
	}
	public void setPendingLeave(String pendingLeave) {
		this.pendingLeave = pendingLeave;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
}
