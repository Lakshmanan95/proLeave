package com.photon.vms.dao;

import java.util.ArrayList;
import java.util.Map;

import com.photon.vms.vo.UnFreezeResponseVO;

public interface FreezeDAO {

	Map<String, ArrayList<?>> getEmployeeAdminLocation(String employeeNumber) throws Exception;

	Map<String, ArrayList<?>> getLeaveUnFreezedDetails(String employeeNumber,int locId)throws Exception;
	
	UnFreezeResponseVO insertUnfreezeDetails(String loginEmployeeCode, String unfreezeEmployeeCode, int locationId, String fromDate, String toDate,
			String comments, String flag) throws Exception;
	Map<String, String> getEmployeeMail(String unfreezeEmployeeCode) throws Exception;	
	Map<String, ArrayList<?>> getActiveEmployee(String employeeNumber) throws Exception;
}
