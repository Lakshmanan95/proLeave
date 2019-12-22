package com.photon.vms.controller;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.service.EncashmentService;
import com.photon.vms.vo.EncashmentDetails;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.SuccessResponseVO;

@RestController
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/encashment")
public class EncashmentController {

	@Autowired
	EncashmentService encashmentService;
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/getEncashmentDetails")
	public EncashmentDetails getEncashmentDetails(@RequestParam("employeeNumber") String employeeNumber) {
		EncashmentDetails encashmentDetails = new EncashmentDetails();
		try {
			if(employeeNumber != null) {
				encashmentDetails = encashmentService.getEncashDetails(employeeNumber);
				encashmentDetails.setStatus(CommonConstants.SUCCESS);
			}
			else {
				encashmentDetails.setStatus(CommonConstants.ERROR);
				encashmentDetails.setErrorMessage(CommonConstants.INVALID_DATA);
			}
			VmsLogging.logInfo(getClass(), "Get Encashment Controller");
		}
		catch(Exception  e) {
			VmsLogging.logError(getClass(), "Get Encashment Controller Fails", e);
			encashmentDetails.setStatus(CommonConstants.ERROR);
			encashmentDetails.setErrorMessage(CommonConstants.INVALID_DATA);
		}
		return encashmentDetails;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/encashmentProcess")
	public SuccessResponseVO leaveEncashmentProcess(@RequestBody RequestParameterVO request) {
		SuccessResponseVO response = new SuccessResponseVO();
		try {
			response = encashmentService.leaveEncashmentProcess(request.getEmployeeNumber(), request.getLeaveEncashId());
		}catch(Exception e) {
			VmsLogging.logError(getClass(), "Encashment Process Failed", e);
		}
		return response;
	}
}
