package com.photon.vms.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photon.vms.TestConfig;
import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.dao.FreezeDAO;
import com.photon.vms.dao.impl.FreezeDAOImpl;
import com.photon.vms.emailTemplate.EmailService;
import com.photon.vms.emailTemplate.EmailTemplateService;
import com.photon.vms.service.FreezeService;
import com.photon.vms.service.impl.FreezeServiceImpl;
import com.photon.vms.util.DateTimeUtils;
import com.photon.vms.vo.EmployeeAdminLocationVO;
import com.photon.vms.vo.EmployeeVO;
import com.photon.vms.vo.LeaveUnfreezeDetails;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.RequestUnfreezeVO;
import com.photon.vms.vo.UnFreezeRecords;
import com.photon.vms.vo.UnFreezeResponseVO;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(FreezeController.class)
public class FreezeControllerTest {

	@Autowired
	protected MockMvc mvc;
	
	@Autowired
	private FreezeController freezeController;
	
	@MockBean
	FreezeDAO freezeDAO;
	
	@MockBean
	FreezeService freezeService;
	
	@MockBean
	EmailTemplateService emailTemplateService;
	
	@MockBean
	EmailService emailService;
	@MockBean
	DateTimeUtils dateTimeUtils;
	
	
	 
	   public void setUp() {
	      mvc = MockMvcBuilders.standaloneSetup(freezeController).build();
	      freezeDAO  = new FreezeDAOImpl();
	    
	   }
	   
