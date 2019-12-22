/**
 * 
 */
package com.photon.vms.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.photon.vms.vo.FetchLeaveRequestInfoVO;
import com.photon.vms.vo.IdByEmailInfoVO;
import com.photon.vms.vo.LeaveRequestIdInfo;
import com.photon.vms.vo.LeaveTypeVO;
import com.photon.vms.vo.ProcessLeaveRequestVO;
import com.photon.vms.vo.SearchHistoryVO;

/**
 * @author karthigaiselvan_r
 *
 */
public interface EmployeeHomeDAO {

	public Map<String, ArrayList<?>> getLoggedUserInfo(String employeeNumber) throws Exception;

	public List<SearchHistoryVO> getTimeoffHistory(String employeeNumber, String appliedDate, String fromDate,
			String toDate, String numberOfDays, String leaveType, String approvedDate, String leaveStatus) throws Exception;

	public List<ProcessLeaveRequestVO> processLeaveRequest(String employeeNumber, String leaveRequestId,
			int employeeId, int leaveTypeId, String leaveStatus, String fromdate, String toDate, String numberOfDays,
			String contactNumber, String leaveReason, int odOptionId, String sourceOfInput) throws Exception;

	public List<LeaveRequestIdInfo> getLeaveRequestInfo(String employeeNumber, String leaveRequestId) throws Exception;

	public LeaveTypeVO getLeaveTypeDetail(int leaveTypeId) throws Exception;

//	public ArrayList<SearchHistoryVO> getLeaveList(String employeeId, String fromdate, String toDate) throws Exception;

	public List<FetchLeaveRequestInfoVO> getChildLeaveRequestId(int leaverequestId, String managerId) throws Exception;

	public List<IdByEmailInfoVO> getEmpidFromEmail(String fromAddress) throws Exception;
}
