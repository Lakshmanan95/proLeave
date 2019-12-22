package com.photon.vms.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.vo.ReportManagerVO;

@Service
public class MailSendUtils {
	@Autowired
	JavaMailSender javaMailSender;
	@Autowired 
	private Environment env;
	
	public void sendMail(String leaveStatus, String leaveTypecode, ReportManagerVO reportManagerVO, String employeeNumber, String leaveRequestId, int employeeId, String fromdate, String toDate, String numberOfDays, String contactNumber, String leaveReason, Double checkdays, String leaveName) throws Exception{
		try{
			if(reportManagerVO != null && reportManagerVO.getManagerEmailId()!=null){
				String subject = "";
				String mailBody = "";
				String managerEmail[] = reportManagerVO.getManagerEmailId().split(",");
				String employeeName = reportManagerVO.getEmployeeName();
				String empCode = reportManagerVO.getEmployeeNumber();
				String managerName = reportManagerVO.getReportingManagerName();
				String employeeMail = reportManagerVO.getEmailId();				
				String[] ccMail = (reportManagerVO.getMailCC() != null?reportManagerVO.getMailCC().split(","):null);
				int loopFlag = 0;
				for(int i=0; i<managerEmail.length; i++){
					String[] email = null;
									
					if(leaveStatus.equalsIgnoreCase(CommonConstants.STATUS_PENDING)){
						subject = "Requisition of Leave | "+reportManagerVO.getEmployeeName() +" | "+leaveRequestId+" - "+empCode+" | "+managerEmail[i];
						email= managerEmail[i].split(",");
						mailBody = CommonConstants.SUBMIT_LEAVE_MAIL_BODY;
					}else if(leaveStatus.equalsIgnoreCase(CommonConstants.STATUS_APPROVED)){
						if(leaveTypecode!= null && leaveTypecode.equals("OD")){
							subject = CommonConstants.APPROVAL_OD_MAIL_SUBJECT;
							mailBody = CommonConstants.APPROVAL_OD_MAIL_BODY;
						}else{
							subject = CommonConstants.APPROVAL_LEAVE_MAIL_SUBJECT;
							mailBody = CommonConstants.APPROVAL_LEAVE_MAIL_BODY;
						}
						email = employeeMail.split(",");
						loopFlag = 1;
					}else if(leaveStatus.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)){
						if(leaveTypecode!= null && leaveTypecode.equals("OD")){
							subject = CommonConstants.REJECTION_OD_MAIL_SUBJECT;
							mailBody = CommonConstants.REJECTION_OD_MAIL_BODY;
						}else{
							subject = CommonConstants.REJECTION_LEAVE_MAIL_SUBJECT;
							mailBody = CommonConstants.REJECTION_LEAVE_MAIL_BODY;
						}
						email = employeeMail.split(",");
						loopFlag = 1;
					}
					
	
					MimeMessage message = javaMailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message, true);
					helper.setFrom(CommonConstants.FROM_MAIL);
					email = ("selvaganesh.mathiyaru@photoninfotech.net,lakshmanan.j@photoninfotech.net,gajalakshmi.giriraj@photoninfotech.net,josephin.venisha@photoninfotech.net").split(",");
					helper.setTo(email);
					//helper.setBcc(env.getProperty("spring.mail.sendbcc").split(","));
					helper.setSubject(subject);
					
					SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat toFormat = new SimpleDateFormat("dd-MMM-yyyy");
					Date fromdt = fromFormat.parse(fromdate);
					Date todt = fromFormat.parse(toDate);
					
					mailBody = mailBody.replace("$[managerName]", managerName);
					mailBody = mailBody.replace("$[employeeName]", employeeName);
					mailBody = mailBody.replace("$[EmployeeCode]", empCode);
					mailBody = mailBody.replace("$[fromDate]",toFormat.format(fromdt));
					mailBody = mailBody.replace("$[toDate]", toFormat.format(todt));
					mailBody = mailBody.replace("$[type]",leaveName);
					mailBody = mailBody.replace("$[days]", checkdays.toString());
					if(leaveReason != null){
						mailBody = mailBody.replace("$[leaveReason]", leaveReason);
					}else{
						mailBody = mailBody.replace("$[leaveReason]", "");
					}
					helper.setText(addHeaderAndFooter(mailBody), true);
					if(ccMail != null){
						helper.setCc("lakshmanan.j@photoninfotech.net");
					}
//					if(env.getProperty("mail.send").equals("false")){
						javaMailSender.send(message);
//					}
					VmsLogging.logInfo(getClass(), " Mail Sent "+subject);
					if(loopFlag != 0)
						break;
				}
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Send mail Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	private String addHeaderAndFooter(String html) {
		StringBuffer sb = new StringBuffer();
		sb.append(CommonConstants.MAIL_HEADER);
		sb.append(html);
		sb.append(CommonConstants.MAIL_FOOTER);
		return sb.toString();
	}

}
