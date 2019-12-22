package com.photon.vms.vo;

import java.sql.Date;

public class PendingLeaveRequsetVO {
	
	private String leaveRequestId;
	private String submittedDate;
	private String employeeId;
	private String employeeCode;
	private String employeeName;
	private String insightId;
	private Date fromDate;
	private Date toDate;
	private int numberOfDays;
	private int leaveTypeId;
	private String leaveCode;
	private String leaveTypeName;
	private String leaveReason;
	private String managerName;
	private String leaveStatus;
	private int leaveBalance;
	private String contactNumber;
	private String approvedStatus;
	private String odOptionName;
	private String odOptionCode;
	private int leaveStatusId;
	
	
	
	public int getLeaveStatusId() {
		return leaveStatusId;
	}
	public void setLeaveStatusId(int leaveStatusId) {
		this.leaveStatusId = leaveStatusId;
	}
	public String getOdOptionName() {
		return odOptionName;
	}
	public void setOdOptionName(String odOptionName) {
		this.odOptionName = odOptionName;
	}
	public String getOdOptionCode() {
		return odOptionCode;
	}
	public void setOdOptionCode(String odOptionCode) {
		this.odOptionCode = odOptionCode;
	}
	public String getLeaveRequestId() {
		return leaveRequestId;
	}
	public void setLeaveRequestId(String leaveRequestId) {
		this.leaveRequestId = leaveRequestId;
	}
	public String getSubmittedDate() {
		return submittedDate;
	}
	public void setSubmittedDate(String submittedDate) {
		this.submittedDate = submittedDate;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
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
	public String getInsightId() {
		return insightId;
	}
	public void setInsightId(String insightId) {
		this.insightId = insightId;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public int getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	public int getLeaveTypeId() {
		return leaveTypeId;
	}
	public void setLeaveTypeId(int leaveTypeId) {
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
	public String getLeaveReason() {
		return leaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getLeaveStatus() {
		return leaveStatus;
	}
	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}
	public int getLeaveBalance() {
		return leaveBalance;
	}
	public void setLeaveBalance(int leaveBalance) {
		this.leaveBalance = leaveBalance;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getApprovedStatus() {
		return approvedStatus;
	}
	public void setApprovedStatus(String approvedStatus) {
		this.approvedStatus = approvedStatus;
	}	
}
