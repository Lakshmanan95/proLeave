package com.photon.vms.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photon.vms.TestConfig;
import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.dao.LeaveCreditDAO;
import com.photon.vms.dao.impl.FreezeDAOImpl;
import com.photon.vms.emailTemplate.EmailService;
import com.photon.vms.emailTemplate.EmailTemplateService;
import com.photon.vms.service.LeaveCreditService;
import com.photon.vms.service.impl.LeaveCreditServiceImpl;
import com.photon.vms.util.DateTimeUtils;
import com.photon.vms.util.JSONUtil;
import com.photon.vms.vo.AutoApprovalEmpDtlVO;
import com.photon.vms.vo.AutoApprovalEmployeeVO;
import com.photon.vms.vo.LeaveBalanceInfoVo;
import com.photon.vms.vo.LeaveCreditLog;
import com.photon.vms.vo.LeaveHistoryVO;
import com.photon.vms.vo.LeaveType;
import com.photon.vms.vo.LeaveUpdateLWRequest;
import com.photon.vms.vo.LocationIdRequestVO;
import com.photon.vms.vo.RequestHistoryVO;
import com.photon.vms.vo.RequestLeaveInsertUpdateVo;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.SuccessResponseVO;
import com.photon.vms.vo.UnFreezeResponseVO;
/**
 * @author lakshmanan_j
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(LeaveCreditController.class)
public class LeaveCreditControllerTest {

	@Autowired
	private LeaveCreditController leaveCreditController;
	
	@MockBean
	LeaveCreditService leaveCreditService;
	@MockBean
	DateTimeUtils dateTimeUtils;

	@MockBean
	FreezeDAOImpl freezeDAOImpl;

	@MockBean
	EmailTemplateService emailTemplateService;

	@MockBean
	EmailService emailService;
	
	@Autowired
	private MockMvc mvc;
	   public void setUp() {
	      mvc = MockMvcBuilders.standaloneSetup(leaveCreditController).build();
	      leaveCreditService = new LeaveCreditServiceImpl();
//	      financeDAO = new FinanceDAOImpl();
	   }
	   
	 	protected String mapToJson(Object obj) throws JsonProcessingException {
	      ObjectMapper objectMapper = new ObjectMapper();
	      return objectMapper.writeValueAsString(obj);
	   }
	
	/**
	 * @author lakshmanan_j
	 * @controller LeaveCreditController
	 */
	@Test
	public void getEmployeeLeaveBalance() {
		String uri = "/leaveCredit/getEmployeeLeaveBalance";
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		ArrayList<LeaveBalanceInfoVo> leaveBalanceInfo = new ArrayList<>();
		RequestParameterVO requestParameter = new RequestParameterVO();
		try {
			LeaveBalanceInfoVo leaveBalanceInfoVo = new LeaveBalanceInfoVo();
			leaveBalanceInfoVo.setLeaveTypeId(1);
			leaveBalanceInfoVo.setLeaveCode("CL");
			leaveBalanceInfoVo.setLeaveTypeName("Casual Leave");
			leaveBalanceInfoVo.setOpenLeave(10.00);
			leaveBalanceInfoVo.setCreditedLeave(0.00);
			leaveBalanceInfoVo.setUsedLeave(7.00);
			leaveBalanceInfoVo.setBalanceLeave(3.00);
			leaveBalanceInfoVo.setLocationCode("CHE");
			leaveBalanceInfoVo.setIsDefault("Y");
			leaveBalanceInfoVo.setIsContinuous("N");
			leaveBalanceInfoVo.setDisableFlag("");
			leaveBalanceInfoVo.setMaxLeaveOpen(5);
			leaveBalanceInfoVo.setMaxLeaveCredit(0);
			leaveBalanceInfo.add(leaveBalanceInfoVo);
			resultmap.put("EmpLeaveBalaceInfo", leaveBalanceInfo);
			requestParameter.setLoginEmployeeNumber("121720");
			requestParameter.setEmployeeNumber("121720");
			String value = mapToJson(requestParameter);
			
			Mockito.when(leaveCreditService.getEmployeeLeaveBalance("121720"))
				.thenReturn(resultmap);
			System.out.println("status "+JSONUtil.toJson(resultmap));
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee Leave Balance Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@Test
	public void getLeaveHistory() {
		String uri = "/leaveCredit/getLeaveHistory";
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		RequestHistoryVO requestParameter = new RequestHistoryVO();
		ArrayList<LeaveHistoryVO> leaveHistoryInfo = new ArrayList<>();
		try {
			LeaveHistoryVO leaveHistoryInfoVo = new LeaveHistoryVO();
			leaveHistoryInfoVo.setSno(1);
			leaveHistoryInfoVo.setLeaveCreditId("116");
			leaveHistoryInfoVo.setEmployeeId("121720");
			leaveHistoryInfoVo.setEmployeeName("Lakshmanan");
			leaveHistoryInfoVo.setCreatedByCode("101130");
			leaveHistoryInfoVo.setCreatedByName("Thiyagaraj");
			leaveHistoryInfoVo.setCreatedByDate("05/03/2019 04:30 PM");
			leaveHistoryInfoVo.setUpdateFlag("update");
			leaveHistoryInfoVo.setLeaveCategory("Open");
			leaveHistoryInfoVo.setExistingValue("22.88");
			leaveHistoryInfoVo.setNewValue("22.00");
			leaveHistoryInfoVo.setLeaveCode("PTO");
			leaveHistoryInfoVo.setLeaveType("Paid Time Off");
			leaveHistoryInfoVo.setComments("Testing");
			leaveHistoryInfoVo.setLocationName("CHE");
			leaveHistoryInfo.add(leaveHistoryInfoVo);
			resultmap.put("EmpLeaveHistoryInfo", leaveHistoryInfo);
			requestParameter.setEmployeeNumber("12720");
			Mockito.when(leaveCreditService.getLeaveHistory(requestParameter)).thenReturn(resultmap);
			String value = mapToJson(requestParameter);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee Leave Balance History Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@Test
	public void insertUpdateLeave() {
		String uri = "/leaveCredit/insertUpdateLeave";
		RequestLeaveInsertUpdateVo request = new RequestLeaveInsertUpdateVo();
		request.setEmployeeNumber("121720");
		request.setLoginEmployeeCode("105180");
		request.setLeaveTypeId(2);
		request.setLeaveOpen(0.00);
		request.setLeaveUsed(0.00);
		request.setLeaveCredit(4.00);
		request.setComments("Test");
		request.setCompoffDate(null);
		request.setRevokeCompoffDate("2019-05-01,2019-12-05");
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		response.setResultCode("SUCCESS");
		response.setResultDesc("Location unfreezed");
		try {
			Mockito.when(leaveCreditService.insertUpdateLeave(request)).thenReturn(response);
			String value = mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Insert Or Update Leave Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	/*@Test
	public void bulkUpload() {
		MultipartFile uploader = null;String employeeNumber = null;
		SuccessResponseVO response = new SuccessResponseVO();
		ServletRequest req = null;
		try {
			Mockito.when(leaveCreditController.bulkUpload(uploader,employeeNumber,req)).thenReturn(response);
			
			SuccessResponseVO resultmap1 = leaveCreditController.bulkUpload(uploader,employeeNumber,req);
			
			Assert.assertEquals(response, resultmap1);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Bulk Upload Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	} */
	
	@Test
	public void leaveUpdateLW() {
		String uri = "/leaveCredit/leaveUpdateLW";
		LeaveUpdateLWRequest requestParameter = new LeaveUpdateLWRequest();
		LeaveUpdateLWRequest request = new LeaveUpdateLWRequest();
		request.setEmployeeCode("121720");
		request.setLeaveTypeId(1);
		request.setLocationId(1);
		request.setNoOfDays(1);
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		response.setResultCode("SUCCESS");
		response.setResultDesc("Location Wise leave has been updated successfully");
		try {
			Mockito.when(leaveCreditService.leaveUpdateLW(request)).thenReturn(response);
			String value = mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Leave Update Location Wise Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@Test
	public void getLeaveTypeLocation() {
		String uri = "/leaveCredit/getLeaveType";
		LocationIdRequestVO request = new LocationIdRequestVO();
		request.setEmployeeNumber("121720");
		request.setLocationId(11);
		List<LeaveType> leaveType = new ArrayList<>();
		LeaveType leave = new LeaveType();
		leave.setLeaveCode("PTO");
		leave.setLeaveType("Paid Time Off");
		leave.setLeaveTypeId(4);
		LeaveType leave1 = new LeaveType();
		leave1.setLeaveCode("APTO");
		leave1.setLeaveType("Advance PTO");
		leave1.setLeaveTypeId(22);
		leaveType.add(leave);
		leaveType.add(leave1);
		try {
			Mockito.when(leaveCreditService.getLeaveTypeByLocation(11)).thenReturn(leaveType);
			String value = mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Leave Update Location Wise Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@Test
	public void getCSVFileStream() {
		List<LeaveCreditLog> leaveCreditLog=null;
		String streamOfData = null;
		try {
//			Mockito.when(leaveCreditController.getCSVFileStream(leaveCreditLog)).thenReturn(streamOfData);
//			
//			String resultmap1 = leaveCreditController.getCSVFileStream(leaveCreditLog);
//			
//			Assert.assertEquals(streamOfData, resultmap1);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Get CSV File Stream Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@Test
	public void getLeaveCreditCOFFDate() {
		String uri = "/leaveCredit/getLeaveCreditCOFFDate";
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		ArrayList<Map<String, Object>> resultmapqq = new ArrayList<>();
		RequestParameterVO requestParameter = new RequestParameterVO();
		requestParameter.setLoginEmployeeNumber("121720");
		requestParameter.setEmployeeNumber("121720");
		Map<String,Object> response = new HashMap<>(); 
		response.put("leaveCOFFDates", "2019-02-09");
		resultmapqq.add(response);
		resultmap.put("LeaveCOFFDatesInfo", resultmapqq);
		try {
			Mockito.when(leaveCreditService.getLeaveCreditCOFFDate("121720")).thenReturn(resultmap);
			String value = mapToJson(requestParameter);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Get Leave Credit COFF Date Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@Test
	public void getAutoApprovalEmpDtl() {
		String uri = "/leaveCredit/getAutoApprovalEmpDtl";
		ArrayList<AutoApprovalEmpDtlVO> autoApprovalEmpDtlInfo = new ArrayList<>();
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		RequestParameterVO requestParameter = new RequestParameterVO();
		requestParameter.setLoginEmployeeNumber("121720");
		requestParameter.setEmployeeNumber("121720");
		AutoApprovalEmpDtlVO autoApprovalEmpDtlVO = new AutoApprovalEmpDtlVO();
		autoApprovalEmpDtlVO.setEmployeeId("121720");
		autoApprovalEmpDtlVO.setEmployeeCode("121720");
		autoApprovalEmpDtlVO.setEmployeeName("Lakshmanan");
		autoApprovalEmpDtlVO.setJobBand("JM1");
		autoApprovalEmpDtlVO.setDesignation("Software Engineer");
		autoApprovalEmpDtlInfo.add(autoApprovalEmpDtlVO);
		try {
			Mockito.when(leaveCreditService.getAutoApprovalEmpDtl()).thenReturn(resultmap);
			String value = mapToJson(requestParameter);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Auot Approval Employee Detail Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@Test
	public void autoApproval() {
		String uri = "/leaveCredit/autoApproval";
		SuccessResponseVO response = new SuccessResponseVO();
		UnFreezeResponseVO responses = new UnFreezeResponseVO();
		responses.setResultCode("SUCCESS");
		responses.setResultDesc("Location Wise leave has been updated successfully");
		RequestParameterVO requestParameter = new RequestParameterVO();
		requestParameter.setLoginEmployeeNumber("121720");
		requestParameter.setEmployeeNumber("121720");
		HttpServletRequest req = null;
		try {
			Mockito.when(leaveCreditService.autoApproval("121720","121720",0)).thenReturn(responses);
			String value = mapToJson(requestParameter);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Auto Approval Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@Test
	public void getAutoApprovalEmployee() {
		String uri = "/leaveCredit/getAutoApprovalEmployee";
		AutoApprovalEmployeeVO response = new AutoApprovalEmployeeVO();
		List<AutoApprovalEmpDtlVO> responseVO = new ArrayList<>();
		AutoApprovalEmpDtlVO employee = new AutoApprovalEmpDtlVO();
		employee.setEmployeeId("121720");
		employee.setEmployeeCode("121720");
		employee.setJobBand("JM1");
		employee.setEmployeeName("Lakshmanan");
		employee.setDesignation("Software Engineer");
		responseVO.add(employee);
		response.setEmployee(responseVO);
		response.setStatus("Success");
		try {
			Mockito.when(leaveCreditService.getAutoApprovalEmployee()).thenReturn(responseVO);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Get Auto Approval Employee Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
}
