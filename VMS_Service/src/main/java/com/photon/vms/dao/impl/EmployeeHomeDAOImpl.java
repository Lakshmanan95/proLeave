package com.photon.vms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.dao.EmployeeHomeDAO;
import com.photon.vms.util.OnlineUtils;
import com.photon.vms.vo.AdHocWorkDaysVO;
import com.photon.vms.vo.FetchLeaveRequestInfoVO;
import com.photon.vms.vo.HolidayListVO;
import com.photon.vms.vo.IdByEmailInfoVO;
import com.photon.vms.vo.LeaveBalanceDetailVO;
import com.photon.vms.vo.LeaveRequestIdInfo;
import com.photon.vms.vo.LeaveTypeVO;
import com.photon.vms.vo.OutOfOfficeInfoVO;
import com.photon.vms.vo.ProcessLeaveRequestVO;
import com.photon.vms.vo.ReportManagerVO;
import com.photon.vms.vo.SearchHistoryVO;

/**
 * @author karthigaiselvan_r
 *
 */
@Service
@Repository
public class EmployeeHomeDAOImpl implements EmployeeHomeDAO{
	@Autowired 
	private DataSource dataSource;
	@Autowired 
	private Environment env;
	
	public static final String EMPLOYEE_ID = "Employee_Id";
	public static final String CONTACT_NUMBER = "Contact_Number";
	public static final String LOCATION_CODE = "Location_Code";
	public static final String LEAVE_TYPE_ID = "Leave_Type_Id";
	public static final String LEAVE_REQUEST_ID = "Leave_Request_Id";
	public static final String FROM_DATE = "From_date";
	public static final String TO_DATE = "TO_Date";
	public static final String APPLIED_ON = "Applied_On";
	public static final String APPROVED_BY ="Approved_By";
	public static final String APPROVED_DATE = "Approved_Date";
	public static final String APPROVED_STATUS="Approved_Status";
	public static final String OD_OPTION_CODE="OD_Option_Code";
	
	@Override
	public Map<String, ArrayList<?>> getLoggedUserInfo(String employeeNumber) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<ReportManagerVO> empReportManagerInfo = new ArrayList<>();
		ArrayList<AdHocWorkDaysVO> adHocWorkDayList = new ArrayList<>();
		ArrayList<HolidayListVO> holidayList = new ArrayList<>();
		ArrayList<LeaveTypeVO> leaveTypeList = new ArrayList<>();
		ArrayList<LeaveBalanceDetailVO> leaveBalanceDetail = new ArrayList<>();
		ArrayList<OutOfOfficeInfoVO> outOfOfficeOptions = new ArrayList<>();
		ArrayList legendInfo = new ArrayList();
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement(CommonConstants.CALL_DATASOURCE+env.getProperty("sp.get_lms_home_detail")+"(?)}");
			pstmt.setString(1, employeeNumber);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Loggeduser info "+employeeNumber);
			
