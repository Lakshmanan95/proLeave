package com.photon.vms.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.dao.EmployeeHomeDAO;
import com.photon.vms.dao.ManagerReviewDAO;
import com.photon.vms.dao.impl.EmployeeHomeDAOImpl;
import com.photon.vms.dao.impl.ManagerReviewDAOImpl;
import com.photon.vms.emailTemplate.EmailService;
import com.photon.vms.emailTemplate.EmailTemplateService;
import com.photon.vms.service.AdminService;
import com.photon.vms.service.EmployeeHomeService;
import com.photon.vms.service.ManagerReviewService;
import com.photon.vms.service.RevocationService;
import com.photon.vms.service.impl.EmployeeHomeServiceImpl;
import com.photon.vms.service.impl.ManagerReviewServiceImpl;
import com.photon.vms.util.DateTimeUtils;
import com.photon.vms.util.JSONUtil;
import com.photon.vms.util.MailSendUtils;
import com.photon.vms.vo.AdHocWorkDaysVO;
import com.photon.vms.vo.HolidayListVO;
import com.photon.vms.vo.LeaveBalanceDetailVO;
import com.photon.vms.vo.LeaveRequestIdInfo;
import com.photon.vms.vo.LeaveRequestProcessVO;
import com.photon.vms.vo.LeaveTypeVO;
import com.photon.vms.vo.OutOfOfficeInfoVO;
import com.photon.vms.vo.ProcessLeaveRequestVO;
import com.photon.vms.vo.ReportManagerVO;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.SearchHistoryVO;
/**
 * @author lakshmanan_j
 */

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

	
	ReviewControllerTest reviewControllerTest = new ReviewControllerTest();
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private HomeController homeController;
	
	@MockBean
	private ManagerReviewService managerReviewDAO;
	@MockBean
	private EmployeeHomeService employeeHomeDAO;
	@Mock
	EmployeeHomeDAO emloyeeDAO;
	
	@MockBean
	AdminService adminService;
	@MockBean
	MailSendUtils mailSendUtils;
	@MockBean
	RevocationService revocationService;
	@MockBean
	EmailTemplateService emailTemplateService;
	@MockBean
	DateTimeUtils dateTimeUTils;
	@MockBean
	EmailService emailService;
	
	
	
	  
	   public void setUp() {
	      mvc = MockMvcBuilders.standaloneSetup(homeController).build();
	      employeeHomeDAO = new EmployeeHomeServiceImpl();
	      managerReviewDAO = new ManagerReviewServiceImpl();
	   }
	protected String mapToJson(Object obj) throws JsonProcessingException {
	      ObjectMapper objectMapper = new ObjectMapper();
	      return objectMapper.writeValueAsString(obj);
	   }
	/**
	 * @author sureshkumar_e
	 * @controller HomeController
	 */
	@Test
	public void getloggeduserinfo() {
		String uri = "/home/getloggeduserinfo";
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		RequestParameterVO requestParameter = new RequestParameterVO();
		ServletRequest req = null;
		
		requestParameter.setEmployeeNumber("121720");
		try {
			String value = mapToJson(requestParameter);
			Mockito.when(employeeHomeDAO.getLoggedUserInfo("121720")).thenReturn(resultmap);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON))
				   		.andExpect(MockMvcResultMatchers.status().isOk())
				   		.andReturn();
			Mockito.verify(employeeHomeDAO).getLoggedUserInfo("121720");
			Assert.assertEquals(200,  mvcResult.getResponse().getStatus());
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Get Logged User Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@Test
	public void gettimeoffhistory() {
		String uri = "/home/gettimeoffhistory";
		ArrayList<SearchHistoryVO> response =  new ArrayList<>();
		RequestParameterVO requestParameter = new RequestParameterVO();
		SearchHistoryVO search = new SearchHistoryVO();
		search.setAppliedOnDate("2019-07-23T11:24:45.013");
		search.setApprovedById("115234 - Jatish V C");
		search.setApprovedDate("07/23/2019");
		search.setApprovedStatus("Approved by Jatish V C");
		search.setContactNumber("9500544899");
		search.setLeaveCode("CL");
		search.setLeaveReason("Marriage function");
		search.setLeaveRequestId("303753");
		search.setLeaveStatus("Approved");
		search.setLeaveTypeId("1");
		search.setLeaveTypeName("Casual Leave");
		search.setNumberDays("1.00");
		response.add(search);
		requestParameter.setEmployeeNumber("121720");
		try {
			String value = mapToJson(requestParameter);
			Mockito.when(employeeHomeDAO.getTimeoffHistory("121720", "", "", "", "", "", "", "")).thenReturn(response);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
				   		.andExpect(MockMvcResultMatchers.status().isOk())
				   		.andReturn();
			Mockito.verify(employeeHomeDAO).getTimeoffHistory("121720", "", "", "", "", "","", "");
			Assert.assertEquals(200, mvcResult.getResponse().getStatus());
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Time Of History Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@Test
	public void getleaverequestinfo() {
		String uri = "/home/getleaverequestinfo";
		List<LeaveRequestIdInfo> response =  leaveRequestInfo();
		RequestParameterVO requestParameter = new RequestParameterVO();
		requestParameter.setEmployeeNumber("121720");
		requestParameter.setLeaveRequestId("1148665");
		try {
			String value = mapToJson(requestParameter);
			Mockito.when(employeeHomeDAO.getLeaveRequestInfo("121720", "1148665")).thenReturn(response);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(value).contentType(MediaType.APPLICATION_JSON)
					  .accept(MediaType.APPLICATION_JSON))
				   		.andExpect(MockMvcResultMatchers.status().isOk())
				   		.andReturn();			
			Mockito.verify(employeeHomeDAO).getLeaveRequestInfo("121720", "1148665");
			Assert.assertEquals(200, mvcResult.getResponse().getStatus());
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Leave Request Detail Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@Test
	public void leaverequestprocess() {
		String uri = "/home/leaverequestprocess";
		ArrayList<LeaveRequestProcessVO> response =  new ArrayList<>();
		RequestParameterVO requestParameter = request();
		ServletRequest req = null;
		LeaveTypeVO leaveType = new LeaveTypeVO();
		leaveType.setLeaveCode("CL");
		leaveType.setLeaveType("Casual Leave");
		leaveType.setLeaveTypeId("1");
		ArrayList<ProcessLeaveRequestVO> result = new ArrayList<>();
		ProcessLeaveRequestVO processLeaveRequestVO =  new ProcessLeaveRequestVO();
		processLeaveRequestVO.setStatus(CommonConstants.SUCCESS);
		processLeaveRequestVO.setLeaveRequestParentId("76567");
		result.add(processLeaveRequestVO);
		Map<String, ArrayList<?>> getUserLogged = getUserLogged();
		List<LeaveRequestIdInfo> leaveRequestInfo =  leaveRequestInfo();
		Map<String, ArrayList<?>> getPendingRequest = reviewControllerTest.getpendingResult();
		String[] leaveStatus = {CommonConstants.STATUS_CANCELLED,CommonConstants.STATUS_APPROVED,CommonConstants.STATUS_REJECTED,
				CommonConstants.STATUS_PENDING_REVOCATION,CommonConstants.STATUS_REVOKED_APPROVED,CommonConstants.STATUS_REVOKED_REJECTED};
		try {
			for(int i = 0; i < leaveStatus.length;i++) {
				requestParameter.setLeaveStatus(leaveStatus[i]);
				if(!requestParameter.getLeaveStatus().equals(CommonConstants.STATUS_PENDING))
					requestParameter.setLeaveRequestId("1148644");
			String value = mapToJson(requestParameter);
			Mockito.when(employeeHomeDAO.getLoggedUserInfo("121720")).thenReturn(getUserLogged);
			Mockito.when(employeeHomeDAO.getLeaveTypeDetail(1)).thenReturn(leaveType);
			Mockito.when(employeeHomeDAO.getLeaveRequestInfo("121720", "1148665")).thenReturn(leaveRequestInfo);
			Mockito.when(employeeHomeDAO.processLeaveRequest(requestParameter.getEmployeeNumber(), requestParameter.getLeaveRequestId(), requestParameter.getEmployeeId(), requestParameter.getLeaveTypeId(), requestParameter.getLeaveStatus(),
					requestParameter.getFromDate(), requestParameter.getToDate(), requestParameter.getNumberOfDays(), requestParameter.getContactNumber(), requestParameter.getLeaveReason(), requestParameter.getOdOptionId(), "MA")).thenReturn(result);
			Mockito.when(managerReviewDAO.getPendingLeaveRequest("121720", "121720", "Lax", "2019-08-08", "2019-08-08", "2019-08-08", "CL",""))
			.thenReturn(getPendingRequest);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(value)
					  .accept(MediaType.APPLICATION_JSON))
				   		.andExpect(MockMvcResultMatchers.status().isOk())
				   		.andReturn();
			Assert.assertEquals(200, mvcResult.getResponse().getStatus());
			}
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Leave Request Process Test Controller info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	public Map<String, ArrayList<?>> getUserLogged() {
		ArrayList<ReportManagerVO> empReportManagerInfo = new ArrayList<>();
		ArrayList<AdHocWorkDaysVO> adHocWorkDayList = new ArrayList<>();
		ArrayList<HolidayListVO> holidayList = new ArrayList<>();
		ArrayList<LeaveTypeVO> leaveTypeList = new ArrayList<>();
		ArrayList<LeaveBalanceDetailVO> leaveBalanceDetail = new ArrayList<>();
		ArrayList<OutOfOfficeInfoVO> outOfOfficeOptions = new ArrayList<>();
		ArrayList legendInfo = new ArrayList();
		ReportManagerVO reportManagerVO = new ReportManagerVO();
		reportManagerVO.setEmployeeId("121720");
		reportManagerVO.setEmployeeNumber("121720");
		reportManagerVO.setEmployeeName("Lakshmanan");
		reportManagerVO.setEmailId("lakshmanan.l@photoninfotech.net");
		reportManagerVO.setReportingManagerId("121721");
		reportManagerVO.setReportingManagerNumber("121721");
		reportManagerVO.setReportingManagerName("Jatish V C");
		reportManagerVO.setLocationCode("02");
		reportManagerVO.setAdminLoc("01");
		reportManagerVO.setLocationName("Chennai");
		reportManagerVO.setContactNumber("9500544899");
		reportManagerVO.setManagerEmailId("jatishvc@photoninfotech.net");
		reportManagerVO.setRole("Employee");
		reportManagerVO.setGender("Male");
		reportManagerVO.setEmployeeType("P");
		reportManagerVO.setEmployeeStatus("EMPLOYED");
		empReportManagerInfo.add(reportManagerVO);
		AdHocWorkDaysVO adHocWorkDaysVO = new AdHocWorkDaysVO();
		adHocWorkDaysVO.setLeaveDate(new Date());
		adHocWorkDaysVO.setLocationCode("01");
		adHocWorkDayList.add(adHocWorkDaysVO);
		HolidayListVO holidayListVO = new HolidayListVO();
		holidayListVO.setHolidayId("491");
		holidayListVO.setHolidayName("New Years Day");
		holidayListVO.setDayOfHoliday("Tue");
		holidayListVO.setLocationName("Chennai");
		holidayList.add(holidayListVO);
		LeaveTypeVO leaveTypeVO = new LeaveTypeVO();
		leaveTypeVO.setLeaveTypeId("12");
		leaveTypeVO.setLeaveCode("AL");
		leaveTypeVO.setLeaveType("Adoption Leave");
		leaveTypeVO.setLocationCode("CHE");
		leaveTypeVO.setIsDefault("N");
		leaveTypeVO.setIsContinuous("Y");
		leaveTypeList.add(leaveTypeVO);
		LeaveBalanceDetailVO leaveBalanceDetailVO = new LeaveBalanceDetailVO();
		leaveBalanceDetailVO.setEmployeeId("121720");
		leaveBalanceDetailVO.setYear("2019");
		leaveBalanceDetailVO.setLeaveTypeId("02");
		leaveBalanceDetailVO.setLeaveCode("02");
		leaveBalanceDetailVO.setLeaveTypeName("CL");
		leaveBalanceDetailVO.setOpenLeave(9);
		leaveBalanceDetailVO.setCreditedLeave("0.00");
		leaveBalanceDetailVO.setUsedLeave("7.00");
		leaveBalanceDetailVO.setBalanceLeave("2.00");
		leaveBalanceDetailVO.setPendingLeave("0.00");
		leaveBalanceDetailVO.setLocationCode("CHE");
		leaveBalanceDetailVO.setApprovedAndPendingBalance("7.00");
		leaveBalanceDetail.add(leaveBalanceDetailVO);
		OutOfOfficeInfoVO outOfOfficeInfo = new OutOfOfficeInfoVO();
		outOfOfficeInfo.setOdOptionId("1");
		outOfOfficeInfo.setOdOptionName("Work From Home");
		outOfOfficeInfo.setOdOptionCode("WFH");
		outOfOfficeOptions.add(outOfOfficeInfo);
		final Map<String, String> LEGEND_COLOR = new HashMap<String, String>();
		LEGEND_COLOR.put("CL","#DB9696");
		LEGEND_COLOR.put("PL","#F5C66E");
		LEGEND_COLOR.put("MDL","#DB9696");
		LEGEND_COLOR.put("ANL","#F5C66E");
		LEGEND_COLOR.put("COFF","#49D1E3");
		LEGEND_COLOR.put("OD","#7ED321");
		LEGEND_COLOR.put("Holiday","#D07CED");
		LEGEND_COLOR.put("PTO","#F5C66E");
		LEGEND_COLOR.put("LOP", "#DF485B");
		LEGEND_COLOR.put("AL", "#DB9696");
		LEGEND_COLOR.put("ML", "#FEBD57");
		LEGEND_COLOR.put("APTO", "#DB9696");
		legendInfo.add(LEGEND_COLOR);
		Map<String, ArrayList<?>> resultmap = new HashMap<>();
		resultmap.put("EmpReportManagerInfo", empReportManagerInfo);
		resultmap.put("AdHocWorkingDays",adHocWorkDayList);
		resultmap.put("HolidayList",holidayList);
		resultmap.put("LeaveTypeList",leaveTypeList);
		resultmap.put("LeaveBalanceInfo",leaveBalanceDetail);
		resultmap.put("OutOfOfficeOptions",outOfOfficeOptions);
		resultmap.put("Legends", legendInfo);
		return resultmap;
	}
	
	public RequestParameterVO request() {
		RequestParameterVO request = new RequestParameterVO();
		request.setEmployeeId(10719);
		request.setEmployeeNumber("121720");
		request.setAppliedDate("2019-05-05");
		request.setFromDate("2019-01-07");
		request.setToDate("2019-01-07");
		request.setContactNumber("894735897");
		request.setLeaveReason("test");
		request.setLeaveTypeId(1);
		request.setLeaveType("Casual Leave");
		request.setLeaveStatus("Pending");
		request.setNumberOfDays("1");
		
		return request;
	}
	
	public List<LeaveRequestIdInfo> leaveRequestInfo(){
		List<LeaveRequestIdInfo> response =  new ArrayList<>();
		 long millis=System.currentTimeMillis(); 
		LeaveRequestIdInfo leaveRequestIdInfo = new LeaveRequestIdInfo();
		leaveRequestIdInfo.setLeaveRequestId(1148665);
		leaveRequestIdInfo.setNumberOfDays(1);
		leaveRequestIdInfo.setLeaveTypeId(5);
		leaveRequestIdInfo.setLeaveTypecode("LOP");
		leaveRequestIdInfo.setLeaveTypeName("Loss Of Pay");
		leaveRequestIdInfo.setFromDate(new java.sql.Date(millis));
		leaveRequestIdInfo.setToDate(new java.sql.Date(millis));
		leaveRequestIdInfo.setContactNumber("9500544899");
		leaveRequestIdInfo.setInsightId("jatish_v");
		leaveRequestIdInfo.setLeaveReason("ADFdf");
		leaveRequestIdInfo.setApprovedById(0);
		leaveRequestIdInfo.setApprovedDate("");
		leaveRequestIdInfo.setLeaveStatus("Cancelled");
		leaveRequestIdInfo.setRejectedById(0);
		leaveRequestIdInfo.setRejectedDate(null);
		leaveRequestIdInfo.setRejectedReason("");
		leaveRequestIdInfo.setRevokedById(0);
		leaveRequestIdInfo.setRevokedDate(null);
		leaveRequestIdInfo.setRevokedReason("");
		leaveRequestIdInfo.setCancelledById(5334);
		leaveRequestIdInfo.setCancelledReason("TEst");
		leaveRequestIdInfo.setApprovedStatus("Jatish V C");
		leaveRequestIdInfo.setOdOptionCode("");
		leaveRequestIdInfo.setOwnerEmployeeNumber("115234");
		leaveRequestIdInfo.setOdOptionName("");
		response.add(leaveRequestIdInfo);
		return response;
	}
}
