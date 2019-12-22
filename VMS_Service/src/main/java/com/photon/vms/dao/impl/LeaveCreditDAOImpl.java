package com.photon.vms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.dao.LeaveCreditDAO;
import com.photon.vms.util.OnlineUtils;
import com.photon.vms.vo.AutoApprovalEmpDtlVO;
import com.photon.vms.vo.LeaveBalanceInfoVo;
import com.photon.vms.vo.LeaveCreditLog;
import com.photon.vms.vo.LeaveHistoryVO;
import com.photon.vms.vo.LeaveType;
import com.photon.vms.vo.LeaveUpdateLWRequest;
import com.photon.vms.vo.RequestHistoryVO;
import com.photon.vms.vo.RequestLeaveInsertUpdateVo;
import com.photon.vms.vo.UnFreezeResponseVO;

@Service
public class LeaveCreditDAOImpl implements LeaveCreditDAO {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private Environment env;
	
	
	public static final String LEAVE_CREDIT_ID = "leave_credit_id";
	public static final String CREATED_BY_CODE = "created_by_code";
	public static final String CREATED_BY_NAME = "created_by_name";
	public static final String INS_UPDATE_FLAG = "Ins_Upd_Flag";
	public static final String LEAVE_CATEGORY = "leave_category";
	public static final String EXISTING_VALUE = "existing_value";
	public static final String NEW_VALUE="new_value";
	public static final String COMMENTS="comments";

	public static final String RESULT_CODE="result_code";
	public static final String RESULT_DESC="result_desc";
	public static final String JOB_BAND="job_band";
	
