package com.photon.vms.vo;
public class BulkApproveMailVO {
	private String leaveStatus;
	private String leaveCode;
	private ReportManagerVO reportManagerVO;
	private String employeeNumber;
	private String requestLeaveId;
	private int employeeId;
	private String fromdate;
	private String toDate;
	private String numberOfDays;
	private String contactNumber;
	private String leaveReason;
	private Double checkdays;
	private String leaveName;
	
	public String getLeaveStatus() {
		return leaveStatus;
	}
	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}
	public String getLeaveCode() {
		return leaveCode;
	}
	public void setLeaveCode(String leaveCode) {
		this.leaveCode = leaveCode;
	}
	public ReportManagerVO getReportManagerVO() {
		return reportManagerVO;
	}
	public void setReportManagerVO(ReportManagerVO reportManagerVO) {
		this.reportManagerVO = reportManagerVO;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getRequestLeaveId() {
		return requestLeaveId;
	}
	public void setRequestLeaveId(String requestLeaveId) {
		this.requestLeaveId = requestLeaveId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getFromdate() {
		return fromdate;
	}
	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(String numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getLeaveReason() {
		return leaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	public Double getCheckdays() {
		return checkdays;
	}
	public void setCheckdays(Double checkdays) {
		this.checkdays = checkdays;
	}
	public String getLeaveName() {
		return leaveName;
	}
	public void setLeaveName(String leaveName) {
		this.leaveName = leaveName;
	}
}
