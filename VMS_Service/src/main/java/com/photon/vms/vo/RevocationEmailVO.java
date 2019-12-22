package com.photon.vms.vo;

import java.util.List;

public class RevocationEmailVO {
	
	
	private String employeeCode;
	private String employeeName;
	private String employeeEmail;
	private String fromDate;
	private String toDate;
	private String leaveRequestId;
	private String leaveCode;
	private String leaveType;
	private String leaveStatus;
	private String leaveStatusCode;
	private String RMCode;
	private String RMName;
	private String RMEmail;
	private String leaveReason;
	private String revokedDate;
	private String revokedReason;
	private String revocationAprRejReason;
	private String revocationAprRejDate;
	private String revocationAprRejByCode;
	private String revocationAprRejByName;
	private String noOfDays;
	private String accessFlag;
	private String financeEmployeeEmail;
	private int leaveTypeId;
	private int odOptionId;
	private String contactNumber;
	List<LocationAdminDetails> adminDetails;
	List<PunchedHours> punchedHoursList;
	
	
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
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
	public String getEmployeeEmail() {
		return employeeEmail;
	}
	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
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
	public String getLeaveCode() {
		return leaveCode;
	}
	public void setLeaveCode(String leaveCode) {
		this.leaveCode = leaveCode;
	}
	public String getLeaveStatus() {
		return leaveStatus;
	}
	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}
	public String getLeaveStatusCode() {
		return leaveStatusCode;
	}
	public void setLeaveStatusCode(String leaveStatusCode) {
		this.leaveStatusCode = leaveStatusCode;
	}
	public String getRMCode() {
		return RMCode;
	}
	public void setRMCode(String rMCode) {
		RMCode = rMCode;
	}
	public String getRMName() {
		return RMName;
	}
	public void setRMName(String rMName) {
		RMName = rMName;
	}
	public String getRMEmail() {
		return RMEmail;
	}
	public void setRMEmail(String rMEmail) {
		RMEmail = rMEmail;
	}
	public String getLeaveReason() {
		return leaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	public String getRevokedDate() {
		return revokedDate;
	}
	public void setRevokedDate(String revokedDate) {
		this.revokedDate = revokedDate;
	}
	public String getRevokedReason() {
		return revokedReason;
	}
	public void setRevokedReason(String revokedReason) {
		this.revokedReason = revokedReason;
	}
	public String getRevocationAprRejReason() {
		return revocationAprRejReason;
	}
	public void setRevocationAprRejReason(String revocationAprRejReason) {
		this.revocationAprRejReason = revocationAprRejReason;
	}
	public String getRevocationAprRejDate() {
		return revocationAprRejDate;
	}
	public void setRevocationAprRejDate(String revocationAprRejDate) {
		this.revocationAprRejDate = revocationAprRejDate;
	}
	public String getRevocationAprRejByCode() {
		return revocationAprRejByCode;
	}
	public void setRevocationAprRejByCode(String revocationAprRejByCode) {
		this.revocationAprRejByCode = revocationAprRejByCode;
	}
	public String getRevocationAprRejByName() {
		return revocationAprRejByName;
	}
	public void setRevocationAprRejByName(String revocationAprRejByName) {
		this.revocationAprRejByName = revocationAprRejByName;
	}
	
	public String getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}
	public List<LocationAdminDetails> getAdminDetails() {
		return adminDetails;
	}
	public void setAdminDetails(List<LocationAdminDetails> adminDetails) {
		this.adminDetails = adminDetails;
	}
	public List<PunchedHours> getPunchedHoursList() {
		return punchedHoursList;
	}
	public void setPunchedHoursList(List<PunchedHours> punchedHoursList) {
		this.punchedHoursList = punchedHoursList;
	}
	public String getAccessFlag() {
		return accessFlag;
	}
	public void setAccessFlag(String accessFlag) {
		this.accessFlag = accessFlag;
	}
	public String getFinanceEmployeeEmail() {
		return financeEmployeeEmail;
	}
	public void setFinanceEmployeeEmail(String financeEmployeeEmail) {
		this.financeEmployeeEmail = financeEmployeeEmail;
	}
	public String getLeaveRequestId() {
		return leaveRequestId;
	}
	public void setLeaveRequestId(String leaveRequestId) {
		this.leaveRequestId = leaveRequestId;
	}
	public int getLeaveTypeId() {
		return leaveTypeId;
	}
	public void setLeaveTypeId(int leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}
	public int getOdOptionId() {
		return odOptionId;
	}
	public void setOdOptionId(int odOptionId) {
		this.odOptionId = odOptionId;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	
	

}
