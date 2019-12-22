package com.photon.vms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.dao.FinanceDAO;
import com.photon.vms.util.JSONUtil;
import com.photon.vms.util.OnlineUtils;
import com.photon.vms.vo.EmployeeDetailVO;
import com.photon.vms.vo.FinanceReportResponse;
import com.photon.vms.vo.FinanceResponceCompOffVO;
import com.photon.vms.vo.FinanceResponceIndiaSalesVO;
import com.photon.vms.vo.FinanceResponceLeaveBalanceVO;
import com.photon.vms.vo.FinanceResponceLeaveRequestVO;
import com.photon.vms.vo.FinanceResponceUsedLeaveVO;
import com.photon.vms.vo.LeaveStatusVO;
import com.photon.vms.vo.LeaveType;
import com.photon.vms.vo.ReportDropDownResponse;
import com.photon.vms.vo.SearchHierarchy;
import com.photon.vms.vo.SearchHierarchyRequestVO;

@Service
public class FinanceDAOImpl implements FinanceDAO {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private Environment env;
	
	@Override
	public ArrayList<FinanceResponceUsedLeaveVO> getFinanceResponceUsedLeaveReport(String companyName, String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<FinanceResponceUsedLeaveVO> financeReportInfo = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.get_LMS_Fin_Leave_Report") + "(?,?,?,?,?,?,?)}");
			if(companyName.equals("null")) {
				pstmt.setString(1, null);	
			}else {
				pstmt.setString(1, companyName);
			}
			if(employeeNumber.equals("null")) {
				pstmt.setString(2, null);	
			}else {
				pstmt.setString(2, employeeNumber);
			}
			if(leaveTypeId.equals("null")) {
			pstmt.setString(3, null);
			}else {
				pstmt.setString(3, leaveTypeId);	
			}
			pstmt.setString(4, reportType);
			pstmt.setString(5, fromDate);
			pstmt.setString(6, toDate);
			pstmt.setInt(7, activeStatus);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Finance Leave Report info" + resultSet);
				while (resultSet.next()) {
					FinanceResponceUsedLeaveVO financeReport = new FinanceResponceUsedLeaveVO();
					financeReport.setEmployeeNumber(resultSet.getString("Employee_Code") != null ? resultSet.getString("Employee_Code") : "");
					financeReport.setEmployeeName(resultSet.getString("Employee_Name") != null ? resultSet.getString("Employee_Name") : "");
					financeReport.setLeaveType(resultSet.getString("Leave_Type") != null ? resultSet.getString("Leave_Type") : "");
					financeReport.setLeaveDate(resultSet.getString("Leave_Date") != null ? resultSet.getString("Leave_Date") : "");
					financeReport.setApprovedDate(resultSet.getString("Approved_Date") != null ? resultSet.getString("Approved_Date") : "");
					financeReport.setNoOfDays(resultSet.getString("No_of_Days") != null ? resultSet.getString("No_of_Days") : "");
					financeReportInfo.add(financeReport);
			}
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance Leave Report info", new VmsApplicationException(e.getMessage()));
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return financeReportInfo;
	}
	
	@Override
	public FinanceReportResponse getFinanceReportEntity() throws Exception{
		FinanceReportResponse response = new FinanceReportResponse();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		List<LeaveType> leaveType = new ArrayList<>();
		List<String> companyName = new ArrayList<>();
		List<String> reportName = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.Get_Payroll_rpt_Combo_Dtl") +"}");
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()){
				companyName.add(resultSet.getString("company_name"));
			}
			if(pstmt.getMoreResults()) {
				resultSet = pstmt.getResultSet();
				while(resultSet.next()) {
					LeaveType leave = new LeaveType();
					leave.setLeaveCode(resultSet.getString("Leave_Code"));
					leave.setLeaveType(resultSet.getString("Leave_Type"));
					leave.setLeaveTypeId(Integer.parseInt(resultSet.getString("Leave_Type_Id")));
					leaveType.add(leave);
				}
			}
			if(pstmt.getMoreResults()) {
				resultSet = pstmt.getResultSet();
				while(resultSet.next()){					
					reportName.add(resultSet.getString("report_name"));
				}
			}
			response.setCompanyName(companyName);
			response.setLeaveType(leaveType);
			response.setReportName(reportName);
			
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance Report Entity DAO info", new VmsApplicationException(e.getMessage()));
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return response;
	}

	@Override
	public ArrayList<FinanceResponceLeaveRequestVO> getFinanceResponceLeaveRequestReport(String companyName,
			String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<FinanceResponceLeaveRequestVO> financeReportInfo = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.get_LMS_Fin_Leave_Report") + "(?,?,?,?,?,?,?)}");
			if(companyName.equals("null")) {
				pstmt.setString(1, null);	
			}else {
				pstmt.setString(1, companyName);
			}
			if(employeeNumber.equals("null")) {
				pstmt.setString(2, null);	
			}else {
				pstmt.setString(2, employeeNumber);
			}
			if(leaveTypeId.equals("null")) {
			pstmt.setString(3, null);
			}else {
				pstmt.setString(3, leaveTypeId);	
			}
			pstmt.setString(4, reportType);
			pstmt.setString(5, fromDate);
			pstmt.setString(6, toDate);
			pstmt.setInt(7, activeStatus);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Finance Leave Report info" + resultSet);
				while (resultSet.next()) {
					FinanceResponceLeaveRequestVO financeReport = new FinanceResponceLeaveRequestVO();
					financeReport.setEmployeeNumber(resultSet.getString("employee_code"));
					financeReport.setEmployeeName(resultSet.getString("employee_name"));
					financeReport.setLeaveType(resultSet.getString("Leave_Type"));
					financeReport.setFromDate(resultSet.getString("From_Date"));
					financeReport.setToDate(resultSet.getString("To_Date"));
					financeReport.setNoOfDays(resultSet.getString("No_of_Days"));
					financeReport.setLeaveStatus(resultSet.getString("leave_status"));
					financeReport.setSubmittedDate(resultSet.getString("Submitted_Date"));
					financeReport.setApprovedBy(resultSet.getString("Approved_By"));
					financeReport.setApprovedDate(resultSet.getString("Approved_Date"));
					financeReport.setReason(resultSet.getString("Reason"));
					financeReport.setLocationName(resultSet.getString("Location_Name"));
					financeReport.setEmployeeStatus(resultSet.getString("employee_status"));
					financeReport.setCancelledByCode(resultSet.getString("Cancelled_By_Code"));
					financeReport.setCancelledByName(resultSet.getString("Cancelled_By_name"));
					financeReport.setCancelledDate(resultSet.getString("Cancelled_Date"));
					financeReport.setCancelledReason(resultSet.getString("Cancelled_Reason"));
					financeReport.setRevokedByCode(resultSet.getString("Revoked_By_Code"));
					financeReport.setRevokedByName(resultSet.getString("Revoked_By_Name"));
					financeReport.setRevokedReason(resultSet.getString("Revoked_Reason"));
					financeReport.setRejectedByCode(resultSet.getString("Rejected_By_Code"));
					financeReport.setRejectedByName(resultSet.getString("Rejected_By_name"));
					financeReport.setRejectedDate(resultSet.getString("Rejected_Date"));
					financeReport.setRejectedReason(resultSet.getString("Rejected_Reason"));
					financeReport.setStatus1(resultSet.getString("Status1"));
					financeReport.setStatus2(resultSet.getString("Status2"));
					if(reportType.equals("Leave Request")) {
						financeReport.setRevocationAprRejReason(resultSet.getString("revocation_apr_rej_reason"));
						financeReport.setRevocationAprRejDate(resultSet.getString("revocation_apr_rej_date"));
					}
					financeReportInfo.add(financeReport);
			}
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance Leave Report info", new VmsApplicationException(e.getMessage()));
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return financeReportInfo;
	}

	@Override
	public ArrayList<FinanceResponceLeaveBalanceVO> getFinanceResponceLeaveBalanceReport(String companyName,
			String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus)  throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<FinanceResponceLeaveBalanceVO> financeReportInfo = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.get_LMS_Fin_Leave_Report") + "(?,?,?,?,?,?,?)}");
			if(companyName.equals("null")) {
				pstmt.setString(1, null);	
			}else {
				pstmt.setString(1, companyName);
			}
			if(employeeNumber.equals("null")) {
				pstmt.setString(2, null);	
			}else {
				pstmt.setString(2, employeeNumber);
			}
			if(leaveTypeId.equals("null")) {
			pstmt.setString(3, null);
			}else {
				pstmt.setString(3, leaveTypeId);	
			}
			pstmt.setString(4, reportType);
			pstmt.setString(5, fromDate);
			pstmt.setString(6, toDate);
			pstmt.setInt(7, activeStatus);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Finance Leave Report info" + resultSet);
				while (resultSet.next()) {
					FinanceResponceLeaveBalanceVO financeReport = new FinanceResponceLeaveBalanceVO();
					financeReport.setEmployeeNumber(resultSet.getString("Employee_Code"));
					financeReport.setEmployeeName(resultSet.getString("Employee_Name"));
					financeReport.setLeaveType(resultSet.getString("Leave_Type"));
					financeReport.setOpen(resultSet.getDouble("Open"));
					financeReport.setCredit(resultSet.getDouble("Credit"));
					financeReport.setUsed(resultSet.getDouble("Used"));
					financeReport.setBalance(resultSet.getDouble("Balance"));
					financeReport.setLocationName(resultSet.getString("Location_Name") != null ? resultSet.getString("Location_Name") : "");
					financeReport.setEmployeeStatus(resultSet.getString("Employee_Status") != null ? resultSet.getString("Employee_Status") : "");
					financeReportInfo.add(financeReport);
			}
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance Leave Report info", new VmsApplicationException(e.getMessage()));
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return financeReportInfo;
	}

	@Override
	public ArrayList<FinanceResponceCompOffVO> FinanceResponceCompOffVO(String companyName,
			String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus) throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<FinanceResponceCompOffVO> financeReportInfo = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.get_LMS_Fin_Leave_Report") + "(?,?,?,?,?,?,?)}");
			if(companyName.equals("null")) {
				pstmt.setString(1, null);	
			}else {
				pstmt.setString(1, companyName);
			}
			if(employeeNumber.equals("null")) {
				pstmt.setString(2, null);	
			}else {
				pstmt.setString(2, employeeNumber);
			}
			if(leaveTypeId.equals("null")) {
			pstmt.setString(3, null);
			}else {
				pstmt.setString(3, leaveTypeId);	
			}
			pstmt.setString(4, reportType);
			pstmt.setString(5, fromDate);
			pstmt.setString(6, toDate);
			pstmt.setInt(7, activeStatus);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Finance Leave Report info" + resultSet);
				while (resultSet.next()) {
					FinanceResponceCompOffVO financeReport = new FinanceResponceCompOffVO();
					financeReport.setEmployeeNumber(resultSet.getString("employee_code"));
					financeReport.setEmployeeName(resultSet.getString("employee_name"));
					financeReport.setCompOffDate(resultSet.getString("Comp_Off_date"));
					financeReport.setCreatedBy(resultSet.getString("created_by"));
					financeReport.setCreatedDate(resultSet.getString("created_date"));
					financeReport.setLocationName(resultSet.getString("Location_Name"));
					financeReport.setEmployeeStatus(resultSet.getString("Employee_status"));
					financeReportInfo.add(financeReport);
			}
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance Leave Report info", new VmsApplicationException(e.getMessage()));
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return financeReportInfo;
	}

	@Override
	public ArrayList<FinanceResponceIndiaSalesVO> getFinanceResponceIndiaSalesReport(String companyName,
			String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus)
			throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<FinanceResponceIndiaSalesVO> financeReportInfo = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.get_LMS_Fin_Leave_Report") + "(?,?,?,?,?,?,?)}");
			if(companyName.equals("null")) {
				pstmt.setString(1, null);	
			}else {
				pstmt.setString(1, companyName);
			}
			if(employeeNumber.equals("null")) {
				pstmt.setString(2, null);	
			}else {
				pstmt.setString(2, employeeNumber);
			}
			if(leaveTypeId.equals("null")) {
			pstmt.setString(3, null);
			}else {
				pstmt.setString(3, leaveTypeId);	
			}
			pstmt.setString(4, reportType);
			pstmt.setString(5, fromDate);
			pstmt.setString(6, toDate);
			pstmt.setInt(7, activeStatus);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Finance Leave Report info" + resultSet);
				while (resultSet.next()) {
					FinanceResponceIndiaSalesVO financeReport = new FinanceResponceIndiaSalesVO();
					financeReport.setEmployeeNumber(resultSet.getString("Employee_code"));
					financeReport.setEmployeeName(resultSet.getString("Employee_Name"));
					financeReport.setDepartmentName(resultSet.getString("department_name"));
					financeReport.setEmployeeStatus(resultSet.getString("employee_status"));
					financeReportInfo.add(financeReport);
			}
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance Leave Report info", new VmsApplicationException(e.getMessage()));
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return financeReportInfo;
	}

	@Override
	public ReportDropDownResponse getReportDropDown(String employeeNumber, String flag) throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<LeaveType> leaveTypeInfo = new ArrayList<>();
		ArrayList<LeaveStatusVO> leaveStatusInfo = new ArrayList<>();
		ArrayList<EmployeeDetailVO> employeeInfo = new ArrayList<>();
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		ReportDropDownResponse response = new ReportDropDownResponse();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.Get_LMS_Report_Home_Page_Dtl") + "(?,?)}");
			pstmt.setString(1, employeeNumber);
			pstmt.setString(2, flag);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Report Employee info " + resultSet);
			while(resultSet.next()){
				LeaveType leaveType = new LeaveType();
				leaveType.setLeaveTypeId(resultSet.getInt("Leave_Type_Id"));
				leaveType.setLeaveCode(resultSet.getString("Leave_Code"));
				leaveType.setLeaveType(resultSet.getString("Leave_Type"));
				leaveTypeInfo.add(leaveType);
			}
