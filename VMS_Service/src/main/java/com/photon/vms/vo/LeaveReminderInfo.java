package com.photon.vms.vo;

import java.util.Date;

public class LeaveReminderInfo {
	
	private String employeeName;
	private String employeeCode;
	private String managerEmail;
	private Date fromDate;
	private Date toDate;
	private String leaveType;
	private Double days;
	private String managerName;
	private Date appliedOn;
	private String status;
	private String reason;
	private String odOptionCode;
	private String odOptionName;
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getOdOptionCode() {
		return odOptionCode;
	}
	public void setOdOptionCode(String odOptionCode) {
		this.odOptionCode = odOptionCode;
	}
	public String getOdOptionName() {
		return odOptionName;
	}
	public void setOdOptionName(String odOptionName) {
		this.odOptionName = odOptionName;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getManagerEmail() {
		return managerEmail;
	}
	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
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
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public Double getDays() {
		return days;
	}
	public void setDays(Double days) {
		this.days = days;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public Date getAppliedOn() {
		return appliedOn;
	}
	public void setAppliedOn(Date appliedOn) {
		this.appliedOn = appliedOn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
