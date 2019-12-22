package com.photon.vms.dao;

import java.util.List;

import com.photon.vms.vo.LocationAdminDetails;
import com.photon.vms.vo.PendingLeaveRequsetVO;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.RevocationEmailVO;
import com.photon.vms.vo.SuccessResponseVO;
import com.photon.vms.vo.ViewRevocactionResponseVO;

public interface RevocationDAO {

	ViewRevocactionResponseVO viewRevocationDetail(String leaveRequestId) throws Exception;
	public RevocationEmailVO getMailDetails(String leaveRequestId) throws Exception;
	public List<PendingLeaveRequsetVO> getPendingLeaveForAdmin(RequestParameterVO request) throws Exception;
	public SuccessResponseVO revocationApproveAndReject(String leaveRequestId, String employeeId, String leaveStatus, String reason) throws Exception;
}
