package com.photon.vms.vo;

import java.util.ArrayList;

public class ReportDropDownResponse extends SuccessResponseVO{

	ArrayList<LeaveType> leaveTypeInfo;
	ArrayList<LeaveStatusVO> leaveStatusInfo;
	ArrayList<EmployeeDetailVO> employeeInfo;
	
	public ArrayList<LeaveType> getLeaveTypeInfo() {
		return leaveTypeInfo;
	}
	public void setLeaveTypeInfo(ArrayList<LeaveType> leaveTypeInfo) {
		this.leaveTypeInfo = leaveTypeInfo;
	}
	public ArrayList<LeaveStatusVO> getLeaveStatusInfo() {
		return leaveStatusInfo;
	}
	public void setLeaveStatusInfo(ArrayList<LeaveStatusVO> leaveStatusInfo) {
		this.leaveStatusInfo = leaveStatusInfo;
	}
	public ArrayList<EmployeeDetailVO> getEmployeeInfo() {
		return employeeInfo;
	}
	public void setEmployeeInfo(ArrayList<EmployeeDetailVO> employeeInfo) {
		this.employeeInfo = employeeInfo;
	}
	
	
}
