package com.photon.vms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.dao.EncashmentDAO;
import com.photon.vms.service.EncashmentService;
import com.photon.vms.vo.EncashmentDetails;
import com.photon.vms.vo.SuccessResponseVO;

@Service
public class EncashmentServiceImpl implements EncashmentService{
	
	@Autowired
	EncashmentDAO encashmentDAO;
	

	@Override
	public EncashmentDetails getEncashDetails(String employeeNumber) throws Exception {
		EncashmentDetails encashmentDetails = new EncashmentDetails();
		try {
			encashmentDetails = encashmentDAO.getEncashDetails(employeeNumber);
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Get Encashment Service Fails", e);
		}
		return encashmentDetails;
	}
	
	@Override
	public SuccessResponseVO leaveEncashmentProcess(String employeeNumber, int leaveEncashId) throws Exception {
		SuccessResponseVO response = new SuccessResponseVO();
		try {
			response = encashmentDAO.leaveEncashmentProcess(employeeNumber, leaveEncashId);
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Encashment Process Service Fails", e);
		}
		return response;
	}
}
