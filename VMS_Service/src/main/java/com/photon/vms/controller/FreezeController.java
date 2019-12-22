package com.photon.vms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

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
import com.photon.vms.service.FreezeService;
import com.photon.vms.util.DateTimeUtils;
import com.photon.vms.util.JSONUtil;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.RequestUnfreezeVO;
import com.photon.vms.vo.UnFreezeRecords;
import com.photon.vms.vo.UnFreezeRequestProcessVO;
import com.photon.vms.vo.UnFreezeResponseVO;

@RestController
@CrossOrigin("*")
@RequestMapping("/freeze")
public class FreezeController {

	@Autowired
	FreezeService freezeService;
	
	@Autowired
	EmailTemplateService emailTemplateService;
	
	@Autowired
	EmailService emailService;
	@Autowired
	DateTimeUtils dateTimeUtils;
		
	@RequestMapping(value="/getEmployeeAdminLocation", method = RequestMethod.POST)
	public Map<String, ArrayList<?>> getLoggedUserInfo(@RequestBody RequestParameterVO requestParameter,ServletRequest req){
		try{
			String employeeNumber = requestParameter.getEmployeeNumber();
			VmsLogging.logInfo(getClass(), "Employee - Admin info "+employeeNumber);
			Map<String, ArrayList<?>> response = null;
			response = freezeService.getEmployeeAdminLocation(employeeNumber);
			return response;
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Employee - Admin Location info Exception ", new VmsApplicationException(e.getMessage()));
			return null;
		}
	}
	
	@RequestMapping(value="/getLeaveUnFreezedDetails", method = RequestMethod.POST)
	public Map<String, ArrayList<?>> getLeaveUnFreezedDetails(@RequestBody RequestParameterVO requestParameter,ServletRequest req){
		try{
			String employeeNumber = requestParameter.getEmployeeNumber();
			int locId = requestParameter.getLocationId();
			VmsLogging.logInfo(getClass(), "Employee Leave UnFreezed info "+employeeNumber +" Location Id Info "+locId);
			Map<String, ArrayList<?>> response = null;
			response = freezeService.getLeaveUnFreezedDetails(employeeNumber,locId);
			return response;
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Employee Leave UnFreezed info Exception ", new VmsApplicationException(e.getMessage()));
			return null;
		}
	}
	
