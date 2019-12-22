package com.photon.vms;

import java.io.IOException;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photon.vms.controller.FreezeController;
import com.photon.vms.controller.HomeController;
import com.photon.vms.controller.LeaveCreditController;
import com.photon.vms.dao.FinanceDAO;
import com.photon.vms.dao.impl.EmployeeHomeDAOImpl;
import com.photon.vms.dao.impl.FinanceDAOImpl;
import com.photon.vms.dao.impl.FreezeDAOImpl;
import com.photon.vms.dao.impl.LeaveCreditDAOImpl;
import com.photon.vms.dao.impl.ManagerReviewDAOImpl;
import com.photon.vms.service.FinanceService;
import com.photon.vms.service.FreezeService;
import com.photon.vms.service.impl.FinanceServiceImpl;

@Profile("test")
@Configuration
public class TestConfig {

//	@Bean
//	@Primary
//	public LeaveCreditController leaveCreditController() {
//		return Mockito.mock(LeaveCreditController.class);
//	}
	
//	@Bean
//	@Primary
//	public HomeController homeController() {
//		return Mockito.mock(HomeController.class);
//	}
	
//	@Bean
//	@Primary
//	public FreezeController freezeController() {
//		return Mockito.mock(FreezeController.class);
//	}
//	
//	@Bean
//	@Primary
//	public FreezeService freezeService() {
//		return Mockito.mock(FreezeService.class);
//	}
//	
//	@Bean
//	@Primary
//	public FreezeDAOImpl freezeDAOImpl() {
//		return Mockito.mock(FreezeDAOImpl.class);
//	}
//		
//	@Bean
//	@Primary
//	public EmployeeHomeDAOImpl employeeHomeDAOImpl() {
//		return Mockito.mock(EmployeeHomeDAOImpl.class);
//	}
//	
//	@Bean
//	@Primary
//	public LeaveCreditDAOImpl leaveCreditDAOImpl() {
//		return Mockito.mock(LeaveCreditDAOImpl.class);
//	}
	
//	@Bean
//	@Primary
//	public ManagerReviewDAOImpl managerReviewDAOImpl() {
//		Mockito.mock(FinanceService.class);
//		Mockito.mock(FinanceDAO.class);
//		Mockito.mock(FinanceServiceImpl.class);
//		Mockito.mock(FinanceDAOImpl.class);
//		return Mockito.mock(ManagerReviewDAOImpl.class);
//	}
	
	protected MockMvc mvc;
	   @Autowired
	   WebApplicationContext webApplicationContext;

	   protected void setUp() {
	      mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	   }
	   protected String mapToJson(Object obj) throws JsonProcessingException {
	      ObjectMapper objectMapper = new ObjectMapper();
	      return objectMapper.writeValueAsString(obj);
	   }
	   protected <T> T mapFromJson(String json, Class<T> clazz)
	      throws JsonParseException, JsonMappingException, IOException {
	      
	      ObjectMapper objectMapper = new ObjectMapper();
	      return objectMapper.readValue(json, clazz);
	   }
	
}
