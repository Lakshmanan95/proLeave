package com.photon.vms.constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CommonConstants {
	
	static ResourceBundle rb = ResourceBundle.getBundle("application");
	private static final String SERVICE_URL = rb.getString("service.url");
	public static final String LEGEND_COLOR_1 = "#DB9696";
	public static final String LEGEND_COLOR_2 = "#F5C66E";
	public static final String SOURCE_OF_INPUT_LA = "LA"; //LA for LMS & BA for chat bot workplace
	public static final String SOURCE_OF_INPUT_MA = "MA";
	public static final String SOURCE_OF_INPUT_PB = "PB";

	public static final String CALL_DATASOURCE = "{call ";
	
	public static final String STATUS_PENDING = "Pending";
	public static final String STATUS_APPROVED = "Approved";
	public static final String STATUS_CANCELLED = "Cancelled";
	public static final String STATUS_REJECTED = "Rejected";
	public static final String STATUS_PENDING_REVOCATION = "Pending Revocation";
	public static final String STATUS_PENDING_WITHDRAWAL= "Pending Withdrawal";
	public static final String STATUS_REVOKED_APPROVED = "Revocation Approved";
	public static final String STATUS_REVOKED_REJECTED = "Revocation Rejected";
	public static final String APPROVE_REJECT_OWN_LEAVE_ERROR_MESSAGE = "You cannot approve/reject your own leaves.";
	public static final String LEAVE_ALREADY_APPLIED_MESSAGE = "Leave has been already applied for the following dates ";
	
	public static final String STATUS_PENDING_ID = "1";
	public static final String STATUS_APPROVED_ID = "2";
	public static final String STATUS_PENDING_REVOCATION_ID = "3";
	
	private static final ArrayList<String> VALID_LEAVE_STATUS = new ArrayList();
	private static final ArrayList<String> VALID_LEAVE_STATUS_ID = new ArrayList();
	private static final List<String> NON_BLOCK_LEAVE_TYPES = new ArrayList();
	private static final List<Integer> INVALID_DAYS = new ArrayList();
	private static final Map<String, String> LEGEND_COLOR = new HashMap();

	public static final String RECEPIENT ="recepient";

	public static final String REQUEST_ID = "requestId";

	public static final String STATUS = "status";

	public static final String MESSAGE = "message";

	public static final String FROM_MAIL = "leave@photoninfotech.net";
	
	public static final String MAIL_STYLE = "<style>p, table{font-family: Arial,sans-serif;  color:#222;}</style>";
	
	public static final String MAIL_HEADER = "<html><head> <meta http-equiv='Content-Type' content='text/html; charset=windows-1252'> <meta name='format-detection' content='telephone=no'> <meta name='viewport' content='width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=no;'> <meta http-equiv='X-UA-Compatible' content='IE=9; IE=8; IE=7; IE=EDGE' /><meta name='viewport' content='width=device-width, initial-scale=1'> <link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet'> </head> <body cz-shortcut-listen='true' class='color:#313332;'> <div class='gmail_quote' style='width:100%;margin: 0 auto;'><u></u> <div style='margin:0 auto;padding:0; width:730px;'>";
	
	public static final String MAIL_FOOTER = "<table border='0' cellpadding='0' cellspacing='0' style='color:#5f6e74;' width='100%'> <tbody> <tr> <td align='center'> <table  width='100%' align='center' border='0' cellpadding='0' cellspacing='0' style='padding: 10px;'> <tbody> <tr> <td align='center'> <span  style='color:#5f6e74;font-family:\"Open Sans\", sans-serif;'>Regards,</span> </td> </tr> <tr> <td align='center' > <span style='color:#000;font-family:\"Open Sans\", sans-serif;font-weight:600;padding: 7px;'>Vacation Management System</span> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </div> </div> </body> </html>";
	
	public static final String APPROVAL_LEAVE_MAIL_SUBJECT = "Approval of Leave";

	public static final String APPROVAL_OD_MAIL_SUBJECT = "Approval of OD";
		
	public static final String REJECTION_LEAVE_MAIL_SUBJECT = "Rejection of Leave";

	public static final String REJECTION_OD_MAIL_SUBJECT = "Rejection of OD Request";
	
	public static final String MAIL_TEMPLATE_BACKGROUND = "https://pulse.photoninfotech.com/pulse/sites/default/files/vms_mail_template_bg.png";
	
	public static final String MAIL_TEMPLATE_LOGO = "https://pulse.photoninfotech.com/pulse/sites/default/files/vms_mail_logo.png";
		
	public static final String SUBMIT_LEAVE_MAIL_BODY = "<table border='0' cellpadding='0' cellspacing='0' width='100%'> <tbody> <tr> <td align='center' style='background-image:url("+MAIL_TEMPLATE_BACKGROUND+");background-repeat: no-repeat;'> <a href='https://photon.okta.com' target='_blank'> <img alt='vms' border='0' src='"+MAIL_TEMPLATE_LOGO+"' style='padding: 20px 0px;'> </a> <table align='center' border='0' cellpadding='0' cellspacing='0' style='width:600px; background-color: rgb(255, 255, 255);box-shadow: inset 0px 0px 3px 0px rgba(0, 0, 0, 0.45);border: 1px solid #ccc; border-radius: 5px;'> <tbody> <tr> <td style='font-size:0'>&nbsp;</td> <td style='border-radius:6px' width='645'> <table bgcolor='#ffffff' border='0' cellpadding='0' cellspacing='0' width='100%'> <tbody> <tr> <td align='left' style='color:#313332;line-height:46px;padding: 4% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-weight:400;font-size: 20px;font-family:\"Open Sans\", sans-serif'>Requisition of Leave</span> </td> </tr> <tr><td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size: 16px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>This is to intimate that <b>$[employeeName]</b> has applied for</span> </td> </tr> <tr> <td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size:16px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>leave as per the following details</span> </td> </tr> </tbody> </table> <table width='100%' style='padding: 10px 50px 30px 140px;font-family: \"Open Sans\", sans-serif;'> <tbody> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Name</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[employeeName]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Employee ID</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[EmployeeCode]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Leave Type</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[type]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>From Date</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[fromDate]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>To Date</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[toDate]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>No of Days</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[days]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Reason</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[leaveReason]</span> </td> </tr> </tbody> </table> <table bgcolor='#ffffff' border='0' cellpadding='0' cellspacing='0' width='100%' style='padding: 0% 0% 6% 0%;'> <tbody> <tr> <td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size: 15px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>Kindly approve the leave request to avoid multiple reminder mails.</span> </td> </tr> <tr> <td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size: 15px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>You can reply inline to this mail with <b>Approved</b> &nbsp;(To approve this request) or <br> <b>Rejected,&lt;Reason&gt;</b> (To reject this request).</span> </td> </tr> <tr> <td align='left' style='color:#5f6e74;padding: 2% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size:15px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>You can view the leave request by clicking on the below link</span><br> <a href='"+SERVICE_URL+ "#/review' style='color:#e68e21;font-size: 14px;font-family:\"Open Sans\", sans-serif;'>"+SERVICE_URL+"#/review</a> </td> </tr> </tbody> </table> </td> <td style='font-size:0'>&nbsp;</td> </tr> </tbody> </table> </td> </tr> </tbody> </table>";

	public static final String REVOKE_LEAVE_MAIL_BODY = "<p>Hi $[managerName],</p><p>This is to intimate that one of your reportee $[firstName]\t 	 $[lastName] has applied for withdrawal of the leave request as per the following details - </p> <table ><tr><tr ><td >EMP ID</td><td>$[EmployeeCode]</td><tr ><td >Leave Type:</td><td>$[type]</td><tr ><td >No. of Days:</td><td>$[days]</td><tr ><td >From Date:</td><td>$[fromDate]</td><tr ><td >To Date:</td><td>$[toDate]</td></table><p>Click <a href='"+SERVICE_URL+ "/'>"+SERVICE_URL+"</a> to enter LMS</p><p>Kindly approve the revocation request to avoid multiple reminder mails.</p><p> Thanks in Advance.</p>	";

	public static final String APPROVAL_LEAVE_MAIL_BODY = "<table border='0' cellpadding='0' cellspacing='0' width='100%'> <tbody> <tr> <td align='center' style='background-image:url("+MAIL_TEMPLATE_BACKGROUND+");background-repeat: no-repeat;'> <a href='https://photon.okta.com' target='_blank'> <img alt='vms' border='0' src='"+MAIL_TEMPLATE_LOGO+"' style='padding: 20px 0px;'> </a> <table align='center' border='0' cellpadding='0' cellspacing='0' style='width:600px; background-color: rgb(255, 255, 255);box-shadow: inset 0px 0px 3px 0px rgba(0, 0, 0, 0.45);border: 1px solid #ccc; border-radius: 5px;'> <tbody> <tr> <td style='font-size:0'>&nbsp;</td> <td style='border-radius:6px' width='645'> <table bgcolor='#ffffff' border='0' cellpadding='0' cellspacing='0' width='100%'> <tbody> <tr> <td align='left' style='color:#313332;line-height:46px;padding: 4% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-weight:400;font-size: 20px;font-family:\"Open Sans\", sans-serif'>Approval of Leave</span> </td> </tr> <tr> <td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size: 16px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>This is to intimate that your manager <b>$[managerName]</b> has approved</span> </td> </tr> <tr> <td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size:16px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>your leave request as per the following details</span> </td> </tr> </tbody> </table> <table width='100%' style='padding: 10px 50px 30px 140px;font-family: \"Open Sans\", sans-serif;'> <tbody> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Name</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[employeeName]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Employee ID</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[EmployeeCode]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Leave Type</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[type]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>From Date</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[fromDate]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>To Date</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[toDate]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>No of Days</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[days]</span> </td> </tr> </tbody> </table> <table bgcolor='#ffffff' border='0' cellpadding='0' cellspacing='0' width='100%' style='padding: 0% 0% 6% 0%;'> <tbody> <tr> <td align='left' style='color:#5f6e74;padding: 2% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size:15px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>You can view the leave request by clicking on the below link</span><br> <a href='"+SERVICE_URL+ "' style='color:#e68e21;font-size: 14px;font-family:\"Open Sans\", sans-serif;'>"+SERVICE_URL+ "</a> </td> </tr> </tbody> </table> </td> <td style='font-size:0'>&nbsp;</td> </tr> </tbody> </table> </td> </tr> </tbody> </table>";

	public static final String APPROVAL_OD_MAIL_BODY = "<table border='0' cellpadding='0' cellspacing='0' width='100%'> <tbody> <tr> <td align='center' style='background-image:url("+MAIL_TEMPLATE_BACKGROUND+");background-repeat: no-repeat;'> <a href='https://photon.okta.com' target='_blank'> <img alt='vms' border='0' src='"+MAIL_TEMPLATE_LOGO+"' style='padding: 20px 0px;'> </a> <table align='center' border='0' cellpadding='0' cellspacing='0' style='width:600px; background-color: rgb(255, 255, 255);box-shadow: inset 0px 0px 3px 0px rgba(0, 0, 0, 0.45);border: 1px solid #ccc; border-radius: 5px;'> <tbody> <tr> <td style='font-size:0'>&nbsp;</td> <td style='border-radius:6px' width='645'> <table bgcolor='#ffffff' border='0' cellpadding='0' cellspacing='0' width='100%'> <tbody> <tr> <td align='left' style='color:#313332;line-height:46px;padding: 4% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-weight:400;font-size: 20px;font-family:\"Open Sans\", sans-serif'>Approval of Leave</span> </td> </tr> <tr> <td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size: 16px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>This is to intimate that your manager <b>$[managerName]</b> has approved</span> </td> </tr> <tr> <td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size:16px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>your OD leave request as per the following details</span> </td> </tr> </tbody> </table> <table width='100%' style='padding: 10px 50px 30px 140px;font-family: \"Open Sans\", sans-serif;'> <tbody> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Name</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[employeeName]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Employee ID</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[EmployeeCode]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Leave Type</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[type]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>From Date</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[fromDate]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>To Date</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[toDate]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>No of Days</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[days]</span> </td> </tr> </tbody> </table> <table bgcolor='#ffffff' border='0' cellpadding='0' cellspacing='0' width='100%' style='padding: 0% 0% 6% 0%;'> <tbody> <tr> <td align='left' style='color:#5f6e74;padding: 2% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size:15px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>You can view the leave request by clicking on the below link</span><br> <a href='"+SERVICE_URL+ "' style='color:#e68e21;font-size: 14px;font-family:\"Open Sans\", sans-serif;'>"+SERVICE_URL+ "</a> </td> </tr> </tbody> </table> </td> <td style='font-size:0'>&nbsp;</td> </tr> </tbody> </table> </td> </tr> </tbody> </table>";

	public static final String REVOKE_APPROVE_LEAVE_MAIL_BODY = "<p>Hi $[firstName]\t 	 $[lastName],</p><p>This is to intimate that your reporting manager $[managerName] ($[managerEmpCode]) has approved your withdrawal approval request as per the following details - </p> <table ><tr ><td >Leave Type:</td><td>$[type]</td><tr ><td >No. of Days:</td><td>$[days]</td><tr ><td >From Date:</td><td>$[fromDate]</td><tr ><td >To Date:</td><td>$[toDate]</td></tr><tr><td >Your leave balance has been credited with $[days] of $[type]</td></tr></table><p>Click <a href='"+SERVICE_URL+ "/'>"+SERVICE_URL+"</a> to enter LMS</p>";

	public static final String REJECTION_OD_MAIL_BODY = "<table border='0' cellpadding='0' cellspacing='0' width='100%'> <tbody> <tr> <td align='center' style='background-image:url("+MAIL_TEMPLATE_BACKGROUND+");background-repeat: no-repeat;'> <a href='https://photon.okta.com' target='_blank'> <img alt='vms' border='0' src='"+MAIL_TEMPLATE_LOGO+"' style='padding: 20px 0px;'> </a> <table align='center' border='0' cellpadding='0' cellspacing='0' style='width:600px; background-color: rgb(255, 255, 255);box-shadow: inset 0px 0px 3px 0px rgba(0, 0, 0, 0.45);border: 1px solid #ccc; border-radius: 5px;'> <tbody> <tr> <td style='font-size:0'>&nbsp;</td> <td style='border-radius:6px' width='645'> <table bgcolor='#ffffff' border='0' cellpadding='0' cellspacing='0' width='100%'> <tbody> <tr> <td align='left' style='color:#313332;line-height:46px;padding: 4% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-weight:400;font-size: 20px;font-family:\"Open Sans\", sans-serif'>Rejection of Leave</span> </td> </tr> <tr> <td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size: 16px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>This is to intimate that your manager <b>$[managerName]</b> has rejected</span> </td> </tr> <tr> <td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size:16px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>your OD leave request as per the following details</span> </td> </tr> </tbody> </table> <table width='100%' style='padding: 10px 50px 30px 140px;font-family: \"Open Sans\", sans-serif;'> <tbody> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Name</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[employeeName]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Employee ID</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[EmployeeCode]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Leave Type</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[type]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>From Date</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[fromDate]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>To Date</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[toDate]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>No of Days</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[days]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Reason of Rejection</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[leaveReason]</span> </td> </tr> </tbody> </table> <table bgcolor='#ffffff' border='0' cellpadding='0' cellspacing='0' width='100%' style='padding: 0% 0% 6% 0%;'> <tbody> <tr> <td align='left' style='color:#5f6e74;padding: 2% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size:15px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>You can view the leave request by clicking on the below link</span><br> <a href='"+SERVICE_URL+ "' style='color:#e68e21;font-size: 14px;font-family:\"Open Sans\", sans-serif;'>"+SERVICE_URL+ "</a> </td> </tr> </tbody> </table> </td> <td style='font-size:0'>&nbsp;</td> </tr> </tbody> </table> </td> </tr> </tbody> </table>";

	public static final String REJECTION_LEAVE_MAIL_BODY = "<table border='0' cellpadding='0' cellspacing='0' width='100%'> <tbody> <tr> <td align='center' style='background-image:url("+MAIL_TEMPLATE_BACKGROUND+");background-repeat: no-repeat;'> <a href='https://photon.okta.com' target='_blank'> <img alt='vms' border='0' src='"+MAIL_TEMPLATE_LOGO+"' style='padding: 20px 0px;'> </a> <table align='center' border='0' cellpadding='0' cellspacing='0' style='width:600px; background-color: rgb(255, 255, 255);box-shadow: inset 0px 0px 3px 0px rgba(0, 0, 0, 0.45);border: 1px solid #ccc; border-radius: 5px;'> <tbody> <tr> <td style='font-size:0'>&nbsp;</td> <td style='border-radius:6px' width='645'> <table bgcolor='#ffffff' border='0' cellpadding='0' cellspacing='0' width='100%'> <tbody> <tr> <td align='left' style='color:#313332;line-height:46px;padding: 4% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-weight:400;font-size: 20px;font-family:\"Open Sans\", sans-serif'>Rejection of Leave</span> </td> </tr> <tr> <td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size: 16px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>This is to intimate that your manager <b>$[managerName]</b> has rejected</span> </td> </tr> <tr> <td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size:16px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>your leave request as per the following details</span> </td> </tr> </tbody> </table> <table width='100%' style='padding: 10px 50px 30px 140px;font-family: \"Open Sans\", sans-serif;'> <tbody> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Name</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[employeeName]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Employee ID</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[EmployeeCode]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Leave Type</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[type]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>From Date</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[fromDate]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>To Date</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[toDate]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>No of Days</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[days]</span> </td> </tr> <tr style='font-size: 14px;'> <td align='left' style='width: 42%;color:#5f6e74;padding: 5% 0% 0% 0%;'> <span>Reason of Rejection</span> </td> <td align='left' style='color:#5f6e74;padding: 5% 0% 0% 0%;font-weight:600;'> <span>$[leaveReason]</span> </td> </tr> </tbody> </table> <table bgcolor='#ffffff' border='0' cellpadding='0' cellspacing='0' width='100%' style='padding: 0% 0% 6% 0%;'> <tbody> <tr> <td align='left' style='color:#5f6e74;padding: 2% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size:15px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>You can view the leave request by clicking on the below link</span><br> <a href='"+SERVICE_URL+ "' style='color:#e68e21;font-size: 14px;font-family:\"Open Sans\", sans-serif;'>"+SERVICE_URL+ "</a> </td> </tr> </tbody> </table> </td> <td style='font-size:0'>&nbsp;</td> </tr> </tbody> </table> </td> </tr> </tbody> </table>";


	public static final String LEAVE_REMINDER_MAIL_BODY = "<table border='0' cellpadding='0' cellspacing='0' width='100%'> <tbody> <tr> <td align='center' style='background-image:url("+MAIL_TEMPLATE_BACKGROUND+");background-repeat: no-repeat;'> <a href='https://photon.okta.com' target='_blank'> <img alt='vms' border='0' src='"+MAIL_TEMPLATE_LOGO+"' style='padding: 20px 0px;'> </a> <table align='center' border='0' cellpadding='0' cellspacing='0' style='width:730px; background-color: rgb(255, 255, 255);box-shadow: inset 0px 0px 3px 0px rgba(0, 0, 0, 0.45);border: 1px solid #ccc; border-radius: 5px;'> <tbody> <tr> <td style='font-size:0'>&nbsp;</td> <td style='border-radius:6px;padding-bottom: 2px;' width='645'> <table bgcolor='#ffffff' border='0' cellpadding='0' cellspacing='0' width='100%'> <tbody> <tr> <td align='left' style='color:#313332;line-height:46px;padding: 4% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-weight:400;font-size: 20px;font-family:\"Open Sans\", sans-serif'>Reminder for Approval</span> </td> </tr> <tr> <td align='left' style='color:#5f6e74;padding: 1% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size: 16px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>The following leave requests are awaiting your review :</span> </td> </tr></tbody> </table>$[DynamicText]<table bgcolor='#ffffff' border='0' cellpadding='0' cellspacing='0' width='100%' style='padding: 0% 0% 6% 0%;'> <tbody> <tr> <td align='left' style='color:#5f6e74;padding: 2% 0% 0% 0%;text-align: -webkit-center;'> <span style='font-size:15px;font-family:\"Open Sans\", sans-serif;font-weight:100;'>You can view the leave request by clicking on the below link</span><br> <a href='"+SERVICE_URL+ "#/review' style='color:#e68e21;font-size: 14px;font-family:\"Open Sans\", sans-serif;'>"+SERVICE_URL+ "#/review</a> </td> </tr> </tbody> </table> </td> <td style='font-size:0'>&nbsp;</td> </tr> </tbody> </table> </td> </tr> </tbody> </table>";
	static {
		getValidLeaveStatus().add(STATUS_PENDING);
		getValidLeaveStatus().add(STATUS_PENDING_REVOCATION);
		getValidLeaveStatus().add(STATUS_APPROVED);
	}
	static {
		VALID_LEAVE_STATUS_ID.add(STATUS_PENDING_ID);
		VALID_LEAVE_STATUS_ID.add(STATUS_PENDING_REVOCATION_ID);
		VALID_LEAVE_STATUS_ID.add(STATUS_APPROVED_ID);
	}
	static {
		getNonBlockLeaveTypes().add("OD");
		getNonBlockLeaveTypes().add("PL");
		getNonBlockLeaveTypes().add("CL");
		getNonBlockLeaveTypes().add("MDL");
		getNonBlockLeaveTypes().add("ANL");
		getNonBlockLeaveTypes().add("PTO");
		getNonBlockLeaveTypes().add("COFF");
		getNonBlockLeaveTypes().add("CNL");
		getNonBlockLeaveTypes().add("TL");
		getNonBlockLeaveTypes().add("PTL");
		getNonBlockLeaveTypes().add("AL");
		getNonBlockLeaveTypes().add("MAL");
		getNonBlockLeaveTypes().add("BL");
		getNonBlockLeaveTypes().add("APTO");
		getNonBlockLeaveTypes().add("SL");
	}
	static{
		getInvalidDays().add(Calendar.SATURDAY);
		getInvalidDays().add(Calendar.SUNDAY);
	}
	static{
		getLegendColor().put("CL",LEGEND_COLOR_1);
		getLegendColor().put("PL",LEGEND_COLOR_2);
		getLegendColor().put("MDL",LEGEND_COLOR_1);
		getLegendColor().put("ANL",LEGEND_COLOR_2);
		getLegendColor().put("COFF","#49D1E3");
		getLegendColor().put("OD","#7ED321");
		getLegendColor().put("Holiday","#D07CED");
		getLegendColor().put("PTO",LEGEND_COLOR_2);
		getLegendColor().put("LOP", "#DF485B");
		getLegendColor().put("AL", LEGEND_COLOR_1);
		getLegendColor().put("ML", "#FEBD57");
		getLegendColor().put("APTO", LEGEND_COLOR_1);
	}
	
	public static final String UNFREEZED_UPDATE ="Record(s) have been updated successfully";
	public static final String UNFREEZED_VALIDATION_ADMIN = "These dates are already unfrozen";
	public static final String UNFREEZED_SUCCESS = "Account has been unfrozen successfully";
	public static final String UNFREEZED_VALIDATION_EMPLOYEE = "These dates are already unfrozen for this employee: ";
	public static final String UNFREEZED_VALIDATION_EMPLOYEE_LOCATION = "These dates are already unfrozen for the employee's location: ";
	public static final String UNFREEZED_MAIL_SUBJECT = "Vacation Management -  Account has been unfrozen";
	public static final String BULK_UPLOAD_SUBJECT = "Vacation Management System - Bulk Upload";
	public static final String REVOCATION_APPROVED_SUBJECT = "Approval of Leave Revocation";
	public static final String REVOCATION_SUBJECT = "Requisition of Leave Revocation";
	public static final String REVOCATION_REJECTED_SUBJECT = "Rejection of Leave Revocation";
	public static final String REVOCATION_MANAGER = "your manager";
	public static final String REVOCATION_FINANCE_ADMIN="Finance team";
	public static final String LEAVE_APPROVED_MESSAGE = "Leave Request(s) has been approved successfully";
	public static final String LEAVE_REJECT_MESSAGE = "Leave request have been rejected successfully";
	
	public static final String ERROR = "Error";
	public static final String INVALID_DATA = "Invalid Data";
	public static final String SUCCESS = "Success";
	public static final String FAILURE = "Failure";
	public static final String CONTACT_NUMBER_ERROR_MSG =  "Your contact number is not available. Please contact HR Team";
	public static final String YYYY_MM_DD="yyyy-MM-dd";
	public static final String FALSE="false";
	public static final String LEAVE_REQUEST_ID_INVALID = "Leave request id is not valid";
	public static final String USED_LEAVES_EXECEEDED= "Used leaves have exceeded the sum of Open and Credit Leaves";
	public static final String CONTENT_DOWNLOAD = "application/download";
	public static final String CONTENT_DISPOSITION = "Content-disposition";
	public static final String ATTACHEMENT_FILENAME = "attachment; filename=";
	public static final String CSV_FILEFORMAT=".csv";
	public static final String PDF_FILEFORMAT=".pdf";
	
	public static final String HEADER_NAME = "Name";
	public static final String HEADER_EMP_ID="Emp ID";
	public static final String HEADER_APPLIED_ON="Applied On";
	public static final String HEADER_FROM="From";
	public static final String HEADER_TO="To";
	public static final String HEADER_DAYS="Days";
	public static final String HEADER_TYPE="Type";
	public static final String HEADER_REASON="Reason";
	public static final String HEADER_BALANCE="Balance";
	public static final String HEADER_REVIEWED_ON="Reviewed On";
	public static final String HEADER_STATUS="Status";
	public static final String HEADER_REPORTING_MANAGER="Reporting Manager";
	public static final String HEADER_LEAVE_DATE = "Leave Date";
	public static final String INCLUDE_HIERARCHY="IH";
	
	
	public static final String LEAVE_CODE = "Leave_code";
	public static final String LEAVE_TYPE = "Leave_type";
	public static final String EMPLOYEE_CODE = "Employee_code";
	public static final String EMPLOYEE_NAME = "Employee_name";
	public static final String LOCATION_NAME="location_name";
	
	public static ArrayList<String> getValidLeaveStatus() {
		return VALID_LEAVE_STATUS;
	}
	public static List<String> getNonBlockLeaveTypes() {
		return NON_BLOCK_LEAVE_TYPES;
	}
	public static List<Integer> getInvalidDays() {
		return INVALID_DAYS;
	}
	public static Map<String, String> getLegendColor() {
		return LEGEND_COLOR;
	}
}
