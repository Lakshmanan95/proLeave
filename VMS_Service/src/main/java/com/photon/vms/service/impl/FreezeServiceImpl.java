package com.photon.vms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.dao.FreezeDAO;
import com.photon.vms.service.FreezeService;
import com.photon.vms.vo.UnFreezeResponseVO;

@Service
public class FreezeServiceImpl implements FreezeService{

	@Autowired
	FreezeDAO freezeDAO;

	@Override
	public Map<String, ArrayList<?>> getEmployeeAdminLocation(String employeeNumber) {
		Map<String, ArrayList<?>> response = new HashMap<>();
		try {
			response = freezeDAO.getEmployeeAdminLocation(employeeNumber);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Admin Location info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}

	@Override
	public Map<String, ArrayList<?>> getLeaveUnFreezedDetails(String employeeNumber,int locId) {
		Map<String, ArrayList<?>> response = new HashMap<>();
		try {
			response = freezeDAO.getLeaveUnFreezedDetails(employeeNumber,locId);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Leave Unfreeze info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	@Override
	public UnFreezeResponseVO insertUnfreezeDetails(String loginEmployeeCode, String unfreezeEmployeeCode, int locationId, String fromDate, String toDate,
										String comments, String flag) {
		UnFreezeResponseVO unfreezeResponse = new UnFreezeResponseVO();
		try {
			unfreezeResponse = freezeDAO.insertUnfreezeDetails(loginEmployeeCode, unfreezeEmployeeCode, locationId, fromDate, toDate, comments, flag);
			Map<String, String> employeeDetails = freezeDAO.getEmployeeMail(unfreezeEmployeeCode);
			unfreezeResponse.setEmail(employeeDetails.get("email"));
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Admin Location info", new VmsApplicationException(e.getMessage()));
		} 
		return unfreezeResponse;
	}

	@Override
	public Map<String, ArrayList<?>> getActiveEmployee(String employeeNumber) {
		Map<String, ArrayList<?>> response = new HashMap<>();
		try {
			response = freezeDAO.getActiveEmployee(employeeNumber);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Active info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
}
