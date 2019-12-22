package com.photon.vms.util;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.service.AdminService;
import com.photon.vms.vo.LeaveReminderInfo;
import com.photon.vms.vo.RevocationEmailVO;

@Service
public class LeaveRequestReminderTask{
	
	@Autowired
	private transient JavaMailSender mailSender;
	@Autowired
	AdminService adminService;	
	@Autowired
	DateTimeUtils dateTimeUtils;
	
//	@Scheduled(cron="0 30 23 * * ?")
	public void execute() throws Exception{
		try{
			List<LeaveReminderInfo> leaveDetailList = new ArrayList<LeaveReminderInfo>();
			leaveDetailList = adminService.getReminderLeaveList();
			VmsLogging.logInfo(getClass(), "** Daily approval reminder Mail Start **");
			
		    Map<String, List<LeaveReminderInfo>> resultMap = new HashMap<String, List<LeaveReminderInfo>>();
			if(leaveDetailList != null && leaveDetailList.size() > 0){
				for(LeaveReminderInfo info : leaveDetailList){
					if(resultMap.containsKey(info.getManagerEmail())){
						List<LeaveReminderInfo> list = resultMap.get(info.getManagerEmail());
						list.add(info);
						resultMap.put(info.getManagerEmail(),list);
					}else {
						List<LeaveReminderInfo> list = new ArrayList<LeaveReminderInfo>();
						list.add(info);
						resultMap.put(info.getManagerEmail(),list);
					}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			if(resultMap != null && resultMap.size() > 0){
				Iterator itr = (Iterator) resultMap.entrySet().iterator();
				while(itr.hasNext()){
					Entry<String, List<LeaveReminderInfo>> pairs = (Entry<String, List<LeaveReminderInfo>>) itr.next();
					List<LeaveReminderInfo> leaveList = pairs.getValue();
					String toEmail = pairs.getKey();
					MimeMessage message = mailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message, true);
					helper.setFrom(CommonConstants.FROM_MAIL);
					String subject = "Reminder for approval";
					helper.setTo(toEmail);
					helper.setSubject(subject);
					String mailBody = CommonConstants.LEAVE_REMINDER_MAIL_BODY;

					String thStyle = " style='font-weight:600;font-size: 12px;border: 1px solid #5f6e74;color:#5f6e74;padding: 3px 5px; border-collapse: collapse;'";
					String tdStyle = " style='padding: 9px 5px; border: 1px solid #5f6e74; border-collapse: collapse;'";
					String trStyle = "  style='font-weight:500;font-size: 11px;color:#5f6e74;text-align: center;'";
					
					String dynamicContent = "<table class='table-responsive' style='font-family:\"Open Sans\", sans-serif;border: 1px solid #5f6e74;color:#5f6e74; border-collapse: collapse; margin: 23px auto;'> <tr> <th rowspan='2'>Applied on</th> <th rowspan='2' "+thStyle+">Emp Id</th> <th  "+thStyle+" rowspan='2'>Employee Name</th> <th colspan='2' "+thStyle+">Leave Applied</th> <th rowspan='2' "+thStyle+">Days</th> <th rowspan='2' "+thStyle+">Type</th> <th rowspan='2' "+thStyle+">Reason </th> </tr> <tr> <th "+thStyle+">From </th> <th "+thStyle+">To</th> </tr>";
					
					for(LeaveReminderInfo info : leaveList){
						String leaveReason = "";
						if(info.getLeaveType().equalsIgnoreCase("OD")){
							if(info.getOdOptionCode().equalsIgnoreCase("OTH")){
								leaveReason = "Others - "+info.getReason();
							}else{
								leaveReason = info.getOdOptionName();
							}
						}else{
							leaveReason = info.getReason();
						}
						String content = "<tr "+trStyle+">";
						content += "<td "+tdStyle+">"+ sdf.format(info.getAppliedOn()) +"</td><td "+tdStyle+">"+ info.getEmployeeCode()+"</td><td "+tdStyle+">"+info.getEmployeeName()+"</td><td "+tdStyle+">"+sdf.format(info.getFromDate())+"</td><td "+tdStyle+">"+sdf.format(info.getToDate())+"</td><td "+tdStyle+">"+info.getDays()+"</td><td "+tdStyle+">"+info.getLeaveType()+"</td><td width='25%' "+tdStyle+">"+leaveReason+"</td>";
						content+="</tr>";
						dynamicContent+=content;
					}
					dynamicContent+="</table>";
					mailBody = mailBody.replace("$[DynamicText]", dynamicContent);
					helper.setText(addHeaderAndFooter(mailBody), true);
					mailSender.send(message);
				}
			}
			VmsLogging.logInfo(getClass(), "** Daily approval reminder Mail End - Result map **"+resultMap.size());
		}catch (MessagingException e) {
			VmsLogging.logError(LeaveRequestReminderTask.class, e.getMessage(), e);
		} catch (SQLException e) {
			VmsLogging.logError(LeaveRequestReminderTask.class, e.getMessage(), e);
		}
	}
	private String addHeaderAndFooter(String html) {
		StringBuffer sb = new StringBuffer();
		sb.append(CommonConstants.MAIL_HEADER);
		sb.append(html);
		sb.append(CommonConstants.MAIL_FOOTER);
		return sb.toString();
	}
	
//	@Scheduled(cron="0 0/30 * * * ?")
	public void executeFinanceAdminRemainder() {
		try {
			List<RevocationEmailVO> revocation = adminService.getRevocationListForAdmin();
			HashMap<String, List<RevocationEmailVO>> hashmap = new HashMap<>();
			for(RevocationEmailVO email : revocation) {
			if(!hashmap.containsKey(email.getFinanceEmployeeEmail())) {
				List<RevocationEmailVO> list = new ArrayList<RevocationEmailVO>();
				list.add(email);
				hashmap.put(email.getFinanceEmployeeEmail(), list);
			}
			else {
				hashmap.get(email.getFinanceEmployeeEmail()).add(email);
			}
			}
			
			for (Map.Entry<String, List<RevocationEmailVO>> entry : hashmap.entrySet()) {
				String toEmail = entry.getKey();
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setFrom(CommonConstants.FROM_MAIL);
				String subject = "Reminder for approval - "+toEmail;
				helper.setTo("josephin.venisha@photoninfotech.net@photoninfotech.net");
				helper.setSubject(subject);
				String mailBody = CommonConstants.LEAVE_REMINDER_MAIL_BODY;
				String thStyle = " style='font-weight:600;font-size: 12px;border: 1px solid #5f6e74;color:#5f6e74;padding: 3px 5px; border-collapse: collapse;'";
				String tdStyle = " style='padding: 9px 5px; border: 1px solid #5f6e74; border-collapse: collapse;'";
				String trStyle = "  style='font-weight:500;font-size: 11px;color:#5f6e74;text-align: center;'";
				
				String dynamicContent = "<table class='table-responsive' style='font-family:\"Open Sans\", sans-serif;border: 1px solid #5f6e74;color:#5f6e74; border-collapse: collapse; margin: 23px auto;'> <tr> <th rowspan='2'>Revoked on</th> <th rowspan='2' "+thStyle+">Emp Id</th> <th  "+thStyle+" rowspan='2'>Employee Name</th> <th colspan='2' "+thStyle+">Leave Applied</th> <th rowspan='2' "+thStyle+">Days</th> <th rowspan='2' "+thStyle+">Type</th> <th rowspan='2' "+thStyle+">Revocation Reason </th> </tr> <tr> <th "+thStyle+">From </th> <th "+thStyle+">To</th> </tr>";
				
				Set<RevocationEmailVO> set = entry.getValue().stream().collect(Collectors.toSet());
				for(RevocationEmailVO revokeEmail : set) {
					String content = "<tr "+trStyle+">";
					content += "<td "+tdStyle+">"+ dateTimeUtils.convertStringFormatRemainder(revokeEmail.getRevokedDate()) +"</td><td "+tdStyle+">"+ revokeEmail.getEmployeeCode()+"</td><td "+tdStyle+">"+revokeEmail.getEmployeeName()+"</td><td "+tdStyle+">"+dateTimeUtils.convertStringFormatRemainder(revokeEmail.getFromDate())+"</td><td "+tdStyle+">"+dateTimeUtils.convertStringFormatRemainder(revokeEmail.getToDate())+"</td><td "+tdStyle+">"+revokeEmail.getNoOfDays()+"</td><td "+tdStyle+">"+revokeEmail.getLeaveCode()+"</td><td width='25%' "+tdStyle+">"+revokeEmail.getRevokedReason()+"</td>";
					content+="</tr>";
					dynamicContent+=content;
				}
				dynamicContent+="</table>";
				mailBody = mailBody.replace("$[DynamicText]", dynamicContent);
				helper.setText(addHeaderAndFooter(mailBody), true);
				mailSender.send(message);
				
				
			}
			 
		} catch (Exception e) {
			VmsLogging.logError(LeaveRequestReminderTask.class, e.getMessage(), e);
		}
	}
}