	@Override
	public Map<String, ArrayList<?>> getEmployeeLeaveBalance(String employeeNumber) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<LeaveBalanceInfoVo> leaveBalanceInfo = new ArrayList<>();
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(CommonConstants.CALL_DATASOURCE + env.getProperty("sp.get_Employee_Leave_Balance") + "(?)}");
			pstmt.setString(1, employeeNumber);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Employee-Leave Balance info" + resultSet);
			while (resultSet.next()) {
				LeaveBalanceInfoVo leaveBalanceInfoVo = new LeaveBalanceInfoVo();
				leaveBalanceInfoVo.setLeaveTypeId(resultSet.getInt("Leave_Type_Id") != 0 ? resultSet.getInt("Leave_Type_Id") : 0);
				leaveBalanceInfoVo.setLeaveCode(resultSet.getString(CommonConstants.LEAVE_CODE) != null ? resultSet.getString(CommonConstants.LEAVE_CODE) : "");
				leaveBalanceInfoVo.setLeaveTypeName(resultSet.getString(CommonConstants.LEAVE_TYPE) != null ? resultSet.getString(CommonConstants.LEAVE_TYPE) : "");
				leaveBalanceInfoVo.setOpenLeave(resultSet.getDouble("Open") != 0 ? resultSet.getDouble("Open") : 0);
				leaveBalanceInfoVo.setCreditedLeave(resultSet.getDouble("Credit") != 0 ? resultSet.getDouble("Credit") : 0);
				leaveBalanceInfoVo.setUsedLeave(resultSet.getDouble("Used") != 0 ? resultSet.getDouble("Used") : 0);
				leaveBalanceInfoVo.setBalanceLeave(resultSet.getDouble("Balance") != 0 ? resultSet.getDouble("Balance") : 0);
				leaveBalanceInfoVo.setLocationCode(resultSet.getString("Location_Code") != null ? resultSet.getString("Location_Code") : "");
				leaveBalanceInfoVo.setIsDefault(resultSet.getString("Is_Default") != null ? resultSet.getString("Is_Default") : "");
				leaveBalanceInfoVo.setIsContinuous(resultSet.getString("Is_Continuous") != null ? resultSet.getString("Is_Continuous") : "");
				leaveBalanceInfoVo.setDisableFlag(resultSet.getString("disable_flag") != null ? resultSet.getString("disable_flag") : "");
				leaveBalanceInfoVo.setMaxLeaveOpen(resultSet.getInt("max_leave_open") != 0 ? resultSet.getInt("max_leave_open") : 0);
				leaveBalanceInfoVo.setMaxLeaveCredit(resultSet.getInt("max_leave_credit") != 0 ? resultSet.getInt("max_leave_credit") : 0);
				leaveBalanceInfo.add(leaveBalanceInfoVo);
			}
			resultmap.put("EmpLeaveBalaceInfo", leaveBalanceInfo);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Leave Balance info", new VmsApplicationException(e.getMessage()));
			return null;
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return resultmap;
	}

	@Override
	public Map<String, ArrayList<?>> getLeaveHistory(RequestHistoryVO requestParameterr) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<LeaveHistoryVO> leaveHistoryInfo = new ArrayList<>();
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(CommonConstants.CALL_DATASOURCE + env.getProperty("sp.get_Manage_Leave_Credit_Log") + "(?,?,?,?)}");
			pstmt.setString(1, requestParameterr.getFlag());
			pstmt.setString(2, requestParameterr.getEmployeeNumber());
			pstmt.setString(3, requestParameterr.getFromDate());
			pstmt.setString(4, requestParameterr.getToDate());
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Employee-Leave History info" + resultSet);
			SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
			while (resultSet.next()) {
				LeaveHistoryVO leaveHistoryInfoVo = new LeaveHistoryVO();
				leaveHistoryInfoVo.setSno(resultSet.getInt("Sno") !=0 ? resultSet.getInt("Sno") : 0);
				leaveHistoryInfoVo.setLeaveCreditId(resultSet.getString(LEAVE_CREDIT_ID) != null ? resultSet.getString(LEAVE_CREDIT_ID) : "");
				leaveHistoryInfoVo.setEmployeeId(resultSet.getString(CommonConstants.EMPLOYEE_CODE) != null ? resultSet.getString(CommonConstants.EMPLOYEE_CODE) : "");
				leaveHistoryInfoVo.setEmployeeName(resultSet.getString(CommonConstants.EMPLOYEE_NAME) != null ? resultSet.getString(CommonConstants.EMPLOYEE_NAME) : "");
				leaveHistoryInfoVo.setCreatedByCode(resultSet.getString(CREATED_BY_CODE) != null ? resultSet.getString(CREATED_BY_CODE) : "");
				leaveHistoryInfoVo.setCreatedByName(resultSet.getString(CREATED_BY_NAME) != null ? resultSet.getString(CREATED_BY_NAME) : "");
				leaveHistoryInfoVo.setCreatedByDate(formatter.format(resultSet.getTimestamp("Created_Date")) !=null ? formatter.format(resultSet.getTimestamp("Created_Date")): "");
				leaveHistoryInfoVo.setUpdateFlag(resultSet.getString(INS_UPDATE_FLAG) != null ? resultSet.getString(INS_UPDATE_FLAG) : "");
				leaveHistoryInfoVo.setLeaveCategory(resultSet.getString(LEAVE_CATEGORY) != null ? resultSet.getString(LEAVE_CATEGORY) : "");
				leaveHistoryInfoVo.setExistingValue(resultSet.getString(EXISTING_VALUE) != null ? resultSet.getString(EXISTING_VALUE) : "");
				leaveHistoryInfoVo.setNewValue(resultSet.getString(NEW_VALUE) != null ? resultSet.getString(NEW_VALUE) : "");
				leaveHistoryInfoVo.setLeaveCode(resultSet.getString(CommonConstants.LEAVE_CODE) != null ? resultSet.getString(CommonConstants.LEAVE_CODE) : "");
				leaveHistoryInfoVo.setLeaveType(resultSet.getString(CommonConstants.LEAVE_TYPE) != null ? resultSet.getString(CommonConstants.LEAVE_TYPE) : "");
				leaveHistoryInfoVo.setComments(resultSet.getString(COMMENTS) != null ? resultSet.getString(COMMENTS) : "");
				leaveHistoryInfoVo.setLocationName(resultSet.getString(CommonConstants.LOCATION_NAME) != null ? resultSet.getString(CommonConstants.LOCATION_NAME) : "");
				leaveHistoryInfo.add(leaveHistoryInfoVo);
			}
			resultmap.put("EmpLeaveHistoryInfo", leaveHistoryInfo);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Leave History info", new VmsApplicationException(e.getMessage()));
			return null;
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return resultmap;
	}

	@Override
	public UnFreezeResponseVO insertUpdateLeave(RequestLeaveInsertUpdateVo requestParameter) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement(CommonConstants.CALL_DATASOURCE+env.getProperty("sp.ins_Upd_Leave_Balance_Dtl")+"(?,?,?,?,?,?,?,?,?)}");
			pstmt.setString(1, requestParameter.getLoginEmployeeCode());
			pstmt.setString(2, requestParameter.getEmployeeNumber());
			pstmt.setInt(3, requestParameter.getLeaveTypeId());
			pstmt.setDouble(4, requestParameter.getLeaveOpen());
			pstmt.setDouble(5, requestParameter.getLeaveCredit());
			pstmt.setDouble(6, requestParameter.getLeaveUsed());
			pstmt.setString(7, requestParameter.getComments());
			pstmt.setString(8, requestParameter.getCompoffDate());
			pstmt.setString(9, requestParameter.getRevokeCompoffDate());
			pstmt.execute();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()){
				VmsLogging.logInfo(getClass(), "resutl in dao "+resultSet.getString(RESULT_CODE));
				response.setResultCode(resultSet.getString(RESULT_CODE) != null ? resultSet.getString(RESULT_CODE):CommonConstants.SUCCESS );
				response.setResultDesc(resultSet.getString(RESULT_DESC) != null ? resultSet.getString(RESULT_DESC):"Location unfreezed" );
			}
			VmsLogging.logInfo(getClass(), "Leave record inserted");
			
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Leave request process exception", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return response;
	}

	@Override
	public UnFreezeResponseVO bulkUpload(String employeeNumber, String bulkData) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		VmsLogging.logInfo(getClass(), " bulk data "+bulkData);
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.Lms_Leave_Bulk_Upload")+"(?,?)}");
			pstmt.setString(1, employeeNumber);
			pstmt.setString(2, bulkData);
			pstmt.execute();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()){
				response.setResultCode(resultSet.getString(RESULT_CODE) != null ? resultSet.getString(RESULT_CODE):CommonConstants.SUCCESS );
				response.setResultDesc(resultSet.getString(RESULT_DESC) != null ? resultSet.getString(RESULT_DESC):"File upload successfully" );
				response.setLeaveCreditId(resultSet.getString(LEAVE_CREDIT_ID) != null ? resultSet.getString(LEAVE_CREDIT_ID) : null);
			}
			VmsLogging.logInfo(getClass(), "file upload record inserted");
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Bulk Data process exception", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return response;
	}
	
	@Override
	public List<LeaveCreditLog> leaveCreditLog(String leaveCreditId) throws Exception{
		List<LeaveCreditLog> leaveCredits = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {			
			con = dataSource.getConnection();
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.Get_Bulk_Upload_log")+"(?)}");
			pstmt.setString(1, leaveCreditId);
			pstmt.execute();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()) {
				LeaveCreditLog leaveLog = new LeaveCreditLog();
				leaveLog.setEmployeeCode(resultSet.getString(CommonConstants.EMPLOYEE_CODE) != null ? resultSet.getString(CommonConstants.EMPLOYEE_CODE) : "");
				leaveLog.setEmployeeName(resultSet.getString(CommonConstants.EMPLOYEE_NAME) != null ? resultSet.getString(CommonConstants.EMPLOYEE_NAME) : "");
				leaveLog.setLeaveType(resultSet.getString(CommonConstants.LEAVE_TYPE) != null ? resultSet.getString(CommonConstants.LEAVE_TYPE) : "");
				leaveLog.setCreditValue(resultSet.getString("credit_value") != null ? resultSet.getString("credit_value") : "");
				leaveLog.setCompOffDate(resultSet.getString("comp_off_date") != null ? resultSet.getString("comp_off_date") : "");
				leaveLog.setCreditedDate(resultSet.getString("Credited_date") != null ? resultSet.getString("Credited_date") : "");
				leaveLog.setUploadStatus(resultSet.getString("upload_status") != null ? resultSet.getString("upload_status") : "");
				leaveLog.setComments(resultSet.getString(COMMENTS) != null ? resultSet.getString(COMMENTS) : "");
				leaveCredits.add(leaveLog);				
			}
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Leave Credit log process exception", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return leaveCredits;
	}
	
	@Override
	public UnFreezeResponseVO leaveUpdateLW(LeaveUpdateLWRequest requestParams) throws Exception {
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			con = dataSource.getConnection();
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.Lms_Locationwise_Balance_Bulk_update")+"(?,?,?,?)}");
			pstmt.setString(1, requestParams.getEmployeeCode());
			pstmt.setInt(2, requestParams.getLocationId());
			pstmt.setInt(3, requestParams.getLeaveTypeId());
			pstmt.setDouble(4, requestParams.getNoOfDays());
			pstmt.execute();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()) {
				response.setResultCode(resultSet.getString(RESULT_CODE) != null ? resultSet.getString(RESULT_CODE):CommonConstants.SUCCESS );
				response.setResultDesc(resultSet.getString(RESULT_DESC) != null ? resultSet.getString(RESULT_DESC):"Location wise leave update successfully" );
			}
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Leave update location wise exception", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return response;
	}

	@Override
	public ArrayList<LeaveHistoryVO> getAdminReport(String employeeNumber,String flag,String fromDate,String toDate) throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<LeaveHistoryVO> leaveHistoryInfo = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.get_Manage_Leave_Credit_Log") + "(?,?,?,?)}");
			pstmt.setString(1, flag);
			pstmt.setString(2, employeeNumber);
			pstmt.setString(3, fromDate);
			pstmt.setString(4, toDate);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Employee-Leave History info" + resultSet);
			SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
			while (resultSet.next()) {
				LeaveHistoryVO leaveHistoryInfoVo = new LeaveHistoryVO();
				leaveHistoryInfoVo.setSno(resultSet.getInt("Sno") !=0 ? resultSet.getInt("Sno") : 0);
				leaveHistoryInfoVo.setLeaveCreditId(resultSet.getString(LEAVE_CREDIT_ID) != null ? resultSet.getString(LEAVE_CREDIT_ID) : "");
				leaveHistoryInfoVo.setEmployeeId(resultSet.getString(CommonConstants.EMPLOYEE_CODE) != null ? resultSet.getString(CommonConstants.EMPLOYEE_CODE) : "");
				leaveHistoryInfoVo.setEmployeeName(resultSet.getString(CommonConstants.EMPLOYEE_NAME) != null ? resultSet.getString(CommonConstants.EMPLOYEE_NAME) : "");
				leaveHistoryInfoVo.setCreatedByCode(resultSet.getString(CREATED_BY_CODE) != null ? resultSet.getString(CREATED_BY_CODE) : "");
				leaveHistoryInfoVo.setCreatedByName(resultSet.getString(CREATED_BY_NAME) != null ? resultSet.getString(CREATED_BY_NAME) : "");
				leaveHistoryInfoVo.setCreatedByDate(formatter.format(resultSet.getTimestamp("Created_Date")) !=null ? formatter.format(resultSet.getTimestamp("Created_Date")): "");
				leaveHistoryInfoVo.setUpdateFlag(resultSet.getString(INS_UPDATE_FLAG) != null ? resultSet.getString(INS_UPDATE_FLAG) : "");
				leaveHistoryInfoVo.setLeaveCategory(resultSet.getString(LEAVE_CATEGORY) != null ? resultSet.getString(LEAVE_CATEGORY) : "");
				leaveHistoryInfoVo.setExistingValue(resultSet.getString(EXISTING_VALUE) != null ? resultSet.getString(EXISTING_VALUE) : "");
				leaveHistoryInfoVo.setNewValue(resultSet.getString(NEW_VALUE) != null ? resultSet.getString(NEW_VALUE) : "");
				leaveHistoryInfoVo.setLeaveCode(resultSet.getString(CommonConstants.LEAVE_CODE) != null ? resultSet.getString(CommonConstants.LEAVE_CODE) : "");
				leaveHistoryInfoVo.setLeaveType(resultSet.getString(CommonConstants.LEAVE_TYPE) != null ? resultSet.getString(CommonConstants.LEAVE_TYPE) : "");
				leaveHistoryInfoVo.setComments(resultSet.getString(COMMENTS) != null ? resultSet.getString(COMMENTS) : "");
				leaveHistoryInfoVo.setLocationName(resultSet.getString(CommonConstants.LOCATION_NAME) != null ? resultSet.getString(CommonConstants.LOCATION_NAME) : "");
				leaveHistoryInfo.add(leaveHistoryInfoVo);
			}
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Leave History info", new VmsApplicationException(e.getMessage()));
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return leaveHistoryInfo;
	}
	
	@Override
	public List<LeaveType> getLeaveTypeByLocation(int locationId) throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		List<LeaveType> leaveType = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.Get_Locationwise_Leave_Type") + "(?)}");
			pstmt.setInt(1, locationId);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Leave Type Query executed info" + resultSet);
			while(resultSet.next()) {
				LeaveType leave = new LeaveType();
				leave.setLeaveCode(resultSet.getString(CommonConstants.LEAVE_CODE));
				leave.setLeaveType(resultSet.getString(CommonConstants.LEAVE_TYPE));
				leave.setLeaveTypeId(resultSet.getInt("Leave_type_id"));
				leaveType.add(leave);
			}
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Leave Type by Location DAO Error", new VmsApplicationException(e.getMessage()));
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return leaveType;
	}

	@Override
	public  Map<String, ArrayList<?>> getLeaveCreditCOFFDate(String employeeNumber) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		ArrayList<Map<String, Object>> resultmapqq = new ArrayList<>();
		Map<String,Object> responce=null;
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.get_Manage_Leave_Credit_Coff_Dates") + "(?)}");
			pstmt.setString(1, employeeNumber);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Employee-Leave COFF Date info" + resultSet);

			while (resultSet.next()) {
				responce = new HashMap<>();
				responce.put("leaveCOFFDates", resultSet.getString("comm_off_date") != null ? resultSet.getString("comm_off_date") : "");
				resultmapqq.add(responce);
			}
			resultmap.put("LeaveCOFFDatesInfo", resultmapqq);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee-Leave COFF Date info", new VmsApplicationException(e.getMessage()));
			return null;
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return resultmap;
	}

	@Override
	public Map<String, ArrayList<?>> getAutoApprovalEmpDtl() throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<AutoApprovalEmpDtlVO> autoApprovalEmpDtlInfo = new ArrayList<>();
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.get_AutoApproval_Emp_Dtl")+ "}");
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Employee - Auto Approval info" + resultSet);

			while (resultSet.next()) {
				AutoApprovalEmpDtlVO autoApprovalEmpDtlVO = new AutoApprovalEmpDtlVO();
				autoApprovalEmpDtlVO.setEmployeeId(resultSet.getString("Employee_Id") != null ? resultSet.getString("Employee_Id") : "");
				autoApprovalEmpDtlVO.setEmployeeCode(resultSet.getString(CommonConstants.EMPLOYEE_CODE) != null ? resultSet.getString(CommonConstants.EMPLOYEE_CODE) : "");
				autoApprovalEmpDtlVO.setEmployeeName(resultSet.getString(CommonConstants.EMPLOYEE_NAME) != null ? resultSet.getString(CommonConstants.EMPLOYEE_NAME) : "");
				autoApprovalEmpDtlVO.setJobBand(resultSet.getString(JOB_BAND) != null ? resultSet.getString(JOB_BAND) : "");
				autoApprovalEmpDtlVO.setDesignation(resultSet.getString("Designation") != null ? resultSet.getString("Designation") : "");
				autoApprovalEmpDtlInfo.add(autoApprovalEmpDtlVO);
			}
			resultmap.put("AutoApprovalEmpDtlInfo", autoApprovalEmpDtlInfo);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Auto Approval info", new VmsApplicationException(e.getMessage()));
			return null;
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return resultmap;
	}
	
	@Override
	public UnFreezeResponseVO autoApproval(String loginEmployeeCode, String employeeCode, int isActive) throws Exception {
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			con = dataSource.getConnection();
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.Ins_Upd_Auto_Approval_Emp_Dtl")+"(?,?,?)}");
			pstmt.setString(1, loginEmployeeCode);
			pstmt.setString(2, employeeCode);
			pstmt.setInt(3, isActive);
			pstmt.execute();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()) {
				response.setResultCode(resultSet.getString(RESULT_CODE));
				response.setResultDesc(resultSet.getString(RESULT_DESC));				
			}
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "auto approval exception", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return response;
	}
	
	@Override
	public List<AutoApprovalEmpDtlVO> getAutoApprovalEmployee() throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		List<AutoApprovalEmpDtlVO> response = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.Get_Auto_Approval_List")+"}");
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()) {
				AutoApprovalEmpDtlVO employee = new AutoApprovalEmpDtlVO();
				employee.setEmployeeId(resultSet.getString("Employee_id"));
				employee.setEmployeeCode(resultSet.getString(CommonConstants.EMPLOYEE_CODE));
				employee.setJobBand(resultSet.getString(JOB_BAND));
				employee.setEmployeeName(resultSet.getString(CommonConstants.EMPLOYEE_NAME));
				employee.setDesignation(resultSet.getString("designation"));
				response.add(employee);
			}
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Get auto approval employee exception", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return response;
	}
}
