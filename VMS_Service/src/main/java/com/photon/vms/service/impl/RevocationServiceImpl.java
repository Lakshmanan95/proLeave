package com.photon.vms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.dao.RevocationDAO;
import com.photon.vms.service.RevocationService;
import com.photon.vms.util.JSONUtil;
import com.photon.vms.vo.PendingLeaveRequsetVO;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.RevocationEmailVO;
import com.photon.vms.vo.SuccessResponseVO;
import com.photon.vms.vo.ViewRevocactionResponseVO;

@Service
public class RevocationServiceImpl implements RevocationService{
	
	@Autowired
	RevocationDAO revocationDAO;

	@Override
	public ViewRevocactionResponseVO viewRevocationDetail(String leaveRequestId) {
		ViewRevocactionResponseVO response = new ViewRevocactionResponseVO();
		try {
			response = revocationDAO.viewRevocationDetail(leaveRequestId);
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "View Revocation Service info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	@Override
	public RevocationEmailVO getMailDetails(String leaveRequestId) {
		RevocationEmailVO  response = new RevocationEmailVO();
		try {
			response = revocationDAO.getMailDetails(leaveRequestId);
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Email Revocation Service info", new VmsApplicationException(e.getMessage()));	
		}
		return response;
	}
	
	@Override
	public List<PendingLeaveRequsetVO> getPendingLeaveForAdmin(RequestParameterVO request){
		List<PendingLeaveRequsetVO> pendingList = new ArrayList();
		try {
			pendingList = revocationDAO.getPendingLeaveForAdmin(request);
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Finance Pending Leave Revocation Service info", new VmsApplicationException(e.getMessage()));	
		}
		return pendingList;
	}
	
	@Override
	public SuccessResponseVO revocationApproveAndReject(String leaveRequestId, String employeeId, String leaveStatus, String reason) {
		SuccessResponseVO response = new SuccessResponseVO();
		try {
			response = revocationDAO.revocationApproveAndReject(leaveRequestId, employeeId, leaveStatus, reason);
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Finance Approve Reject Leave Revocation Service info", new VmsApplicationException(e.getMessage()));	
		}
		return response;
	}

}
