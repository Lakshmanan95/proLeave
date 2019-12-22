package com.photon.vms.vo;

import java.util.Date;

public class SearchHistoryVO {
	
	private String leaveRequestId;
	private Date fromDate;
	private Date toDate;
	private String numberDays;
	private String leaveTypeId;
	private String leaveCode;
	private String leaveTypeName;
	private String contactNumber;
	private String leaveReason;
	private String appliedOnDate;
	private String approvedById;
	private String approvedDate;
	private String leaveStatus;
	private String approvedStatus;
	private String odOptioncode;
	private String odOptionName;
	
	public String getOdOptionName() {
		return odOptionName;
	}
	public void setOdOptionName(String odOptionName) {
		this.odOptionName = odOptionName;
	}
	public String getOdOptioncode() {
		return odOptioncode;
	}
	public void setOdOptioncode(String odOptioncode) {
		this.odOptioncode = odOptioncode;
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
	public String getLeaveRequestId() {
		return leaveRequestId;
	}
	public void setLeaveRequestId(String leaveRequestId) {
		this.leaveRequestId = leaveRequestId;
	}
	public String getNumberDays() {
		return numberDays;
	}
	public void setNumberDays(String numberDays) {
		this.numberDays = numberDays;
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
	public String getAppliedOnDate() {
		return appliedOnDate;
	}
	public void setAppliedOnDate(String appliedOnDate) {
		this.appliedOnDate = appliedOnDate;
	}
	public String getApprovedById() {
		return approvedById;
	}
	public void setApprovedById(String approvedById) {
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
	public String getApprovedStatus() {
		return approvedStatus;
	}
	public void setApprovedStatus(String approvedStatus) {
		this.approvedStatus = approvedStatus;
	}
}
