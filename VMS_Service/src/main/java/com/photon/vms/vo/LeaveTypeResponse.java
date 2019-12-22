package com.photon.vms.vo;

import java.util.List;

public class LeaveTypeResponse extends SuccessResponseVO{

	List<LeaveType> leaveType;

	public List<LeaveType> getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(List<LeaveType> leaveType) {
		this.leaveType = leaveType;
	}
}
