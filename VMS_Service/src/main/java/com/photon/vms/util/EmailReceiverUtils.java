package com.photon.vms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import org.apache.logging.log4j.core.appender.rolling.action.CommonsCompressAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.controller.HomeController;
import com.photon.vms.service.EmployeeHomeService;
import com.photon.vms.service.ManagerReviewService;
import com.photon.vms.service.RevocationService;
import com.photon.vms.service.UtilService;
import com.photon.vms.vo.AutoApprovalVO;
import com.photon.vms.vo.FetchLeaveRequestInfoVO;
import com.photon.vms.vo.IdByEmailInfoVO;
import com.photon.vms.vo.LeaveRequestIdInfo;
import com.photon.vms.vo.ProcessLeaveRequestVO;
import com.photon.vms.vo.ReportManagerVO;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.RevocationEmailVO;

/**
 * @author karthigaiselvan_r
 *
 */
@Service
public class EmailReceiverUtils {
	
	@Autowired
	HomeController homeController;
	@Autowired
	EmployeeHomeService employeeHomeService;
	@Autowired
	RevocationService revocationService;
	@Autowired
	MailSendUtils mailSendUtils;
	@Autowired
	ManagerReviewService managerReviewService;
	@Autowired
	SecurityUtil securityUtil;
	@Autowired
	DateTimeUtils dateTimeUtils;
	@Autowired 
	private Environment env;
	@Autowired
	UtilService utilService;
	
	private final static String PROTOCOL = "imap";
	private final static String HOST = "imap.gmail.com";
	private final static String PORT = String.valueOf(993);
	private final static String ERROR_CONTENT = "[Error downloading content]";
	
