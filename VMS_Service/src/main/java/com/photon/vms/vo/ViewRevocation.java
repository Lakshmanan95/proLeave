package com.photon.vms.vo;

import java.util.List;

public class ViewRevocation {

	private String employeeCode;
	private String fromDate;
	private String toDate;
	private String revocationRequestedOn;
	private String revocationReason;
	private String approvedStatus;
	private List<PunchedHours> punchedHours;
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
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
	public String getRevocationRequestedOn() {
		return revocationRequestedOn;
	}
	public void setRevocationRequestedOn(String revocationRequestedOn) {
		this.revocationRequestedOn = revocationRequestedOn;
	}
	public String getRevocationReason() {
		return revocationReason;
	}
	public void setRevocationReason(String revocationReason) {
		this.revocationReason = revocationReason;
	}
	
	public String getApprovedStatus() {
		return approvedStatus;
	}
	public void setApprovedStatus(String approvedStatus) {
		this.approvedStatus = approvedStatus;
	}
	public List<PunchedHours> getPunchedHours() {
		return punchedHours;
	}
	public void setPunchedHours(List<PunchedHours> punchedHours) {
		this.punchedHours = punchedHours;
	}
	
	
}
