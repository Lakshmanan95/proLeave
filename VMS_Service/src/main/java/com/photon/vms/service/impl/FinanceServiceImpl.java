package com.photon.vms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.dao.FinanceDAO;
import com.photon.vms.service.FinanceService;
import com.photon.vms.vo.FinanceReportResponse;
import com.photon.vms.vo.FinanceResponceCompOffVO;
import com.photon.vms.vo.FinanceResponceIndiaSalesVO;
import com.photon.vms.vo.FinanceResponceLeaveBalanceVO;
import com.photon.vms.vo.FinanceResponceLeaveRequestVO;
import com.photon.vms.vo.FinanceResponceUsedLeaveVO;
import com.photon.vms.vo.ReportDropDownResponse;
import com.photon.vms.vo.SearchHierarchy;
import com.photon.vms.vo.SearchHierarchyRequestVO;

@Service
public class FinanceServiceImpl implements FinanceService{

	@Autowired
	FinanceDAO financeDAO;
	@Override
	public ArrayList<FinanceResponceUsedLeaveVO> getFinanceResponceUsedLeaveReport(String companyName, String employeeNumber, String leaveTypeId,
			String reportType, String fromDate, String toDate,int activeStatus) {
		ArrayList<FinanceResponceUsedLeaveVO> response = new ArrayList<>();
		try {
			response = financeDAO.getFinanceResponceUsedLeaveReport(companyName,employeeNumber,leaveTypeId,reportType,fromDate,toDate,activeStatus);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance Leave Report info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}


	@Override
	public FinanceReportResponse getFinanceReportEntity() {
		FinanceReportResponse response = new FinanceReportResponse();
		try {
			response = financeDAO.getFinanceReportEntity();
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance Report Entity service info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}


	@Override
	public ArrayList<FinanceResponceLeaveRequestVO> getFinanceResponceLeaveRequestReport(String companyName,
			String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus) {
		ArrayList<FinanceResponceLeaveRequestVO> response = new ArrayList<>();
		try {
			response = financeDAO.getFinanceResponceLeaveRequestReport(companyName,employeeNumber,leaveTypeId,reportType,fromDate,toDate,activeStatus);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance Leave Report info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}


	@Override
	public ArrayList<FinanceResponceLeaveBalanceVO> getFinanceResponceLeaveBalanceReport(String companyName,
			String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus) {
		ArrayList<FinanceResponceLeaveBalanceVO> response = new ArrayList<>();
		try {
			response = financeDAO.getFinanceResponceLeaveBalanceReport(companyName,employeeNumber,leaveTypeId,reportType,fromDate,toDate,activeStatus);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance Leave Report info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}


	@Override
	public ArrayList<FinanceResponceCompOffVO> getFinanceResponceCompOffReport(String companyName,
			String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus) {
		ArrayList<FinanceResponceCompOffVO> response = new ArrayList<>();
		try {
			response = financeDAO.FinanceResponceCompOffVO(companyName,employeeNumber,leaveTypeId,reportType,fromDate,toDate,activeStatus);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance Leave Report info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}


	@Override
	public ArrayList<FinanceResponceIndiaSalesVO> getFinanceResponceIndiaSalesReport(String companyName,
			String employeeNumber, String leaveTypeId, String reportType, String fromDate, String toDate,int activeStatus) {
		ArrayList<FinanceResponceIndiaSalesVO> response = new ArrayList<>();
		try {
			response = financeDAO.getFinanceResponceIndiaSalesReport(companyName,employeeNumber,leaveTypeId,reportType,fromDate,toDate,activeStatus);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance Leave Report info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}


	@Override
	public ReportDropDownResponse getReportDropDown(String employeeNumber, String flag) {
		ReportDropDownResponse response = new ReportDropDownResponse();
		try {
			response = financeDAO.getReportDropDown(employeeNumber,flag);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Report Employee info Error ", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	@Override
	public List<SearchHierarchy> searchAndReportHierarchy(SearchHierarchyRequestVO request) {
		List<SearchHierarchy> searchList = new ArrayList<SearchHierarchy>();
		try {
			searchList = financeDAO.searchAndReportHierarchy(request);
			VmsLogging.logInfo(getClass(), "Search Hierarchy Service Info");
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Search Hierarchy Service Failed info ", new VmsApplicationException(e.getMessage()));
		}
		return searchList;
	}
	
	@Override
	public List<FinanceResponceLeaveBalanceVO> getLeaveBalanceReportByHierarchy(String loginEmployeeCode, String employeeCode, String flag){
		List<FinanceResponceLeaveBalanceVO> reportList = new ArrayList<FinanceResponceLeaveBalanceVO>();
		try {
			reportList = financeDAO.getLeaveBalanceReportByHierarchy(loginEmployeeCode, employeeCode, flag);
			VmsLogging.logInfo(getClass(), "Leave Balance Report Hierarchy Service Info");
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Leave Balance Report Service Failed info ", new VmsApplicationException(e.getMessage()));
		}
		return reportList;
	}
}
