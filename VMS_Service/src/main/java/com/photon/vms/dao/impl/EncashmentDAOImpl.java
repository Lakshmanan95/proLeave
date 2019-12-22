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
import com.photon.vms.dao.EncashmentDAO;
import com.photon.vms.util.JSONUtil;
import com.photon.vms.util.OnlineUtils;
import com.photon.vms.vo.EncashmentDays;
import com.photon.vms.vo.EncashmentDetails;
import com.photon.vms.vo.EncashmentPolicyAndOption;
import com.photon.vms.vo.SuccessResponseVO;

@Service
@Repository
public class EncashmentDAOImpl implements EncashmentDAO{

	@Autowired 
	private DataSource dataSource;
	@Autowired 
	private Environment env;
	
	@Override
	public EncashmentDetails getEncashDetails(String employeeNumber) throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		EncashmentDetails encashmentDetails = new EncashmentDetails();
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.Get_Lms_Leave_Encashment_Dtl")+"(?)}");
			pstmt.setString(1, employeeNumber);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			List<EncashmentPolicyAndOption> encashmentPolicy = new ArrayList<>();
			List<EncashmentPolicyAndOption> encashmentOption = new ArrayList<>();
			List<EncashmentDays> encashmentDays = new ArrayList<>();
			
			while (resultSet.next()) {
				EncashmentPolicyAndOption policy = new EncashmentPolicyAndOption();
				policy.setLeaveEncashId(resultSet.getInt("leave_encash_id"));
				policy.setFlag(resultSet.getString("flag"));
				policy.setMastDescription(resultSet.getString("mast_description"));
				policy.setMastValue(resultSet.getInt("mast_value"));
				encashmentPolicy.add(policy);
				
			}
			System.out.println("policy "+JSONUtil.toJson(encashmentPolicy));
			encashmentDetails.setEncashmentPolicy(encashmentPolicy);
			if(pstmt.getMoreResults()){
				resultSet = pstmt.getResultSet();
				while(resultSet.next()) {
					EncashmentPolicyAndOption option = new EncashmentPolicyAndOption();
					option.setLeaveEncashId(resultSet.getInt("leave_encash_id"));
					option.setFlag(resultSet.getString("flag"));
					option.setMastDescription(resultSet.getString("mast_description"));
					option.setMastValue(resultSet.getInt("mast_value"));
					option.setIsSelected(resultSet.getInt("is_selected"));
					encashmentOption.add(option);
				}
			}
			System.out.println("option "+JSONUtil.toJson(encashmentOption));
			encashmentDetails.setEncashmentOption(encashmentOption);
			if(pstmt.getMoreResults()){
				resultSet = pstmt.getResultSet();
				while(resultSet.next()) {
					EncashmentDays encashDays = new EncashmentDays();
					encashDays.setExpectedAsOn(resultSet.getString("expected_as_on"));
					encashDays.setCurrentPlBalance(resultSet.getFloat("current_pl_balance"));
					encashDays.setCarryForwardedDaysOp1(resultSet.getFloat("carry_forwarded_days_op1"));
					encashDays.setCarryForwardedDaysOp2(resultSet.getFloat("carry_forwarded_days_op2"));
					encashDays.setEncashedDaysOp1(resultSet.getFloat("encashed_days_op1"));
					encashDays.setEncashedDaysOp2(resultSet.getFloat("encashed_days_op2"));
					encashmentDays.add(encashDays);
					
				}
			}
			encashmentDetails.setEncashmentDays(encashmentDays);
		}catch(Exception e){
			VmsLogging.logError(getClass(), e.getMessage(), e);
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return encashmentDetails;
	}
	
	
	@Override
	public SuccessResponseVO leaveEncashmentProcess(String employeeNumber, int leaveEncashId) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		SuccessResponseVO response = new  SuccessResponseVO();
		try{
			con = dataSource.getConnection();
			pstmt =con.prepareStatement("{call "+env.getProperty("sp.Ind_Ups_Leave_Encashment_Dtl")+"(?,?)}");
			pstmt.setString(1, employeeNumber);
			pstmt.setInt(2, leaveEncashId);
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();
			while(resultSet.next()) {
				response.setStatus(resultSet.getString("error_code"));
				if(response.getStatus().equals("SUCCESS"))
					response.setSuccessMessage(resultSet.getString("error_desc"));
				else
					response.setErrorMessage(resultSet.getString("error_desc"));
			}
		}
		catch(Exception e){
			VmsLogging.logError(getClass(), e.getMessage(), e);
		}finally{
			OnlineUtils.destroyObjects(resultSet,pstmt,con);
		}
		return response;
	}
	
}
