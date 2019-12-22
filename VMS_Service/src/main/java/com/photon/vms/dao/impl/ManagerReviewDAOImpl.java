package com.photon.vms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.dao.ManagerReviewDAO;
import com.photon.vms.util.JSONUtil;
import com.photon.vms.util.OnlineUtils;
import com.photon.vms.vo.PendingLeaveRequsetVO;
import com.photon.vms.vo.ReportGenerateVO;
import com.photon.vms.vo.RequestParameterVO;

/**
 * @author karthigaiselvan_r
 *
 */
@Service
@Repository
public class ManagerReviewDAOImpl implements ManagerReviewDAO{
	@Autowired 
	private DataSource dataSource;
	@Autowired 
	private Environment env;
	
	@Override
	public Map<String, ArrayList<?>> getPendingLeaveRequest(String employeeNumber, String reporteeNumber, String reporteeName, String fromDate, String toDate, 
			String appliedDate, String leaveType, String leaveStatus) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<PendingLeaveRequsetVO> leaveRequestInfo = new ArrayList<>();
		ArrayList<String> leaveRequestList = new ArrayList<>();
		Map<String, ArrayList<?>> finalData = new HashMap<>();
		try{
						
			con = dataSource.getConnection();
//			if(request.getRole().equals("Admin")) {
//				pstmt =con.prepareStatement("{call "+sp+"(?,?,?,?,?,?,?,?)}");
//				System.out.println("rev");
//			}else {
//				sp = "get_LMS_Pending_Request";			
//				pstmt =con.prepareStatement("{call "+sp+"(?,?,?,?,?,?,?)}");
//				System.out.println("pending");
//			}
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.get_leave_pending_request")+"(?,?,?,?,?,?,?,?)}");
			pstmt.setString(1, employeeNumber);
			pstmt.setString(2, !reporteeNumber.isEmpty()?reporteeNumber:null);
			pstmt.setString(3, !reporteeName.isEmpty()?reporteeName:null);
			pstmt.setString(4, !fromDate.isEmpty()?fromDate:null);
			pstmt.setString(5, !toDate.isEmpty()?toDate:null);
			pstmt.setString(6, !appliedDate.isEmpty()?appliedDate:null);
			pstmt.setString(7, !leaveType.isEmpty()?leaveType:null);
			pstmt.setString(8, !leaveStatus.isEmpty()?leaveStatus:null);
			pstmt.executeQuery();
			System.out.println("list");
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Get Pending leave request"+resultSet);
			while(resultSet.next()){
				PendingLeaveRequsetVO pendingLeaveRequsetVO=  new PendingLeaveRequsetVO();
				pendingLeaveRequsetVO.setLeaveRequestId(resultSet.getString("Leave_Request_Id")!=null?resultSet.getString("Leave_Request_Id"):"");
				pendingLeaveRequsetVO.setSubmittedDate(resultSet.getString("Submitted_Date")!=null?resultSet.getString("Submitted_Date").toString().replace(" ", "T"):"");
				pendingLeaveRequsetVO.setEmployeeId(resultSet.getString("Employee_Id")!=null?resultSet.getString("Employee_Id"):"");
				pendingLeaveRequsetVO.setEmployeeCode(resultSet.getString("Employee_Code")!=null?resultSet.getString("Employee_Code"):"");
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
				pendingLeaveRequsetVO.setApprovedStatus(resultSet.getString("Approved_Status")!=null?resultSet.getString("Approved_Status"):"");
				pendingLeaveRequsetVO.setOdOptionName(resultSet.getString("od_option_name")!=null?resultSet.getString("od_option_name"):"");
				pendingLeaveRequsetVO.setOdOptionCode(resultSet.getString("od_option_code")!=null?resultSet.getString("od_option_code"):"");
				pendingLeaveRequsetVO.setLeaveStatusId(resultSet.getInt("leave_status_id"));
				
				leaveRequestList.add(resultSet.getString("Leave_Request_Id")!=null?resultSet.getString("Leave_Request_Id"):"");
				leaveRequestInfo.add(pendingLeaveRequsetVO);
			}
			finalData.put("PendingLeaveRequest", leaveRequestInfo);
			finalData.put("LeaveRequestList", leaveRequestList);
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Error Pending leave request",e);
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return finalData;
	}

	@Override
	public ArrayList<ReportGenerateVO> generateCSVReport(String employeeNumber, RequestParameterVO requestParameter) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<ReportGenerateVO> reportGenerateVO = new ArrayList<>();
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.get_Leave_Request_Report")+"(?,?,?,?,?,?,?,?,?,?,?)}");
			pstmt.setString(1, employeeNumber);			
			pstmt.setDate(2, ((requestParameter.getAppliedDate() == null || requestParameter.getAppliedDate().equalsIgnoreCase("null") || requestParameter.getAppliedDate().equalsIgnoreCase(null))?null:new java.sql.Date(formatter.parse(requestParameter.getAppliedDate()).getTime())));
			pstmt.setDate(3, ((requestParameter.getFromDate() == null || requestParameter.getFromDate().equalsIgnoreCase(null) || requestParameter.getFromDate().equalsIgnoreCase("null")) ?null:new java.sql.Date(formatter.parse(requestParameter.getFromDate()).getTime())));
			pstmt.setDate(4, ((requestParameter.getToDate() == null || requestParameter.getToDate().equalsIgnoreCase(null) || requestParameter.getToDate().equalsIgnoreCase("null"))?null:new java.sql.Date(formatter.parse(requestParameter.getToDate()).getTime())));
			pstmt.setString(5, ((requestParameter.getNumberOfDays() == null || requestParameter.getNumberOfDays().equalsIgnoreCase(null) || requestParameter.getNumberOfDays().equalsIgnoreCase("null"))?null:requestParameter.getNumberOfDays()));
			pstmt.setString(6, ((requestParameter.getLeaveType() == null || requestParameter.getLeaveType().equalsIgnoreCase(null) || requestParameter.getLeaveType().equalsIgnoreCase("null"))?null:requestParameter.getLeaveType()));
			pstmt.setDate(7, ((requestParameter.getApprovedDate() == null || requestParameter.getApprovedDate().equalsIgnoreCase(null) || requestParameter.getApprovedDate().equalsIgnoreCase("null"))?null:new java.sql.Date(formatter.parse(requestParameter.getApprovedDate()).getTime())));
			pstmt.setString(8, ((requestParameter.getLeaveStatus() == null || requestParameter.getLeaveStatus().equalsIgnoreCase(null) || requestParameter.getLeaveStatus().equalsIgnoreCase("null"))?null:requestParameter.getLeaveStatus()));	
			pstmt.setString(9, ((requestParameter.getReporteeEmployeeNumber() == null || requestParameter.getReporteeEmployeeNumber().equalsIgnoreCase(null) || requestParameter.getReporteeEmployeeNumber().equalsIgnoreCase("null"))?null:requestParameter.getReporteeEmployeeNumber()));	
			pstmt.setString(10, ((requestParameter.getReporteeName() == null || requestParameter.getReporteeName().equalsIgnoreCase(null) || requestParameter.getReporteeName().equalsIgnoreCase("null"))?null:requestParameter.getReporteeName()));	
			pstmt.setString(11, "M");	
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()){
				ReportGenerateVO response = new ReportGenerateVO();
				response.setLeaveRequestId(resultSet.getInt("Leave_Request_Id"));
				response.setEmployeeCode(resultSet.getString("Employee_Code")!=null?resultSet.getString("Employee_Code"):"");
				response.setEmployeeName(resultSet.getString("Employee_Name")!=null?resultSet.getString("Employee_Name"):"");
				response.setInsightId(resultSet.getString("Insight_Id")!=null?resultSet.getString("Insight_Id"):"");
				response.setReportingManagerName(resultSet.getString("Reporting_Mgr_name")!=null?resultSet.getString("Reporting_Mgr_name"):"");
				response.setFromDate(resultSet.getDate("From_Date"));
				response.setToDate(resultSet.getDate("To_Date"));
				response.setNumberOfDays(resultSet.getInt("No_Of_Days"));
				response.setLeaveTypeId(resultSet.getInt("Leave_Type_Id"));
				response.setLeaveTypecode(resultSet.getString("Leave_Code")!=null?resultSet.getString("Leave_Code"):"");
				response.setLeaveTypeName(resultSet.getString("Leave_Type")!=null?resultSet.getString("Leave_Type"):"");
				response.setContactNumber(resultSet.getString("Contact_Number")!=null?resultSet.getString("Contact_Number"):"");
				response.setAppliedDate(resultSet.getDate("Applied_On"));
				response.setApprovedById(resultSet.getInt("Approved_By"));
				response.setApprovedDate(resultSet.getString("Approved_Date")!=null?resultSet.getString("Approved_Date"):"");
				response.setLeaveStatus(resultSet.getString("Leave_Status")!=null?resultSet.getString("Leave_Status"):"");
				response.setRejectedById(resultSet.getInt("Rejected_By"));
				response.setRejectedDate(resultSet.getDate("Rejected_Date"));
				response.setRejectedReason(resultSet.getString("Rejected_Reason")!=null?resultSet.getString("Rejected_Reason"):"");
				response.setRevokedById(resultSet.getInt("Revoked_By"));
				response.setRevokedDate(resultSet.getDate("Revoked_Date"));
				response.setRevokedReason(resultSet.getString("Revoked_Reason")!=null?resultSet.getString("Revoked_Reason"):"");
				response.setCancelledById(resultSet.getInt("Cancelled_By"));
				response.setCancelledDate(resultSet.getDate("Cancelled_Date"));
				response.setCancelledReason(resultSet.getString("Cancelled_Reason")!=null?resultSet.getString("Cancelled_Reason"):"");
				response.setSolutionCenter(resultSet.getString("Solution_Center")!=null?resultSet.getString("Solution_Center"):"");
				response.setOdOptionCode(resultSet.getString("option_code")!=null?resultSet.getString("option_code"):"");
				response.setOdOptionName(resultSet.getString("option_name")!=null?resultSet.getString("option_name"):"");
				
				if(response.getLeaveTypecode().equalsIgnoreCase("OD") && response.getOdOptionCode().equalsIgnoreCase("OTH")){
					response.setLeaveReason("Other - "+resultSet.getString("Reason"));
				}else if(response.getLeaveTypecode().equalsIgnoreCase("OD")){
					response.setLeaveReason(resultSet.getString("option_name")!=null?resultSet.getString("option_name"):"");
				}else{
					response.setLeaveReason(resultSet.getString("Reason")!=null?resultSet.getString("Reason"):"");
				}
				reportGenerateVO.add(response);
			}
		}catch(SQLException e){
			VmsLogging.logError(getClass(), "Error Report Generate",e);
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Error Report Generate",e);
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return reportGenerateVO;
	}
}
