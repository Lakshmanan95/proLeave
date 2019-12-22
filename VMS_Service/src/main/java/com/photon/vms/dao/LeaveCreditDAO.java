package com.photon.vms.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.photon.vms.vo.AutoApprovalEmpDtlVO;
import com.photon.vms.vo.LeaveCreditLog;
import com.photon.vms.vo.LeaveHistoryVO;
import com.photon.vms.vo.LeaveType;
import com.photon.vms.vo.LeaveUpdateLWRequest;
import com.photon.vms.vo.RequestHistoryVO;
import com.photon.vms.vo.RequestLeaveInsertUpdateVo;
import com.photon.vms.vo.UnFreezeResponseVO;

public interface LeaveCreditDAO {

	Map<String, ArrayList<?>> getEmployeeLeaveBalance(String employeeNumber) throws Exception;

	Map<String, ArrayList<?>> getLeaveHistory(RequestHistoryVO requestParameter) throws Exception ;

	UnFreezeResponseVO insertUpdateLeave(RequestLeaveInsertUpdateVo requestParameter) throws Exception;
	
	UnFreezeResponseVO bulkUpload(String employeeNumber, String bulkData) throws Exception;
	
	List<LeaveCreditLog> leaveCreditLog(String leaveCreeditId) throws Exception;
	
	UnFreezeResponseVO leaveUpdateLW(LeaveUpdateLWRequest request) throws Exception;

	ArrayList<LeaveHistoryVO> getAdminReport(String employeeNumber,String flag,String fromDate,String toDate) throws Exception;

	List<LeaveType> getLeaveTypeByLocation(int locationId) throws Exception;

	 Map<String, ArrayList<?>> getLeaveCreditCOFFDate(String employeeNumber) throws Exception;

	Map<String, ArrayList<?>> getAutoApprovalEmpDtl() throws Exception;
	
	UnFreezeResponseVO autoApproval(String loginEmployeeCode, String employeeCode, int isActive) throws Exception;
	
	List<AutoApprovalEmpDtlVO> getAutoApprovalEmployee() throws Exception;
}
