package com.photon.vms.controller;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.service.AdminService;
import com.photon.vms.service.EmployeeHomeService;
import com.photon.vms.service.ManagerReviewService;
import com.photon.vms.vo.PendingLeaveRequsetVO;
import com.photon.vms.vo.ReportGenerateVO;
import com.photon.vms.vo.RequestParameterVO;

/**
 * @author karthigaiselvan_r
 *
 */
@RestController
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/review")
public class ReviewController {
	@Autowired
	ManagerReviewService managerReviewService;
	@Autowired
	EmployeeHomeService employeeHomeService;
	@Autowired
	AdminService adminService;
	@Autowired
	JavaMailSender javaMailSender;
	
	
	/**
	 * @param requestParameter
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/getPendingRequest", method = RequestMethod.POST)
	public ArrayList<PendingLeaveRequsetVO> getPendingLeaveRequest(@RequestBody RequestParameterVO requestParameter,ServletRequest req){
		ArrayList<PendingLeaveRequsetVO> response = new ArrayList<>();
		Map<String, ArrayList<?>> pendingList = new HashMap<>();
		try{
			String employeeNumber = requestParameter.getEmployeeNumber();
			String reporteeNumber = (requestParameter.getReporteeEmployeeNumber()!=null?requestParameter.getReporteeEmployeeNumber():"");
			String reporteeName = (requestParameter.getReporteeName()!=null?requestParameter.getReporteeName():"");
			String appliedDate = (requestParameter.getAppliedDate()!=null?requestParameter.getAppliedDate():"");
			String fromDate = (requestParameter.getFromDate()!=null?requestParameter.getFromDate():"");
			String toDate = (requestParameter.getToDate()!=null?requestParameter.getToDate():"");
			String leaveType = (requestParameter.getLeaveType()!=null?requestParameter.getLeaveType():"");
			String leaveStatus = (requestParameter.getLeaveStatus()!=null?requestParameter.getLeaveStatus():"");
			if(employeeNumber != null) {
				pendingList = managerReviewService.getPendingLeaveRequest(employeeNumber, reporteeNumber, reporteeName, fromDate, toDate, appliedDate, leaveType, leaveStatus);
				response = (ArrayList<PendingLeaveRequsetVO>) pendingList.get("PendingLeaveRequest");					
			}
			
			VmsLogging.logInfo(getClass(), "Fetch pending leave request "+employeeNumber);
			
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Fetch pending leave request Exception ", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}

	/**
	 * @param employeeNumber
	 * @param approvedDate
	 * @param fromDate
	 * @param toDate
	 * @param appliedDate
	 * @param leaveType
	 * @param leaveStatus
	 * @param reporteeEmployeeNumber
	 * @param reporteeName
	 * @param numberOfDays
	 * @param req
	 * @param servletResponse
	 */
	@RequestMapping(value="/exportreport", method=RequestMethod.GET)
	private void generateCSVReport(@RequestParam (value="employeeNumber", defaultValue="") String employeeNumber,@RequestParam (value="approvedDate", defaultValue="null") String approvedDate,@RequestParam (value="fromDate", defaultValue="null") String fromDate,@RequestParam (value="toDate", defaultValue="null") String toDate,
				@RequestParam (value="appliedDate", defaultValue="null") String appliedDate,@RequestParam (value="leaveType", defaultValue="null") String leaveType,
				@RequestParam (value="leaveStatus", defaultValue="null") String leaveStatus,@RequestParam (value="reporteeEmployeeNumber", defaultValue="null") String reporteeEmployeeNumber,
				@RequestParam (value="reporteeName", defaultValue="null") String reporteeName,@RequestParam (value="numberOfDays", defaultValue="null") String numberOfDays,
				ServletRequest req, HttpServletResponse servletResponse){
		try{
			RequestParameterVO requestParameter = new RequestParameterVO();
			if(employeeNumber != null && !employeeNumber.isEmpty()){
				requestParameter.setEmployeeNumber(employeeNumber);
				requestParameter.setAppliedDate((appliedDate.isEmpty()|| appliedDate.equals("null"))?null:appliedDate);
				requestParameter.setFromDate((fromDate.isEmpty() || fromDate.equals("null"))?null:fromDate);
				requestParameter.setToDate((toDate.isEmpty() || toDate.equals("null"))?null:toDate);
				requestParameter.setNumberOfDays((numberOfDays.isEmpty() || numberOfDays.equals("null"))?null:numberOfDays);
				requestParameter.setLeaveType((leaveType.isEmpty() || leaveType.equals("null"))?null:leaveType);
				requestParameter.setApprovedDate((approvedDate.isEmpty() || approvedDate.equalsIgnoreCase("") || approvedDate.equals("null"))?null:approvedDate);
				requestParameter.setLeaveStatus((leaveStatus.isEmpty() || leaveStatus.equals("null"))?null:leaveStatus);
				requestParameter.setReporteeEmployeeNumber((reporteeEmployeeNumber.isEmpty() || reporteeEmployeeNumber.equals("null"))?null:reporteeEmployeeNumber);
				requestParameter.setReporteeName((reporteeName.isEmpty() || reporteeName.equals("null"))?null:reporteeName);
				
				ArrayList<ReportGenerateVO> response = managerReviewService.generateCSVReport(employeeNumber, requestParameter);
				pushDatatoCSV(response, servletResponse);
			}
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Generate Report Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	/**
	 * @param reportGenerateVO
	 * @param servletResponse
	 */
	private void pushDatatoCSV(ArrayList<ReportGenerateVO> reportGenerateVO, HttpServletResponse servletResponse) {
		try{
			
			SimpleDateFormat formatter=new SimpleDateFormat("MM-dd-yyyy");			
			servletResponse.setContentType("application/download");
			servletResponse.setHeader("Content-disposition", "attachment; filename=Leave Report_"+formatter.format(new Date())+".csv");
			CSVPrinter csvPrinter = new CSVPrinter(servletResponse.getWriter(), CSVFormat.DEFAULT
				.withHeader("Leave request id", "Applied Date","Employee Number", "Employee Name", "From Date", "To Date", "Number of days", "Leave type", "Leave Reason", "Contact number", "Leave Status"/*, "Rejected Date", "Rejected Reason", "Revoked Date", "Revoked Reason", "Cancelled Date", "Cancelled Reason", "Solution center"*/));			
			
			for (ReportGenerateVO record : reportGenerateVO) {
				csvPrinter.printRecord(record.getLeaveRequestId(), record.getAppliedDate(),record.getEmployeeCode(),record.getEmployeeName(),record.getFromDate(),record.getToDate(),record.getNumberOfDays(),record.getLeaveTypeName(), record.getLeaveReason(), record.getContactNumber(), record.getLeaveStatus()/*,record.getRejectedDate(),record.getRejectedReason(),record.getRevokedDate(),record.getRevokedReason(),record.getCancelledDate(),record.getCancelledReason(),record.getSolutionCenter()*/);
			}
			servletResponse.getWriter().flush();
			csvPrinter.flush();
			csvPrinter.close();
		}catch (FileNotFoundException e) {
			VmsLogging.logError(getClass(), "Report file not found Exception ", new VmsApplicationException(e.getMessage()));
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Generate Report Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
}
