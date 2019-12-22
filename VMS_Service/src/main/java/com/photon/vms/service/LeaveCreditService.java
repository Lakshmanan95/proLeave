package com.photon.vms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.photon.vms.vo.AutoApprovalEmpDtlVO;
import com.photon.vms.vo.LeaveCreditDataVO;
import com.photon.vms.vo.LeaveCreditLog;
import com.photon.vms.vo.LeaveHistoryVO;
import com.photon.vms.vo.LeaveType;
import com.photon.vms.vo.LeaveUpdateLWRequest;
import com.photon.vms.vo.RequestHistoryVO;
import com.photon.vms.vo.RequestLeaveInsertUpdateVo;
import com.photon.vms.vo.UnFreezeResponseVO;

public interface LeaveCreditService {

	Map<String, ArrayList<?>> getEmployeeLeaveBalance(String employeeNumber);

	Map<String, ArrayList<?>> getLeaveHistory(RequestHistoryVO requestParameter);

	UnFreezeResponseVO insertUpdateLeave(RequestLeaveInsertUpdateVo requestParameter);
	
	UnFreezeResponseVO bulkUpload(String employeeNumber,LeaveCreditDataVO leaveList);
	
	List<LeaveCreditLog> leaveCreditLog(String leaveCreditId);
	
	UnFreezeResponseVO leaveUpdateLW(LeaveUpdateLWRequest request);

	ArrayList<LeaveHistoryVO> getAdminReport(String employeeNumber,String flag,String fromDate,String toDate);

	List<LeaveType> getLeaveTypeByLocation(int locationId);

	 Map<String, ArrayList<?>> getLeaveCreditCOFFDate(String employeeNumber);

	Map<String, ArrayList<?>> getAutoApprovalEmpDtl();
	
	UnFreezeResponseVO autoApproval(String loginEmployeeCode, String employeeCode, int isActive);
	
	List<AutoApprovalEmpDtlVO> getAutoApprovalEmployee();
}
