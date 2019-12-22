package com.photon.vms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.dao.UtilDAO;
import com.photon.vms.util.OnlineUtils;
import com.photon.vms.vo.AutoApprovalVO;

/**
 * @author sureshkumar_e
 *
 */
@Service
@Repository
public class UtilDAOImpl implements UtilDAO{

	@Autowired 
	private DataSource dataSource;
	@Autowired
	private Environment env;
	
	@Override
	public List<AutoApprovalVO> getAutoApprovalLeaves() throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AutoApprovalVO> leaveDetailList = new ArrayList<>();
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.get.LMS_Auto_Approve_SM_Leaves")+"}");
			pstmt.executeQuery();
			rs = pstmt.getResultSet();
			VmsLogging.logInfo(getClass(), "Auto Approval info" + rs);
			while (rs.next()) {
				AutoApprovalVO info = new AutoApprovalVO();
				info.setEmployeeNumber(rs.getString("Employee_code"));
				info.setEmployeeName(rs.getString("Employee_Name"));
				info.setEmail(rs.getString("Email"));
				info.setLeaveReqId(rs.getString("Leave_Request_Id"));
				info.setEmployeeId(rs.getInt("Employee_Id"));
				info.setLeaveTypeId(rs.getInt("Leave_Type_Id"));
				info.setLeaveTypeName(rs.getString("Leave_Type"));
				info.setFromDate(rs.getString("From_Date"));
				info.setToDate(rs.getString("To_Date"));
				info.setNoOfDays(rs.getString("No_Of_Days"));
				info.setContactNumber(rs.getString("Contact_Number"));
				info.setReason(rs.getString("Reason"));
				info.setOdType(rs.getInt("OD_Type"));
				info.setReportingMgrId(rs.getInt("Reporting_Mgr_Id"));
				leaveDetailList.add(info);
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), e.getMessage(), e);
		}finally{
			OnlineUtils.destroyObjects(rs,pstmt,con);
		}
		return leaveDetailList;
	}

}
