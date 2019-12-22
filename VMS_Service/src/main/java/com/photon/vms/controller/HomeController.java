package com.photon.vms.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.emailTemplate.EmailService;
import com.photon.vms.emailTemplate.EmailTemplateService;
import com.photon.vms.service.AdminService;
import com.photon.vms.service.EmployeeHomeService;
import com.photon.vms.service.ManagerReviewService;
import com.photon.vms.service.RevocationService;
import com.photon.vms.util.DateTimeUtils;
import com.photon.vms.util.JSONUtil;
import com.photon.vms.util.MailSendUtils;
import com.photon.vms.vo.AdHocWorkDaysVO;
import com.photon.vms.vo.BulkApproveMailVO;
import com.photon.vms.vo.HolidayListVO;
import com.photon.vms.vo.LeaveBalanceDetailVO;
import com.photon.vms.vo.LeaveRequestIdInfo;
import com.photon.vms.vo.LeaveRequestProcessVO;
import com.photon.vms.vo.LeaveTypeVO;
import com.photon.vms.vo.LocationAdminDetails;
import com.photon.vms.vo.OutOfOfficeInfoVO;
import com.photon.vms.vo.ProcessLeaveRequestVO;
import com.photon.vms.vo.PunchedHours;
import com.photon.vms.vo.ReportManagerVO;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.RevocationEmailVO;
import com.photon.vms.vo.SearchHistoryVO;

/**
 * @author karthigaiselvan_r
 *
 */
@RestController
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/home")
public class HomeController {
	
	@Autowired
	EmployeeHomeService employeeHomeService;
	@Autowired
	ManagerReviewService managerReviewService;
	@Autowired
	AdminService adminService;
	@Autowired
	MailSendUtils mailSendUtils;
	@Autowired
	RevocationService revocationService;
	@Autowired
	EmailTemplateService emailTemplateService;
	@Autowired
	DateTimeUtils dateTimeUtils;
	@Autowired
	EmailService emailService;