			while(resultSet.next()){
				ReportManagerVO reportManagerVO = new ReportManagerVO();
				reportManagerVO.setEmployeeId(resultSet.getString(EMPLOYEE_ID)!=null?resultSet.getString(EMPLOYEE_ID):"");
				reportManagerVO.setEmployeeNumber(resultSet.getString(CommonConstants.EMPLOYEE_CODE)!=null?resultSet.getString(CommonConstants.EMPLOYEE_CODE):"");
				reportManagerVO.setEmployeeName(resultSet.getString(CommonConstants.EMPLOYEE_NAME)!=null?resultSet.getString(CommonConstants.EMPLOYEE_NAME):"");
				reportManagerVO.setEmailId(resultSet.getString("email")!=null?resultSet.getString("email"):"");
				reportManagerVO.setReportingManagerId(resultSet.getString("RMgr_Id")!=null?resultSet.getString("RMgr_Id"):"");
				reportManagerVO.setReportingManagerNumber(resultSet.getString("RMgr_Code")!=null?resultSet.getString("RMgr_Code"):"");
				reportManagerVO.setReportingManagerName(resultSet.getString("RMgr_Name")!=null?resultSet.getString("RMgr_Name"):"");
				reportManagerVO.setLocationCode(resultSet.getString(LOCATION_CODE)!=null?resultSet.getString(LOCATION_CODE):"");
				reportManagerVO.setAdminLoc(resultSet.getString("Admin_Loc")!=null?resultSet.getString("Admin_Loc"):"");
				reportManagerVO.setLocationName(resultSet.getString(CommonConstants.LOCATION_NAME)!=null?resultSet.getString(CommonConstants.LOCATION_NAME):"");
				reportManagerVO.setContactNumber(resultSet.getString(CONTACT_NUMBER)!=null?resultSet.getString(CONTACT_NUMBER):"");
				reportManagerVO.setManagerEmailId(resultSet.getString("Email_Ids")!=null?resultSet.getString("Email_Ids"):"");
				reportManagerVO.setRole(resultSet.getString("Role")!=null?resultSet.getString("Role"):"");
				reportManagerVO.setGender(resultSet.getString("Gender")!=null?resultSet.getString("Gender"):"");
				reportManagerVO.setEmployeeType(resultSet.getString("Employee_Type")!=null?resultSet.getString("Employee_Type"):"");
				reportManagerVO.setEmployeeStatus(resultSet.getString("Employee_Status")!=null?resultSet.getString("Employee_Status"):"");
				empReportManagerInfo.add(reportManagerVO);
			}
			resultmap.put("EmpReportManagerInfo", empReportManagerInfo);
			if(pstmt.getMoreResults()){
				resultSet = pstmt.getResultSet();
				while(resultSet.next()){
					AdHocWorkDaysVO adHocWorkDaysVO = new AdHocWorkDaysVO();
					adHocWorkDaysVO.setLeaveDate(resultSet.getDate("Leave_Date"));
					adHocWorkDaysVO.setLocationCode(resultSet.getString(LOCATION_CODE)!=null?resultSet.getString(LOCATION_CODE):"");
					adHocWorkDayList.add(adHocWorkDaysVO);
				}
			}
			resultmap.put("AdHocWorkingDays",adHocWorkDayList);
			if(pstmt.getMoreResults()){
				resultSet = pstmt.getResultSet();
				while(resultSet.next()){
					HolidayListVO holidayListVO = new HolidayListVO();
					holidayListVO.setHolidayId(resultSet.getString("Holiday_ID")!=null?resultSet.getString("Holiday_ID"):"");
					holidayListVO.setHolidayDate(resultSet.getDate("Holiday_Date"));
					holidayListVO.setHolidayName(resultSet.getString("Holiday")!=null?resultSet.getString("Holiday"):"");
					holidayListVO.setDayOfHoliday(resultSet.getString("Holiday_Day")!=null?resultSet.getString("Holiday_Day"):"");
					holidayListVO.setLocationName(resultSet.getString(CommonConstants.LOCATION_NAME)!=null?resultSet.getString(CommonConstants.LOCATION_NAME):"");
					holidayList.add(holidayListVO);
				}
			}
			resultmap.put("HolidayList",holidayList);
			if(pstmt.getMoreResults()){
				resultSet = pstmt.getResultSet();
				while(resultSet.next()){
					LeaveTypeVO leaveTypeVO = new LeaveTypeVO();
					leaveTypeVO.setLeaveTypeId(resultSet.getString(LEAVE_TYPE_ID)!=null?resultSet.getString(LEAVE_TYPE_ID):"");
					leaveTypeVO.setLeaveCode(resultSet.getString(CommonConstants.LEAVE_CODE)!=null?resultSet.getString(CommonConstants.LEAVE_CODE):"");
					leaveTypeVO.setLeaveType(resultSet.getString(CommonConstants.LEAVE_TYPE)!=null?resultSet.getString(CommonConstants.LEAVE_TYPE):"");
					leaveTypeVO.setLocationCode(resultSet.getString(LOCATION_CODE)!=null?resultSet.getString(LOCATION_CODE):"");
					leaveTypeVO.setIsDefault(resultSet.getString("Is_Default")!=null?resultSet.getString("Is_Default"):"");
					leaveTypeVO.setIsContinuous(resultSet.getString("Is_Continuous")!=null?resultSet.getString("Is_Continuous"):"");
					leaveTypeList.add(leaveTypeVO);
				}	
			}
			resultmap.put("LeaveTypeList",leaveTypeList);
			if(pstmt.getMoreResults()){
				resultSet = pstmt.getResultSet();
				while(resultSet.next()){
					LeaveBalanceDetailVO leaveBalanceDetailVO = new LeaveBalanceDetailVO();
					leaveBalanceDetailVO.setEmployeeId(resultSet.getString(EMPLOYEE_ID)!=null?resultSet.getString(EMPLOYEE_ID):"");
					leaveBalanceDetailVO.setYear(resultSet.getString("year")!=null?resultSet.getString("year"):"");
					leaveBalanceDetailVO.setLeaveTypeId(resultSet.getString(LEAVE_TYPE_ID)!=null?resultSet.getString(LEAVE_TYPE_ID):"");
					leaveBalanceDetailVO.setLeaveCode(resultSet.getString(CommonConstants.LEAVE_CODE)!=null?resultSet.getString(CommonConstants.LEAVE_CODE):"");
					leaveBalanceDetailVO.setLeaveTypeName(resultSet.getString(CommonConstants.LEAVE_TYPE)!=null?resultSet.getString(CommonConstants.LEAVE_TYPE):"");
					leaveBalanceDetailVO.setOpenLeave(resultSet.getDouble("Open"));
					leaveBalanceDetailVO.setCreditedLeave(resultSet.getString("Credit")!=null?resultSet.getString("Credit"):"");
					leaveBalanceDetailVO.setUsedLeave(resultSet.getString("Used")!=null?resultSet.getString("Used"):"");
					leaveBalanceDetailVO.setBalanceLeave(resultSet.getString("Balance")!=null?resultSet.getString("Balance"):"");
					leaveBalanceDetailVO.setPendingLeave(resultSet.getString("Pending")!=null?resultSet.getString("Pending"):"");
					leaveBalanceDetailVO.setLocationCode(resultSet.getString(LOCATION_CODE)!=null?resultSet.getString(LOCATION_CODE):"");
					leaveBalanceDetailVO.setApprovedAndPendingBalance(resultSet.getString("Approved_Pending_balance")!=null?resultSet.getString("Approved_Pending_balance"):"");
					leaveBalanceDetail.add(leaveBalanceDetailVO);
				}
			}
			resultmap.put("LeaveBalanceInfo",leaveBalanceDetail);
			if(pstmt.getMoreResults()){
				resultSet = pstmt.getResultSet();
				while(resultSet.next()){
					OutOfOfficeInfoVO outOfOfficeInfo = new OutOfOfficeInfoVO();
					outOfOfficeInfo.setOdOptionId(resultSet.getString("Od_Option_Id")!=null?resultSet.getString("Od_Option_Id"):"");
					outOfOfficeInfo.setOdOptionName(resultSet.getString("Od_Option_Name")!=null?resultSet.getString("Od_Option_Name"):"");
					outOfOfficeInfo.setOdOptionCode(resultSet.getString(OD_OPTION_CODE)!=null?resultSet.getString(OD_OPTION_CODE):"");
					outOfOfficeOptions.add(outOfOfficeInfo);
				}
			}
			resultmap.put("OutOfOfficeOptions",outOfOfficeOptions);
			legendInfo.add(CommonConstants.getLegendColor());
			resultmap.put("Legends", legendInfo);
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Logged user info", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}			
		return resultmap;
	}

	@Override
	public ArrayList<SearchHistoryVO> getTimeoffHistory(String employeeNumber, String appliedDate, String fromDate,
			String toDate, String numberOfDays, String leaveType, String approvedDate, String leaveStatus) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<SearchHistoryVO> searchResult = new ArrayList<>();
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement(CommonConstants.CALL_DATASOURCE+env.getProperty("sp.get_Employee_Leave_History")+"(?,?,?,?,?,?,?,?)}");
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
			pstmt.setString(1, employeeNumber);
			pstmt.setDate(2, !fromDate.isEmpty()?new java.sql.Date(formatter.parse(fromDate).getTime()):null);
			pstmt.setDate(3, !toDate.isEmpty()?new java.sql.Date(formatter.parse(toDate).getTime()):null);
			pstmt.setDate(4, !appliedDate.isEmpty()?new java.sql.Date(formatter.parse(appliedDate).getTime()):null);			
			pstmt.setString(5, !numberOfDays.isEmpty()?numberOfDays:null);
			pstmt.setString(6, !leaveType.isEmpty()?leaveType:null);
			pstmt.setDate(7, !approvedDate.isEmpty()?new java.sql.Date(formatter.parse(approvedDate).getTime()):null);
			pstmt.setString(8, !leaveStatus.isEmpty()?leaveStatus:null);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()){
				SearchHistoryVO searchHistoryVO = new SearchHistoryVO();
				searchHistoryVO.setLeaveRequestId(resultSet.getString("Leave_Request_Id"));
				searchHistoryVO.setFromDate(resultSet.getDate(FROM_DATE));
				searchHistoryVO.setToDate(resultSet.getDate(TO_DATE));
				searchHistoryVO.setNumberDays(resultSet.getString("Days")!=null?resultSet.getString("Days"):"");
				searchHistoryVO.setLeaveTypeId(resultSet.getString(LEAVE_TYPE_ID)!=null?resultSet.getString(LEAVE_TYPE_ID):"");
				searchHistoryVO.setLeaveCode(resultSet.getString("Code")!=null?resultSet.getString("Code"):"");
				searchHistoryVO.setLeaveTypeName(resultSet.getString("Type")!=null?resultSet.getString("Type"):"");
				searchHistoryVO.setContactNumber(resultSet.getString(CONTACT_NUMBER)!=null?resultSet.getString(CONTACT_NUMBER):"");
				searchHistoryVO.setLeaveReason(resultSet.getString("Reason")!=null?resultSet.getString("Reason"):"");
				searchHistoryVO.setAppliedOnDate(resultSet.getString(APPLIED_ON)!=null?resultSet.getString(APPLIED_ON).toString().replace(" ", "T"):"");
				searchHistoryVO.setApprovedById(resultSet.getString(APPROVED_BY)!=null?resultSet.getString(APPROVED_BY):"");
				searchHistoryVO.setApprovedDate(resultSet.getString(APPROVED_DATE)!=null?resultSet.getString(APPROVED_DATE).replace(" ", "T"):"");
				searchHistoryVO.setLeaveStatus(resultSet.getString("Status")!=null?resultSet.getString("Status"):"");
				searchHistoryVO.setApprovedStatus(resultSet.getString(APPROVED_STATUS)!=null?resultSet.getString(APPROVED_STATUS):"");
				searchHistoryVO.setOdOptioncode(resultSet.getString(OD_OPTION_CODE)!=null?resultSet.getString(OD_OPTION_CODE):"");
				searchHistoryVO.setOdOptionName(resultSet.getString("Option_Name")!=null?resultSet.getString("Option_Name"):"");
				searchResult.add(searchHistoryVO);
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Get timeoff history ", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}			
		return searchResult;

	}

	@Override
	public List<ProcessLeaveRequestVO> processLeaveRequest(String employeeNumber, String leaveRequestId,
			int employeeId, int leaveTypeId, String leaveStatus, String fromdate, String toDate, String numberOfDays,
			String contactNumber, String leaveReason, int odOptionId, String sourceOfInput) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		List<ProcessLeaveRequestVO> result = new ArrayList<>();
		try{
			
			con = dataSource.getConnection();
			pstmt =con.prepareStatement(CommonConstants.CALL_DATASOURCE+env.getProperty("sp.process_leave_request")+"(?,?,?,?,?,?,?,?,?,?,?)}");
			SimpleDateFormat formatter=new SimpleDateFormat(CommonConstants.YYYY_MM_DD);
			VmsLogging.logInfo(getClass(), "  "+Integer.parseInt(leaveRequestId)+" :: "+employeeId+" :: "+leaveTypeId+" :: "+ leaveStatus);
			VmsLogging.logInfo(getClass(), " "+fromdate+" :: "+toDate+" :: "+numberOfDays+" :: "+contactNumber+" :: "+ leaveReason+ " :: "+odOptionId+" :: "+sourceOfInput);
			pstmt.setInt(1, Integer.parseInt(leaveRequestId));
			pstmt.setInt(2, employeeId);
			pstmt.setInt(3, leaveTypeId);
			pstmt.setString(4, !leaveStatus.isEmpty()?leaveStatus:null);
			if(leaveStatus.equals(CommonConstants.STATUS_REVOKED_APPROVED) || leaveStatus.equals(CommonConstants.STATUS_REVOKED_REJECTED)) {
				pstmt.setString(5, fromdate);
				pstmt.setString(6, toDate);
			}
			else {
				pstmt.setDate(5, fromdate!=null?new java.sql.Date(formatter.parse(fromdate).getTime()):null);
				pstmt.setDate(6, toDate!=null?new java.sql.Date(formatter.parse(toDate).getTime()):null);
			}
			pstmt.setString(7, !numberOfDays.isEmpty()?numberOfDays:null);
			pstmt.setString(8, !contactNumber.isEmpty()?contactNumber:null);
			pstmt.setString(9, !leaveReason.isEmpty()?leaveReason:null);
			pstmt.setInt(10, odOptionId);
			pstmt.setString(11, sourceOfInput);			
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()){
				ProcessLeaveRequestVO processLeaveRequestVO =  new ProcessLeaveRequestVO();
				processLeaveRequestVO.setStatus(CommonConstants.SUCCESS);
				processLeaveRequestVO.setLeaveRequestParentId(resultSet.getString("leave_request_link_id"));
					result.add(processLeaveRequestVO);
			}
		}catch(Exception e){
			ProcessLeaveRequestVO processLeaveRequestVO =  new ProcessLeaveRequestVO();
			processLeaveRequestVO.setStatus(CommonConstants.ERROR);
			VmsLogging.logError(getClass(), "Leave request process exception", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return result;
	}

	@Override
	public ArrayList<LeaveRequestIdInfo> getLeaveRequestInfo(String employeeNumber, String leaveRequestId) throws Exception,SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<LeaveRequestIdInfo> requestResonse = new ArrayList<>();
		try{
			VmsLogging.logInfo(getClass(), "Get Leave request information - "+employeeNumber+" - Leave Id -"+leaveRequestId);
			con = dataSource.getConnection();
			pstmt =con.prepareStatement(CommonConstants.CALL_DATASOURCE+env.getProperty("sp.get_leave_request_info")+"(?,?)}");
			pstmt.setString(1, employeeNumber);
			pstmt.setInt(2, Integer.parseInt(leaveRequestId));
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()){
				LeaveRequestIdInfo leaveRequestIdInfo = new LeaveRequestIdInfo();
				leaveRequestIdInfo.setLeaveRequestId(resultSet.getInt("Leave_Request_Id"));
				leaveRequestIdInfo.setFromDate(resultSet.getDate(FROM_DATE));
				leaveRequestIdInfo.setToDate(resultSet.getDate(TO_DATE));
				leaveRequestIdInfo.setNumberOfDays(resultSet.getInt("Days"));
				leaveRequestIdInfo.setLeaveTypeId(resultSet.getInt(LEAVE_TYPE_ID));
				leaveRequestIdInfo.setLeaveTypecode(resultSet.getString("Code")!=null?resultSet.getString("Code"):"");
				leaveRequestIdInfo.setLeaveTypeName(resultSet.getString("Type")!=null?resultSet.getString("Type"):"");
				leaveRequestIdInfo.setContactNumber(resultSet.getString(CONTACT_NUMBER)!=null?resultSet.getString(CONTACT_NUMBER):"");
				leaveRequestIdInfo.setInsightId(resultSet.getString("Insight_Id")!=null?resultSet.getString("Insight_Id"):"");
				leaveRequestIdInfo.setLeaveReason(resultSet.getString("Reason")!=null?resultSet.getString("Reason"):"");
				leaveRequestIdInfo.setAppliedDate(resultSet.getDate(APPLIED_ON));
				leaveRequestIdInfo.setApprovedById(resultSet.getInt(APPROVED_BY));
				leaveRequestIdInfo.setApprovedDate(resultSet.getString(APPROVED_DATE)!=null?resultSet.getString(APPROVED_DATE):"");
				leaveRequestIdInfo.setLeaveStatus(resultSet.getString("Status")!=null?resultSet.getString("Status"):"");
				leaveRequestIdInfo.setRejectedById(resultSet.getInt("Rejected_By"));
				leaveRequestIdInfo.setRejectedDate(resultSet.getDate("Rejected_Date"));
				leaveRequestIdInfo.setRejectedReason(resultSet.getString("Rejected_Reason")!=null?resultSet.getString("Rejected_Reason"):"");
				leaveRequestIdInfo.setRevokedById(resultSet.getInt("Revoked_By"));
				leaveRequestIdInfo.setRevokedDate(resultSet.getDate("Revoked_Date"));
				leaveRequestIdInfo.setRevokedReason(resultSet.getString("Revoked_Reason")!=null?resultSet.getString("Revoked_Reason"):"");
				leaveRequestIdInfo.setCancelledById(resultSet.getInt("Cancelled_By"));
				leaveRequestIdInfo.setCancelledDate(resultSet.getDate("Cancelled_Date"));
				leaveRequestIdInfo.setCancelledReason(resultSet.getString("Cancelled_Reason")!=null?resultSet.getString("Cancelled_Reason"):"");
				leaveRequestIdInfo.setApprovedStatus(resultSet.getString(APPROVED_STATUS)!=null?resultSet.getString(APPROVED_STATUS):"");
				leaveRequestIdInfo.setOdOptionCode(resultSet.getString(OD_OPTION_CODE)!=null?resultSet.getString(OD_OPTION_CODE):"");
				leaveRequestIdInfo.setOwnerEmployeeNumber(resultSet.getString(CommonConstants.EMPLOYEE_CODE)!=null?resultSet.getString(CommonConstants.EMPLOYEE_CODE):"");
				leaveRequestIdInfo.setOdOptionName(resultSet.getString("OD_Option_name")!=null?resultSet.getString("OD_Option_name"):"");
				requestResonse.add(leaveRequestIdInfo);
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Get leave request info", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return requestResonse;
	}

	@Override
	public LeaveTypeVO getLeaveTypeDetail(int leaveTypeId) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		LeaveTypeVO leaveType = new LeaveTypeVO();
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement(CommonConstants.CALL_DATASOURCE+env.getProperty("sp.get_leave_type_info")+"(?)}");
			pstmt.setInt(1, leaveTypeId);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()){
				leaveType.setLeaveTypeId(resultSet.getString(LEAVE_TYPE_ID));
				leaveType.setLeaveCode(resultSet.getString(CommonConstants.LEAVE_CODE));
				leaveType.setLeaveType(resultSet.getString(CommonConstants.LEAVE_TYPE));
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Get leave type detail", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return leaveType;
	}

	@Override
	public ArrayList<FetchLeaveRequestInfoVO> getChildLeaveRequestId(int leaverequestId, String managerId)
			throws Exception {
		ArrayList<FetchLeaveRequestInfoVO> childLeaveId = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement(CommonConstants.CALL_DATASOURCE+env.getProperty("sp.get_Lms_Leave_Request_Details")+"(?,?)}");
			pstmt.setString(1, managerId);
			pstmt.setInt(2, leaverequestId);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();		
			while(resultSet.next()){
				FetchLeaveRequestInfoVO leaveRequestInfoVO = new FetchLeaveRequestInfoVO();
				leaveRequestInfoVO.setLeaveRequestId(resultSet.getString("Leave_Request_Id"));
				leaveRequestInfoVO.setEmployeeCode(managerId);
				leaveRequestInfoVO.setFromDate(resultSet.getString(FROM_DATE));
				leaveRequestInfoVO.setToDate(resultSet.getString(TO_DATE));
				leaveRequestInfoVO.setNumberOfDays(resultSet.getDouble("No_Of_Days"));
				leaveRequestInfoVO.setLeaveTypeId(resultSet.getInt(LEAVE_TYPE_ID));
				leaveRequestInfoVO.setLeaveStatus(resultSet.getString("Leave_Status_Id"));
				leaveRequestInfoVO.setLeaveTypeName(resultSet.getString(CommonConstants.LEAVE_TYPE));
				childLeaveId.add(leaveRequestInfoVO);
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Fetch Leave Request Info Error", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return childLeaveId;
	}

	@Override
	public ArrayList<IdByEmailInfoVO> getEmpidFromEmail(String fromAddress) throws Exception {
		ArrayList<IdByEmailInfoVO> empData = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement(CommonConstants.CALL_DATASOURCE+env.getProperty("sp.get_employee_detail_from_email")+"(?)}");
			pstmt.setString(1, fromAddress);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()){
				IdByEmailInfoVO info = new IdByEmailInfoVO();
				info.setEmployeeCode(resultSet.getString(CommonConstants.EMPLOYEE_CODE)!=null?resultSet.getString(CommonConstants.EMPLOYEE_CODE):"");
				info.setEmployeeId(resultSet.getString(EMPLOYEE_ID)!=null?resultSet.getString(EMPLOYEE_ID):"");
				info.setEmployeeName(resultSet.getString(CommonConstants.EMPLOYEE_NAME)!=null?resultSet.getString(CommonConstants.EMPLOYEE_NAME):"");
				empData.add(info);
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Id By Email Info Error", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return empData;
	}

}