	private Properties getServerProperties(String protocol, String host, String port) {
		Properties properties = new Properties();
		properties.put("mail.imap.host", host);
		properties.put("mail.imap.port", port);
		properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.imap.socketFactory.fallback", "false");
		properties.setProperty("mail.imap.socketFactory.port", port);
		return properties;
	}
	/**
	 * Downloads new messages and fetches details for unread message. "0 0/4 * * * ?"
	 * 
	 */
//	@Scheduled(cron="0 0/5 * * * ?")
	public void downloadEmails() {
		VmsLogging.logInfo(getClass(), "Read Mail box: downloadEmails");
		Properties properties = getServerProperties(PROTOCOL, HOST, PORT);
		Session session = Session.getDefaultInstance(properties);
		try {
			// Connects to the IMAP message store
			Store store = session.getStore(PROTOCOL);
			String password = env.getProperty("spring.mail.password");
			//password = securityUtil.decryptPassword(password);
			store.connect(env.getProperty("spring.mail.username"),password);

			// Opens the INBOX folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_WRITE);

			Message[] messages = folderInbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			if(messages.length>0)
				VmsLogging.logInfo(getClass(), "Unread mails- "+messages.length);
			
			for (int i = 0; i < messages.length; i++) {
				Message msg = messages[i];

				String contentType = msg.getContentType();
				VmsLogging.logInfo(getClass(),"Content Type : " + contentType+ ":");
				String messageContent = "";
				if (contentType.toLowerCase().contains("text/plain") || contentType.toLowerCase().contains("text/html")) {
					try {
						Object content = msg.getContent();
						if (content != null) {
							messageContent = content.toString();
						}
					} catch (Exception ex) {
						messageContent = ERROR_CONTENT;
						VmsLogging.logError(getClass(), "Error - ",new VmsApplicationException(ex.getMessage()));
					}
				} else if (contentType.toLowerCase().contains("multipart/mixed") || contentType.toLowerCase().contains("multipart/alternative")) {
					try {
						Object content = msg.getContent();
						MimeMultipart mimeMultipart = (MimeMultipart) content;
						if (mimeMultipart != null) {
							BodyPart bp = mimeMultipart.getBodyPart(0);
							messageContent = bp.getContent().toString();
						}
					} catch (Exception ex) {
						messageContent = ERROR_CONTENT;
						VmsLogging.logError(getClass(), "Error - ",new VmsApplicationException(ex.getMessage()));
					}
				} else if(contentType.toLowerCase().contains("multipart/related")){
					continue;
				}else {
					messageContent = ERROR_CONTENT;
					VmsLogging.logInfo(getClass(), "Unknown content type");
				}
				// print out details of each message
				Address[] fromAddress = msg.getFrom();
				String from = fromAddress[0].toString();
				if(from.indexOf('<') != -1)
					from = from.substring(from.indexOf('<')+1, from.indexOf('>'));
				String subject = msg.getSubject();
				String toList = parseAddresses(msg.getRecipients(RecipientType.TO));
				String ccList = parseAddresses(msg.getRecipients(RecipientType.CC));
				String sentDate = msg.getSentDate().toString();
				String leaveSubject = null;

				VmsLogging.logInfo(getClass(),"Message #" + i + ":");
				VmsLogging.logInfo(getClass(),"\t From: " + from);
				VmsLogging.logInfo(getClass(),"\t To: " + toList);
				VmsLogging.logInfo(getClass(),"\t CC: " + ccList);
				VmsLogging.logInfo(getClass(),"\t Subject: " + subject);
				VmsLogging.logInfo(getClass(),"\t Sent Date: " + sentDate);
				VmsLogging.logInfo(getClass(),"\t Message: " + messageContent);
				String[] contents = messageContent.split("\n");
				messageContent = contents[0];
				
				VmsLogging.logInfo(getClass(),"\t Latest thread: " + messageContent);
				
				String[] subArray = subject.split(" \\| ");
				leaveSubject = subArray[0];
				for(String it : subArray) {
					VmsLogging.logInfo(getClass(),it);
				}
				if(subArray.length >= 4) {
					try {

						// not approved, rejected, reject, 
						// not rejected, apprvd, granted, approve, aprvd, go ahead, goahead
						from = subArray[subArray.length - 1];
						String val = subArray[subArray.length - 2];
						val = val.replaceAll("\\|", "").trim();
						String splitData[] = val.split("-");
						if(splitData.length == 2){
							int leaverequestId = Integer.parseInt(splitData[0].trim());
							String employeeCode = splitData[1].trim();
							if (messageContent.toLowerCase().contains("approved") || messageContent.toLowerCase().contains("approve")) {
								String leavestatus = "Approved";
								String reason = "";
								 String status = leaveApproveReject(leaverequestId, from, leavestatus,leaveSubject,
								 employeeCode, reason); if(status.equals("true")){ msg.setFlag(Flag.SEEN,
								 true); }
							}else if (messageContent.toLowerCase().contains("rejected") || messageContent.toLowerCase().contains("reject")) {
								String leavestatus = "Rejected";
								String reason = "";
								if(messageContent.indexOf(",") > 1){
									reason = messageContent.substring(messageContent.indexOf(",")+1, messageContent.length()-1);
								}else{
									reason = "Rejected";
								}
								 String status = leaveApproveReject(leaverequestId, from, leavestatus,leaveSubject,
								 employeeCode, reason); if(status.equals("true")){ msg.setFlag(Flag.SEEN,
								 true); }
								 
							}else {
								msg.setFlag(Flag.SEEN, false);
								VmsLogging.logInfo(getClass(),"Not an approved / rejected email");
							}
						}else{
							msg.setFlag(Flag.SEEN, false);
							VmsLogging.logInfo(getClass(),"Leave not approved / rejected "+val+" "+from);
						}
					}catch (Exception e) {
						VmsLogging.logInfo(getClass(),"Invalid "+subArray[subArray.length - 1]);
					}
				}else {
					msg.setFlag(Flag.SEEN, true);
					VmsLogging.logInfo(getClass(),"Not an leave email");
				}	
			}
		}catch (MessagingException ex) {
			VmsLogging.logError(getClass(), "Error in reading mail - ",new VmsApplicationException(ex.getMessage()));
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Error in reading mail - ",new VmsApplicationException(e.getMessage()));
		}
	}
	private String leaveApproveReject(int leaverequestId, String from, String leaveStatus, String leaveSubject, String employeeCode, String reason) {
		try{
			ArrayList<String> pendingListString = new ArrayList<String>();
			Map<String, ArrayList<?>> pendingList = new HashMap<String, ArrayList<?>>();
			List<IdByEmailInfoVO> empData = employeeHomeService.getEmpidFromEmail(from);
			VmsLogging.logInfo(getClass(),"empdata : "+JSONUtil.toJson(empData));
			if(empData!=null && !empData.isEmpty()){
				Boolean flag  = false;
				Boolean revocationFlag = false;
				String managerId = empData.get(0).getEmployeeId();
				VmsLogging.logInfo(getClass(),"emp data : "+empData.get(0).getEmployeeCode());
				RequestParameterVO request = new RequestParameterVO();
				request.setEmployeeNumber(empData.get(0).getEmployeeCode());
				pendingList = managerReviewService.getPendingLeaveRequest(empData.get(0).getEmployeeCode(), "", "", "", "", "", "", "");
				pendingListString = (ArrayList<String>) pendingList.get("LeaveRequestList");
				VmsLogging.logInfo(getClass(),"after pending leaverequest"+JSONUtil.toJson(pendingListString)+" leavereq Id "+leaverequestId);
				RevocationEmailVO getMailDetails = new RevocationEmailVO();
				String employeeNumber="", fromdate=null, toDate = null, leaveName=null, numberOfDays="", contactNumber="", sourceOfInput=CommonConstants.SOURCE_OF_INPUT_MA;
				Double checkdays = 0.0;
				int odOptionId = 0;
				String subjectAarray[] = leaveSubject.split(":");
				
				if(subjectAarray[1].trim().equals(CommonConstants.REVOCATION_SUBJECT)) {
					revocationFlag = true;
					if(leaveStatus.equals(CommonConstants.STATUS_APPROVED))	
						leaveStatus = CommonConstants.STATUS_REVOKED_APPROVED;
					else
						leaveStatus = CommonConstants.STATUS_REVOKED_REJECTED;
					 getMailDetails = revocationService.getMailDetails(String.valueOf(leaverequestId));
					 fromdate = dateTimeUtils.convertReverseDateString(getMailDetails.getFromDate());
					 toDate = dateTimeUtils.convertReverseDateString(getMailDetails.getToDate());
					 List<ProcessLeaveRequestVO> outputdata = employeeHomeService.processLeaveRequest(employeeNumber, String.valueOf(leaverequestId), Integer.parseInt(managerId), getMailDetails.getLeaveTypeId(), leaveStatus, getMailDetails.getFromDate(), getMailDetails.getToDate(),
							 getMailDetails.getNoOfDays(), getMailDetails.getContactNumber(), getMailDetails.getRevokedReason(), odOptionId, CommonConstants.SOURCE_OF_INPUT_MA);
						if((!outputdata.isEmpty()) &&   outputdata.get(0).getStatus().equalsIgnoreCase("Success")){
							flag = true;
							checkdays = checkdays+Double.parseDouble(getMailDetails.getNoOfDays());
						}
					
				}
				else {
					List<FetchLeaveRequestInfoVO> result= employeeHomeService.getChildLeaveRequestId(leaverequestId, employeeCode);
					VmsLogging.logInfo(getClass(),"after child leaverequestId"+result.size()+" zarrat "+JSONUtil.toJson(result));
					Iterator it = result.iterator();										
					while(it.hasNext()){
						FetchLeaveRequestInfoVO info = (FetchLeaveRequestInfoVO) it.next();
						fromdate = fromdate==null?info.getFromDate().toString():fromdate;
						toDate = info.getToDate().toString();
						leaveName = String.valueOf(info.getLeaveTypeName());
						VmsLogging.logInfo(getClass(),"leaverequest Info "+fromdate+" todate : "+toDate+" leavename "+leaveName);
											
						if(pendingListString.contains(info.getLeaveRequestId())){
							VmsLogging.logInfo(getClass(), "Mail inline - employeeNumber: "+employeeNumber+", LeaverequestId: "+info.getLeaveRequestId()+", Managerid: "+Integer.parseInt(managerId)+", Leavetype Id: "+info.getLeaveTypeId()+", leaveStatus: "+leaveStatus);
							List<ProcessLeaveRequestVO> outputdata = employeeHomeService.processLeaveRequest(employeeNumber, info.getLeaveRequestId(), Integer.parseInt(managerId), info.getLeaveTypeId(), leaveStatus, fromdate, toDate, numberOfDays, contactNumber, reason, odOptionId, CommonConstants.SOURCE_OF_INPUT_MA);
							if((!outputdata.isEmpty()) &&   outputdata.get(0).getStatus().equalsIgnoreCase("Success")){
								flag = true;
								checkdays = checkdays+info.getNumberOfDays();
							}
						}
					}
				}
				if(flag){
					VmsLogging.logInfo(getClass(),"flag "+flag);
					Map<String, ArrayList<?>> managerinfo = employeeHomeService.getLoggedUserInfo(employeeCode);
					ReportManagerVO reportManagerVO = new ReportManagerVO();				
					reportManagerVO = (com.photon.vms.vo.ReportManagerVO) managerinfo.get("EmpReportManagerInfo").get(0);
					reportManagerVO.setReportingManagerName(empData.get(0).getEmployeeName());
					String managermail[] = reportManagerVO.getManagerEmailId().split(",");
					if(managermail.length > 1)
						reportManagerVO.setMailCC(reportManagerVO.getManagerEmailId());
					getMailDetails = revocationService.getMailDetails(String.valueOf(leaverequestId));
					if(!revocationFlag)
						mailSendUtils.sendMail(leaveStatus, null, reportManagerVO, null, null, 0, fromdate, toDate, numberOfDays, contactNumber, reason, checkdays, leaveName);
					else
						homeController.sendBulkRevocationEmailToEmployee(getMailDetails, getMailDetails.getAdminDetails().size(),CommonConstants.SOURCE_OF_INPUT_MA);
					return "true";	
				}
			}
			return "false";
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Mail inline error - ", new VmsApplicationException(e.getMessage()));
			return "false";
		}	
	}
	/**
	 * Returns a list of addresses in String format separated by comma
	 */
	private String parseAddresses(Address[] address) {
		String listAddress = "";

		if (address != null) {
			for (int i = 0; i < address.length; i++) {
				listAddress += address[i].toString() + ", ";
			}
		}
		if (listAddress.length() > 1) {
			listAddress = listAddress.substring(0, listAddress.length() - 2);
		}
		return listAddress;
	}
	
//	@Scheduled(cron="0 0/5 * * * ?")
	public void autoApproval() {
		try {
			List<AutoApprovalVO> leaveList = utilService.getAutoApprovalLeaves();
			if (leaveList != null && !leaveList.isEmpty()) {
				for (AutoApprovalVO leave : leaveList) {
					List<ProcessLeaveRequestVO> result = employeeHomeService.processLeaveRequest(leave.getEmployeeNumber(), leave.getLeaveReqId(), leave.getReportingMgrId(),
							leave.getLeaveTypeId(), "Approved", leave.getFromDate(), leave.getToDate(),
							leave.getNoOfDays(), leave.getContactNumber(), leave.getReason(), leave.getOdType(), "AA");
					if (result.get(0).getStatus().equals("Success")) {
						Map<String, ArrayList<?>> managerinfo = employeeHomeService.getLoggedUserInfo(leave.getEmployeeNumber());
						ReportManagerVO reportManagerVO = new ReportManagerVO();				
						reportManagerVO = (com.photon.vms.vo.ReportManagerVO) managerinfo.get("EmpReportManagerInfo").get(0);
						String managermail[] = reportManagerVO.getManagerEmailId().split(",");
						if(managermail.length > 1)
							reportManagerVO.setMailCC(reportManagerVO.getManagerEmailId());
						double checkDays = Double.valueOf(leave.getNoOfDays()); 
						mailSendUtils.sendMail("Approved", null, reportManagerVO, null, null, 0, leave.getFromDate(), leave.getToDate(),
								leave.getNoOfDays(), leave.getContactNumber(), leave.getReason(), checkDays, leave.getLeaveTypeName());
					}
				}
			} else {
				VmsLogging.logInfo(getClass(), "Auto Approval Leave List Empty");
			}
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Auto Approval Mail error - ", new VmsApplicationException(e.getMessage()));
		}
	}
}