	/**
	 * @param requestParameter
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/getloggeduserinfo", method = RequestMethod.POST)
	public Map<String, ArrayList<?>> getLoggedUserInfo(@RequestBody RequestParameterVO requestParameter,ServletRequest req){
		Map<String, ArrayList<?>> response = new HashMap<>();
		try{
			String employeeNumber = requestParameter.getEmployeeNumber();

			VmsLogging.logInfo(getClass(), "Loggeduser info "+employeeNumber);
			response = employeeHomeService.getLoggedUserInfo(employeeNumber);
		
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Loggeduser info Exception ", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	/**
	 * @param requestParameter
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/gettimeoffhistory", method = RequestMethod.POST)
	public List<SearchHistoryVO> getTimeoffHistory(@RequestBody RequestParameterVO requestParameter,ServletRequest req){
		List<SearchHistoryVO> response =  new ArrayList<>();
		try{
			String employeeNumber = requestParameter.getEmployeeNumber();
			String appliedDate = (requestParameter.getAppliedDate()!=null?requestParameter.getAppliedDate():"");
			String fromDate = (requestParameter.getFromDate()!=null?requestParameter.getFromDate():"");
			String toDate = (requestParameter.getToDate()!=null?requestParameter.getToDate():"");
			String numberOfDays = (requestParameter.getNumberOfDays()!=null?requestParameter.getNumberOfDays():"");
			String leaveType = (requestParameter.getLeaveType()!=null?requestParameter.getLeaveType():"");
			String approvedDate = (requestParameter.getApprovedDate()!=null?requestParameter.getApprovedDate():"");
			String leaveStatus = (requestParameter.getLeaveStatus()!=null?requestParameter.getLeaveStatus():"");
		
			VmsLogging.logInfo(getClass(), "Vaction history info "+employeeNumber);			
			response = employeeHomeService.getTimeoffHistory(employeeNumber, appliedDate, fromDate, toDate, numberOfDays, leaveType, approvedDate, leaveStatus);
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Vaction history Exception ", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	/**
	 * @param requestParameter
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/getleaverequestinfo", method = RequestMethod.POST)
	public List<LeaveRequestIdInfo> getLeaveRequestInfo(@RequestBody RequestParameterVO requestParameter,ServletRequest req){
		List<LeaveRequestIdInfo> response = new ArrayList<>();
		try{
			String employeeNumber = requestParameter.getEmployeeNumber();
			String leaveRequestId = requestParameter.getLeaveRequestId();
			
			if(employeeNumber!=null){
				response = employeeHomeService.getLeaveRequestInfo(employeeNumber, leaveRequestId);
			}
			VmsLogging.logInfo(getClass(), "get leave request info "+employeeNumber+",leaveRequestId "+leaveRequestId);
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Leave request info Exception ", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	/**
	 * @param requestParameter
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/leaverequestprocess", method = RequestMethod.POST)
	public List<LeaveRequestProcessVO> leaveRequest(@RequestBody RequestParameterVO requestParameter,ServletRequest req){
		LeaveRequestProcessVO leaveRequestProcessVO = new LeaveRequestProcessVO();
		ArrayList<LeaveRequestProcessVO> response = new ArrayList<>();
		try{
			String employeeNumber = requestParameter.getEmployeeNumber();
			int employeeId = requestParameter.getEmployeeId();
			VmsLogging.logInfo(getClass(), "request "+JSONUtil.toJson(requestParameter));
			if(employeeNumber!=null){
				Map<String, ArrayList<?>> managerinfo = employeeHomeService.getLoggedUserInfo(employeeNumber);
				ReportManagerVO reportManagerVO = new ReportManagerVO();				
				reportManagerVO = (com.photon.vms.vo.ReportManagerVO) managerinfo.get("EmpReportManagerInfo").get(0);
				if(requestParameter.getContactNumber() == null || requestParameter.getContactNumber().isEmpty() || requestParameter.getContactNumber().equals("null")) {
					leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
					leaveRequestProcessVO.setErrorCode("03");
					leaveRequestProcessVO.setErrorMessage(CommonConstants.CONTACT_NUMBER_ERROR_MSG);
					response.add(leaveRequestProcessVO);
					VmsLogging.logError(getClass(), CommonConstants.CONTACT_NUMBER_ERROR_MSG, null);
					return response;
				}
				/*Submit leave request start*/
				if(requestParameter.getLeaveRequestId().equalsIgnoreCase("0") && requestParameter.getLeaveStatus().equals(CommonConstants.STATUS_PENDING)){
					leaveRequestProcessVO = submitLeaveRequest(managerinfo, reportManagerVO, employeeNumber, employeeId, requestParameter);
				}				
				/*Submit leave request End*/
				else if(requestParameter.getLeaveStatus().equals(CommonConstants.STATUS_CANCELLED)){
					leaveRequestProcessVO = cancelLeaveRequest(employeeNumber, employeeId, requestParameter);
				}else if(requestParameter.getLeaveStatus().equals(CommonConstants.STATUS_APPROVED)){
					leaveRequestProcessVO = approveLeaveRequest(reportManagerVO, employeeNumber, employeeId, requestParameter);
				}else if(requestParameter.getLeaveStatus().equals(CommonConstants.STATUS_REJECTED)){
					leaveRequestProcessVO = rejectLeaveRequest(reportManagerVO, employeeNumber, employeeId, requestParameter);
				}else if(requestParameter.getLeaveStatus().equals(CommonConstants.STATUS_PENDING_REVOCATION) || requestParameter.getLeaveStatus().equals(CommonConstants.STATUS_REVOKED_REJECTED) || requestParameter.getLeaveStatus().equals(CommonConstants.STATUS_REVOKED_APPROVED)) {
					leaveRequestProcessVO = revocationPendingAndReject(employeeNumber, employeeId, requestParameter);
				}
				else{
					leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
					leaveRequestProcessVO.setErrorCode("03");
					leaveRequestProcessVO.setErrorMessage(CommonConstants.INVALID_DATA);
					VmsLogging.logError(getClass(), CommonConstants.INVALID_DATA , new VmsApplicationException("employeeNumber "+employeeNumber+", leaveRequestId "+requestParameter.getLeaveRequestId()+", leaveStatus "+requestParameter.getLeaveStatus()));
				}
			}else{
				leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
				leaveRequestProcessVO.setErrorCode("03");
				leaveRequestProcessVO.setErrorMessage(CommonConstants.INVALID_DATA);
				VmsLogging.logError(getClass(), CommonConstants.INVALID_DATA, null);
			}
			response.add(leaveRequestProcessVO);
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Submit leave request Exception ", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}	
	private LeaveRequestProcessVO submitLeaveRequest(Map<String, ArrayList<?>> managerinfo,
			ReportManagerVO reportManagerVO, String employeeNumber, int employeeId, RequestParameterVO request){

		VmsLogging.logInfo(getClass(), "Leave request "+employeeNumber);
		LeaveRequestProcessVO leaveRequestProcessVO = new LeaveRequestProcessVO();
		ArrayList<String> continuousLeaveCode = new ArrayList<>();
		ArrayList<BulkApproveMailVO> bulkApproveMailVO =  new ArrayList<>();
		try{
			double clBalance = 0.0;
			boolean continousLeaveFlag = false;
			if(reportManagerVO!=null && reportManagerVO.getManagerEmailId()!=null && reportManagerVO.getReportingManagerName()!=null){
				boolean isValid = false;
				Calendar calInfo = Calendar.getInstance();
				SimpleDateFormat dateFormat = new SimpleDateFormat(CommonConstants.YYYY_MM_DD);
				Date d = calInfo.getTime();
				Date currentDate = dateFormat.parse(dateFormat.format(d));
				Date compareFromDate = dateFormat.parse(request.getFromDate());
				LeaveTypeVO leaveType = employeeHomeService.getLeaveTypeDetail(request.getLeaveTypeId());
	
				ArrayList<LeaveBalanceDetailVO> leaveBalanceDetail = (ArrayList<LeaveBalanceDetailVO>) managerinfo.get("LeaveBalanceInfo");
				ArrayList<AdHocWorkDaysVO> adHocWorkDayList = (ArrayList<AdHocWorkDaysVO>) managerinfo.get("AdHocWorkingDays");
				ArrayList<HolidayListVO> holidayList = (ArrayList<HolidayListVO>) managerinfo.get("HolidayList");
				ArrayList<OutOfOfficeInfoVO> outOfOfficeInfo = (ArrayList<OutOfOfficeInfoVO>) managerinfo.get("OutOfOfficeOptions");
				
				ArrayList<String> calculateOut = calculateNumberOfDays(request.getFromDate(), request.getToDate(), adHocWorkDayList, holidayList, employeeNumber, leaveType.getLeaveCode());
				Double checkdays = Double.valueOf(calculateOut.get(0));
				if(calculateOut.get(1).equals(CommonConstants.FALSE)){
					leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
					leaveRequestProcessVO.setErrorCode("01");
					leaveRequestProcessVO.setErrorMessage("You are not allowed to submit "+leaveType.getLeaveType()+" on Saturday/Sunday or any other holidays!");
					VmsLogging.logError(getClass(), "You are not allowed to submit "+leaveType.getLeaveType()+" on Saturday/Sunday or any other holidays! "+employeeNumber, null);
					return leaveRequestProcessVO;
				}
				
				VmsLogging.logInfo(getClass(),"Submit leave request -- employeeNumber "+employeeNumber+", leaveRequestId "+request.getLeaveRequestId()+", leaveStatus "+request.getLeaveStatus()+", Source "+CommonConstants.SOURCE_OF_INPUT_LA);
				
				for(int i=0; i<managerinfo.get("LeaveTypeList").size(); i++){
					LeaveTypeVO leaveTypeVO = new LeaveTypeVO();
					leaveTypeVO = (LeaveTypeVO) managerinfo.get("LeaveTypeList").get(i);
					if(leaveTypeVO.getIsContinuous().equalsIgnoreCase("y")){
						continuousLeaveCode.add(leaveTypeVO.getLeaveCode().toUpperCase());
					}
				}
				for(int i=0; i<managerinfo.get("LeaveBalanceInfo").size(); i++){
					LeaveBalanceDetailVO leaveBalanceInfo = new LeaveBalanceDetailVO();
					leaveBalanceInfo = (LeaveBalanceDetailVO) managerinfo.get("LeaveBalanceInfo").get(i);
					if(leaveBalanceInfo.getLeaveCode().equals("CL")){
						clBalance = leaveBalanceInfo.getOpenLeave();
					}					
					if(continuousLeaveCode.contains(leaveType.getLeaveCode().toUpperCase()) && Double.parseDouble(leaveBalanceInfo.getApprovedAndPendingBalance())>0 && leaveType.getLeaveCode().equalsIgnoreCase(leaveBalanceInfo.getLeaveCode())){
						continousLeaveFlag = true;
					}
				}
				if(continousLeaveFlag){
					leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
					leaveRequestProcessVO.setErrorCode("01");
					leaveRequestProcessVO.setErrorMessage("You have already used the "+leaveType.getLeaveType()+", can not apply again");
					VmsLogging.logError(getClass(), "You have already used the "+leaveType.getLeaveType()+", can not apply again"+employeeNumber, null);
					return leaveRequestProcessVO;
				}
				switch (leaveType.getLeaveCode().toUpperCase()) {
					case "MML":
						if(checkdays > 30){
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("You cannot apply Maternity Medical Leave more than 1 month ");
							VmsLogging.logError(getClass(), "You cannot apply Maternity Medical Leave more than 1 month "+employeeNumber, null);
							return leaveRequestProcessVO;
						}
						break;
					case "MCL":
						if(checkdays > 21 && reportManagerVO.getLocationCode().equalsIgnoreCase("IND")){
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("You cannot apply Miscarriage Leave more than 3 weeks ");
							VmsLogging.logError(getClass(), "You cannot apply Miscarriage Leave more than 3 weeks "+employeeNumber, null);
							return leaveRequestProcessVO;
						}else if(checkdays > 42){
							leaveRequestProcessVO.setStatus("Error");
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("You cannot apply Miscarriage Leave more than 6 weeks ");
							VmsLogging.logError(getClass(), "You cannot apply Miscarriage Leave more than 6 weeks "+employeeNumber, null);
							return leaveRequestProcessVO;
						}
						break;
					case "TL":
						if(checkdays > 10){
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("You cannot apply Tubectomy Leave more than 2 weeks ");
							VmsLogging.logError(getClass(), "You cannot apply Tubectomy Leave more than 2 weeks "+employeeNumber, null);
							return leaveRequestProcessVO;
						}
						break;
					case "PTL":
						if(checkdays > 3){
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("You cannot apply Paternity Leave more than 3 days ");
							VmsLogging.logError(getClass(), "You cannot apply Paternity Leave more than 3 days "+employeeNumber, null);
							return leaveRequestProcessVO;
						}
						break;
					case "AL":
						if(checkdays > 7){
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("You cannot apply Adoption Leave more than 1 week ");
							VmsLogging.logError(getClass(), "You cannot apply Adoption Leave more than 1 week "+employeeNumber, null);
							return leaveRequestProcessVO;
						}
						break;
					case "BL":
						if(checkdays > 3){
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("You cannot apply Bereavement  Leave more than 3 days ");
							VmsLogging.logError(getClass(), "You cannot apply Bereavement  Leave more than 3 days "+employeeNumber, null);
							return leaveRequestProcessVO;
						}
						break;
					case "FML":
						if(checkdays > 84){
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("You cannot apply Family and Medical Leave more than 12 weeks ");
							VmsLogging.logError(getClass(), "You cannot apply Family and Medical Leave more than 12 weeks "+employeeNumber, null);
							return leaveRequestProcessVO;
						}
						break;
					case "ANL":
						if(checkdays > 3){
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("You cannot apply more than 3 days Annual Leave at a time ");
							VmsLogging.logError(getClass(), "You cannot apply more than 3 days Annual Leave at a time "+employeeNumber, null);
							return leaveRequestProcessVO;
						}
						break;
					case "CNL":
						if(checkdays > 3){
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("You cannot apply Condolence Leave more than 3 days ");
							VmsLogging.logError(getClass(), "You cannot apply Condolence Leave more than 3 days "+employeeNumber, null);
							return leaveRequestProcessVO;
						}
						break;
				}
				if((leaveType.getLeaveCode().equals("PL") && clBalance>0.0) || leaveType.getLeaveCode().equals("ANL")){
					if((compareFromDate.compareTo(currentDate) < 0)){
						isValid = true;
					}
				}
				if(leaveType !=null && checkIsValidLeaveType(leaveType,leaveBalanceDetail,adHocWorkDayList,holidayList,employeeNumber,request.getNumberOfDays(),reportManagerVO.getLocationCode(),request.getFromDate(),request.getToDate(), checkdays)){
					String appliedDate = "";
					String noofDays=""; 
					String levType = "";
					String approvedDate="";
					String levStatus="";
					if(leaveType.getLeaveTypeId().equals("2") || leaveType.getLeaveTypeId().equals("15"))
						calInfo.add(Calendar.DATE, -8);
					else {
						calInfo.add(Calendar.MONTH, -2);
						calInfo.set(Calendar.DAY_OF_MONTH, calInfo.getActualMaximum(Calendar.DAY_OF_MONTH));
						isValid = true;
					}
					Date date = calInfo.getTime();
					VmsLogging.logInfo(getClass(), "date "+date);
					boolean flag = false;
					
					List<SearchHistoryVO> searchList = employeeHomeService.getTimeoffHistory(employeeNumber, appliedDate, request.getFromDate(), request.getToDate(), noofDays, levType, approvedDate, levStatus);
					VmsLogging.logDebug(getClass(), "getTimeoffHistory "+searchList.size());
					
					if(searchList != null && !searchList.isEmpty()){
						if(searchList.size() == 1){
							SearchHistoryVO info = searchList.get(0);
							if(info.getLeaveRequestId().equals(request.getLeaveRequestId())){
								flag = true;
							}else{
								if(CommonConstants.getValidLeaveStatus().contains(info.getLeaveStatus())){
									flag=false;
									leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
									leaveRequestProcessVO.setErrorCode("01");
									leaveRequestProcessVO.setErrorMessage(CommonConstants.LEAVE_ALREADY_APPLIED_MESSAGE+ info.getFromDate()+" to "+info.getToDate()+ ".");
									VmsLogging.logError(getClass(), CommonConstants.LEAVE_ALREADY_APPLIED_MESSAGE+ info.getFromDate()+" to "+info.getToDate()+ ". "+employeeNumber, null);
								
								}else{
									flag=true;
								}
							}
						}else{
							
							StringBuilder dateStr = new StringBuilder();
							for(int i=0;i<searchList.size();i++){
								SearchHistoryVO info =  searchList.get(i);
								if(CommonConstants.getValidLeaveStatus().contains(info.getLeaveStatus())){
									if(i == searchList.size()-1){
										dateStr.append(info.getFromDate() +" to "+info.getToDate());
									}else{
										dateStr.append(info.getFromDate() +" to "+info.getToDate()+",");
									}
								}
							}
							if(dateStr.length() > 0){
								flag=false;
								leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
								leaveRequestProcessVO.setErrorCode("01");
								leaveRequestProcessVO.setErrorMessage(CommonConstants.LEAVE_ALREADY_APPLIED_MESSAGE+dateStr+".");
								VmsLogging.logError(getClass(), CommonConstants.LEAVE_ALREADY_APPLIED_MESSAGE+dateStr+ ". "+employeeNumber, null);
							}else{
								flag=true;
							}
						}
					}else{
						flag=true;
					}
					VmsLogging.logInfo(getClass(), compareFromDate.compareTo(date)+" flag : "+flag+" isValis : "+isValid);
					if(compareFromDate.compareTo(date) <0 && isValid && flag){
						boolean unfreezeRequest = adminService.isValidUnfrozenRecords(request.getFromDate(), request.getToDate(), employeeId);
						if(unfreezeRequest){
							flag = true;
						}else{
							flag=false;
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("These dates have been froze and hence, leave cannot be applied: "+request.getFromDate()+".");
							VmsLogging.logError(getClass(), "These dates have been froze and hence, leave cannot be applied: "+request.getFromDate(), null);
						}
					}
					if(flag){
						List<ProcessLeaveRequestVO> outputdata = employeeHomeService.processLeaveRequest(employeeNumber, request.getLeaveRequestId(), employeeId, request.getLeaveTypeId(), request.getLeaveStatus(), request.getFromDate(), request.getToDate(), request.getNumberOfDays(), request.getContactNumber(), request.getLeaveReason(), request.getOdOptionId(), CommonConstants.SOURCE_OF_INPUT_LA);
						if(!outputdata.isEmpty() && outputdata.get(0).getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)){
							leaveRequestProcessVO.setStatus(CommonConstants.SUCCESS);
							leaveRequestProcessVO.setSuccessMessage("Your Leave Request has been placed successfully");
							VmsLogging.logInfo(getClass(),"Your Leave Request has been placed successfully");
							
							for (OutOfOfficeInfoVO data : outOfOfficeInfo) {
								request.setLeaveReason((Integer.parseInt(data.getOdOptionId()) == request.getOdOptionId())?(!request.getLeaveReason().equals("")?data.getOdOptionName()+"-"+request.getLeaveReason():data.getOdOptionName()):request.getLeaveReason());
							}
							BulkApproveMailVO newData = new BulkApproveMailVO();
							newData.setLeaveStatus(CommonConstants.STATUS_PENDING);
							newData.setLeaveCode(leaveType.getLeaveCode());
							newData.setReportManagerVO(reportManagerVO);
							newData.setEmployeeNumber(employeeNumber);
							newData.setRequestLeaveId(outputdata.get(0).getLeaveRequestParentId());
							newData.setEmployeeId(employeeId);
							newData.setFromdate(request.getFromDate());
							newData.setToDate(request.getToDate());
							newData.setNumberOfDays(request.getNumberOfDays());
							newData.setContactNumber(request.getContactNumber());
							newData.setLeaveReason(request.getLeaveReason());
							newData.setCheckdays(checkdays);
							newData.setLeaveName(leaveType.getLeaveType());
							bulkApproveMailVO.add(newData);

						}else{
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("Error in submitting leave request");
							VmsLogging.logError(getClass(), "Error in submitting leave request "+employeeNumber, null);
						}
					}
				}else{
					leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
					leaveRequestProcessVO.setErrorCode("01");
					leaveRequestProcessVO.setErrorMessage("You don't have sufficient leave balance to apply for leave!");
					VmsLogging.logError(getClass(), "You don't have sufficient leave balance to apply for leave! "+employeeNumber, null);
				}
			}else{
				leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
				leaveRequestProcessVO.setErrorCode("01");
				leaveRequestProcessVO.setErrorMessage("You don't have manager to submit the leave request!");
				VmsLogging.logError(getClass(), "You don't have manager to submit the leave request! "+employeeNumber, null);
			}
			return leaveRequestProcessVO;
		}catch(Exception e){
			leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
			leaveRequestProcessVO.setErrorCode("01");
			leaveRequestProcessVO.setErrorMessage("Something went wrong..!!");
			VmsLogging.logError(getClass(), "Submit leave request Exception ", new VmsApplicationException(e.getMessage()));
			return leaveRequestProcessVO;
		}finally{
			 new Thread() {
			    @Override
			    public void run() {
					for (BulkApproveMailVO data : bulkApproveMailVO) {
						try {
							
							mailSendUtils.sendMail(data.getLeaveStatus(), data.getLeaveCode(), data.getReportManagerVO(), data.getEmployeeNumber(), data.getRequestLeaveId(), data.getEmployeeId(), data.getFromdate(), data.getToDate(), data.getNumberOfDays(), data.getContactNumber(), data.getLeaveReason(), data.getCheckdays(), data.getLeaveName());
						} catch (Exception e) {
							VmsLogging.logError(getClass(), "bulk upload mail error", new VmsApplicationException(e.getMessage()));
						}
					}
			    }
			}.start();		
		}	
	}
	/**
	 * @param ownInfo
	 * @param employeeNumber
	 * @param employeeId
	 * @param leaveRequestId
	 * @param leaveTypeId
	 * @param leaveStatus
	 * @param fromdate
	 * @param toDate
	 * @param numberOfDays
	 * @param contactNumber
	 * @param leaveReason
	 * @param odOptionId
	 * @return
	 */
	private LeaveRequestProcessVO approveLeaveRequest(ReportManagerVO ownInfo, String employeeNumber, int employeeId, RequestParameterVO request) {
		LeaveRequestProcessVO leaveRequestProcessVO = new LeaveRequestProcessVO();
		Map<String, ArrayList<?>> pendingList = new HashMap<>();
		ArrayList<BulkApproveMailVO> bulkApproveMailVO =  new ArrayList<>();
		ArrayList<RevocationEmailVO> emailList = new ArrayList<>();
		try{
			ArrayList<String> leaveRequestList = new ArrayList<>(Arrays.asList(request.getLeaveRequestId().split(",")));
			request.setEmployeeNumber(employeeNumber);
			pendingList = managerReviewService.getPendingLeaveRequest(employeeNumber,"", "", "", "", "","", "");
			ArrayList<String> pendingListAlone = (ArrayList<String>) pendingList.get("LeaveRequestList");
			VmsLogging.logInfo(getClass(),"Approve leave request -- employeeNumber "+employeeNumber+", leaveRequestId "+request.getLeaveRequestId()+", leaveStatus "+request.getLeaveStatus()+", Source "+CommonConstants.SOURCE_OF_INPUT_LA);
			
			Iterator<String> it = leaveRequestList.iterator();
			String employeeName = ownInfo.getEmployeeName();
			while(it.hasNext()){
				BulkApproveMailVO newData = new BulkApproveMailVO();
				String requestLeaveId = it.next();
				if(pendingListAlone.contains(requestLeaveId)){
					List<LeaveRequestIdInfo> responseData = employeeHomeService.getLeaveRequestInfo(employeeNumber, requestLeaveId);
					if(!responseData.isEmpty()) {
						request.setLeaveStatus(responseData.get(0).getLeaveStatus());
						if(request.getLeaveStatus().equals(CommonConstants.STATUS_PENDING))
							request.setLeaveStatus(CommonConstants.STATUS_APPROVED);
						else
							request.setLeaveStatus(CommonConstants.STATUS_REVOKED_APPROVED);
					}
					List<ProcessLeaveRequestVO> outputdata = employeeHomeService.processLeaveRequest(employeeNumber, requestLeaveId, employeeId, request.getLeaveTypeId(), request.getLeaveStatus(), request.getFromDate(), request.getToDate(), request.getNumberOfDays(), request.getContactNumber(), request.getLeaveReason(), request.getOdOptionId(), CommonConstants.SOURCE_OF_INPUT_LA);
					if(!outputdata.isEmpty() && outputdata.get(0).getStatus().equals(CommonConstants.SUCCESS)){
						Double checkdays=0.0;
						if(!request.getLeaveStatus().equals(CommonConstants.STATUS_REVOKED_APPROVED)) {
							if(!responseData.isEmpty()){
								request.setFromDate(responseData.get(0).getFromDate().toString());
								request.setToDate(responseData.get(0).getToDate().toString());
								request.setNumberOfDays(Integer.toString(responseData.get(0).getNumberOfDays()));
								checkdays = Double.valueOf(responseData.get(0).getNumberOfDays());
								request.setContactNumber(responseData.get(0).getContactNumber());
								request.setLeaveStatus(responseData.get(0).getLeaveStatus());
								request.setLeaveReason((responseData.get(0).getOdOptionName()!=null?responseData.get(0).getOdOptionName()+"-"+responseData.get(0).getLeaveReason():responseData.get(0).getLeaveReason()));
								String leaveCode = responseData.get(0).getLeaveTypecode();
								String leaveName = responseData.get(0).getLeaveTypeName();
								
								Map<String, ArrayList<?>> managerinfo = employeeHomeService.getLoggedUserInfo(responseData.get(0).getOwnerEmployeeNumber());
								ReportManagerVO reportManagerVO = new ReportManagerVO();				
								reportManagerVO = (com.photon.vms.vo.ReportManagerVO) managerinfo.get("EmpReportManagerInfo").get(0);
								reportManagerVO.setReportingManagerName(employeeName);
								String[] managermail = reportManagerVO.getManagerEmailId().split(",");
								if(managermail.length > 1)
									reportManagerVO.setMailCC(reportManagerVO.getManagerEmailId());
								
								newData.setLeaveStatus(CommonConstants.STATUS_APPROVED);
								newData.setLeaveCode(leaveCode);
								newData.setReportManagerVO(reportManagerVO);
								newData.setEmployeeNumber(employeeNumber);
								newData.setRequestLeaveId(requestLeaveId);
								newData.setEmployeeId(employeeId);
								newData.setFromdate(request.getFromDate());
								newData.setToDate(request.getToDate());
								newData.setNumberOfDays(request.getNumberOfDays());
								newData.setContactNumber(request.getContactNumber());
								newData.setLeaveReason(request.getLeaveReason());
								newData.setCheckdays(checkdays);
								newData.setLeaveName(leaveName);
								bulkApproveMailVO.add(newData);
							}
						}
						else {
							RevocationEmailVO getMailDetails = revocationService.getMailDetails(requestLeaveId);
							emailList.add(getMailDetails);
						}
						leaveRequestProcessVO.setStatus(CommonConstants.SUCCESS);
						leaveRequestProcessVO.setSuccessMessage("Leave request(s) has been approved successfully..!");
						VmsLogging.logInfo(getClass(),"Approve leave request Success-"+requestLeaveId);
					}else{
						leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
						leaveRequestProcessVO.setErrorCode("01");
						leaveRequestProcessVO.setErrorMessage("Submission error...!!");
						VmsLogging.logError(getClass(), "Submission error - employeeNumber"+employeeNumber+" requestLeaveId "+requestLeaveId, null);
					}
				}else{
					leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
					leaveRequestProcessVO.setErrorCode("01");
					leaveRequestProcessVO.setErrorMessage("The request has been already processed");
					VmsLogging.logError(getClass(), "The request has been already processed - "+employeeNumber, null);
				}
			}
			return leaveRequestProcessVO;
		}catch(Exception e){
			leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
			leaveRequestProcessVO.setErrorCode("01");
			leaveRequestProcessVO.setErrorMessage("Something went wrong..!!");
			VmsLogging.logError(getClass(), "Approve leave request Exception ", new VmsApplicationException(e.getMessage()));
			return leaveRequestProcessVO;
		}finally{
			 new Thread() {
    		    @Override
    		    public void run() {
    				for (BulkApproveMailVO data : bulkApproveMailVO) {
    					try {
    						mailSendUtils.sendMail(data.getLeaveStatus(), data.getLeaveCode(), data.getReportManagerVO(), data.getEmployeeNumber(), data.getRequestLeaveId(), data.getEmployeeId(), data.getFromdate(), data.getToDate(), data.getNumberOfDays(), data.getContactNumber(), data.getLeaveReason(), data.getCheckdays(), data.getLeaveName());
    					} catch (Exception e) {
    						VmsLogging.logError(getClass(), "bulk mail for Approved", new VmsApplicationException(e.getMessage()));;
    					}
    				}
    				for(RevocationEmailVO data : emailList) {
    					try {
    						sendBulkRevocationEmailToEmployee(data,data.getAdminDetails().size(),CommonConstants.SOURCE_OF_INPUT_LA);
    					} catch (Exception e) {
    						VmsLogging.logError(getClass(), "bulk mail for Revocation Approved", new VmsApplicationException(e.getMessage()));;
    					}
    				}
    		    }
    		}.start();
		}
	}
	/**
	 * @param employeeNumber
	 * @param employeeId
	 * @param leaveRequestId
	 * @param leaveTypeId
	 * @param leaveStatus
	 * @param fromdate
	 * @param toDate
	 * @param numberOfDays
	 * @param contactNumber
	 * @param leaveReason
	 * @param odOptionId
	 * @return
	 */
	private LeaveRequestProcessVO cancelLeaveRequest(String employeeNumber, int employeeId, RequestParameterVO request) {
		LeaveRequestProcessVO leaveRequestProcessVO = new LeaveRequestProcessVO();
		List<LeaveRequestIdInfo> response = new ArrayList<>();
		try{
			if(request.getLeaveRequestId()!=null){
				VmsLogging.logInfo(getClass(),"Cancel leave request -employeeNumber "+employeeNumber+", leaveRequestId-"+request.getLeaveRequestId());
				response = employeeHomeService.getLeaveRequestInfo(employeeNumber, request.getLeaveRequestId());
				if(response!=null){
					LeaveRequestIdInfo details = response.get(0);
					if(details.getLeaveStatus()!=null && !details.getLeaveStatus().equalsIgnoreCase(CommonConstants.STATUS_CANCELLED)){
						if(details.getFromDate() !=null && details.getLeaveStatus().equalsIgnoreCase(CommonConstants.STATUS_PENDING)){
							List<ProcessLeaveRequestVO> outputdata = employeeHomeService.processLeaveRequest(employeeNumber, request.getLeaveRequestId(), employeeId,request.getLeaveTypeId(), request.getLeaveStatus(), request.getFromDate(), request.getToDate(), request.getNumberOfDays(), request.getContactNumber(), request.getLeaveReason(), request.getOdOptionId(), CommonConstants.SOURCE_OF_INPUT_LA);
							if(!outputdata.isEmpty() && outputdata.get(0).getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)){
								leaveRequestProcessVO.setStatus(CommonConstants.SUCCESS);
								leaveRequestProcessVO.setSuccessMessage("Leave request cancelled successfully..!");
								VmsLogging.logInfo(getClass(),"Cancel Leave request success- "+request.getLeaveRequestId());
							}else{
								leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
								leaveRequestProcessVO.setErrorCode("01");
								leaveRequestProcessVO.setErrorMessage("Leave request can not be cancelled");
								VmsLogging.logError(getClass(), "Leave request can not be cancelled", null);
							}
						}else{
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("Leave request can not be cancelled for back date(s)");	
							VmsLogging.logError(getClass(), "Leave request can not be cancelled for back date(s)", null);
						}
					}else{
						leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
						leaveRequestProcessVO.setErrorCode("01");
						leaveRequestProcessVO.setErrorMessage("Leave request has been cancelled already");
						VmsLogging.logError(getClass(), "Leave request has been cancelled already", null);
					}
				}else{
					leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
					leaveRequestProcessVO.setErrorCode("03");
					leaveRequestProcessVO.setErrorMessage(CommonConstants.LEAVE_REQUEST_ID_INVALID);
					VmsLogging.logError(getClass(), CommonConstants.LEAVE_REQUEST_ID_INVALID, null);
				}
			}else{
				leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
				leaveRequestProcessVO.setErrorCode("03");
				leaveRequestProcessVO.setErrorMessage(CommonConstants.LEAVE_REQUEST_ID_INVALID);
				VmsLogging.logError(getClass(), CommonConstants.LEAVE_REQUEST_ID_INVALID, null);
			}
		}catch(Exception e){
			leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
			leaveRequestProcessVO.setErrorCode("04");
			leaveRequestProcessVO.setErrorMessage("Something went wrong..!");
			VmsLogging.logError(getClass(), "Cancel leave request Exception ", new VmsApplicationException(e.getMessage()));
		}
		return leaveRequestProcessVO;
	}
	/**
	 * @param ownInfo
	 * @param employeeNumber
	 * @param employeeId
	 * @param leaveRequestId
	 * @param leaveTypeId
	 * @param leaveStatus
	 * @param fromdate
	 * @param toDate
	 * @param numberOfDays
	 * @param contactNumber
	 * @param leaveReason
	 * @param odOptionId
	 * @return
	 */
	private LeaveRequestProcessVO rejectLeaveRequest(ReportManagerVO ownInfo, String employeeNumber, int employeeId,RequestParameterVO request) {
		LeaveRequestProcessVO leaveRequestProcessVO = new LeaveRequestProcessVO();
		Map<String, ArrayList<?>> pendingList = new HashMap<>();
		ArrayList<BulkApproveMailVO> bulkApproveMailVO =  new ArrayList<>();
		try{
			if(request.getLeaveRequestId() != null){
				request.setEmployeeNumber(employeeNumber);
				pendingList = managerReviewService.getPendingLeaveRequest(employeeNumber,"", "", "", "", "", "", "");
				ArrayList<String> pendingListAlone = (ArrayList<String>) pendingList.get("LeaveRequestList");
				VmsLogging.logInfo(getClass(),"Rejection leave request -employeeNumber "+employeeNumber+", leaveRequestId-"+request.getLeaveRequestId());
				
					if(pendingListAlone.contains(request.getLeaveRequestId())){
						String employeeName = ownInfo.getEmployeeName();
						List<ProcessLeaveRequestVO> outputdata = employeeHomeService.processLeaveRequest(employeeNumber, request.getLeaveRequestId(), employeeId, request.getLeaveTypeId(), request.getLeaveStatus(), request.getFromDate(), request.getToDate(), request.getNumberOfDays(), request.getContactNumber(), request.getLeaveReason(), request.getOdOptionId(), CommonConstants.SOURCE_OF_INPUT_LA);
						if(outputdata.size()>0 && outputdata.get(0).getStatus().equals(CommonConstants.SUCCESS)){
							Double checkdays=0.0;
							List<LeaveRequestIdInfo> responseData = employeeHomeService.getLeaveRequestInfo(employeeNumber, request.getLeaveRequestId());
							if(!responseData.isEmpty()){
								request.setFromDate(responseData.get(0).getFromDate().toString());
								request.setToDate(responseData.get(0).getToDate().toString());
								request.setNumberOfDays(Integer.toString(responseData.get(0).getNumberOfDays()));
								checkdays = Double.valueOf(responseData.get(0).getNumberOfDays());
								request.setContactNumber(responseData.get(0).getContactNumber());
								String leaveCode = responseData.get(0).getLeaveTypecode();
								String leaveName = responseData.get(0).getLeaveTypeName();
								
								Map<String, ArrayList<?>> managerinfo = employeeHomeService.getLoggedUserInfo(responseData.get(0).getOwnerEmployeeNumber());
								ReportManagerVO reportManagerVO = new ReportManagerVO();				
								reportManagerVO = (com.photon.vms.vo.ReportManagerVO) managerinfo.get("EmpReportManagerInfo").get(0);
								reportManagerVO.setReportingManagerName(employeeName);
								String managermail[] = reportManagerVO.getManagerEmailId().split(",");
								if(managermail.length > 1)
									reportManagerVO.setMailCC(reportManagerVO.getManagerEmailId());
								
								BulkApproveMailVO newData = new BulkApproveMailVO();
								newData.setLeaveStatus(CommonConstants.STATUS_REJECTED);
								newData.setLeaveCode(leaveCode);
								newData.setReportManagerVO(reportManagerVO);
								newData.setEmployeeNumber(employeeNumber);
								newData.setRequestLeaveId(request.getLeaveRequestId());
								newData.setEmployeeId(employeeId);
								newData.setFromdate(request.getFromDate());
								newData.setToDate(request.getToDate());
								newData.setNumberOfDays(request.getNumberOfDays());
								newData.setContactNumber(request.getContactNumber());
								newData.setLeaveReason(request.getLeaveReason());
								newData.setCheckdays(checkdays);
								newData.setLeaveName(leaveName);
								bulkApproveMailVO.add(newData);
							}
							leaveRequestProcessVO.setStatus(CommonConstants.SUCCESS);
							leaveRequestProcessVO.setSuccessMessage("Leave request(s) has been rejected successfully..!");
							VmsLogging.logInfo(getClass(),"Rejection Leave request success- "+request.getLeaveRequestId());
						}else{
							leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
							leaveRequestProcessVO.setErrorCode("01");
							leaveRequestProcessVO.setErrorMessage("Submission error...!!");
							VmsLogging.logError(getClass(), "Submission error...!! ", null);
						}
					}else{
						leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
						leaveRequestProcessVO.setErrorCode("01");
						leaveRequestProcessVO.setErrorMessage("Leave request not belongs to you...!!");
						VmsLogging.logError(getClass(), "Leave request not belongs to you...!!", null);
					}
			}else{
				leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
				leaveRequestProcessVO.setErrorCode("01");
				leaveRequestProcessVO.setErrorMessage("Not a valid leave request Id");
				VmsLogging.logError(getClass(), "Not a valid leave request Id "+request.getLeaveRequestId(), null);
			}
			return leaveRequestProcessVO;
		}catch(Exception e){
			leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
			leaveRequestProcessVO.setErrorCode("01");
			leaveRequestProcessVO.setErrorMessage("Error on leave request dispute");
			VmsLogging.logError(getClass(), "Dispute leave request Exception ", new VmsApplicationException(e.getMessage()));
			return leaveRequestProcessVO;
		}finally{
			 new Thread() {
				    @Override
				    public void run() {
						for (BulkApproveMailVO data : bulkApproveMailVO) {
							try {
								mailSendUtils.sendMail(data.getLeaveStatus(), data.getLeaveCode(), data.getReportManagerVO(), data.getEmployeeNumber(), data.getRequestLeaveId(), data.getEmployeeId(), data.getFromdate(), data.getToDate(), data.getNumberOfDays(), data.getContactNumber(), data.getLeaveReason(), data.getCheckdays(), data.getLeaveName());
							} catch (Exception e) {
								VmsLogging.logError(getClass(), "bulk upload mail error", new VmsApplicationException(e.getMessage()));
							}
						}
				    }
				}.start();		
			}
	}
	/**
	 * @param leaveType
	 * @param leaveBalanceDetail
	 * @param adHocWorkDayList
	 * @param holidayList
	 * @param employeeNumber
	 * @param numberOfDays
	 * @param locationCode
	 * @param fromdate
	 * @param toDate
	 * @param checkdays
	 * @return
	 */
	private boolean checkIsValidLeaveType(LeaveTypeVO leaveType, ArrayList<LeaveBalanceDetailVO> leaveBalanceDetail, ArrayList<AdHocWorkDaysVO> adHocWorkDayList, ArrayList<HolidayListVO> holidayList, String employeeNumber, String numberOfDays,
			String locationCode, String fromdate, String toDate, Double checkdays) {		
		boolean flag = false;
		try{
			if(leaveType != null){
				if(!leaveType.getLeaveCode().equalsIgnoreCase("OD") && !leaveType.getLeaveCode().equalsIgnoreCase("LOP") ){
					if(leaveBalanceDetail != null && leaveBalanceDetail.size()>0){
						for(LeaveBalanceDetailVO LeaveBalanceInfo: leaveBalanceDetail){
							if(LeaveBalanceInfo.getLeaveCode().equalsIgnoreCase(leaveType.getLeaveCode())){
								flag =(Double.parseDouble(LeaveBalanceInfo.getBalanceLeave())-Double.parseDouble(LeaveBalanceInfo.getPendingLeave())) >= checkdays;
								break;
							}
						}
					}					
				}else{
					flag = true;
				}
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Validate leave type exception ", new VmsApplicationException(e.getMessage()));
		}
		return flag;
	}
	/**
	 * @param fromdate
	 * @param toDate
	 * @param adHocWorkDayList
	 * @param holidayList
	 * @param employeeNumber
	 * @param leaveCode
	 * @return
	 */
	private ArrayList<String> calculateNumberOfDays(String fromdate, String toDate, ArrayList<AdHocWorkDaysVO> adHocWorkDayList,
			ArrayList<HolidayListVO> holidayList, String employeeNumber, String leaveCode) {			
		Double days = 0.0;
		String validDateFlag = "true";
		ArrayList<String> result =  new ArrayList<>();
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.YYYY_MM_DD);
			Long daysDifference = (sdf.parse(toDate).getTime() - sdf.parse(fromdate).getTime())/(1000*60*60*24);
			ArrayList<String> workingdays = new ArrayList<>();
			ArrayList<String> holidays = new ArrayList<>();
			ArrayList<String> dateList = new ArrayList<>();
			for(AdHocWorkDaysVO adhoc: adHocWorkDayList){
				workingdays.add(adhoc.getLeaveDate().toString());
			}
			for(HolidayListVO hollist: holidayList){
				holidays.add(hollist.getHolidayDate().toString());
			}
			if(CommonConstants.getNonBlockLeaveTypes().contains(leaveCode)){
				if(daysDifference > 0){
					for(int i=0;i<=daysDifference;i++){
						Calendar c= Calendar.getInstance();
						c.setTime(sdf.parse(fromdate));
						c.add(Calendar.DATE, i);
						Date followDate = c.getTime();
						String nextDate = DateToString(followDate);						
						if(!CommonConstants.getInvalidDays().contains(followDate.getDay()+1)){
							if(!holidays.contains(nextDate)){
								dateList.add(nextDate);
							}else if(workingdays.contains(nextDate)){
								dateList.add(nextDate);
							}else if(i == 0){
								validDateFlag = CommonConstants.FALSE;
							}
						}else if(workingdays.contains(nextDate)){
							dateList.add(nextDate);
						}else if(i == 0){
							validDateFlag = CommonConstants.FALSE;
						}
					}
				}else{
					Calendar c= Calendar.getInstance();
					c.setTime(sdf.parse(fromdate));
					Date followDate = c.getTime();
					String nextDate = DateToString(followDate);
					
					if(!CommonConstants.getInvalidDays().contains(sdf.parse(fromdate).getDay()+1)){
						if(!holidays.contains(nextDate)){
							dateList.add(nextDate);
						}else if(workingdays.contains(nextDate)){
							dateList.add(nextDate);
						}else{
							validDateFlag = CommonConstants.FALSE;
						}
					}else if(workingdays.contains(nextDate)){
						dateList.add(nextDate);
					}else{
						validDateFlag = CommonConstants.FALSE;
					}
				}
				days = Double.valueOf(dateList.size());
			}else{
				days = daysDifference.doubleValue()+1;
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Calculate number of days Exception ", new VmsApplicationException(e.getMessage()));
		}
		result.add(days.toString());
		result.add(validDateFlag);
		return result;
	}

	/**
	 * @param date
	 * @return
	 */
	public static String DateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.YYYY_MM_DD);
		String hdate = null;
		try {
			hdate = sdf.format(date);
		} catch (Exception e) {
			VmsLogging.logError(null, "DateToString Exception ", new VmsApplicationException(e.getMessage()));
		}
		return hdate;
	}
	
	public LeaveRequestProcessVO revocationPendingAndReject(String employeeNumber, int employeeId, RequestParameterVO request) {
		
		LeaveRequestProcessVO leaveRequestProcessVO = new LeaveRequestProcessVO();
		try {
			String leaveIdLength[] = request.getLeaveRequestId().split(",");
			if(leaveIdLength.length <= 1) {
				if(request.getLeaveRequestId() != null && !request.getLeaveRequestId().isEmpty() && request.getFromDate() != null && !request.getFromDate().isEmpty() && request.getToDate() != null && !request.getToDate().isEmpty() && request.getNumberOfDays() != null && ! request.getNumberOfDays().isEmpty()) {
					List<ProcessLeaveRequestVO> outputdata = employeeHomeService.processLeaveRequest(employeeNumber, request.getLeaveRequestId(), employeeId, request.getLeaveTypeId(), request.getLeaveStatus(), request.getFromDate(), request.getToDate(), request.getNumberOfDays(), request.getContactNumber(), request.getLeaveReason(), request.getOdOptionId(), CommonConstants.SOURCE_OF_INPUT_LA);
					if(outputdata.size()>0 && outputdata.get(0).getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)){
						leaveRequestProcessVO.setStatus(CommonConstants.SUCCESS);
						if(request.getLeaveStatus().equals(CommonConstants.STATUS_PENDING_REVOCATION))
							leaveRequestProcessVO.setSuccessMessage("Your leave request has been revoked successfully");
						else if(request.getLeaveStatus().equals(CommonConstants.STATUS_REVOKED_APPROVED)) {
							leaveRequestProcessVO.setSuccessMessage("Leave Request(s) has been approved successfully");
						}
						else
							leaveRequestProcessVO.setSuccessMessage("Leave request have been rejected successfully");
						VmsLogging.logInfo(getClass(),"Revocation pending success- "+request.getLeaveRequestId());
						RevocationEmailVO getMailDetails = revocationService.getMailDetails(request.getLeaveRequestId());
						Map<String, Object> map = new HashMap<>();
						map.put("mailObject", getMailDetails);
						String subject = null;
						String emailTo = null;
						String emailTemplate = null;
						boolean rejectReason = false;
						
						if(getMailDetails.getLeaveStatus().equals(CommonConstants.STATUS_REVOKED_APPROVED) || getMailDetails.getLeaveStatus().equals(CommonConstants.STATUS_APPROVED)) {
							map.put("team",getMailDetails.getAdminDetails().size() > 0 ? CommonConstants.REVOCATION_FINANCE_ADMIN : CommonConstants.REVOCATION_MANAGER);
							VmsLogging.logInfo(getClass(), JSONUtil.toJson(getMailDetails));
							emailTo = getMailDetails.getEmployeeEmail();
							emailTemplate = "employeeTemplate.vm";
							if(getMailDetails.getLeaveStatus().equals(CommonConstants.STATUS_REVOKED_APPROVED)) {
								subject = CommonConstants.REVOCATION_APPROVED_SUBJECT;
								map.put("title", CommonConstants.REVOCATION_APPROVED_SUBJECT);
								map.put("status", "approved");
								map.put("revokeReason", rejectReason);
							}
							else {
								if(!getMailDetails.getRevocationAprRejReason().isEmpty() || getMailDetails.getRevocationAprRejReason() != null);
									rejectReason = true;
								subject = CommonConstants.REVOCATION_REJECTED_SUBJECT;
								map.put("title", CommonConstants.REVOCATION_REJECTED_SUBJECT);
								map.put("status","rejected");
								map.put("revokeReason", rejectReason);
							}
						}
						else{
							boolean checkForPastDate = dateTimeUtils.checkForPastDate(getMailDetails.getFromDate(), getMailDetails.getToDate());
							map.put("checkForPastDate", checkForPastDate);
							if(getMailDetails.getAdminDetails().size() > 0) {
								emailTemplate = "financeAdminTemplate.vm";
								subject = CommonConstants.REVOCATION_SUBJECT+" | "+getMailDetails.getEmployeeName();
							}
							else {
								emailTemplate = "managerTemplate.vm";
								subject = CommonConstants.REVOCATION_SUBJECT+" | "+getMailDetails.getEmployeeName()+" | "+request.getLeaveRequestId()+"-"+getMailDetails.getRMCode() + " | "+getMailDetails.getRMEmail();
								
							}
							List<PunchedHours> punchedHoursList = new ArrayList<>();							
								for(PunchedHours punchedHrs : getMailDetails.getPunchedHoursList()) {
									PunchedHours punchHrs = new PunchedHours();
									if(punchedHrs.getPunchDate() == null && !punchedHrs.getPunchDate().isEmpty()) {
										punchHrs.setPunchDate("false");
									}
									else {
										String date = dateTimeUtils.convertStringDateFormat(punchedHrs.getPunchDate());
										punchHrs.setPunchDate(date);
										if(punchedHrs.getPrdHrs() != null) {
											punchHrs.setPrdHrs(punchedHrs.getPrdHrs());
										}
										else {
											punchHrs.setPrdHrs("00:00");
										}
									}
									if(punchedHrs.getProdMins() != null && !punchedHrs.getProdMins().isEmpty())
										punchHrs.setProductionMins(Integer.valueOf(punchedHrs.getProdMins()));
									else
										punchHrs.setProductionMins(60);
									VmsLogging.logInfo(getClass(), JSONUtil.toJson(punchHrs));
									punchedHoursList.add(punchHrs);
								}
								map.put("punchedHoursList", punchedHoursList);					
						}
						String emailBody = emailTemplateService.getEmailTemplate(emailTemplate, map);						
						emailService.sendEmail(emailTo, subject, emailBody);						
					}else{
						leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
						leaveRequestProcessVO.setErrorCode("01");
						leaveRequestProcessVO.setErrorMessage("Request Submission Error");
						VmsLogging.logError(getClass(), "Revocation pending Failure", null);
					}				
				}else {
					leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
					leaveRequestProcessVO.setErrorCode("02");
					leaveRequestProcessVO.setErrorMessage("Invalid Data");
					return leaveRequestProcessVO;
				}
			}
			else {
				leaveRequestProcessVO = multipleRevocationApprove(employeeNumber,employeeId,request);
			}	
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Revocation pending Failure", e);
		}
		return leaveRequestProcessVO;
	}
	
	public LeaveRequestProcessVO multipleRevocationApprove(String employeeNumber, int employeeId, RequestParameterVO request) {
		LeaveRequestProcessVO leaveRequestProcessVO = new LeaveRequestProcessVO();
		List<RevocationEmailVO> emailList = new ArrayList();
		try {
			ArrayList<String> leaveRequestList = new ArrayList<>(Arrays.asList(request.getLeaveRequestId().split(",")));
			Iterator<String> it = leaveRequestList.iterator();
			while(it.hasNext()) {
				String leaveRequestId = it.next();
				List<LeaveRequestIdInfo> responseData = employeeHomeService.getLeaveRequestInfo("", leaveRequestId);
				responseData.get(0).setLeaveStatus(CommonConstants.STATUS_REVOKED_APPROVED);
				String fromDate = dateTimeUtils.convertDateFormat(responseData.get(0).getFromDate());
				String toDate = dateTimeUtils.convertDateFormat(responseData.get(0).getToDate());
				List<ProcessLeaveRequestVO> outputdata = employeeHomeService.processLeaveRequest(employeeNumber, leaveRequestId, employeeId, responseData.get(0).getLeaveTypeId(), responseData.get(0).getLeaveStatus(), fromDate, toDate, 
							request.getNumberOfDays(), responseData.get(0).getContactNumber(), responseData.get(0).getLeaveReason(), 0, CommonConstants.SOURCE_OF_INPUT_LA);
				if(outputdata.size()>0 && outputdata.get(0).getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					leaveRequestProcessVO.setStatus(CommonConstants.SUCCESS);
					leaveRequestProcessVO.setSuccessMessage("Leave Request(s) has been approved successfully");
				}
				else
					leaveRequestProcessVO.setStatus(CommonConstants.ERROR);
				
				RevocationEmailVO getMailDetails = revocationService.getMailDetails(leaveRequestId);
				emailList.add(getMailDetails);
			}
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Revocation Bulk Approve Failure", e);
		}
		finally{
			 new Thread() {
				    @Override
				    public void run() {
						for (RevocationEmailVO data : emailList) {
							sendBulkRevocationEmailToEmployee(data, data.getAdminDetails().size(),CommonConstants.SOURCE_OF_INPUT_LA);
						}
				    }		
				}.start();		
			}
		return leaveRequestProcessVO;
	}
	
	public void sendBulkRevocationEmailToEmployee(RevocationEmailVO data, int locationAdminSize, String sourceOfInput) {
		try {
			VmsLogging.logInfo(getClass(), "JSON Mail details "+JSONUtil.toJson(data));
			Map<String, Object> map = new HashMap<>();
			map.put("mailObject", data);
			String subject = null;
			String emailTemplate = null;
			String emailBody = null;
			boolean rejectReason = false;
			
			VmsLogging.logInfo(getClass(), "reject reason "+rejectReason);
			if(data.getLeaveStatus().equals(CommonConstants.STATUS_REVOKED_APPROVED) || data.getLeaveStatus().equals(CommonConstants.STATUS_REVOKED_REJECTED) || data.getLeaveStatus().equals(CommonConstants.STATUS_APPROVED) ) {
				map.put("team",locationAdminSize > 0 ? CommonConstants.REVOCATION_FINANCE_ADMIN : CommonConstants.REVOCATION_MANAGER);
				emailTemplate = "employeeTemplate.vm";
				map.put("adminSize", locationAdminSize);
				if(data.getLeaveStatus().equals(CommonConstants.STATUS_REVOKED_APPROVED)) {
					subject = CommonConstants.REVOCATION_APPROVED_SUBJECT;
					map.put("title", CommonConstants.REVOCATION_APPROVED_SUBJECT);
					map.put("status", "approved");
					map.put("revokeReason", rejectReason);
				}	
				else {
					if(sourceOfInput.equals(CommonConstants.SOURCE_OF_INPUT_MA)) {
						data.setRevocationAprRejReason("Rejected");
						rejectReason = true;
					}
					subject = CommonConstants.REVOCATION_REJECTED_SUBJECT;
					map.put("title", CommonConstants.REVOCATION_REJECTED_SUBJECT);
					map.put("status","rejected");
					map.put("revokeReason", rejectReason);
				}
				emailBody = emailTemplateService.getEmailTemplate(emailTemplate,map);						
				emailService.sendEmail(data.getEmployeeEmail(), subject, emailBody);
			}
			else {
				List<String> emailList = new ArrayList<>();
				for(LocationAdminDetails admin : data.getAdminDetails()) {
					emailList.add(admin.getLocAdminEmail());
				}
				emailTemplate = "financeAdminTemplate.vm";
				boolean checkForPastDate = dateTimeUtils.checkForPastDate(data.getFromDate(), data.getToDate());
				map.put("checkForPastDate", checkForPastDate);
				subject = subject = CommonConstants.REVOCATION_SUBJECT+" | "+data.getEmployeeName();
				List<PunchedHours> punchedHoursList = new ArrayList<>();
				VmsLogging.logInfo(getClass(), JSONUtil.toJson(data));
					for(PunchedHours punchedHrs : data.getPunchedHoursList()) {
						PunchedHours punchHrs = new PunchedHours();
							String date = dateTimeUtils.convertStringDateFormat(punchedHrs.getPunchDate());
							punchHrs.setPunchDate(date);
							if(punchedHrs.getPrdHrs() != null) {
								punchHrs.setPrdHrs(punchedHrs.getPrdHrs());
							}
							else {
								punchHrs.setPrdHrs("00:00");
							}
						if(punchedHrs.getProdMins() != null && !punchedHrs.getProdMins().isEmpty())
							punchHrs.setProductionMins(Integer.valueOf(punchedHrs.getProdMins()));
						else
							punchHrs.setProductionMins(60);
						VmsLogging.logInfo(getClass(), JSONUtil.toJson(punchHrs));
						punchedHoursList.add(punchHrs);
					map.put("punchedHoursList", punchedHoursList);
				}
				emailBody = emailTemplateService.getEmailTemplate(emailTemplate,map);						
				emailService.sendMultipleToEmail(emailList, subject, emailBody);
			}
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "bulk revocation mail error", new VmsApplicationException(e.getMessage()));
		}
	}
}
