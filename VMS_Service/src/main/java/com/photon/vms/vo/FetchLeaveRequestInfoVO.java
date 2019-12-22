package com.photon.vms.vo;

import java.sql.Date;

public class FetchLeaveRequestInfoVO {
	
	private String leaveRequestId;
	private String employeeCode;
	private String employeeName;
	private String employeeInsightId;
	private String reportingManagerName;
	private String fromDate;
	private String toDate;
	private double numberOfDays;
	private int leaveTypeId;
	private String leaveTypecode;
	private String leaveTypeName;
	private String contactNumber;
	private String insightId;
	private String leaveReason;
	private Date appliedDate;
	private int approvedById;
	private String approvedDate;
	private String leaveStatus;
	private int rejectedById;
	private Date rejectedDate;
	private String rejectedReason;
	private int revokedById;
	private Date revokedDate;
	private String revokedReason;
	private int cancelledById;
	private Date cancelledDate;
	private String cancelledReason;
	private String approvedStatus;
	private String odOptionCode;
	private String ownerEmployeeNumber;
	private String solutionCenter;
	
	public String getSolutionCenter() {
		return solutionCenter;
	}
	public void setSolutionCenter(String solutionCenter) {
		this.solutionCenter = solutionCenter;
	}
	public String getLeaveRequestId() {
		return leaveRequestId;
	}
	public void setLeaveRequestId(String leaveRequestId) {
		this.leaveRequestId = leaveRequestId;
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
	public Double getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(double numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	public int getLeaveTypeId() {
		return leaveTypeId;
	}
	public void setLeaveTypeId(int leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}
	public String getLeaveTypecode() {
		return leaveTypecode;
	}
	public void setLeaveTypecode(String leaveTypecode) {
		this.leaveTypecode = leaveTypecode;
	}
	public String getLeaveTypeName() {
		return leaveTypeName;
	}
	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getInsightId() {
		return insightId;
	}
	public void setInsightId(String insightId) {
		this.insightId = insightId;
	}
	public String getLeaveReason() {
		return leaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	public Date getAppliedDate() {
		return appliedDate;
	}
	public void setAppliedDate(Date appliedDate) {
		this.appliedDate = appliedDate;
	}
	public int getApprovedById() {
		return approvedById;
	}
	public void setApprovedById(int approvedById) {
		this.approvedById = approvedById;
	}
	public String getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}
	public String getLeaveStatus() {
		return leaveStatus;
	}
	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}
	public int getRejectedById() {
		return rejectedById;
	}
	public void setRejectedById(int rejectedById) {
		this.rejectedById = rejectedById;
	}
	public Date getRejectedDate() {
		return rejectedDate;
	}
	public void setRejectedDate(Date rejectedDate) {
		this.rejectedDate = rejectedDate;
	}
	public String getRejectedReason() {
		return rejectedReason;
	}
	public void setRejectedReason(String rejectedReason) {
		this.rejectedReason = rejectedReason;
	}
	public int getRevokedById() {
		return revokedById;
	}
	public void setRevokedById(int revokedById) {
		this.revokedById = revokedById;
	}
	public Date getRevokedDate() {
		return revokedDate;
	}
	public void setRevokedDate(Date revokedDate) {
		this.revokedDate = revokedDate;
	}
	public String getRevokedReason() {
		return revokedReason;
	}
	public void setRevokedReason(String revokedReason) {
		this.revokedReason = revokedReason;
	}
	public int getCancelledById() {
		return cancelledById;
	}
	public void setCancelledById(int cancelledById) {
		this.cancelledById = cancelledById;
	}
	public Date getCancelledDate() {
		return cancelledDate;
	}
	public void setCancelledDate(Date cancelledDate) {
		this.cancelledDate = cancelledDate;
	}
	public String getCancelledReason() {
		return cancelledReason;
	}
	public void setCancelledReason(String cancelledReason) {
		this.cancelledReason = cancelledReason;
	}
	public String getApprovedStatus() {
		return approvedStatus;
	}
	public void setApprovedStatus(String approvedStatus) {
		this.approvedStatus = approvedStatus;
	}
	public String getOdOptionCode() {
		return odOptionCode;
	}
	public void setOdOptionCode(String odOptionCode) {
		this.odOptionCode = odOptionCode;
	}
	public String getOwnerEmployeeNumber() {
		return ownerEmployeeNumber;
	}
	public void setOwnerEmployeeNumber(String ownerEmployeeNumber) {
		this.ownerEmployeeNumber = ownerEmployeeNumber;
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
	public String getEmployeeInsightId() {
		return employeeInsightId;
	}
	public void setEmployeeInsightId(String employeeInsightId) {
		this.employeeInsightId = employeeInsightId;
	}
	public String getReportingManagerName() {
		return reportingManagerName;
	}
	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}
}
