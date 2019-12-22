package com.photon.vms.service;

import java.util.List;

import com.photon.vms.vo.LeaveReminderInfo;
import com.photon.vms.vo.RevocationEmailVO;

public interface AdminService {

	public boolean isValidUnfrozenRecords(String fromdate, String toDate, int employeeId) throws Exception;

	public List<LeaveReminderInfo> getReminderLeaveList() throws Exception;
	
	public List<RevocationEmailVO> getRevocationListForAdmin() throws Exception;
}
