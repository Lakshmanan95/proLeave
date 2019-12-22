package com.photon.vms.controller;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photon.vms.Application;
import com.photon.vms.TestConfig;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.dao.RevocationDAO;
import com.photon.vms.dao.impl.RevocationDAOImpl;
import com.photon.vms.emailTemplate.EmailService;
import com.photon.vms.emailTemplate.EmailTemplateService;
import com.photon.vms.service.EmployeeHomeService;
import com.photon.vms.service.RevocationService;
import com.photon.vms.util.DateTimeUtils;
import com.photon.vms.vo.PendingLeaveRequsetVO;
import com.photon.vms.vo.PunchedHours;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.SuccessResponseVO;
import com.photon.vms.vo.ViewRevocactionResponseVO;
import com.photon.vms.vo.ViewRevocation;

@ActiveProfiles("test")
@WebMvcTest(RevocationController.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RevocationControllerTest {

	@Autowired
	protected MockMvc mvc;
	@Autowired
	RevocationController revocationController;
	
	HomeControllerTest homeControllerTest = new HomeControllerTest();
	
	@MockBean 
	RevocationService revocationService;
		
	@MockBean
	RevocationDAO revocationDAO;
	@MockBean
	EmployeeHomeService employeeHomeService;
	@MockBean
	HomeController homeController;
	@MockBean
	EmailService emailService;
	@MockBean
	EmailTemplateService emailTemplateService;
	
	@MockBean
	DateTimeUtils dateTimeUtils;
	
	private static final String EMPLOYEE_NUMBER="121720";
	private static final String LEAVE_DATE="03/28/2019";
		
	

	   public void setUp() {
	      mvc = MockMvcBuilders.standaloneSetup(revocationController).build();
	      revocationDAO = new RevocationDAOImpl();
		}
	   
	 	protected String mapToJson(Object obj) throws JsonProcessingException {
	      ObjectMapper objectMapper = new ObjectMapper();
	      return objectMapper.writeValueAsString(obj);
	   }
	 	
	 @Test
	 public void viewRevocation() {
		 String uri = "/revocation/viewRevocation";
		 RequestParameterVO request = new RequestParameterVO();
		 request.setEmployeeNumber(EMPLOYEE_NUMBER);
		 request.setLeaveRequestId("141442");
		 ViewRevocation revocation = new ViewRevocation();
		 ViewRevocactionResponseVO   response = new ViewRevocactionResponseVO();
		 List<PunchedHours> punchedHoursList = new ArrayList<>();
		 revocation.setEmployeeCode(EMPLOYEE_NUMBER);
		 revocation.setFromDate(LEAVE_DATE);
		 revocation.setToDate(LEAVE_DATE);
		 revocation.setRevocationReason("Test");
		 revocation.setRevocationRequestedOn("04/28/2019");
		 revocation.setApprovedStatus("Pending Revocation");
		 PunchedHours punchedHours = new PunchedHours();
		 punchedHours.setEmployeeCode(EMPLOYEE_NUMBER);
		 punchedHours.setPunchDate(LEAVE_DATE);
		 punchedHours.setPrdHrs("480");
		 punchedHoursList.add(punchedHours);
		 revocation.setPunchedHours(punchedHoursList);
		 response.setViewRevocation(revocation);
		
		 try {
			String value = mapToJson(request);
			Mockito.when(revocationDAO.viewRevocationDetail(request.getLeaveRequestId())).thenReturn(response);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
//			Mockito.verify(revocationDAO).viewRevocationDetail(request.getLeaveRequestId());
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "View Revocation Test cases", e);
		}
		 
	 }
	 
	 @Test
	 public void getpendingRevocationListForFinance() {
		 String uri = "/revocation/pendingRevocationListForFinance";
		 RequestParameterVO request = homeControllerTest.request();
		 List<PendingLeaveRequsetVO> pendingList = pendingList();
		 try {
			String value = mapToJson(request);
			Mockito.when(revocationDAO.getPendingLeaveForAdmin(request)).thenReturn(pendingList);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
//			Mockito.verify(revocationDAO.getPendingLeaveForAdmin(request));
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Get Revocation List Test cases", e);
		}
		 
	 }
	 
	 @Test
	 public void revocationRequestProcess() {
		 String uri = "/revocation/revocationRequestProcess";
		 RequestParameterVO request = homeControllerTest.request();
		 request.setLeaveRequestId("121212");
		 request.setLeaveStatus(CommonConstants.STATUS_REVOKED_APPROVED);
		 SuccessResponseVO response = new SuccessResponseVO();
		 response.setStatus(CommonConstants.SUCCESS);
		 try {
			 String value = mapToJson(request);
			 Mockito.when(revocationDAO.revocationApproveAndReject(request.getLeaveRequestId(), String.valueOf(request.getEmployeeId()), request.getLeaveStatus(), request.getLeaveReason()))
			 .thenReturn(response);
			 MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
//			 Mockito.verify(revocationDAO).revocationApproveAndReject(request.getLeaveRequestId(), String.valueOf(request.getEmployeeId()), request.getLeaveStatus(), request.getLeaveReason());
			 int status = mvcResult.getResponse().getStatus();
			 assertEquals(200, status);			 
		 }catch(Exception e) {
			 VmsLogging.logError(getClass(), "Revocation Approved Test cases", e);
		 }
		 request.setLeaveStatus(CommonConstants.STATUS_REVOKED_REJECTED);
		 try {
			 String value = mapToJson(request);
			 Mockito.when(revocationDAO.revocationApproveAndReject(request.getLeaveRequestId(), String.valueOf(request.getEmployeeId()), request.getLeaveStatus(), request.getLeaveReason()))
			 .thenReturn(response);
			 MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
//			 Mockito.verify(revocationDAO).revocationApproveAndReject(request.getLeaveRequestId(), String.valueOf(request.getEmployeeId()), request.getLeaveStatus(), request.getLeaveReason());
			 int status = mvcResult.getResponse().getStatus();
			 assertEquals(200, status);			 
		 }catch(Exception e) {
			 VmsLogging.logError(getClass(), "Revocation Approved Test cases", e);
		 }
	 }
	 
	 public List<PendingLeaveRequsetVO> pendingList(){
		 List<PendingLeaveRequsetVO> pendingList = new ArrayList();
		 PendingLeaveRequsetVO pendingLeaveRequsetVO=  new PendingLeaveRequsetVO();
			pendingLeaveRequsetVO.setLeaveRequestId("311569");
			pendingLeaveRequsetVO.setSubmittedDate("2019-09-04T13:54:54.953");
			pendingLeaveRequsetVO.setEmployeeId("5334");
			pendingLeaveRequsetVO.setEmployeeCode("115234");
			pendingLeaveRequsetVO.setEmployeeName("Jatish V C");
			pendingLeaveRequsetVO.setInsightId("jatish_v");
			long d = System.currentTimeMillis();
			pendingLeaveRequsetVO.setFromDate(new Date(d));
			pendingLeaveRequsetVO.setToDate(new Date(d));
			pendingLeaveRequsetVO.setNumberOfDays(1);
			pendingLeaveRequsetVO.setLeaveTypeId(6);
			pendingLeaveRequsetVO.setLeaveTypeName("ON Duty");
			pendingLeaveRequsetVO.setLeaveCode("OD");
			pendingLeaveRequsetVO.setLeaveReason("Test");
			pendingLeaveRequsetVO.setManagerName("Thiyagaraj H");
			pendingLeaveRequsetVO.setLeaveStatus("Pending Revocation");
			pendingLeaveRequsetVO.setLeaveBalance(0);
			pendingLeaveRequsetVO.setContactNumber("95058849849");
			pendingLeaveRequsetVO.setApprovedStatus("Pending Revocation with Finance Team,Revocation Approved by Sanjiv Chellappa Lochan");
			pendingLeaveRequsetVO.setOdOptionName("Others");
			pendingLeaveRequsetVO.setOdOptionCode("OTH");
			pendingList.add(pendingLeaveRequsetVO);
		 return pendingList;
	 }
}
