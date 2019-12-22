package com.photon.vms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.photon.vms.vo.FinanceReportResponse;
import com.photon.vms.vo.FinanceResponceCompOffVO;
import com.photon.vms.vo.FinanceResponceIndiaSalesVO;
import com.photon.vms.vo.FinanceResponceLeaveBalanceVO;
import com.photon.vms.vo.FinanceResponceLeaveRequestVO;
import com.photon.vms.vo.FinanceResponceUsedLeaveVO;
import com.photon.vms.vo.ReportDropDownResponse;
import com.photon.vms.vo.SearchHierarchy;
import com.photon.vms.vo.SearchHierarchyRequestVO;

public interface FinanceService {

	ArrayList<FinanceResponceUsedLeaveVO> getFinanceResponceUsedLeaveReport(String companyName, String employeeNumber, String leaveTypeId,String reportType, String fromDate, String toDate,int activeStatus);

	FinanceReportResponse getFinanceReportEntity();

	ArrayList<FinanceResponceLeaveRequestVO> getFinanceResponceLeaveRequestReport(String companyName, String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus);

	ArrayList<FinanceResponceLeaveBalanceVO> getFinanceResponceLeaveBalanceReport(String companyName,
			String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus);

	ArrayList<FinanceResponceCompOffVO> getFinanceResponceCompOffReport(String companyName, String employeeNumber,
			String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus);

	ArrayList<FinanceResponceIndiaSalesVO> getFinanceResponceIndiaSalesReport(String companyName, String employeeNumber,
			String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus);

	ReportDropDownResponse getReportDropDown(String employeeNumber, String flag);
	
	List<SearchHierarchy> searchAndReportHierarchy(SearchHierarchyRequestVO request);

	List<FinanceResponceLeaveBalanceVO> getLeaveBalanceReportByHierarchy(String loginEmployeeCode, String employeeCode, String flag); 
}
