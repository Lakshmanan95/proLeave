/**
 * 
 */
package com.photon.vms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.photon.vms.dao.EmployeeHomeDAO;
import com.photon.vms.service.EmployeeHomeService;
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
@Service
public class EmployeeHomeServiceImpl implements EmployeeHomeService{
	
	@Autowired
	EmployeeHomeDAO employeeHomeDAO;
	
	public Map<String, ArrayList<?>> getLoggedUserInfo(String employeeNumber) throws Exception{
		Map<String, ArrayList<?>> response = new HashMap<String, ArrayList<?>>();
		response = employeeHomeDAO.getLoggedUserInfo(employeeNumber);
		return response;
	}

	@Override
	public List<SearchHistoryVO> getTimeoffHistory(String employeeNumber, String appliedDate, String fromDate,
			String toDate, String numberOfDays, String leaveType, String approvedDate, String leaveStatus) throws Exception{
		List<SearchHistoryVO> response = new ArrayList<SearchHistoryVO>();
		try{
			response = employeeHomeDAO.getTimeoffHistory(employeeNumber, appliedDate, fromDate, toDate, numberOfDays, leaveType, approvedDate, leaveStatus);
		} catch (Exception e) {
		}
		return response;
	}

	@Override
	public List<ProcessLeaveRequestVO> processLeaveRequest(String employeeNumber, String leaveRequestId,
			int employeeId, int leaveTypeId, String leaveStatus, String fromdate, String toDate, String numberOfDays,
			String contactNumber, String leaveReason, int odOptionId, String sourceOfInput) throws Exception {
			List<ProcessLeaveRequestVO> response = new ArrayList<ProcessLeaveRequestVO>();
			response = employeeHomeDAO.processLeaveRequest(employeeNumber, leaveRequestId, employeeId, leaveTypeId, leaveStatus, fromdate, toDate, numberOfDays, contactNumber, leaveReason, odOptionId, sourceOfInput);
			return response;
	}

	@Override
	public List<LeaveRequestIdInfo> getLeaveRequestInfo(String employeeNumber, String leaveRequestId) throws Exception {
		List<LeaveRequestIdInfo> response = new ArrayList<LeaveRequestIdInfo>();
		response = employeeHomeDAO.getLeaveRequestInfo(employeeNumber, leaveRequestId);
		return response;
	}

	@Override
	public LeaveTypeVO getLeaveTypeDetail(int leaveTypeId)  throws Exception{
		LeaveTypeVO response = new LeaveTypeVO();
		response = employeeHomeDAO.getLeaveTypeDetail(leaveTypeId);
		return response;
	}

	/*
	 * @Override public ArrayList<SearchHistoryVO> getLeaveList(String employeeId,
	 * String fromdate, String toDate) throws Exception { ArrayList<SearchHistoryVO>
	 * response= new ArrayList<SearchHistoryVO>(); response =
	 * employeeHomeDAO.getLeaveList(employeeId, fromdate, toDate); return response;
	 * }
	 */

	@Override
	public List<FetchLeaveRequestInfoVO> getChildLeaveRequestId(int leaverequestId, String managerId)
			throws Exception {
		List<FetchLeaveRequestInfoVO> response =  new ArrayList<FetchLeaveRequestInfoVO>();
		response = employeeHomeDAO.getChildLeaveRequestId(leaverequestId, managerId);
		return response;
	}

	@Override
	public List<IdByEmailInfoVO> getEmpidFromEmail(String fromAddress) throws Exception {
		List<IdByEmailInfoVO> managerInfo = new ArrayList<IdByEmailInfoVO>();
		managerInfo = employeeHomeDAO.getEmpidFromEmail(fromAddress);
		return managerInfo;
	}
}