//			resultmap.put("LeaveTypeInfo", leaveTypeInfo);
			response.setLeaveTypeInfo(leaveTypeInfo);
			if(pstmt.getMoreResults()){
				resultSet = pstmt.getResultSet();
				while(resultSet.next()){
					LeaveStatusVO leaveStatusVO = new LeaveStatusVO();
					leaveStatusVO.setLeaveStatusId(resultSet.getInt("Leave_Status_Id"));
					leaveStatusVO.setLeaveStatusCode(resultSet.getString("Leave_Status_Code"));
					leaveStatusVO.setLeaveStatus(resultSet.getString("Leave_Status"));
					leaveStatusInfo.add(leaveStatusVO);
				}
			}
//			resultmap.put("LeaveStatusInfo",leaveStatusInfo);
			response.setLeaveStatusInfo(leaveStatusInfo);
			if(pstmt.getMoreResults()){
				resultSet = pstmt.getResultSet();
				while(resultSet.next()){
					EmployeeDetailVO employeeDetailVO = new EmployeeDetailVO();
					employeeDetailVO.setEmployeeId(resultSet.getInt("Employee_Id"));
					employeeDetailVO.setEmployeeCode(resultSet.getString("Employee_code"));
					employeeDetailVO.setEmployeeName(resultSet.getString("Employee_Name"));
					employeeInfo.add(employeeDetailVO);
				}
			}
