/**
 * 
 */
package com.photon.vms.service;

import java.util.ArrayList;
import java.util.Map;

import com.photon.vms.vo.ReportGenerateVO;
import com.photon.vms.vo.RequestParameterVO;

/**
 * @author karthigaiselvan_r
 *
 */
public interface ManagerReviewService {

	public Map<String, ArrayList<?>> getPendingLeaveRequest(String employeeNumber, String reporteeNumber, String reporteeName, String fromDate, String toDate, 
			String appliedDate, String leaveType, String leaveStatus) throws Exception;

	public ArrayList<ReportGenerateVO> generateCSVReport(String employeeNumber, RequestParameterVO requestParameter) throws Exception;

}
