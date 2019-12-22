package com.photon.vms.controller;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photon.vms.TestConfig;
import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.dao.ManagerReviewDAO;
import com.photon.vms.vo.PendingLeaveRequsetVO;
import com.photon.vms.vo.RequestParameterVO;
/**
 * @author lakshmanan_j
 */
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ReviewControllerTest extends TestConfig{

	@Autowired
	private ReviewController reviewController;
	
	@MockBean
	private ManagerReviewDAO managerReviewDAO;
	@Override
	   @Before
	   public void setUp() {
	      mvc = MockMvcBuilders.standaloneSetup(reviewController).build();
	    
//	      financeDAO = new FinanceDAOImpl();
	   }
	   
	 	protected String mapToJson(Object obj) throws JsonProcessingException {
	      ObjectMapper objectMapper = new ObjectMapper();
	      return objectMapper.writeValueAsString(obj);
	   }
	/**
	 * @author lakshmanan_j
	 * @controller ReviewController
	 */
	
	@Test
	public void getPendingRequest() {
		String uri = "/review/getPendingRequest";
		ArrayList<PendingLeaveRequsetVO> response = new ArrayList<>();
		RequestParameterVO requestParameter = new RequestParameterVO();
		requestParameter.setReporteeEmployeeNumber("121720");
		requestParameter.setReporteeEmployeeNumber("Lakshmanan");
		requestParameter.setAppliedDate("2019-08-08");
		requestParameter.setFromDate("2019-08-08");
		requestParameter.setToDate("2019-08-08");
		requestParameter.setLeaveType("CL");
		ArrayList<PendingLeaveRequsetVO> leaveRequestInfo = new ArrayList<>();
		ArrayList<String> leaveRequestList = new ArrayList<>();
		Map<String, ArrayList<?>> finalData = new HashMap<>();
		PendingLeaveRequsetVO pendingLeaveRequsetVO=  new PendingLeaveRequsetVO();
		pendingLeaveRequsetVO.setLeaveRequestId("1148644");
		pendingLeaveRequsetVO.setSubmittedDate("2019-08-01T15:19:59.843");
		pendingLeaveRequsetVO.setEmployeeId("1791");
		pendingLeaveRequsetVO.setEmployeeCode("103873");
		pendingLeaveRequsetVO.setEmployeeName("Nithyadevan D");
		pendingLeaveRequsetVO.setInsightId("Nithyadevan_D");
		pendingLeaveRequsetVO.setNumberOfDays(2);
		pendingLeaveRequsetVO.setLeaveTypeId(2);
		pendingLeaveRequsetVO.setLeaveTypeName("Privilege Leave");
		pendingLeaveRequsetVO.setLeaveCode("PL");
		pendingLeaveRequsetVO.setLeaveReason("Test");
		pendingLeaveRequsetVO.setManagerName("Jatish V C");
		pendingLeaveRequsetVO.setLeaveStatus("Pending");
		pendingLeaveRequsetVO.setLeaveBalance(16);
		pendingLeaveRequsetVO.setContactNumber("8939284496");
		pendingLeaveRequsetVO.setApprovedStatus("Pending with Jatish V C");
		pendingLeaveRequsetVO.setOdOptionName("");
		pendingLeaveRequsetVO.setOdOptionCode("");
		
		leaveRequestList.add("1148644");
		leaveRequestInfo.add(pendingLeaveRequsetVO);
		finalData.put("PendingLeaveRequest", leaveRequestInfo);
		finalData.put("LeaveRequestList", leaveRequestList);
		
		try {
			Mockito.when(managerReviewDAO.getPendingLeaveRequest("121720", "121720", "Lax", "2019-08-08", "2019-08-08", "2019-08-08", "CL",""))
			.thenReturn(finalData);
			String value = mapToJson(requestParameter);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
				  .accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Get Pending Request Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	public Map<String, ArrayList<?>> getpendingResult(){
		ArrayList<PendingLeaveRequsetVO> leaveRequestInfo = new ArrayList<>();
		ArrayList<String> leaveRequestList = new ArrayList<>();
		Map<String, ArrayList<?>> finalData = new HashMap<>();
		PendingLeaveRequsetVO pendingLeaveRequsetVO=  new PendingLeaveRequsetVO();
		pendingLeaveRequsetVO.setLeaveRequestId("1148644");
		pendingLeaveRequsetVO.setSubmittedDate("2019-08-01T15:19:59.843");
		pendingLeaveRequsetVO.setEmployeeId("1791");
		pendingLeaveRequsetVO.setEmployeeCode("103873");
		pendingLeaveRequsetVO.setEmployeeName("Nithyadevan D");
		pendingLeaveRequsetVO.setInsightId("Nithyadevan_D");
		pendingLeaveRequsetVO.setNumberOfDays(2);
		pendingLeaveRequsetVO.setLeaveTypeId(2);
		pendingLeaveRequsetVO.setLeaveTypeName("Privilege Leave");
		pendingLeaveRequsetVO.setLeaveCode("PL");
		pendingLeaveRequsetVO.setLeaveReason("Test");
		pendingLeaveRequsetVO.setManagerName("Jatish V C");
		pendingLeaveRequsetVO.setLeaveStatus("Pending");
		pendingLeaveRequsetVO.setLeaveBalance(16);
		pendingLeaveRequsetVO.setContactNumber("8939284496");
		pendingLeaveRequsetVO.setApprovedStatus("Pending with Jatish V C");
		pendingLeaveRequsetVO.setOdOptionName("");
		pendingLeaveRequsetVO.setOdOptionCode("");
		
		leaveRequestList.add("1148644");
		leaveRequestInfo.add(pendingLeaveRequsetVO);
		finalData.put("PendingLeaveRequest", leaveRequestInfo);
		finalData.put("LeaveRequestList", leaveRequestList);
		
		return finalData;
	}
}
