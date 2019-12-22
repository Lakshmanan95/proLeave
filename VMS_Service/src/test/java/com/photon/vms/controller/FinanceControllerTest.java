package com.photon.vms.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockitoSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photon.vms.Application;
import com.photon.vms.TestConfig;
import com.photon.vms.dao.FinanceDAO;
import com.photon.vms.dao.impl.FinanceDAOImpl;
import com.photon.vms.service.FinanceService;
import com.photon.vms.service.impl.FinanceServiceImpl;
import com.photon.vms.util.JSONUtil;
import com.photon.vms.vo.EmployeeDetailVO;
import com.photon.vms.vo.FinanceReportResponse;
import com.photon.vms.vo.FinanceResponceUsedLeaveVO;
import com.photon.vms.vo.HierarchResponseVO;
import com.photon.vms.vo.LeaveStatusVO;
import com.photon.vms.vo.LeaveType;
import com.photon.vms.vo.ReportDropDownResponse;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.SearchHierarchy;
import com.photon.vms.vo.SearchHierarchyRequestVO;

@ActiveProfiles("test")
@WebMvcTest(FinanceController.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class FinanceControllerTest {
	
	 @Autowired
	 protected MockMvc mvc;
 
	 @MockBean
	 FinanceDAO financeDAO;
	 
	 @Autowired
	 FinanceController financeController;
	 
	 @MockBean
	 FinanceService financeService;

	
	   public void setUp() {
	      mvc = MockMvcBuilders.standaloneSetup(financeController).build();
	      financeDAO = new FinanceDAOImpl();
//	      financeService = new FinanceServiceImpl();
	   }
	   
	 	protected String mapToJson(Object obj) throws JsonProcessingException {
	      ObjectMapper objectMapper = new ObjectMapper();
	      return objectMapper.writeValueAsString(obj);
	   }
	   @Test
	   public void getFinaceLeaveReport() throws Exception{
		   String uri = "/finance/getFinanceReportEntities";
		   FinanceReportResponse response = new FinanceReportResponse();
		   List<LeaveType> leaveList = new ArrayList<LeaveType>();
		   LeaveType leave1 = new LeaveType();
		   leave1.setLeaveCode("03");
		   leave1.setLeaveType("PTO");
		   leave1.setLeaveTypeId(3);
		   LeaveType leave2 = new LeaveType();
		   leave1.setLeaveCode("02");
		   leave1.setLeaveType("PL");
		   leave1.setLeaveTypeId(2);
		   leaveList.add(leave1);
		   leaveList.add(leave2);
		   List<String> companyName = new ArrayList<String>();
		   companyName.add("Photon");
		   companyName.add("Fidelity");
		   List<String> reportName = new ArrayList<String>();
		   reportName.add("report");
		   reportName.add("csv");
		   response.setCompanyName(companyName);
		   response.setLeaveType(leaveList);
		   response.setReportName(reportName);
		   
		   Mockito.when(financeDAO.getFinanceReportEntity()).thenReturn(response);
		   
		   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON))
		   		.andExpect(MockMvcResultMatchers.status().isOk())
		   		.andReturn();
//		   Mockito.verify(financeDAO).getFinanceReportEntity();
		   int status = mvcResult.getResponse().getStatus();
		   assertEquals(200, status);
		 }
	   
	   @Test
	   public void getReportDropDown() throws Exception {
		   String uri = "/finance/getReportDropDown";
		   
		   ReportDropDownResponse resultmap = new ReportDropDownResponse();
		   ArrayList<LeaveType> leaveTypeInfo = new ArrayList<>();
		   ArrayList<LeaveStatusVO> leaveStatusInfo = new ArrayList<>();
		   ArrayList<EmployeeDetailVO> employeeInfo = new ArrayList<>();
		   LeaveType leaveType = new LeaveType();
		   leaveType.setLeaveCode("01");
		   leaveType.setLeaveType("PTO");
		   leaveType.setLeaveTypeId(3);
		   LeaveType leave2 = new LeaveType();
		   leave2.setLeaveCode("02");
		   leave2.setLeaveType("PL");
		   leave2.setLeaveTypeId(2);
		   leaveTypeInfo.add(leaveType);
		   leaveTypeInfo.add(leave2);
		   LeaveStatusVO leaveStatus = new LeaveStatusVO();
		   leaveStatus.setLeaveStatus("Approved");
		   leaveStatus.setLeaveStatusCode("05");
		   leaveStatus.setLeaveStatusId(5);
		   LeaveStatusVO leaveStatus1 = new LeaveStatusVO();
		   leaveStatus1.setLeaveStatus("Pending");
		   leaveStatus1.setLeaveStatusCode("03");
		   leaveStatus1.setLeaveStatusId(3);
		   leaveStatusInfo.add(leaveStatus);
		   leaveStatusInfo.add(leaveStatus1);
		   EmployeeDetailVO employeeDetail = new EmployeeDetailVO();
		   employeeDetail.setEmployeeCode("121720");
		   employeeDetail.setEmployeeId(121720);
		   employeeDetail.setEmployeeName("Lakshmanan");
		   employeeInfo.add(employeeDetail);
		   resultmap.setLeaveTypeInfo(leaveTypeInfo);
		   resultmap.setLeaveStatusInfo(leaveStatusInfo);
		   resultmap.setEmployeeInfo(employeeInfo);
		   RequestParameterVO req = new RequestParameterVO();
		   req.setEmployeeNumber("121720");
		   req.setFlag("IH");
		   String value = mapToJson(req);
		   Mockito.when(financeDAO.getReportDropDown("121720", "IH")).thenReturn(resultmap);

			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
//			Mockito.verify(financeDAO).getReportDropDown("121720", "IH");
			  int status = mvcResult.getResponse().getStatus();
			   assertEquals(200, status);
	   }
	   
	   @Test 
	   public void searchHierarchy() throws Exception {
		   String uri = "/finance/searchHierarchy";
		   HierarchResponseVO response = new HierarchResponseVO();
		   List<SearchHierarchy> searchHierarchy = new ArrayList<SearchHierarchy>();
		   SearchHierarchy search = new SearchHierarchy();
		   search.setEmployeeCode("121720");
		   search.setEmployeeName("Lakshmanan");
		   search.setAppliedOn("2019-05-21");
		   search.setFromDate("2019-05-24");
		   search.setToDate("2019-05-24");
		   search.setBalance("2");
		   search.setNoOfDays(1.00);
		   search.setLeaveCode("03");
		   search.setLeaveDate("2019-05-24");
		   search.setLevelId(3);
		   search.setLeaveStatus("Approved");
		   search.setReason("Marriage function");
		   search.setReviewBy("Jatish V C");
		   search.setReviewOn("2019-05-21");
		   searchHierarchy.add(search);
		   response.setSearchList(searchHierarchy);
		   response.setStatus("Success");
		   SearchHierarchyRequestVO request =new SearchHierarchyRequestVO();
		   request.setLoginEmployeeCode("121720");
		   request.setFlag("IH");
		   request.setLeaveFromDate("2019-05-20");
		   request.setLeaveToDate("2019-05-28");
		   request.setEmployeeCode("121720");
		   request.setFileFormat("CSV");
		   request.setLeaveAppliedOn("2019-05-18");
		   request.setLeaveReviewedOn(null);
		   request.setLeaveTypeId("2");
		   String value = mapToJson(request);
//		   Mockito.verify(financeDAO,atLeastOnce()).searchAndReportHierarchy(request);
		   Mockito.when(financeDAO.searchAndReportHierarchy(request)).thenReturn(searchHierarchy);
				
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn();
			
		
			  int status = mvcResult.getResponse().getStatus();
			   assertEquals(200, status);
	   }
	   
	  /* @Test 
	   public void getFinanceLeaveReport() throws Exception {
		   String uri = "/finance/getFinaceLeaveReport";
		   ArrayList<FinanceResponceUsedLeaveVO> financeReportInfo = new ArrayList<>();
		   FinanceResponceUsedLeaveVO financeReport = new FinanceResponceUsedLeaveVO();
			financeReport.setEmployeeNumber("121720");
			financeReport.setEmployeeName("Lakkshmanan");
			financeReport.setLeaveType("CL");
			financeReport.setLeaveDate("23-08-2019");
			financeReport.setApprovedDate("28-08-2019");
			financeReport.setNoOfDays("2");
			financeReportInfo.add(financeReport);
		    Mockito.doThrow().when(financeDAO.getFinanceResponceUsedLeaveReport("","","","","","",0));
			
	mvc.perform(MockMvcRequestBuilders.get(uri).param("reportType", "Used Leave - Payroll").param("companyName", "Photon Interactive")
						.param("employeeNumber", "").param("leaveTypeId", "").param("fromDate", "").param("toDate", "").contentType(MediaType.APPLICATION_JSON)
	.accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
			  
	   } */
}
