package com.photon.vms.service.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.dao.LeaveCreditDAO;
import com.photon.vms.service.LeaveCreditService;
import com.photon.vms.vo.AutoApprovalEmpDtlVO;
import com.photon.vms.vo.LeaveCreditDataVO;
import com.photon.vms.vo.LeaveCreditLog;
import com.photon.vms.vo.LeaveHistoryVO;
import com.photon.vms.vo.LeaveType;
import com.photon.vms.vo.LeaveUpdateLWRequest;
import com.photon.vms.vo.RequestHistoryVO;
import com.photon.vms.vo.RequestLeaveInsertUpdateVo;
import com.photon.vms.vo.UnFreezeResponseVO;

@Service
public class LeaveCreditServiceImpl implements LeaveCreditService{

	@Autowired
	LeaveCreditDAO leaveCreditDAO;
	
	@Override
	public Map<String, ArrayList<?>> getEmployeeLeaveBalance(String employeeNumber) {
		Map<String, ArrayList<?>> response = new HashMap<>();
		try {
			response = leaveCreditDAO.getEmployeeLeaveBalance(employeeNumber);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Leave Balance info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}

	@Override
	public Map<String, ArrayList<?>> getLeaveHistory(RequestHistoryVO requestParameter) {
		Map<String, ArrayList<?>> response = new HashMap<>();
		try {
			response = leaveCreditDAO.getLeaveHistory(requestParameter);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Leave History info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}

	@Override
	public UnFreezeResponseVO insertUpdateLeave(RequestLeaveInsertUpdateVo requestParameter) {
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		try {
			response = leaveCreditDAO.insertUpdateLeave(requestParameter);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Admin Location info", new VmsApplicationException(e.getMessage()));
		} 
		return response;
	}
	
	@Override
	public UnFreezeResponseVO bulkUpload(String employeeNumber, LeaveCreditDataVO leaveData){
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		try {
			String bulkData = convertObjectToXML(leaveData);
			System.out.println("Bulk "+bulkData);
			VmsLogging.logInfo(getClass() , "bulk "+bulkData);
			response = leaveCreditDAO.bulkUpload(employeeNumber, bulkData);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Admin Location info", new VmsApplicationException(e.getMessage()));
		}
		
		return response;
	}
	
	@Override
	public List<LeaveCreditLog> leaveCreditLog(String leaveCreditId) {
		List<LeaveCreditLog> leaveLog = null;
		try {
			leaveLog = leaveCreditDAO.leaveCreditLog(leaveCreditId);
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Leave Log File", new VmsApplicationException(e.getMessage()));
		}
		return leaveLog;
	}

	@Override
	public UnFreezeResponseVO leaveUpdateLW(LeaveUpdateLWRequest request) {
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		try {
			response = leaveCreditDAO.leaveUpdateLW(request);
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Leave Update LW Failed", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	public String convertObjectToXML(LeaveCreditDataVO leaveData) throws JAXBException {
		String xmlObject;
		
        JAXBContext jaxbContext = JAXBContext.newInstance(LeaveCreditDataVO.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(leaveData, sw);
        xmlObject = sw.toString();
		return xmlObject;
	}

	@Override
	public ArrayList<LeaveHistoryVO> getAdminReport(String employeeNumber,String flag,String fromDate,String toDate) {
		ArrayList<LeaveHistoryVO> response = new ArrayList<>();
		try {
			response = leaveCreditDAO.getAdminReport(employeeNumber,flag,fromDate,toDate);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Admin - Report info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	@Override
	public List<LeaveType> getLeaveTypeByLocation(int locationId){
		List<LeaveType> response = null;
		try {
			response = leaveCreditDAO.getLeaveTypeByLocation(locationId);
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Leave Type By Location - info", new VmsApplicationException(e.getMessage()));
			
		}
		return response;
	}

	@Override
	public  Map<String, ArrayList<?>> getLeaveCreditCOFFDate(String employeeNumber) {
		Map<String, ArrayList<?>> response = new HashMap<>();
		try {
			response = leaveCreditDAO.getLeaveCreditCOFFDate(employeeNumber);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Leave Credit COFF Date  service Info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}

	@Override
	public Map<String, ArrayList<?>> getAutoApprovalEmpDtl() {
		Map<String, ArrayList<?>> response = new HashMap<>();
		try {
			response = leaveCreditDAO.getAutoApprovalEmpDtl();
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Auto Approval service info", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	@Override
	public UnFreezeResponseVO autoApproval(String loginEmployeeCode, String employeeCode, int isActive) {
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		try {
			response = leaveCreditDAO.autoApproval(loginEmployeeCode,employeeCode,isActive);
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Add or Update Employee - Auto Approval service info", new VmsApplicationException(e.getMessage()));	
		}
		return response;
	}
	
	@Override
	public List<AutoApprovalEmpDtlVO> getAutoApprovalEmployee(){
		List<AutoApprovalEmpDtlVO> response = null;
		try {
			response = leaveCreditDAO.getAutoApprovalEmployee();
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Get Employee List - Auto Approval service info", new VmsApplicationException(e.getMessage()));	
			}
		return response;
	}
}
