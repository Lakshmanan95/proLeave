package com.photon.vms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.dao.RevocationDAO;
import com.photon.vms.util.DateTimeUtils;
import com.photon.vms.util.JSONUtil;
import com.photon.vms.util.OnlineUtils;
import com.photon.vms.vo.LocationAdminDetails;
import com.photon.vms.vo.PendingLeaveRequsetVO;
import com.photon.vms.vo.PunchedHours;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.RevocationEmailVO;
import com.photon.vms.vo.SuccessResponseVO;
import com.photon.vms.vo.ViewRevocactionResponseVO;
import com.photon.vms.vo.ViewRevocation;

@Service
@Repository
public class RevocationDAOImpl implements RevocationDAO{

	@Autowired 
	private DataSource dataSource;
	@Autowired
	private Environment env;
	@Autowired
	private DateTimeUtils dateTimeUtils;
	
	public static final String EMPLOYEE_CODE = "employee_code";
	public static final String APPROVED_STATUS = "Approved_status";
	
	@Override
	public ViewRevocactionResponseVO viewRevocationDetail(String leaveRequestId) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ViewRevocation revocation = new ViewRevocation();
		ViewRevocactionResponseVO   response = new ViewRevocactionResponseVO();
		List<PunchedHours> punchedHoursList = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt =con.prepareStatement(CommonConstants.CALL_DATASOURCE+env.getProperty("sp.Get_LMS_Leave_Acs_Dtl") + "(?)}");
			pstmt.setString(1, leaveRequestId);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while (resultSet.next()) {
				revocation.setEmployeeCode(resultSet.getString(EMPLOYEE_CODE));
				revocation.setFromDate(resultSet.getString("from_date"));
				revocation.setToDate(resultSet.getString("To_date"));
				revocation.setRevocationReason(resultSet.getString("Revocation_Reason"));
				revocation.setRevocationRequestedOn(resultSet.getString("Revocation_Requested_on"));
				revocation.setApprovedStatus(resultSet.getString(APPROVED_STATUS));
			}
			if(pstmt.getMoreResults()){
				resultSet = pstmt.getResultSet();
				while(resultSet.next()){
					PunchedHours punchedHours = new PunchedHours();
					punchedHours.setEmployeeCode(resultSet.getString(EMPLOYEE_CODE));
					punchedHours.setPunchDate(resultSet.getString("punch_date"));
					punchedHours.setPrdHrs(resultSet.getString("prd_hrs"));
					punchedHoursList.add(punchedHours);
				}
			}
			revocation.setPunchedHours(punchedHoursList);
			response.setViewRevocation(revocation);
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Error Pending leave request",e);
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return response;
	}
	
	@Override
	public RevocationEmailVO getMailDetails(String leaveRequestId) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		RevocationEmailVO   response = new RevocationEmailVO();
		List<LocationAdminDetails> locAdminDetails = new ArrayList<>();
		List<PunchedHours> punchedHoursList = new ArrayList<>();

		try {
			con = dataSource.getConnection();
			pstmt =con.prepareStatement(CommonConstants.CALL_DATASOURCE+env.getProperty("sp.Get_Leave_Revocation_Mail_Dtl") + "(?)}");
			pstmt.setString(1, leaveRequestId);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while (resultSet.next()) {
				response.setEmployeeCode(resultSet.getString(EMPLOYEE_CODE));
				response.setEmployeeEmail(resultSet.getString("employee_email"));
				response.setEmployeeName(resultSet.getString("employee_name"));
				String fromDate = dateTimeUtils.convertStringDateFormat(resultSet.getString("from_date"));
				response.setFromDate(fromDate);
				String toDate = dateTimeUtils.convertStringDateFormat(resultSet.getString("to_date"));
				response.setToDate(toDate);
				response.setLeaveCode(resultSet.getString("leave_code"));
				response.setLeaveReason(resultSet.getString("leave_reason"));
				response.setLeaveType(resultSet.getString("leave_type"));
				response.setLeaveStatus(resultSet.getString("leave_status"));
				response.setLeaveStatusCode(resultSet.getString("leave_status_code"));
				response.setRMCode(resultSet.getString("RM_code"));
				response.setRMEmail(resultSet.getString("RM_Email"));
				response.setRMName(resultSet.getString("RM_Name"));
				response.setRevokedDate(resultSet.getString("revoked_date"));
				response.setRevokedReason(resultSet.getString("revoked_reason"));
				response.setRevocationAprRejDate(resultSet.getString("revocation_apr_rej_date"));
				response.setRevocationAprRejReason(resultSet.getString("revocation_apr_rej_reason"));
				response.setRevocationAprRejByCode(resultSet.getString("revocation_apr_rej_by_code"));
				response.setRevocationAprRejByName(resultSet.getString("revocation_apr_rej_by_name"));
				response.setNoOfDays(resultSet.getString("no_of_days"));
				response.setAccessFlag(resultSet.getString("ACS_Lessthan_flag"));
				response.setLeaveTypeId(resultSet.getInt("leave_type_id"));
				response.setOdOptionId(resultSet.getInt("OD_Type"));
				response.setContactNumber(resultSet.getString("contact_number"));
			}
			if(pstmt.getMoreResults()){
				resultSet = pstmt.getResultSet();
				while(resultSet.next()){
					LocationAdminDetails locAdmin = new LocationAdminDetails();
					locAdmin.setLocAdminCode(resultSet.getString("loc_admin_code"));
					locAdmin.setLocAdminEmail(resultSet.getString("loc_admin_email"));
					locAdmin.setLocAdminName(resultSet.getString("loc_admin_name"));
					locAdminDetails.add(locAdmin);
				}
			}
			if(pstmt.getMoreResults()) {
				resultSet = pstmt.getResultSet();
				while(resultSet.next()) {
					PunchedHours punchedHours = new PunchedHours();
					punchedHours.setEmployeeCode(resultSet.getString(EMPLOYEE_CODE));
					punchedHours.setPunchDate(resultSet.getString("punch_date"));
					punchedHours.setProdMins(resultSet.getString("prod_mins"));
					punchedHours.setPrdHrs(resultSet.getString("prd_hrs"));
					punchedHoursList.add(punchedHours);
				}
			}
			response.setAdminDetails(locAdminDetails);		
			response.setPunchedHoursList(punchedHoursList);
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Error Pending leave request",e);
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return response;
	}
	
	@Override
	public List<PendingLeaveRequsetVO> getPendingLeaveForAdmin(RequestParameterVO request) throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		List<PendingLeaveRequsetVO> pendingList = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt =con.prepareStatement(CommonConstants.CALL_DATASOURCE+env.getProperty("sp.Get_LMS_Pending_Revocation_Request")+"(?,?,?,?,?,?,?,?)}");
			pstmt.setString(1, request.getEmployeeNumber());
			pstmt.setString(2, !request.getReporteeEmployeeNumber().isEmpty()?request.getReporteeEmployeeNumber():null);
			pstmt.setString(3, !request.getReporteeName().isEmpty()?request.getReporteeName():null);
			pstmt.setString(4, !request.getFromDate().isEmpty()?request.getFromDate():null);
			pstmt.setString(5, !request.getToDate().isEmpty()?request.getToDate():null);
			pstmt.setString(6, !request.getAppliedDate().isEmpty()?request.getAppliedDate().toString().replace(" ", "T"):null);
			pstmt.setString(7, !request.getLeaveType().isEmpty()?request.getLeaveType():null);
			pstmt.setString(8, request.getLocationId() != 0? String.valueOf(request.getLocationId()) : null);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Get Pending leave request"+resultSet);
			while(resultSet.next()){
				PendingLeaveRequsetVO pendingLeaveRequsetVO=  new PendingLeaveRequsetVO();
				pendingLeaveRequsetVO.setLeaveRequestId(resultSet.getString("Leave_Request_Id")!=null?resultSet.getString("Leave_Request_Id"):"");
				pendingLeaveRequsetVO.setSubmittedDate(resultSet.getString("Submitted_Date")!=null?resultSet.getString("Submitted_Date").toString().replace(" ", "T"):"");
				pendingLeaveRequsetVO.setEmployeeId(resultSet.getString("Employee_Id")!=null?resultSet.getString("Employee_Id"):"");
				pendingLeaveRequsetVO.setEmployeeCode(resultSet.getString(EMPLOYEE_CODE)!=null?resultSet.getString("Employee_Code"):"");
				pendingLeaveRequsetVO.setEmployeeName(resultSet.getString("Employee_Name")!=null?resultSet.getString("Employee_Name"):"");
				pendingLeaveRequsetVO.setInsightId(resultSet.getString("Insight_Id")!=null?resultSet.getString("Insight_Id"):"");
				pendingLeaveRequsetVO.setFromDate(resultSet.getDate("From_Date"));
				pendingLeaveRequsetVO.setToDate(resultSet.getDate("To_Date"));
				pendingLeaveRequsetVO.setNumberOfDays(resultSet.getInt("No_Of_Days"));
				pendingLeaveRequsetVO.setLeaveTypeId(resultSet.getInt("Leave_Type_Id"));
				pendingLeaveRequsetVO.setLeaveTypeName(resultSet.getString("Leave_Type")!=null?resultSet.getString("Leave_Type"):"");
				pendingLeaveRequsetVO.setLeaveCode(resultSet.getString("Leave_Code")!=null?resultSet.getString("Leave_Code"):"");
				pendingLeaveRequsetVO.setLeaveReason(resultSet.getString("Reason")!=null?resultSet.getString("Reason"):"");
				pendingLeaveRequsetVO.setManagerName(resultSet.getString("Manage")!=null?resultSet.getString("Manage"):"");
				pendingLeaveRequsetVO.setLeaveStatus(resultSet.getString("Leave_Status")!=null?resultSet.getString("Leave_Status"):"");
				pendingLeaveRequsetVO.setLeaveBalance(resultSet.getInt("Leave_Balance"));
				pendingLeaveRequsetVO.setContactNumber(resultSet.getString("Contact_Number")!=null?resultSet.getString("Contact_Number"):"");
				pendingLeaveRequsetVO.setApprovedStatus(resultSet.getString(APPROVED_STATUS)!=null?resultSet.getString(APPROVED_STATUS):"");
				pendingLeaveRequsetVO.setOdOptionName(resultSet.getString("od_option_name")!=null?resultSet.getString("od_option_name"):"");
				pendingLeaveRequsetVO.setOdOptionCode(resultSet.getString("od_option_code")!=null?resultSet.getString("od_option_code"):"");
				pendingList.add(pendingLeaveRequsetVO);
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Error Employee Admin Loc request",e);
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return pendingList;
	}
	
	@Override
	public SuccessResponseVO revocationApproveAndReject(String leaveRequestId, String employeeId, String leaveStatus, String reason) throws Exception{
		SuccessResponseVO response = new SuccessResponseVO();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			con = dataSource.getConnection();
			pstmt =con.prepareStatement(CommonConstants.CALL_DATASOURCE+env.getProperty("sp.Ins_upd_Leave_Revocation_Request")+"(?,?,?,?)}");
			pstmt.setString(1, leaveRequestId);
			pstmt.setString(2, employeeId);
			pstmt.setString(3, leaveStatus);
			pstmt.setString(4, !reason.isEmpty()?reason:null);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()){
					response.setStatus(resultSet.getString("Error_Code"));
			}
			
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Error in Approve and Reject Revocation request",e);
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return response;
	}

}
