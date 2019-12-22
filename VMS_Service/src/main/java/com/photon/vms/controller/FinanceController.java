package com.photon.vms.controller;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.service.FinanceService;
import com.photon.vms.vo.FinanceReportResponse;
import com.photon.vms.vo.FinanceResponceCompOffVO;
import com.photon.vms.vo.FinanceResponceIndiaSalesVO;
import com.photon.vms.vo.FinanceResponceLeaveBalanceVO;
import com.photon.vms.vo.FinanceResponceLeaveRequestVO;
import com.photon.vms.vo.FinanceResponceUsedLeaveVO;
import com.photon.vms.vo.HierarchResponseVO;
import com.photon.vms.vo.ReportDropDownResponse;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.SearchHierarchy;
import com.photon.vms.vo.SearchHierarchyRequestVO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/finance")
public class FinanceController {

	@Autowired
	FinanceService financeService;

	private static final String DATE_FORMAT = "MM-dd-yyyy";
	private static final String DATE_FORMAT_SLASH = "MM/dd/YYY";
	private static final String REPORT_FILENAME= " Report_";
	private static final String REPORT_FILE_NOT_FOUND="Report file not found Exception ";
	private static final String GENERATE_FILE_EXCEPTION="Generate Report Exception ";
	private static final String LEAVE_BALANCE_EXCEPTION="Leave Balance Report file not found Exception ";
	private static final String LEAVE_GENERATE_EXCEPTION="Leave Balance Report Generate Report Exception ";
	private static final String EMPLOYEE_CODE="Employee Code";
	private static final String EMPLOYEE_NAME="Employee Name";
	private static final String LOCATION_NAME="Location Name";
	private static final String EMPLOYEE_STATUS="Employee Status";
	private static final String LEAVE_TYPE="Leave Type";
	private static final String OPEN="Open";
	private static final String USED="Used";
	private static final String CREDIT="Credit";
	
	
	@RequestMapping(value = "/getFinaceLeaveReport", method = RequestMethod.GET)
	public void getFinaceLeaveReport(@RequestParam(value = "companyName", defaultValue = "") String companyName,
			@RequestParam(value = "employeeNumber", defaultValue = "") String employeeNumber,
			@RequestParam(value = "leaveTypeId", defaultValue = "") String leaveTypeId,
			@RequestParam(value = "reportType", defaultValue = "") String reportType,
			@RequestParam(value = "fromDate", defaultValue = "") String fromDate,
			@RequestParam(value = "toDate", defaultValue = "") String toDate,
			@RequestParam(value = "activeStatus", defaultValue = "") int activeStatus,ServletRequest req,
			HttpServletResponse servletResponse) {
		try {
			VmsLogging.logInfo(getClass(), "Employee - Leave info " + employeeNumber);
			if(reportType.equals("Used Leave - Payroll")) {
			ArrayList<FinanceResponceUsedLeaveVO> response = financeService.getFinanceResponceUsedLeaveReport(companyName, employeeNumber,leaveTypeId, reportType, fromDate, toDate,activeStatus);
			usedLeaveDataPushtoCSV(response, reportType, servletResponse);
			}else if(reportType.equals("Leave Request")) {
				ArrayList<FinanceResponceLeaveRequestVO> response = financeService.getFinanceResponceLeaveRequestReport(companyName, employeeNumber,leaveTypeId, reportType, fromDate, toDate,activeStatus);
				leaveRequestDataPushtoCSV(response, reportType, servletResponse);
			}else if(reportType.equals("Leave Balance")) {
				ArrayList<FinanceResponceLeaveBalanceVO> response = financeService.getFinanceResponceLeaveBalanceReport(companyName, employeeNumber,leaveTypeId, reportType, fromDate, toDate,activeStatus);
				leaveBalanceDataPushtoCSV(response, reportType, servletResponse);
			}else if(reportType.equals("Comp Off")) {
				ArrayList<FinanceResponceCompOffVO> response = financeService.getFinanceResponceCompOffReport(companyName, employeeNumber,leaveTypeId, reportType, fromDate, toDate,activeStatus);
				compOffDataPushtoCSV(response, reportType, servletResponse);
			}else if(reportType.equals("India Sales")) {
				ArrayList<FinanceResponceIndiaSalesVO> response = financeService.getFinanceResponceIndiaSalesReport(companyName, employeeNumber,leaveTypeId, reportType, fromDate, toDate,activeStatus);
				indiaSalesDataPushtoCSV(response, reportType, servletResponse);
			}
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Finance - Leave Report info Exception ",
					new VmsApplicationException(e.getMessage()));
		}
	}

	private void usedLeaveDataPushtoCSV(ArrayList<FinanceResponceUsedLeaveVO> reportGenerateVO, String reportType, HttpServletResponse servletResponse) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			servletResponse.setContentType(CommonConstants.CONTENT_DOWNLOAD);
			servletResponse.setHeader(CommonConstants.CONTENT_DISPOSITION,CommonConstants.ATTACHEMENT_FILENAME+reportType+REPORT_FILENAME + formatter.format(new Date()) + CommonConstants.CSV_FILEFORMAT);
			CSVPrinter csvPrinter = new CSVPrinter(servletResponse.getWriter(),CSVFormat.DEFAULT.withHeader("Employee Number", EMPLOYEE_NAME, LEAVE_TYPE,"Leave Date",
					"Approved Date","No of Days"));
			for (FinanceResponceUsedLeaveVO record : reportGenerateVO) {
				csvPrinter.printRecord(record.getEmployeeNumber(),record.getEmployeeName(),record.getLeaveType(),record.getLeaveDate(),record.getApprovedDate(),record.getNoOfDays());
			}
			servletResponse.getWriter().flush();
			csvPrinter.flush();
			csvPrinter.close();
		} catch (FileNotFoundException e) {
			VmsLogging.logError(getClass(), REPORT_FILE_NOT_FOUND,	new VmsApplicationException(e.getMessage()));
		} catch (Exception e) {
			VmsLogging.logError(getClass(), GENERATE_FILE_EXCEPTION, new VmsApplicationException(e.getMessage()));
		}
	}
	
	private void leaveRequestDataPushtoCSV(ArrayList<FinanceResponceLeaveRequestVO> reportGenerateVO, String reportType, HttpServletResponse servletResponse) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			servletResponse.setContentType(CommonConstants.CONTENT_DOWNLOAD);
			servletResponse.setHeader(CommonConstants.CONTENT_DISPOSITION,CommonConstants.ATTACHEMENT_FILENAME+reportType+REPORT_FILENAME + formatter.format(new Date()) + CommonConstants.CSV_FILEFORMAT);
			CSVPrinter csvPrinter = new CSVPrinter(servletResponse.getWriter(),CSVFormat.DEFAULT.withHeader(EMPLOYEE_CODE,EMPLOYEE_NAME,LEAVE_TYPE,"From Date","To Date","No of Days","Leave Status",
							"Submitted Date","Approved By","Approved Date",CommonConstants.HEADER_REASON,LOCATION_NAME,EMPLOYEE_STATUS,"Cancelled By Code","Cancelled By Name","Cancelled By Date","Cancelled By Reason",
							"Revoked By Code","Revoked By Name","Revoked Reason","Rejected By Code","Rejected By Name","Rejected Date","Rejected Reason","Revocation Rejected Reason","Revocation Rejected Date","Status1","Status2"));
			for (FinanceResponceLeaveRequestVO record : reportGenerateVO) {
				csvPrinter.printRecord(record.getEmployeeNumber(),record.getEmployeeName(),record.getLeaveType(),record.getFromDate(),record.getToDate(),record.getNoOfDays(),record.getLeaveStatus(),
						record.getSubmittedDate(),record.getApprovedBy(),record.getApprovedDate(),record.getReason(),record.getLocationName(),record.getEmployeeStatus(),record.getCancelledByCode(),
						record.getCancelledByName(),record.getCancelledDate(),record.getCancelledReason(),record.getRevokedByCode(),record.getRevokedByName(),record.getRevokedReason(),
						record.getRejectedByCode(),record.getRejectedByName(),record.getRejectedDate(),record.getRejectedReason(),record.getRevocationAprRejReason(),record.getRevocationAprRejDate(),record.getStatus1(),record.getStatus2());
			}
			servletResponse.getWriter().flush();
			csvPrinter.flush();
			csvPrinter.close();
		} catch (FileNotFoundException e) {
			VmsLogging.logError(getClass(), REPORT_FILE_NOT_FOUND,
					new VmsApplicationException(e.getMessage()));
		} catch (Exception e) {
			VmsLogging.logError(getClass(), GENERATE_FILE_EXCEPTION, new VmsApplicationException(e.getMessage()));
		}
	}
	
	private void leaveBalanceDataPushtoCSV(ArrayList<FinanceResponceLeaveBalanceVO> reportGenerateVO, String reportType, HttpServletResponse servletResponse) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			servletResponse.setContentType(CommonConstants.CONTENT_DOWNLOAD);
			servletResponse.setHeader(CommonConstants.CONTENT_DISPOSITION,CommonConstants.ATTACHEMENT_FILENAME+reportType+REPORT_FILENAME + formatter.format(new Date()) + CommonConstants.CSV_FILEFORMAT);
			CSVPrinter csvPrinter = new CSVPrinter(servletResponse.getWriter(),CSVFormat.DEFAULT.withHeader(EMPLOYEE_CODE,EMPLOYEE_NAME,LEAVE_TYPE,OPEN,CREDIT,USED,CommonConstants.HEADER_BALANCE,LOCATION_NAME,EMPLOYEE_STATUS));
			for (FinanceResponceLeaveBalanceVO record : reportGenerateVO) {
				csvPrinter.printRecord(record.getEmployeeNumber(),record.getEmployeeName(),record.getLeaveType(),record.getOpen(),record.getCredit(),record.getUsed(),record.getBalance(),record.getLocationName(),record.getEmployeeStatus());
			}
			servletResponse.getWriter().flush();
			csvPrinter.flush();
			csvPrinter.close();
		} catch (FileNotFoundException e) {
			VmsLogging.logError(getClass(), REPORT_FILE_NOT_FOUND,
					new VmsApplicationException(e.getMessage()));
		} catch (Exception e) {
			VmsLogging.logError(getClass(), GENERATE_FILE_EXCEPTION, new VmsApplicationException(e.getMessage()));
		}
	}
	
	private void compOffDataPushtoCSV(ArrayList<FinanceResponceCompOffVO> reportGenerateVO, String reportType, HttpServletResponse servletResponse) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			servletResponse.setContentType(CommonConstants.CONTENT_DOWNLOAD);
			servletResponse.setHeader(CommonConstants.CONTENT_DISPOSITION,CommonConstants.ATTACHEMENT_FILENAME+reportType+REPORT_FILENAME + formatter.format(new Date()) + CommonConstants.CSV_FILEFORMAT);
			CSVPrinter csvPrinter = new CSVPrinter(servletResponse.getWriter(),CSVFormat.DEFAULT.withHeader(EMPLOYEE_CODE,EMPLOYEE_NAME,"Comp-Off Date","Created By","Created Date",LOCATION_NAME,EMPLOYEE_STATUS));
			for (FinanceResponceCompOffVO record : reportGenerateVO) {
				csvPrinter.printRecord(record.getEmployeeNumber(),record.getEmployeeName(),record.getCompOffDate(),record.getCreatedBy(),record.getCreatedDate(),record.getLocationName(),record.getEmployeeStatus());
			}
			servletResponse.getWriter().flush();
			csvPrinter.flush();
			csvPrinter.close();
		} catch (FileNotFoundException e) {
			VmsLogging.logError(getClass(), REPORT_FILE_NOT_FOUND,
					new VmsApplicationException(e.getMessage()));
		} catch (Exception e) {
			VmsLogging.logError(getClass(), GENERATE_FILE_EXCEPTION, new VmsApplicationException(e.getMessage()));
		}
	}
	
	private void indiaSalesDataPushtoCSV(ArrayList<FinanceResponceIndiaSalesVO> reportGenerateVO, String reportType, HttpServletResponse servletResponse) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			servletResponse.setContentType(CommonConstants.CONTENT_DOWNLOAD);
			servletResponse.setHeader(CommonConstants.CONTENT_DISPOSITION,CommonConstants.ATTACHEMENT_FILENAME+reportType+REPORT_FILENAME + formatter.format(new Date()) + CommonConstants.CSV_FILEFORMAT);
			CSVPrinter csvPrinter = new CSVPrinter(servletResponse.getWriter(),CSVFormat.DEFAULT.withHeader(EMPLOYEE_CODE,EMPLOYEE_NAME,"Department Name",EMPLOYEE_STATUS));
			for (FinanceResponceIndiaSalesVO record : reportGenerateVO) {
				csvPrinter.printRecord(record.getEmployeeNumber(),record.getEmployeeName(),record.getDepartmentName(),record.getEmployeeStatus());
			}
			servletResponse.getWriter().flush();
			csvPrinter.flush();
			csvPrinter.close();
		} catch (FileNotFoundException e) {
			VmsLogging.logError(getClass(), REPORT_FILE_NOT_FOUND,
					new VmsApplicationException(e.getMessage()));
		} catch (Exception e) {
			VmsLogging.logError(getClass(), GENERATE_FILE_EXCEPTION, new VmsApplicationException(e.getMessage()));
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getFinanceReportEntities")
	public FinanceReportResponse getFinanceReportEntity() {
		FinanceReportResponse response = new FinanceReportResponse();
		try {
			response = financeService.getFinanceReportEntity();
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Get Finanace Report Exception ", new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	@RequestMapping(value="/getReportDropDown", method = RequestMethod.POST)
	public ReportDropDownResponse getReportDropDown(@RequestBody RequestParameterVO requestParameter,ServletRequest req){
		ReportDropDownResponse response = new ReportDropDownResponse();
		try{
			VmsLogging.logInfo(getClass(), "Report Employee info "+requestParameter.getEmployeeNumber() + " | "+requestParameter.getFlag());
			
			response = financeService.getReportDropDown(requestParameter.getEmployeeNumber(),requestParameter.getFlag());
			return response;
		}catch(Exception e){
			VmsLogging.logError(getClass(), "Report Employee info Exception ", new VmsApplicationException(e.getMessage()));
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/searchHierarchy")
	public HierarchResponseVO searchHierarchy(@RequestBody SearchHierarchyRequestVO request) {
		HierarchResponseVO response = new HierarchResponseVO();
		try {
			List<SearchHierarchy> searchList;
			if(request.getLoginEmployeeCode() != null) {
			 searchList = financeService.searchAndReportHierarchy(request);
			 response.setStatus(CommonConstants.SUCCESS);
			 response.setSearchList(searchList);
			}else {
				response.setStatus(CommonConstants.ERROR);
				response.setErrorMessage(CommonConstants.INVALID_DATA);
			}
			
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Search hierarchy info Exception ", new VmsApplicationException(e.getMessage()));	
			response.setStatus(CommonConstants.ERROR);
			response.setErrorMessage("Search Failed");
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/leaveBalanceReport")
	public void getLeaveBalanceReport(@RequestParam(value = "loginEmployeeCode", defaultValue = "") String loginEmployeeCode,
			@RequestParam(value= "employeeCode", defaultValue = "") String employeeCode,
			@RequestParam(value = "flag", defaultValue="") String flag,HttpServletResponse servletResponse) {
		try {
			if(loginEmployeeCode != null) {
			List<FinanceResponceLeaveBalanceVO> reportList = financeService.getLeaveBalanceReportByHierarchy(loginEmployeeCode, employeeCode, flag);
			getLeaveBalanceReportToCSV(reportList, servletResponse);
			}
			else {
				VmsLogging.logInfo(getClass(), "LeaveBalance Report Controller info Exception ");
			}
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "LeaveBalance Report Controller info Exception ", new VmsApplicationException(e.getMessage()));	
		}
	}
	
	private void getLeaveBalanceReportToCSV(List<FinanceResponceLeaveBalanceVO> reportGenerateVO, HttpServletResponse servletResponse) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_SLASH);
			servletResponse.setContentType(CommonConstants.CONTENT_DOWNLOAD);
			servletResponse.setHeader(CommonConstants.CONTENT_DISPOSITION,CommonConstants.ATTACHEMENT_FILENAME+"Leave Balance Report_" + formatter.format(new Date()) + CommonConstants.CSV_FILEFORMAT);
			CSVPrinter csvPrinter = new CSVPrinter(servletResponse.getWriter(),CSVFormat.DEFAULT.withHeader(EMPLOYEE_CODE,EMPLOYEE_NAME,LEAVE_TYPE,OPEN,CREDIT,USED,CommonConstants.HEADER_BALANCE,EMPLOYEE_STATUS,CommonConstants.HEADER_REPORTING_MANAGER));
			for (FinanceResponceLeaveBalanceVO record : reportGenerateVO) {
				csvPrinter.printRecord(record.getEmployeeNumber(),record.getEmployeeName(),record.getLeaveType(),record.getOpen(),record.getCredit(),record.getUsed(),record.getBalance(),record.getEmployeeStatus(),record.getReportingManager());
			}
			servletResponse.getWriter().flush();
			csvPrinter.flush();
			csvPrinter.close();
		} catch (FileNotFoundException e) {
			VmsLogging.logError(getClass(), LEAVE_BALANCE_EXCEPTION,
					new VmsApplicationException(e.getMessage()));
		} catch (Exception e) {
			VmsLogging.logError(getClass(), LEAVE_GENERATE_EXCEPTION, new VmsApplicationException(e.getMessage()));
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/exportLeaveBalance")
	public void exportLeaveBalance(@RequestParam(value = "loginEmployeeCode", defaultValue = "") String loginEmployeeCode,
			@RequestParam(value = "employeeCode", defaultValue = "undefined") String employeeCode,
			@RequestParam(value = "leaveFromDate", defaultValue = "null") String leaveFromDate,
			@RequestParam(value = "leaveToDate", defaultValue = "null") String leaveToDate,
			@RequestParam(value = "leaveAppliedOn", defaultValue = "null") String leaveAppliedOn,
			@RequestParam(value = "leaveReviewedOn", defaultValue = "null") String leaveReviewedOn,
			@RequestParam(value = "leaveStatusId", defaultValue = "null") String leaveStatusId,
			@RequestParam(value = "leaveTypeId", defaultValue = "null") String leaveTypeId,
			@RequestParam(value = "flag", defaultValue = "null") String flag,
			@RequestParam(value = "fileFormat", defaultValue = "null") String fileFormat,
			@RequestParam(value = "byRows", defaultValue = "0") int byRows,
			HttpServletResponse response) {
		try {
			SearchHierarchyRequestVO request = new SearchHierarchyRequestVO();
			request.setLoginEmployeeCode(loginEmployeeCode);
			request.setEmployeeCode(employeeCode.equals("undefined") ? null : employeeCode);
			request.setLeaveFromDate(!leaveFromDate.equals("null") ? leaveFromDate : null );
			request.setLeaveToDate(!leaveToDate.equals("null") ? leaveToDate : null);
			request.setLeaveAppliedOn(!leaveAppliedOn.equals("null") ? leaveAppliedOn : null);
			request.setLeaveReviewedOn(!leaveReviewedOn.equals("null") ? leaveReviewedOn : null);
			request.setLeaveStatusId(!leaveStatusId.equals("null") ? leaveStatusId : null);
			request.setLeaveTypeId(!leaveTypeId.equals("null") ? leaveTypeId : null);
			request.setFileFormat(fileFormat);
			request.setFlag(flag);
			request.setByRows(byRows);
			if(loginEmployeeCode != null) {
			List<SearchHierarchy> searchList;
			searchList = financeService.searchAndReportHierarchy(request);
				if(fileFormat.equals("csv")) {
					getExportLeaveBalanceToCSV(searchList, response, byRows, flag);
				}
				else {
					getExportLeaveBalanceToPDF(searchList,response, flag);
				}
			}
			VmsLogging.logInfo(getClass(), "Report Generated");
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Report Generate Report Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	private void getExportLeaveBalanceToCSV(List<SearchHierarchy> exportHierarchy, HttpServletResponse servletResponse, int byRows, String flag) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_SLASH);
			servletResponse.setContentType(CommonConstants.CONTENT_DOWNLOAD);
			String fileName="Leave Report_";
			if(byRows != 0)
				fileName="Day Wise Leave Report_";
			servletResponse.setHeader(CommonConstants.CONTENT_DISPOSITION,CommonConstants.ATTACHEMENT_FILENAME+fileName + formatter.format(new Date()) + CommonConstants.CSV_FILEFORMAT);
			CSVPrinter csvPrinter = null;
			if(byRows == 0) {
				
				if(flag.equalsIgnoreCase(CommonConstants.INCLUDE_HIERARCHY)) 
					csvPrinter = new CSVPrinter(servletResponse.getWriter(),CSVFormat.DEFAULT.withHeader(CommonConstants.HEADER_NAME,CommonConstants.HEADER_EMP_ID,CommonConstants.HEADER_APPLIED_ON,CommonConstants.HEADER_FROM,CommonConstants.HEADER_TO,
	            			CommonConstants.HEADER_DAYS,CommonConstants.HEADER_TYPE,CommonConstants.HEADER_REASON,CommonConstants.HEADER_BALANCE,CommonConstants.HEADER_REVIEWED_ON,CommonConstants.HEADER_STATUS,CommonConstants.HEADER_REPORTING_MANAGER));
			
				else 
					csvPrinter = new CSVPrinter(servletResponse.getWriter(),CSVFormat.DEFAULT.withHeader(CommonConstants.HEADER_NAME,CommonConstants.HEADER_EMP_ID,CommonConstants.HEADER_APPLIED_ON,CommonConstants.HEADER_FROM,CommonConstants.HEADER_TO,
	            			CommonConstants.HEADER_DAYS,CommonConstants.HEADER_TYPE,CommonConstants.HEADER_REASON,CommonConstants.HEADER_BALANCE,CommonConstants.HEADER_REVIEWED_ON,CommonConstants.HEADER_STATUS));
				
				for (SearchHierarchy record : exportHierarchy) {
					if(!flag.equalsIgnoreCase(CommonConstants.INCLUDE_HIERARCHY)) {
						csvPrinter.printRecord(record.getEmployeeName(),record.getEmployeeCode(),record.getAppliedOn(),record.getFromDate(),record.getToDate(),record.getNoOfDays(),
							record.getLeaveCode(),record.getReason(),record.getBalance(),record.getReviewOn(),record.getLeaveStatus());
					}
					else {
						String manager = record.getRmEmpName()+"/"+record.getRmEmpCode();
						csvPrinter.printRecord(record.getEmployeeName(),record.getEmployeeCode(),record.getAppliedOn(),record.getFromDate(),record.getToDate(),record.getNoOfDays(),
								record.getLeaveCode(),record.getReason(),record.getBalance(),record.getReviewOn(),record.getLeaveStatus(),manager);
					}
				}	
			}
			else {
				if(flag.equalsIgnoreCase(CommonConstants.INCLUDE_HIERARCHY))
					csvPrinter = new CSVPrinter(servletResponse.getWriter(),CSVFormat.DEFAULT.withHeader(CommonConstants.HEADER_NAME,CommonConstants.HEADER_EMP_ID,CommonConstants.HEADER_APPLIED_ON,CommonConstants.HEADER_LEAVE_DATE,
	            			CommonConstants.HEADER_DAYS,CommonConstants.HEADER_TYPE,CommonConstants.HEADER_REASON,CommonConstants.HEADER_BALANCE,CommonConstants.HEADER_REVIEWED_ON,CommonConstants.HEADER_STATUS,CommonConstants.HEADER_REPORTING_MANAGER));
				else
					csvPrinter = new CSVPrinter(servletResponse.getWriter(),CSVFormat.DEFAULT.withHeader(CommonConstants.HEADER_NAME,CommonConstants.HEADER_EMP_ID,CommonConstants.HEADER_APPLIED_ON,CommonConstants.HEADER_LEAVE_DATE,
	            			CommonConstants.HEADER_DAYS,CommonConstants.HEADER_TYPE,CommonConstants.HEADER_REASON,CommonConstants.HEADER_BALANCE,CommonConstants.HEADER_REVIEWED_ON,CommonConstants.HEADER_STATUS));
				for (SearchHierarchy record : exportHierarchy) {
					if(!flag.equalsIgnoreCase(CommonConstants.INCLUDE_HIERARCHY)) {
						csvPrinter.printRecord(record.getEmployeeName(),record.getEmployeeCode(),record.getAppliedOn(),record.getLeaveDate(),record.getNoOfDays(),
								record.getLeaveCode(),record.getReason(),record.getBalance(),record.getReviewOn(),record.getLeaveStatus());
					}
					else {
						String manager = record.getRmEmpName()+"/"+record.getRmEmpCode();
						csvPrinter.printRecord(record.getEmployeeName(),record.getEmployeeCode(),record.getAppliedOn(),record.getLeaveDate(),record.getNoOfDays(),
								record.getLeaveCode(),record.getReason(),record.getBalance(),record.getReviewOn(),record.getLeaveStatus(),manager);
					}
				}
			}
			servletResponse.getWriter().flush();
			csvPrinter.flush();
			csvPrinter.close();
			
		} catch (FileNotFoundException e) {
			VmsLogging.logError(getClass(), LEAVE_BALANCE_EXCEPTION,
					new VmsApplicationException(e.getMessage()));
		} catch (Exception e) {
			VmsLogging.logError(getClass(), LEAVE_GENERATE_EXCEPTION, new VmsApplicationException(e.getMessage()));
		}
	}
	
	private void getExportLeaveBalanceToPDF(List<SearchHierarchy> exportHierarchy, HttpServletResponse response, String flag) {
		     Font titleHeaderFont = new Font(Font.FontFamily.HELVETICA, 12);
		     Font titleFont = new Font(Font.FontFamily.HELVETICA, 10);
		     Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
		     ByteArrayOutputStream baos = new ByteArrayOutputStream();
		     
		     try {
		            Document document = new Document();
		            PdfWriter.getInstance(document, baos);
		            Rectangle two = new Rectangle(800,1000);
		            document.setPageSize(two);
		            document.setMargins(20, 20, 20, 20);
		            document.open();
		            document.addTitle("Leave Report");
		            Paragraph para = new Paragraph("Leave Report", titleHeaderFont);
		            para.setAlignment(Element.ALIGN_CENTER);
		            para.setSpacingAfter(25f);
		            document.add(para);
		            int columns = 11;
		            if(flag.equalsIgnoreCase(CommonConstants.INCLUDE_HIERARCHY)) {
		            	  VmsLogging.logInfo(getClass(), "FLAG 1 "+flag);
		            	columns = 12;
		            }
		            PdfPTable table = new PdfPTable(columns);
		            table.setWidthPercentage(104);
		           
		            String[] headers = {CommonConstants.HEADER_NAME,CommonConstants.HEADER_EMP_ID,CommonConstants.HEADER_APPLIED_ON, CommonConstants.HEADER_FROM,CommonConstants.HEADER_TO,
		            		CommonConstants.HEADER_DAYS,CommonConstants.HEADER_TYPE,CommonConstants.HEADER_REASON,CommonConstants.HEADER_BALANCE,CommonConstants.HEADER_REVIEWED_ON,CommonConstants.HEADER_STATUS};
		            VmsLogging.logInfo(getClass(), "FLAG "+flag);
		            VmsLogging.logInfo(getClass(), "Condition "+flag.equals(CommonConstants.INCLUDE_HIERARCHY));
		            if(flag.equalsIgnoreCase(CommonConstants.INCLUDE_HIERARCHY))
		            	headers = new String[]{CommonConstants.HEADER_NAME,CommonConstants.HEADER_EMP_ID,CommonConstants.HEADER_APPLIED_ON, CommonConstants.HEADER_FROM,CommonConstants.HEADER_TO,
		            			CommonConstants.HEADER_DAYS,CommonConstants.HEADER_TYPE,CommonConstants.HEADER_REASON,CommonConstants.HEADER_BALANCE,CommonConstants.HEADER_REVIEWED_ON,CommonConstants.HEADER_STATUS,CommonConstants.HEADER_REPORTING_MANAGER};
		            
		            for(String value : headers) {
		            	 PdfPCell c1 = new PdfPCell(new Phrase(value,titleFont));
		            	 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	 c1.setPadding(10);
		            	 table.addCell(c1);
		            }		            
		            table.setHeaderRows(1);
		            
		            for(SearchHierarchy search : exportHierarchy) {
		            	PdfPCell c2 = new PdfPCell(new Phrase(search.getEmployeeName(),textFont));
		            	table.addCell(c2);
		            	PdfPCell c3 = new PdfPCell(new Phrase(search.getEmployeeCode(),textFont));
		            	c3.setVerticalAlignment(Element.ALIGN_CENTER); 
		            	c3.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	table.addCell(c3);
		            	PdfPCell c4 = new PdfPCell(new Phrase(search.getAppliedOn(),textFont));
		            	c4.setVerticalAlignment(Element.ALIGN_CENTER); 
		            	c4.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	table.addCell(c4);
		            	PdfPCell c5 = new PdfPCell(new Phrase(search.getFromDate(),textFont));
		            	c5.setVerticalAlignment(Element.ALIGN_CENTER);
		            	c5.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	table.addCell(c5);
		            	PdfPCell c6 = new PdfPCell(new Phrase(search.getToDate(),textFont));
		            	c6.setVerticalAlignment(Element.ALIGN_CENTER);
		            	c6.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	table.addCell(c6);
		            	PdfPCell c7 = new PdfPCell(new Phrase(String.valueOf(search.getNoOfDays()),textFont));
		            	c7.setVerticalAlignment(Element.ALIGN_CENTER);
		            	c7.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	table.addCell(c7);
		            	PdfPCell c8 = new PdfPCell(new Phrase(search.getLeaveCode(),textFont));
		            	c8.setVerticalAlignment(Element.ALIGN_CENTER);
		            	c8.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	table.addCell(c8);
		            	PdfPCell c9 = new PdfPCell(new Phrase(search.getReason(),textFont));
		            	table.addCell(c9);
		            	PdfPCell c10 = new PdfPCell(new Phrase(search.getBalance(),textFont));
		            	c10.setVerticalAlignment(Element.ALIGN_CENTER);
		            	c10.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	table.addCell(c10);
		            	PdfPCell c11 = new PdfPCell(new Phrase(search.getReviewOn(),textFont));
		            	c11.setVerticalAlignment(Element.ALIGN_CENTER);
		            	c11.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	table.addCell(c11);
		            	PdfPCell c12 = new PdfPCell(new Phrase(search.getLeaveStatus(),textFont));
		            	c12.setVerticalAlignment(Element.ALIGN_CENTER);
		            	c12.setHorizontalAlignment(Element.ALIGN_CENTER);
		            	table.addCell(c12);
		            	if(flag.equalsIgnoreCase(CommonConstants.INCLUDE_HIERARCHY)) {
			            	PdfPCell c13 = new PdfPCell(new Phrase(search.getRmEmpName()+"/"+search.getRmEmpCode(),textFont));
			            	table.addCell(c13);
		            	}
		            	
		            }
		            
		            document.add(table);
		            document.close();
		            response.setContentType(CommonConstants.CONTENT_DOWNLOAD);
		            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		            response.setHeader(CommonConstants.CONTENT_DISPOSITION,CommonConstants.ATTACHEMENT_FILENAME+"Leave Report_" + formatter.format(new Date()) + CommonConstants.PDF_FILEFORMAT);
		            document.close();
		            baos.writeTo(response.getOutputStream());
		            
		            
		        } catch (Exception e) {
		            VmsLogging.logError(getClass(), "Export PDF Exception", e);
		        }

	}
	
}
