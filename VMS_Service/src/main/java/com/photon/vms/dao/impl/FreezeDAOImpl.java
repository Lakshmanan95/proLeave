package com.photon.vms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.dao.FreezeDAO;
import com.photon.vms.util.OnlineUtils;
import com.photon.vms.vo.EmployeeAdminLocationVO;
import com.photon.vms.vo.EmployeeVO;
import com.photon.vms.vo.LeaveUnfreezeDetails;
import com.photon.vms.vo.UnFreezeResponseVO;

@Service
public class FreezeDAOImpl implements FreezeDAO {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private Environment env;

	@Override
	public Map<String, ArrayList<?>> getEmployeeAdminLocation(String employeeNumber) throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<EmployeeAdminLocationVO> empAdminLocationInfo = new ArrayList<>();
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.get_LMS_Employee_Admin_Location") + "(?)}");
			pstmt.setString(1, employeeNumber);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Employee-Admin Location info" + resultSet);
			while (resultSet.next()) {
				EmployeeAdminLocationVO employeeAdminLocationVO = new EmployeeAdminLocationVO();
				employeeAdminLocationVO.setLocationId(resultSet.getString("Location_Id") != null ? resultSet.getString("Location_Id") : "");
				employeeAdminLocationVO.setLocationCode(resultSet.getString("Location_Code") != null ? resultSet.getString("Location_Code") : "");
				employeeAdminLocationVO.setLocationName(resultSet.getString("Location_Name") != null ? resultSet.getString("Location_Name") : "");
				employeeAdminLocationVO.setCompanyName(resultSet.getString("Company_Name") != null ? resultSet.getString("Company_Name") : "");
				employeeAdminLocationVO.setIsAdmin(resultSet.getString("Is_Admin") != null ? resultSet.getString("Is_Admin") : "");
				empAdminLocationInfo.add(employeeAdminLocationVO);
			}
			resultmap.put("EmpAdminLocationInfo", empAdminLocationInfo);
			return resultmap;
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Admin Location info", new VmsApplicationException(e.getMessage()));
			return null;
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
	}

	@Override
	public Map<String, ArrayList<?>> getLeaveUnFreezedDetails(String employeeNumber,int locId) throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<LeaveUnfreezeDetails> leaveUnfreezeInfo = new ArrayList<>();
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.get_LMS_Leave_UnFreezed_Details") + "(?,?)}");
			pstmt.setString(1, employeeNumber);
			pstmt.setInt(2, locId);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Leave UnFreeze info" + resultSet);
			while (resultSet.next()) {
				LeaveUnfreezeDetails leaveUnfreezeDetailsVO = new LeaveUnfreezeDetails();
				leaveUnfreezeDetailsVO.setUnFreezeId(resultSet.getString("UnFreeze_Id") != null ? resultSet.getString("UnFreeze_Id") : "");
				leaveUnfreezeDetailsVO.setFromDate(resultSet.getDate("From_Date"));
				leaveUnfreezeDetailsVO.setToDate(resultSet.getDate("To_Date"));
				leaveUnfreezeDetailsVO.setLocationId(resultSet.getString("loc_id") != null ? resultSet.getString("loc_id") : "");
				leaveUnfreezeDetailsVO.setLocationCode(resultSet.getString("loc_code") != null ? resultSet.getString("loc_code") : "");
				leaveUnfreezeDetailsVO.setLocationName(resultSet.getString("loc_name") != null ? resultSet.getString("loc_name") : "");
				leaveUnfreezeDetailsVO.setUnFreezeComment(resultSet.getString("UnFreeze_Comments") != null ? resultSet.getString("UnFreeze_Comments") : "");
				leaveUnfreezeDetailsVO.setUnFreezeDate(formatter.format(resultSet.getTimestamp("UnFreeze_Date")) !=null ? formatter.format(resultSet.getTimestamp("UnFreeze_Date")) : "");
				leaveUnfreezeDetailsVO.setUnFreezeByCode(resultSet.getString("UnFreeze_BY_code") != null ? resultSet.getString("UnFreeze_BY_code") : "");
				leaveUnfreezeDetailsVO.setUnFreezeByName(resultSet.getString("UnFreeze_BY_Name") != null ? resultSet.getString("UnFreeze_BY_Name") : "");
				leaveUnfreezeDetailsVO.setUnFreezeMonth(resultSet.getString("UnFreeze_Month") != null ? resultSet.getString("UnFreeze_Month") : "");
				leaveUnfreezeDetailsVO.setUnFreezeStatus(resultSet.getString("UnFreeze_status") != null ? resultSet.getString("UnFreeze_status") : "");
				leaveUnfreezeDetailsVO.setUnFreezeYear(resultSet.getString("UnFreeze_year") != null ? resultSet.getString("UnFreeze_year") : "");
				leaveUnfreezeInfo.add(leaveUnfreezeDetailsVO);
			}
			resultmap.put("LeaveUnfreezeInfo", leaveUnfreezeInfo);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Leave UnFreeze info", new VmsApplicationException(e.getMessage()));
			return null;
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return resultmap;
	}

	@Override
	public UnFreezeResponseVO insertUnfreezeDetails(String loginEmployeeCode, String unfreezeEmployeeCode, int locationId, String fromDate, String toDate,
										String comments, String flag) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.Ins_Upd_Leave_UnFreeze_Detail")+"(?,?,?,?,?,?,?)}");
			SimpleDateFormat formatter=new SimpleDateFormat(CommonConstants.YYYY_MM_DD);
			pstmt.setString(1, loginEmployeeCode);
			pstmt.setString(2, unfreezeEmployeeCode);
			pstmt.setInt(3, locationId);
			pstmt.setDate(4, fromDate!=null?new java.sql.Date(formatter.parse(fromDate).getTime()):null);
			pstmt.setDate(5, toDate!=null?new java.sql.Date(formatter.parse(toDate).getTime()):null);
			pstmt.setString(6, comments);
			pstmt.setString(7, flag);
			pstmt.execute();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()){
				response.setResultCode(resultSet.getString("Result_Code") != null ? resultSet.getString("Result_Code"):"SUCCESS" );
				response.setResultDesc(resultSet.getString("Result_Desc") != null ? resultSet.getString("Result_DESC"):"Location unfreezed" );
			}
			VmsLogging.logInfo(getClass(), "Unfreeze record inserted");
			
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Unfreeze request process exception", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return response;
	}
	
	@Override
	public Map<String, String> getEmployeeMail(String unfreezeEmployeeCode) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Map<String, String> employeeDetails = new HashMap<>();
		try{
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call "+env.getProperty("sp.get_Employee_Email")+"(?)}");
			pstmt.setString(1, unfreezeEmployeeCode);
			pstmt.execute();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()){
				employeeDetails.put("email", resultSet.getString("email"));
				employeeDetails.put("employeeName", resultSet.getString("Employee_name"));
			}
			VmsLogging.logInfo(getClass(), "Unfreeze record inserted");
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Unfreeze request process exception", new VmsApplicationException(e.getMessage()));
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return employeeDetails;
	}

	@Override
	public Map<String, ArrayList<?>> getActiveEmployee(String employeeNumber) throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ArrayList<EmployeeVO> employeeInfo = new ArrayList<>();
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement("{call " + env.getProperty("sp.get_LMS_Active_Employee")+"(?)}");
			pstmt.setString(1, employeeNumber);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Active Employee info" + resultSet);
			while (resultSet.next()) {
				EmployeeVO employeeVO = new EmployeeVO();
				employeeVO.setEmployeeId(resultSet.getString("employee_code") != null ? resultSet.getString("employee_code") : "");
				employeeVO.setEmployeeName(resultSet.getString("employee_name") != null ? resultSet.getString("employee_name") : "");
				employeeVO.setEmail(resultSet.getString("email") != null ? resultSet.getString("email") : "");
				employeeVO.setDoj(resultSet.getString("doj") != null ? resultSet.getString("doj") : "");
				employeeInfo.add(employeeVO);
			}
			resultmap.put("EmployeeInfo", employeeInfo);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Active Employee info Exception", new VmsApplicationException(e.getMessage()));
			return null;
		} finally {
			OnlineUtils.destroyObjects(resultSet, pstmt, con);
		}
		return resultmap;
	}
}
