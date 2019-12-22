package com.photon.vms.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import com.photon.vms.dao.EncashmentDAO;
import com.photon.vms.dao.impl.EmployeeHomeDAOImpl;
import com.photon.vms.dao.impl.EncashmentDAOImpl;
import com.photon.vms.service.EncashmentService;
import com.photon.vms.service.impl.EncashmentServiceImpl;
import com.photon.vms.util.JSONUtil;
import com.photon.vms.vo.EncashmentDays;
import com.photon.vms.vo.EncashmentDetails;
import com.photon.vms.vo.EncashmentPolicyAndOption;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.SuccessResponseVO;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

//@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(EncashmentController.class)
public class EncashmentControllerTest {

	@Autowired
	protected MockMvc mvc;
	
	@Autowired
	EncashmentController encashmentController;
	
	@MockBean
	EncashmentService encashmentService;
	
	@MockBean
	EncashmentDAO encashmentDAO;
	
	public void setUp() {
	      mvc = MockMvcBuilders.standaloneSetup(encashmentController).build();
	      encashmentService = new EncashmentServiceImpl();
	      encashmentDAO = new EncashmentDAOImpl();
	   }
	protected String mapToJson(Object obj) throws JsonProcessingException {
	      ObjectMapper objectMapper = new ObjectMapper();
	      return objectMapper.writeValueAsString(obj);
	   }
	
	@Test
	public void getEncashmentDetails() {
		String uri = "/encashment/getEncashmentDetails";
		EncashmentDetails encashmentDetails = new EncashmentDetails();
		List<EncashmentPolicyAndOption> encashmentPolicy = new ArrayList<>();
		List<EncashmentPolicyAndOption> encashmentOption = new ArrayList<>();
		List<EncashmentDays> encashmentDays = new ArrayList<>();
		EncashmentPolicyAndOption policy = new EncashmentPolicyAndOption();
		policy.setLeaveEncashId(1);
		policy.setFlag("POLICY");
		policy.setMastDescription("accumulated above 24 days eligible for encashment");
		policy.setMastValue(24);
		encashmentPolicy.add(policy);
		EncashmentPolicyAndOption option = new EncashmentPolicyAndOption();
		option.setLeaveEncashId(3);
		option.setFlag("OPTION");
		option.setMastDescription("(Choosing this option will leave you with 24 days to carry forward to next year)");
		option.setMastValue(24);
		option.setIsSelected(0);
		encashmentOption.add(option);
		EncashmentDays encashDays = new EncashmentDays();
		encashDays.setExpectedAsOn("2019-11-11");
		encashDays.setCurrentPlBalance(1);
		encashDays.setCarryForwardedDaysOp1(0);
		encashDays.setCarryForwardedDaysOp2(0);
		encashDays.setEncashedDaysOp1(1);
		encashDays.setEncashedDaysOp2(1);
		encashmentDays.add(encashDays);
		encashmentDetails.setEncashmentPolicy(encashmentPolicy);
		encashmentDetails.setEncashmentOption(encashmentOption);
		encashmentDetails.setEncashmentDays(encashmentDays);
		try {
			Mockito.when(encashmentDAO.getEncashDetails("121720")).thenReturn(encashmentDetails);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("employeeNumber", "121720").contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			   		.andExpect(MockMvcResultMatchers.status().isOk())
			   		.andReturn();
			Assert.assertEquals(200,  mvcResult.getResponse().getStatus());
		} catch (Exception e) {  
			VmsLogging.logError(getClass(), "Test Encashment Details", e);
		}
	}
	
	@Test
	public void encashmentProcess() {
		String uri = "/encashment/encashmentProcess";
		RequestParameterVO request = new RequestParameterVO();
		request.setEmployeeNumber("121720");
		request.setLeaveEncashId(1);
		SuccessResponseVO response = new SuccessResponseVO();
		response.setStatus("Success");
		response.setSuccessMessage("Success");
		try {
			String value = mapToJson(request);
			Mockito.when(encashmentDAO.leaveEncashmentProcess(request.getEmployeeNumber(), request.getLeaveEncashId())).thenReturn(response);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			   		.andExpect(MockMvcResultMatchers.status().isOk())
			   		.andReturn();
//			Mockito.verify(encashmentDAO).leaveEncashmentProcess(request.getEmployeeNumber(), request.getLeaveEncashId());
			Assert.assertEquals(200, mvcResult.getResponse().getStatus());
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Test Encashment Process", e);
		}
	}
}