	@RequestMapping(value="/insertUnfreezeDetails", method = RequestMethod.POST)
	public UnFreezeRequestProcessVO  insertUnfreezeDetails(@RequestBody RequestUnfreezeVO requestParameter,ServletRequest req) {
		UnFreezeRequestProcessVO response = new UnFreezeRequestProcessVO();
		try {
			VmsLogging.logInfo(getClass(), "Record request "+JSONUtil.toJson(requestParameter));
			String employeeNumber = requestParameter.getEmployeeNumber();
			String successMessage = null;
			String validationMessage = null;
			List<Integer> dateIndex = new ArrayList<>();
			List<String> duplicateDates = new ArrayList<>();
			 StringBuilder sb= new StringBuilder();
			if(employeeNumber != null) {		
				if(!requestParameter.getUnfreezeRecords().isEmpty() ) {
					successMessage = CommonConstants.UNFREEZED_UPDATE;
					validationMessage = CommonConstants.UNFREEZED_VALIDATION_ADMIN;
					for(UnFreezeRecords records : requestParameter.getUnfreezeRecords()) {
						records.setLoginEmployeeCode(employeeNumber);
						UnFreezeResponseVO resultResponse = insertUnfreezeRecord(records);
						if(!resultResponse.isValidation() || resultResponse.getResultCode().isEmpty() || resultResponse.getResultCode() == null ) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorCode("03");
							response.setErrorMessage(CommonConstants.INVALID_DATA);
							return response;
						}
						else if(resultResponse.getResultCode().equals("ERROR")) {
							String fromDate = dateTimeUtils.convertUnfreezeDates(records.getFromDate());
							String toDate = dateTimeUtils.convertUnfreezeDates(records.getToDate());
							sb.append(fromDate).append(" to ").append(toDate);
							dateIndex.add(records.getIndex());
							duplicateDates.add(fromDate+" to "+toDate);
						}
					}					
				}
				else if(requestParameter.getRecord() != null) {
					successMessage = CommonConstants.UNFREEZED_SUCCESS;
					validationMessage = CommonConstants.UNFREEZED_VALIDATION_EMPLOYEE;
					requestParameter.getRecord().setLoginEmployeeCode(employeeNumber);
					UnFreezeResponseVO resultResponse = insertUnfreezeRecord(requestParameter.getRecord());
					String[] resultSet = {};
					if(resultResponse.getResultCode() != null && !resultResponse.getResultCode().isEmpty())
						resultSet = resultResponse.getResultCode().split("-");
					if(!resultResponse.isValidation()|| resultResponse.getResultCode().isEmpty() || resultResponse.getResultCode() == null ) {
						response.setStatus(CommonConstants.ERROR);
						response.setErrorCode("03");
						response.setErrorMessage(CommonConstants.INVALID_DATA);
						return response;
					}
					else if(resultSet[0].equals("ERROR")) {
						if(resultSet[1].equals("002"))
							validationMessage = CommonConstants.UNFREEZED_VALIDATION_EMPLOYEE_LOCATION;
						String fromDate = dateTimeUtils.convertUnfreezeDates(requestParameter.getRecord().getFromDate());
						String toDate = dateTimeUtils.convertUnfreezeDates(requestParameter.getRecord().getToDate());
						sb.append(fromDate).append(" to ").append(toDate);
					}
					if(resultResponse.getResultCode().equals("SUCCESS")) {
						String fromDate = dateTimeUtils.convertStringDateFormat(requestParameter.getRecord().getFromDate());
						String toDate = dateTimeUtils.convertStringDateFormat(requestParameter.getRecord().getToDate());
						Map<String, Object> map = new HashMap<>();
						map.put("fromDate", fromDate);
						map.put("toDate", toDate);
						String emailBody = emailTemplateService.getEmailTemplate("unfreezeTemplate.vm",map);						
						emailService.sendEmail(resultResponse.getEmail(), CommonConstants.UNFREEZED_MAIL_SUBJECT, emailBody);					
					}
				}
				if(sb.length() > 0) {
					response.setStatus(CommonConstants.FAILURE);	
					response.setErrorCode("03");
					if(requestParameter.getRecord() != null)
						response.setValidationMessage(validationMessage+sb.toString());
					else
						response.setValidationMessage(validationMessage);
					response.setDuplicateDateIndex(dateIndex);
					response.setDuplicateDates(duplicateDates);
				}
				else {
					response.setStatus(CommonConstants.SUCCESS);
					response.setSuccessMessage(successMessage);
				}
			}
			else {
				response.setStatus(CommonConstants.ERROR);
				response.setErrorCode("03");
				response.setErrorMessage(CommonConstants.INVALID_DATA);
				VmsLogging.logError(getClass(), CommonConstants.INVALID_DATA, null);
			}
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Insert Unfreeze Record Failed", e);
		}
		return response;
	}
		
	@RequestMapping(value="/getActiveEmployee", method = RequestMethod.POST)
	public Map<String, ArrayList<?>> getActiveEmployee(@RequestBody RequestParameterVO requestParameter,ServletRequest req){
		try{
			Map<String, ArrayList<?>> response = null;
			String employeeNumber = requestParameter.getEmployeeNumber();
			VmsLogging.logInfo(getClass(), "LMS Active Employee info " +employeeNumber);
			response = freezeService.getActiveEmployee(employeeNumber);
			return response;
		}catch(Exception e){
			VmsLogging.logError(getClass(), "LMS Active Employee info Exception ", new VmsApplicationException(e.getMessage()));
			return null;
		}
	}
	
	public UnFreezeResponseVO insertUnfreezeRecord(UnFreezeRecords records) {
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		try {
			int locationId = 0;
			String unfreezeEmployeeCode = null;
			String loginEmployeeCode = (records.getLoginEmployeeCode() != null?records.getLoginEmployeeCode():null);
			String fromDate = (records.getFromDate()!=null?records.getFromDate():"");
			String toDate = (records.getToDate()!=null?records.getToDate():"");
			String comments = (records.getComments()!=null?records.getComments():"");
			String flag = (records.getFlag() != null ? records.getFlag():"");
			locationId = (records.getLocationId() != 0? records.getLocationId():0);
			unfreezeEmployeeCode = (records.getUnfreezeEmployeeCode() != null?records.getUnfreezeEmployeeCode():null);
			boolean result = checkUnfreezeValidation(flag, unfreezeEmployeeCode, locationId);
			if(!result) {
				response.setValidation(result);
				return response;
			}
			response = freezeService.insertUnfreezeDetails(loginEmployeeCode, unfreezeEmployeeCode, locationId, fromDate, 
					toDate, comments, flag);
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Insert Unfreeze Record Failed ", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	public boolean checkUnfreezeValidation(String flag, String unfreezeEmployeeCode, int locationId) {
		boolean result = true;
		if(flag.equals("LW")) {
			if(locationId == 0) {
				return false;
			}
		}
		else if(flag.equals("EW")) {
			if(unfreezeEmployeeCode == null || unfreezeEmployeeCode.isEmpty() || unfreezeEmployeeCode.equals("null")) {
				return false;
			}		
		}
		return result;
	}
}
