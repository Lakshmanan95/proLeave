package com.photon.vms.vo;

/**
 * @author sureshkumar_e
 *
 */
public class AutoApprovalVO {

	private String employeeNumber;
	private String employeeName;
	private String email;
	private String leaveReqId;
	private int employeeId;
	private int leaveTypeId;
	private String leaveTypeName;
	private String fromDate;
	private String toDate;
	private String noOfDays;
	private String contactNumber;
	private String reason;
	private int odType;
	private int reportingMgrId;
	
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLeaveReqId() {
		return leaveReqId;
	}
	public void setLeaveReqId(String leaveReqId) {
		this.leaveReqId = leaveReqId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public int getLeaveTypeId() {
		return leaveTypeId;
	}
	public void setLeaveTypeId(int leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}
	public String getLeaveTypeName() {
		return leaveTypeName;
	}
	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
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
	public String getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getOdType() {
		return odType;
	}
	public void setOdType(int odType) {
		this.odType = odType;
	}
	public int getReportingMgrId() {
		return reportingMgrId;
	}
	public void setReportingMgrId(int reportingMgrId) {
		this.reportingMgrId = reportingMgrId;
	}
}
