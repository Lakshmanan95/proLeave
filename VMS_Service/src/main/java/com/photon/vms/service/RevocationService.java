package com.photon.vms.service;

import java.util.List;

import com.photon.vms.vo.PendingLeaveRequsetVO;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.RevocationEmailVO;
import com.photon.vms.vo.SuccessResponseVO;
import com.photon.vms.vo.ViewRevocactionResponseVO;

public interface RevocationService {

	public ViewRevocactionResponseVO viewRevocationDetail(String leaveRequestId);
	public RevocationEmailVO getMailDetails(String leaveRequestId);
	public List<PendingLeaveRequsetVO> getPendingLeaveForAdmin(RequestParameterVO request);
	public SuccessResponseVO revocationApproveAndReject(String leaveRequestId, String employeeId, String leaveStatus, String reason);
}
