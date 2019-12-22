package com.photon.vms.vo;

import java.util.List;

public class FinancePendingRevocation extends SuccessResponseVO{

	List<PendingLeaveRequsetVO> pendingLeaveList;

	public List<PendingLeaveRequsetVO> getPendingLeaveList() {
		return pendingLeaveList;
	}

	public void setPendingLeaveList(List<PendingLeaveRequsetVO> pendingLeaveList) {
		this.pendingLeaveList = pendingLeaveList;
	}
	
	
}