	 	protected String mapToJson(Object obj) throws JsonProcessingException {
	      ObjectMapper objectMapper = new ObjectMapper();
	      return objectMapper.writeValueAsString(obj);
	   }
	/**
	 * @author sureshkumar_e
	 * @controller FreezeController
	 */
	@Test
	public void getEmployeeAdminLocation() {
		String uri = "/freeze/getEmployeeAdminLocation";
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		ArrayList<EmployeeAdminLocationVO> empAdminLocationInfo = new ArrayList<>();
		EmployeeAdminLocationVO employeeAdminLocationVO = new EmployeeAdminLocationVO();
		RequestParameterVO requestParameter = new RequestParameterVO();
		requestParameter.setEmployeeNumber("121720");
		employeeAdminLocationVO.setLocationId("3");
		employeeAdminLocationVO.setLocationCode("USA");
		employeeAdminLocationVO.setLocationName("USA");
		employeeAdminLocationVO.setCompanyName("Photon Infotech Inc. Ltd");
		employeeAdminLocationVO.setIsAdmin("1");
		empAdminLocationInfo.add(employeeAdminLocationVO);
		resultmap.put("EmpAdminLocationInfo", empAdminLocationInfo);
		try {
			Mockito.when(freezeDAO.getEmployeeAdminLocation("121720"))
			.thenReturn(resultmap);
		String value = mapToJson(requestParameter);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
				  .accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee Admin Location Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}

	@Test
	public void getLeaveUnFreezedDetails() {
		String uri = "/freeze/getLeaveUnFreezedDetails";
		ArrayList<LeaveUnfreezeDetails> leaveUnfreezeInfo = new ArrayList<>();
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		LeaveUnfreezeDetails leaveUnfreezeDetailsVO = new LeaveUnfreezeDetails();
		RequestParameterVO requestParameter = new RequestParameterVO();
		requestParameter.setEmployeeNumber("121720");
		requestParameter.setLocationId(2);
		leaveUnfreezeDetailsVO.setUnFreezeId("167");
		leaveUnfreezeDetailsVO.setFromDate(new Date());
		leaveUnfreezeDetailsVO.setToDate(new Date());
		leaveUnfreezeDetailsVO.setLocationId("3");
		leaveUnfreezeDetailsVO.setLocationCode("USA");
		leaveUnfreezeDetailsVO.setLocationName("USA");
		leaveUnfreezeDetailsVO.setUnFreezeComment("test");
		leaveUnfreezeDetailsVO.setUnFreezeDate("04/24/2019 06:47 PM");
		leaveUnfreezeDetailsVO.setUnFreezeByCode("100644");
		leaveUnfreezeDetailsVO.setUnFreezeByName("Suresh Kumar E");
		leaveUnfreezeDetailsVO.setUnFreezeMonth("April");
		leaveUnfreezeDetailsVO.setUnFreezeStatus("Active");
		leaveUnfreezeDetailsVO.setUnFreezeYear("2019");
		leaveUnfreezeInfo.add(leaveUnfreezeDetailsVO);
		resultmap.put("LeaveUnfreezeInfo", leaveUnfreezeInfo);
		try {
			Mockito.when(freezeDAO.getLeaveUnFreezedDetails("121720",2))
			.thenReturn(resultmap);
			String value = mapToJson(requestParameter);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Leave UnFreezed Details Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}

	@Test
	public void insertUnfreezeDetails() {
		String uri = "/freeze/insertUnfreezeDetails";
		UnFreezeResponseVO response = new UnFreezeResponseVO();
		response.setResultCode("SUCCESS");
		response.setResultDesc("Record(s) have been updated successfully");
		List<UnFreezeRecords> listRecord = new ArrayList<>();
		UnFreezeRecords record = new UnFreezeRecords();
		record.setEmployeeCode("121720");
		record.setLoginEmployeeCode("121720");
		record.setComments("test");
		record.setFlag("LW");
		record.setFromDate("2018-07-10");
		record.setToDate("2018-07-10");
		record.setIndex(0);
		listRecord.add(record);
		RequestUnfreezeVO requestParameter = new RequestUnfreezeVO();
		requestParameter.setEmployeeNumber("121720");
		requestParameter.setRecord(null);
		requestParameter.setUnfreezeRecords(listRecord);
		try {
			Mockito.when(freezeDAO.insertUnfreezeDetails("121720", "121720", 11, "2018-07-10", "2018-07-10", "test", "LW"))
			.thenReturn(response);
			String value = mapToJson(requestParameter);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Insert UnFreeze Detail Controller info Exception ",new VmsApplicationException(e.getMessage()));
		}
	}

	@Test
	public void getActiveEmployee() {
		String uri = "/freeze/getActiveEmployee";
		RequestParameterVO request = new RequestParameterVO();
		request.setEmployeeNumber("121720");
		ArrayList<EmployeeVO> employeeInfo = new ArrayList<>();
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		EmployeeVO employeeVO = new EmployeeVO();
		employeeVO.setEmployeeId("100000");
		employeeVO.setEmployeeName("Suresh Kumar E");
		employeeInfo.add(employeeVO);
		resultmap.put("EmployeeInfo", employeeInfo);
		try {
			Mockito.when(freezeDAO.getActiveEmployee("121720"))
			.thenReturn(resultmap);
			String value = mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Active Employee Controller info Exception ",new VmsApplicationException(e.getMessage()));
		}
	}
	
/*	@Test
	public void insertUnfreezeRecord() {
		UnFreezeRecords records = new UnFreezeRecords();
		try {
			UnFreezeResponseVO response = new UnFreezeResponseVO();
			Mockito.when(freezeController.insertUnfreezeRecord(records)).thenReturn(response);
			UnFreezeResponseVO resultmap1 = freezeController.insertUnfreezeRecord(records);
			Assert.assertEquals(response, resultmap1);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Insert UnFreeze Record Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@Test
	public void checkUnfreezeValidation() {
		String flag = null ; String unfreezeEmployeeCode = null; int locationId=0;
		boolean result = true;
		try {
			Mockito.when(freezeController.checkUnfreezeValidation(flag,unfreezeEmployeeCode,locationId)).thenReturn(result);
			boolean resultmap1 = freezeController.checkUnfreezeValidation(flag,unfreezeEmployeeCode,locationId);
			Assert.assertEquals(result, resultmap1);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Insert UnFreeze Record Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}*/
}
