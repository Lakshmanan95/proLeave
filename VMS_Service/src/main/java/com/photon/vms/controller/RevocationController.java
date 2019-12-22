package com.photon.vms.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.emailTemplate.EmailService;
import com.photon.vms.emailTemplate.EmailTemplateService;
import com.photon.vms.service.EmployeeHomeService;
import com.photon.vms.service.RevocationService;
import com.photon.vms.util.DateTimeUtils;
import com.photon.vms.vo.FinancePendingRevocation;
import com.photon.vms.vo.LeaveRequestIdInfo;
import com.photon.vms.vo.PendingLeaveRequsetVO;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.RevocationEmailVO;
import com.photon.vms.vo.SuccessResponseVO;
import com.photon.vms.vo.ViewRevocactionResponseVO;

@RestController
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/revocation")
public class RevocationController {

	@Autowired
	RevocationService revocationService;
	
	@Autowired
	EmployeeHomeService employeeHomeService;
	
	@Autowired
	HomeController homeController;
	
	@Autowired
	EmailService emailService;
	@Autowired
	EmailTemplateService emailTemplateService;
	
	@Autowired
	DateTimeUtils dateTimeUtils;
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/viewRevocation")
	public ViewRevocactionResponseVO viewRevocation(@RequestBody RequestParameterVO request) {
		ViewRevocactionResponseVO response = new ViewRevocactionResponseVO();
		try {
			if(request.getEmployeeNumber() != null && !request.getEmployeeNumber().isEmpty()) {
				response = revocationService.viewRevocationDetail(request.getLeaveRequestId());
				String fromDate = dateTimeUtils.convertStringFormat(response.getViewRevocation().getFromDate());
				String toDate = dateTimeUtils.convertStringFormat(response.getViewRevocation().getToDate());
				response.getViewRevocation().setFromDate(fromDate);
				response.getViewRevocation().setToDate(toDate);
				response.setStatus(CommonConstants.SUCCESS);
			}
			else {
				response.setErrorCode(CommonConstants.ERROR);
				response.setErrorMessage(CommonConstants.INVALID_DATA);
			}
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "View Revoication Failed in Controller", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/pendingRevocationListForFinance")
	public FinancePendingRevocation getpendingRevocationListForFinance(@RequestBody RequestParameterVO request) {
		FinancePendingRevocation response = new FinancePendingRevocation();
		try {
			if(request.getEmployeeNumber() != null) {
				RequestParameterVO requestparam = new RequestParameterVO();
				requestparam.setEmployeeNumber(request.getEmployeeNumber());
				requestparam.setReporteeEmployeeNumber(request.getReporteeEmployeeNumber() != null ? request.getReporteeEmployeeNumber() : "");
				requestparam.setReporteeName(request.getReporteeName() != null ? request.getReporteeName() : "");
				requestparam.setFromDate(request.getFromDate() != null ? request.getFromDate() : "");
				requestparam.setToDate(request.getFromDate() != null ? request.getToDate() : "");
				requestparam.setAppliedDate(request.getAppliedDate() != null ? request.getAppliedDate() : "");
				requestparam.setLeaveType(request.getLeaveType() != null ? request.getLeaveType() : "");
				requestparam.setLocationId(String.valueOf(request.getLocationId()) != null ? request.getLocationId() : 0);
				List<PendingLeaveRequsetVO> pendingList = revocationService.getPendingLeaveForAdmin(requestparam);
			
				response.setPendingLeaveList(pendingList);
				VmsLogging.logInfo(getClass(), "Pending Revocation Service");
			}
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Finance Pending Revoication Failed in Controller", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value ="/revocationRequestProcess")
	public SuccessResponseVO revocationApproveAndReject(@RequestBody RequestParameterVO request) {
		SuccessResponseVO response = new SuccessResponseVO();
		try {
			RequestParameterVO requestparam = new RequestParameterVO();
			requestparam.setEmployeeNumber(request.getEmployeeNumber());
			requestparam.setReporteeEmployeeNumber(request.getReporteeEmployeeNumber() != null ? request.getReporteeEmployeeNumber() : "");
			requestparam.setReporteeName(request.getReporteeName() != null ? request.getReporteeName() : "");
			requestparam.setFromDate(request.getFromDate() != null ? request.getFromDate() : "");
			requestparam.setToDate(request.getFromDate() != null ? request.getToDate() : "");
			requestparam.setAppliedDate(request.getAppliedDate() != null ? request.getAppliedDate() : "");
			requestparam.setLeaveType(request.getLeaveType() != null ? request.getLeaveType() : "");
		
			if(!request.getEmployeeNumber().isEmpty() && request.getEmployeeNumber() != null && !request.getLeaveRequestId().isEmpty() && request.getLeaveRequestId() != null ) {
					if(request.getLeaveStatus().equals(CommonConstants.STATUS_REVOKED_APPROVED))
						response = revocationApproveProcess(String.valueOf(request.getEmployeeId()),request.getEmployeeNumber(),request.getLeaveRequestId(),request.getLeaveReason());
					if(request.getLeaveStatus().equals(CommonConstants.STATUS_REVOKED_REJECTED)) {
						response = revocationRejectProcess(String.valueOf(request.getEmployeeId()),request.getEmployeeNumber(),request.getLeaveRequestId(),request.getLeaveStatus(),request.getLeaveReason());
					}
			}
			else {
				response.setStatus(CommonConstants.ERROR);
				response.setErrorMessage(CommonConstants.INVALID_DATA);
			}
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Finance Approved and Reject Revoication Failed in Controller", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	public boolean employeeContainsLeaveId(RequestParameterVO request, String leaveId) {
		List<PendingLeaveRequsetVO> pendingList = revocationService.getPendingLeaveForAdmin(request);
		
		return pendingList.stream().anyMatch(a -> a.getLeaveRequestId().equals(leaveId));
	}
	
	public SuccessResponseVO revocationRejectProcess(String employeeId,String employeeNumber, String leaveRequestId, String leaveStatus, String reason) throws VelocityException, IOException {
		SuccessResponseVO response = new SuccessResponseVO();
		boolean checkForNotAnOwnLeaves = checkforNotAOwnRevocationLeave(employeeNumber, leaveRequestId);
		if(!checkForNotAnOwnLeaves) {
			response.setStatus(CommonConstants.ERROR);
			response.setErrorMessage(CommonConstants.APPROVE_REJECT_OWN_LEAVE_ERROR_MESSAGE);
			return response;
		}			
		response = revocationService.revocationApproveAndReject(leaveRequestId, employeeId, leaveStatus, reason);
		response.setSuccessMessage(CommonConstants.LEAVE_REJECT_MESSAGE);
		RevocationEmailVO getMailDetails = revocationService.getMailDetails(leaveRequestId);
		Map<String, Object> map = new HashMap<>();
		map.put("mailObject", getMailDetails);
		map.put("team", CommonConstants.REVOCATION_FINANCE_ADMIN );
		map.put("title", CommonConstants.REVOCATION_REJECTED_SUBJECT);
		map.put("revokeReason", true);
		map.put("adminSize", 1);
		map.put("status","rejected");
		String emailBody = emailTemplateService.getEmailTemplate("employeeTemplate.vm",map);						
		emailService.sendEmail(getMailDetails.getEmployeeEmail(), CommonConstants.REVOCATION_REJECTED_SUBJECT, emailBody);	
		
		return response;
	}
	
	public SuccessResponseVO revocationApproveProcess(String employeeId,String employeeNumber, String leaveRequestId, String reason) {
		SuccessResponseVO response = new SuccessResponseVO();
		List<RevocationEmailVO> emailList = new ArrayList<>();
		ArrayList<String> leaveRequestList = new ArrayList<>(Arrays.asList(leaveRequestId.split(",")));
		Iterator<String> it = leaveRequestList.iterator();
		boolean checkForOwnLeave = false;
		boolean checkForNotAnOwnLeaves = false;
		String leaveStatus = null;
		try {
			while(it.hasNext()) {
				String leaveId = it.next();
				checkForNotAnOwnLeaves = checkforNotAOwnRevocationLeave(employeeNumber,leaveId);
				if(checkForNotAnOwnLeaves) {
					List<LeaveRequestIdInfo> responseData = employeeHomeService.getLeaveRequestInfo(employeeId, leaveId);
					if(!responseData.isEmpty()) {
						if(responseData.get(0).getLeaveStatus().equals("Pending Revocation"))
							leaveStatus = CommonConstants.STATUS_REVOKED_APPROVED;
						response = revocationService.revocationApproveAndReject(leaveId, employeeId, leaveStatus, reason);
						response.setSuccessMessage(CommonConstants.LEAVE_APPROVED_MESSAGE);
						RevocationEmailVO getMailDetails = revocationService.getMailDetails(leaveId);
						emailList.add(getMailDetails);
					}
				}else {
					checkForOwnLeave = true;
	
				}
			}
			if(checkForOwnLeave) {
				response.setErrorMessage(CommonConstants.APPROVE_REJECT_OWN_LEAVE_ERROR_MESSAGE);
			}
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Finance Approved Revoication Failed in Controller", new VmsApplicationException(e.getMessage()));
		}
		finally{
			 new Thread() {
				    @Override
				    public void run() {
				    	for (RevocationEmailVO data : emailList) {
							homeController.sendBulkRevocationEmailToEmployee(data, 1, CommonConstants.SOURCE_OF_INPUT_LA);
						}
				    }
				}.start();
		}
		return response;
	}
	
	public boolean checkforNotAOwnRevocationLeave(String employeeNumber, String leaveRequestId) {
		boolean checkforNotAOwnLeave = false;
		RevocationEmailVO getMailDetails = revocationService.getMailDetails(leaveRequestId);
		if(!getMailDetails.getEmployeeCode().equals(employeeNumber))
			checkforNotAOwnLeave = true;		
		return checkforNotAOwnLeave;
	}
}
