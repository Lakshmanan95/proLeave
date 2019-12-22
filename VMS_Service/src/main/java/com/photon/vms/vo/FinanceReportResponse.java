package com.photon.vms.vo;

import java.util.List;

public class FinanceReportResponse extends SuccessResponseVO {

	List<LeaveType> leaveType;
	List<String> companyName;
	List<String> reportName;
	
	public List<LeaveType> getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(List<LeaveType> leaveType) {
		this.leaveType = leaveType;
	}
	public List<String> getCompanyName() {
		return companyName;
	}
	public void setCompanyName(List<String> companyName) {
		this.companyName = companyName;
	}
	public List<String> getReportName() {
		return reportName;
	}
	public void setReportName(List<String> reportName) {
		this.reportName = reportName;
	}
	
	
}
