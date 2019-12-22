package com.photon.vms.dao;

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

public interface FinanceDAO {

	ArrayList<FinanceResponceUsedLeaveVO> getFinanceResponceUsedLeaveReport(String companyName, String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus) throws Exception ;

	FinanceReportResponse getFinanceReportEntity() throws Exception;

	ArrayList<FinanceResponceLeaveRequestVO> getFinanceResponceLeaveRequestReport(String companyName, String employeeNumber,
			String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus) throws Exception;

	ArrayList<FinanceResponceLeaveBalanceVO> getFinanceResponceLeaveBalanceReport(String companyName,
			String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus)  throws Exception ;

	ArrayList<FinanceResponceCompOffVO> FinanceResponceCompOffVO(String companyName, String employeeNumber,
			String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus) throws Exception;

	ArrayList<FinanceResponceIndiaSalesVO> getFinanceResponceIndiaSalesReport(String companyName, String employeeNumber,
			String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus) throws Exception;

	ReportDropDownResponse getReportDropDown(String employeeNumber, String flag) throws Exception;
	
	List<SearchHierarchy> searchAndReportHierarchy(SearchHierarchyRequestVO request) throws Exception;
	
	List<FinanceResponceLeaveBalanceVO> getLeaveBalanceReportByHierarchy(String loginEmployeeCode, String employeeCode, String flag) throws Exception;
	
}