//			resultmap.put("EmployeeInfo",employeeInfo);
			response.setEmployeeInfo(employeeInfo);
			return response;
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Report Employee info ", new VmsApplicationException(e.getMessage()));
			return null;
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
	}
	
	@Override
	public List<SearchHierarchy> searchAndReportHierarchy(SearchHierarchyRequestVO request) throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		List<SearchHierarchy> searchList = new ArrayList<SearchHierarchy>();
		String property = null;
		if(request.getByRows() == 0)
			property = env.getProperty("sp.Get_LMS_Report_Search_Dtl");
		else
			property = env.getProperty("sp.Get_LMS_Report_Export_Rowwise");
			
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + property + "(?,?,?,?,?,?,?,?,?)}");
			pstmt.setString(1, request.getLoginEmployeeCode());			
			pstmt.setString(2, request.getLeaveFromDate());
			pstmt.setString(3, request.getLeaveToDate());
			pstmt.setString(4, request.getLeaveAppliedOn());
			pstmt.setString(5, request.getLeaveReviewedOn());
			pstmt.setString(6, request.getEmployeeCode());
			pstmt.setString(7, request.getLeaveTypeId());
			pstmt.setString(8, request.getLeaveStatusId());
			pstmt.setString(9, request.getFlag());
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()) {
				SearchHierarchy search = new SearchHierarchy();
				search.setEmployeeCode(resultSet.getString("employee_code"));
				search.setEmployeeName(resultSet.getString("employee_name"));
				search.setAppliedOn(resultSet.getString("applied_on"));
				if(request.getByRows() != 0)
					search.setLeaveDate(resultSet.getString("leave_date"));
				search.setFromDate(resultSet.getString("from_date"));
				search.setToDate(resultSet.getString("to_date"));
				search.setNoOfDays(resultSet.getDouble("no_of_days"));
				search.setLeaveType(resultSet.getString("leave_type"));
				search.setReason(resultSet.getString("reason"));
				search.setReviewOn(resultSet.getString("review_on"));
				search.setReviewBy(resultSet.getString("review_by"));
				search.setLeaveStatus(resultSet.getString("leave_status"));
				search.setBalance(resultSet.getString("balance"));
				search.setRmEmpCode(resultSet.getString("rm_emp_code"));
				search.setRmEmpName(resultSet.getString("rm_emp_name"));
				search.setLeaveCode(resultSet.getString("leave_code"));
				search.setLevelId(resultSet.getInt("level_id"));
				searchList.add(search);
				VmsLogging.logInfo(getClass(), "Report Employee info ");
			}
		}catch (Exception e) {
			VmsLogging.logError(getClass(), "Report Employee Exception ", new VmsApplicationException(e.getMessage()));
			return null;
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return searchList;
	}
	
	@Override
	public List<FinanceResponceLeaveBalanceVO>  getLeaveBalanceReportByHierarchy(String loginEmployeeCode, String employeeCode, String flag) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		List<FinanceResponceLeaveBalanceVO> leaveBalanceList = new ArrayList<FinanceResponceLeaveBalanceVO>();
		try {
			con = dataSource.getConnection();
			if(employeeCode.equals("null") || employeeCode.isEmpty())
				employeeCode= null;
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.Get_LMS_Report_Balance_Dtl") + "(?,?,?)}");
			pstmt.setString(1, loginEmployeeCode);
			pstmt.setString(2, employeeCode);
			pstmt.setString(3, flag);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()) {
				FinanceResponceLeaveBalanceVO leaveBalance = new FinanceResponceLeaveBalanceVO();
				leaveBalance.setBalance(resultSet.getDouble("Balance"));
				leaveBalance.setEmployeeNumber(resultSet.getString("Employee_code"));
				leaveBalance.setEmployeeName(resultSet.getString("Employee_name"));
				leaveBalance.setLeaveType(resultSet.getString("leave_type"));
				leaveBalance.setOpen(resultSet.getDouble("Open"));
				leaveBalance.setCredit(resultSet.getDouble("Credit"));
				leaveBalance.setUsed(resultSet.getDouble("Used"));
				leaveBalance.setEmployeeStatus(resultSet.getString("Employee_status"));
				leaveBalance.setReportingManager(resultSet.getString("R_Employee_name")+"/"+resultSet.getString("R_Employee_Code"));
				System.out.println("JSON Value "+JSONUtil.toJson(leaveBalance));
				leaveBalanceList.add(leaveBalance);
			}
			VmsLogging.logInfo(getClass(), "Leave Balance Report Employee Info ");
			
		}catch (Exception e) {
			VmsLogging.logError(getClass(), "Leave Balance Report Employee Exception ", new VmsApplicationException(e.getMessage()));
			return null;
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return leaveBalanceList;
	}
}
