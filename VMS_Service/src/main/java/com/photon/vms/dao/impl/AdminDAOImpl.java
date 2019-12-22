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
import com.photon.vms.dao.AdminDAO;
import com.photon.vms.util.OnlineUtils;
import com.photon.vms.vo.LeaveReminderInfo;
import com.photon.vms.vo.RevocationEmailVO;

/**
 * @author karthigaiselvan_r
 *
 */
@Service
@Repository
public class AdminDAOImpl implements AdminDAO{
	@Autowired 
	private DataSource dataSource;
	@Autowired
	private Environment env;
	
	@Override
	public boolean isValidUnfrozenRecords(String fromDate, String toDate, int employeeId) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			VmsLogging.logInfo(getClass(), "Check freeze date info - "+employeeId+" - fromDate & toDate -"+fromDate+" "+fromDate);
			con = dataSource.getConnection();
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.check_unfreeze_dates")+"(?,?,?,?)}");
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
			pstmt.setDate(1, !fromDate.isEmpty()?new java.sql.Date(formatter.parse(fromDate).getTime()):null);
			pstmt.setDate(2, !toDate.isEmpty()?new java.sql.Date(formatter.parse(toDate).getTime()):null);
			pstmt.setInt(3, employeeId);
			java.util.Date d = sdf.parse(sdf.format(new java.util.Date()));
			pstmt.setDate(4, new java.sql.Date(d.getTime()));
	
			boolean isResultAvailable = pstmt.execute();
			int count = 0;
			if(isResultAvailable){
				resultSet= pstmt.getResultSet();
				while(resultSet.next()){
					count++;
				}
			}
			if(count > 0){
				return true;
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Check freeze date info - "+employeeId+" - fromDate & toDate -"+fromDate+" "+fromDate, e);
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return false;
	}

	@Override
	public List<LeaveReminderInfo> getReminderLeaveList() throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<LeaveReminderInfo> leaveDetailList = new ArrayList<>();
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.get_LMS_Mgr_Reminder")+"}");
			pstmt.executeQuery();
			rs = pstmt.getResultSet();
			while (rs.next()) {
				LeaveReminderInfo info = new LeaveReminderInfo();
				info.setEmployeeCode(rs.getString("Employee_Code"));
				info.setEmployeeName(rs.getString("Employee_Name"));
				info.setFromDate(rs.getDate("From_Date"));
				info.setManagerEmail(rs.getString("Manager_Email"));
				info.setToDate(rs.getDate("To_Date"));
				info.setDays(rs.getDouble("No_Of_Days"));
				info.setLeaveType(rs.getString("Leave_Code"));
				info.setManagerName(rs.getString("Reporting_Manager_Name"));
				info.setAppliedOn(rs.getDate("Submitted_Date"));
				info.setStatus(rs.getString("Leave_Status"));
				info.setReason(rs.getString("Reason"));
				info.setOdOptionCode(rs.getString("option_code"));
				info.setOdOptionName(rs.getString("option_name"));
				leaveDetailList.add(info);
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), e.getMessage(), e);
		}finally{
			OnlineUtils.destroyObjects(rs,pstmt,con);
		}
		return leaveDetailList;
	}
	
	public List<RevocationEmailVO> getRevocationListForAdmin() throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		List<RevocationEmailVO> revocationEmail = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.Get_LMS_Fin_Admin_Reminder")+"}");
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while (resultSet.next()) {
				RevocationEmailVO response = new RevocationEmailVO();
				response.setEmployeeCode(resultSet.getString("employee_code"));
				response.setLeaveRequestId(resultSet.getString("leave_request_id"));
				response.setEmployeeName(resultSet.getString("employee_name"));
				response.setFromDate(resultSet.getString("from_date"));
				response.setToDate(resultSet.getString("to_date"));
				response.setLeaveCode(resultSet.getString("leave_code"));
				response.setLeaveStatus(resultSet.getString("leave_status"));
				response.setRevokedReason(resultSet.getString("revoked_reason"));
				response.setRevokedDate(resultSet.getString("revoked_date"));
				response.setNoOfDays(resultSet.getString("no_of_days"));
				response.setFinanceEmployeeEmail(resultSet.getString("finance_emp_email"));
				revocationEmail.add(response);
			}
			
		}catch(Exception e){
			VmsLogging.logError(getClass(), e.getMessage(), e);
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return revocationEmail;
	}

}
