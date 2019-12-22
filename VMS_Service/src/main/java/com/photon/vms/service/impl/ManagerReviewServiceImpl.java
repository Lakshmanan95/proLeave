/**
 * 
 */
package com.photon.vms.service.impl;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.photon.vms.dao.ManagerReviewDAO;
import com.photon.vms.service.ManagerReviewService;
import com.photon.vms.vo.ReportGenerateVO;
import com.photon.vms.vo.RequestParameterVO;

/**
 * @author karthigaiselvan_r
 *
 */
@Service
public class ManagerReviewServiceImpl implements ManagerReviewService{
	
	@Autowired
	ManagerReviewDAO managerReviewDAO;
	
	@Override
	public Map<String, ArrayList<?>> getPendingLeaveRequest(String employeeNumber, String reporteeNumber, String reporteeName, String fromDate, String toDate, 
			String appliedDate, String leaveType, String leaveStatus) throws Exception {
		Map<String, ArrayList<?>> response = managerReviewDAO.getPendingLeaveRequest(employeeNumber, reporteeNumber, reporteeName, fromDate, toDate, appliedDate, leaveType,leaveStatus);
		return response;
	}

	@Override
	public ArrayList<ReportGenerateVO> generateCSVReport(String employeeNumber, RequestParameterVO requestParameter) throws Exception {
		ArrayList<ReportGenerateVO> reportGenerateVO = managerReviewDAO.generateCSVReport(employeeNumber, requestParameter);
		return reportGenerateVO;
	}

}
