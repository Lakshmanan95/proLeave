package com.photon.vms.service;

import java.util.ArrayList;
import java.util.Map;

import com.photon.vms.vo.UnFreezeResponseVO;

public interface FreezeService {

	Map<String, ArrayList<?>> getEmployeeAdminLocation(String employeeNumber);

	Map<String, ArrayList<?>> getLeaveUnFreezedDetails(String employeeNumber,int locId);

	UnFreezeResponseVO insertUnfreezeDetails(String loginEmployeeCode, String unfreezeEmployeeCode,int locationId, String fromDate, String toDate,
			String comments, String flag);

	Map<String, ArrayList<?>> getActiveEmployee(String employeeNumber);
}
