package com.photon.vms.dao;

import java.util.List;

import com.photon.vms.vo.LeaveReminderInfo;
import com.photon.vms.vo.RevocationEmailVO;

public interface AdminDAO {

	boolean isValidUnfrozenRecords(String fromdate, String toDate, int employeeId) throws Exception;

	List<LeaveReminderInfo> getReminderLeaveList() throws Exception;

	List<RevocationEmailVO> getRevocationListForAdmin() throws Exception;
}
