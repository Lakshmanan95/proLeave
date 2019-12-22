package com.photon.vms.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.dao.impl.FreezeDAOImpl;
import com.photon.vms.emailTemplate.EmailService;
import com.photon.vms.emailTemplate.EmailTemplateService;
import com.photon.vms.service.LeaveCreditService;
import com.photon.vms.util.DateTimeUtils;
import com.photon.vms.vo.AutoApprovalEmpDtlVO;
import com.photon.vms.vo.AutoApprovalEmployeeVO;
import com.photon.vms.vo.LeaveBalanceInfoVo;
import com.photon.vms.vo.LeaveCreditDataVO;
import com.photon.vms.vo.LeaveCreditLog;
import com.photon.vms.vo.LeaveCreditVO;
import com.photon.vms.vo.LeaveHistoryVO;
import com.photon.vms.vo.LeaveType;
import com.photon.vms.vo.LeaveTypeResponse;
import com.photon.vms.vo.LeaveUpdateLWRequest;
import com.photon.vms.vo.LocationIdRequestVO;
import com.photon.vms.vo.RequestHistoryVO;
import com.photon.vms.vo.RequestLeaveInsertUpdateVo;
import com.photon.vms.vo.RequestParameterVO;
import com.photon.vms.vo.SuccessResponseVO;
import com.photon.vms.vo.UnFreezeResponseVO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/leaveCredit")
public class LeaveCreditController {

	@Autowired
	LeaveCreditService leaveCreditService;

	@Autowired
	DateTimeUtils dateTimeUtils;

	@Autowired
	FreezeDAOImpl freezeDAOImpl;

	@Autowired
	EmailTemplateService emailTemplateService;

	@Autowired
	EmailService emailService;

	@RequestMapping(value = "/getEmployeeLeaveBalance", method = RequestMethod.POST)
	public Map<String, ArrayList<?>> getEmployeeLeaveBalance(@RequestBody RequestParameterVO requestParameter,
			ServletRequest req) {
		try {
			String employeeNumber = requestParameter.getEmployeeNumber();
			VmsLogging.logInfo(getClass(), "Employee - Leave info " + employeeNumber);

			Map<String, ArrayList<?>> response = leaveCreditService.getEmployeeLeaveBalance(employeeNumber);
			return response;
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Leave Balance info Exception ",
					new VmsApplicationException(e.getMessage()));
			return null;
		}
	}

	@RequestMapping(value = "/getLeaveHistory", method = RequestMethod.POST)
	public Map<String, ArrayList<?>> getLeaveHistory(@RequestBody RequestHistoryVO requestParameter,
			ServletRequest req) {
		try {
			String employeeNumber = requestParameter.getEmployeeNumber();
			requestParameter.setEmployeeNumber(employeeNumber);
			VmsLogging.logInfo(getClass(), "Employee - Leave History info " + employeeNumber);

			Map<String, ArrayList<?>> response = leaveCreditService.getLeaveHistory(requestParameter);
			return response;
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Leave History info Exception ",	new VmsApplicationException(e.getMessage()));
			return null;
		}
	}

	@RequestMapping(value = "/insertUpdateLeave", method = RequestMethod.POST)
	public SuccessResponseVO insertUpdateLeave(@RequestBody RequestLeaveInsertUpdateVo requestParameter,
			ServletRequest req) {
		SuccessResponseVO response = new SuccessResponseVO();
		try {
 			String employeeNumber = requestParameter.getLoginEmployeeCode();
			VmsLogging.logInfo(getClass(), "Employee - Leave History info " + employeeNumber);
			if (employeeNumber != null) {
				int open = 0;
				int credit = 0;
				Double sumOfOpenCredit = null;
				String leaveCode = null;

				Map<String, ArrayList<?>> countResult = leaveCreditService.getEmployeeLeaveBalance(requestParameter.getEmployeeNumber());
				if (countResult != null && !countResult.isEmpty()) {
					ArrayList<LeaveBalanceInfoVo> leaveBalance = (ArrayList<LeaveBalanceInfoVo>) countResult.get("EmpLeaveBalaceInfo");
					for (LeaveBalanceInfoVo leaveList : leaveBalance) {
						if (leaveList.getLeaveTypeId() == requestParameter.getLeaveTypeId()) {
							open = leaveList.getMaxLeaveOpen();
							credit = leaveList.getMaxLeaveCredit();
							sumOfOpenCredit = requestParameter.getLeaveOpen() + requestParameter.getLeaveCredit();
							leaveCode = leaveList.getLeaveCode();
							VmsLogging.logInfo(getClass(),"open:" + open + " credit:" + credit + " leaveCode :" + leaveCode);
						}
					}
					if (open != -999 && credit != -999) {
						if (open == 0 && open < requestParameter.getLeaveOpen()) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorCode("001");
							response.setErrorMessage("This leave type does not have an open balance");
							return response;
						}
						if (credit == 0 && credit < requestParameter.getLeaveCredit()) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorCode("001");
							response.setErrorMessage("This leave type does not have an credit balance");
							return response;
						}
						if (leaveCode.equals("CL")) {
							if (open < (requestParameter.getLeaveOpen() + requestParameter.getLeaveCredit())) {
								response.setStatus(CommonConstants.ERROR);
								response.setErrorCode("001");
								response.setErrorMessage("Open+Credit leaves has exceeded 10 days");
								return response;
							} else if (sumOfOpenCredit < requestParameter.getLeaveUsed()) {
								response.setStatus(CommonConstants.ERROR);
								response.setErrorCode("001");
								response.setErrorMessage(CommonConstants.USED_LEAVES_EXECEEDED);
								return response;
							}
						} else {
							if (open < requestParameter.getLeaveOpen()) {
								response.setStatus(CommonConstants.ERROR);
								response.setErrorCode("001");
								response.setErrorMessage("You cannot credit more than " + open + " days of leave");
								return response;
							} else if (credit < requestParameter.getLeaveCredit()) {
								response.setStatus(CommonConstants.ERROR);
								response.setErrorCode("001");
								response.setErrorMessage("You cannot credit more than " + credit + " days of leave");
								return response;
							} else if (sumOfOpenCredit < requestParameter.getLeaveUsed()) {
								response.setStatus(CommonConstants.ERROR);
								response.setErrorCode("001");
								response.setErrorMessage(CommonConstants.USED_LEAVES_EXECEEDED);
								return response;
							}
						}
					} else if (open >= 0 && credit == -999 || open == -999 && credit >= 0) {
						if (open == 0 && open < requestParameter.getLeaveOpen()) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorCode("001");
							response.setErrorMessage("This leave type does not have an open balance");
							return response;
						}
						if (credit == 0 && credit < requestParameter.getLeaveCredit()) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorCode("001");
							response.setErrorMessage("This leave type does not have an credit balance");
							return response;
						}
						if (open != 0 && open < requestParameter.getLeaveOpen() && open != -999) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorCode("001");
							response.setErrorMessage("You cannot credit more than " + open + " days of leave");
							return response;
						}
						if (credit != 0 && credit < requestParameter.getLeaveCredit() && credit != -999) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorCode("001");
							response.setErrorMessage("You cannot credit more than " + credit + " days of leave");
							return response;
						}
						if (sumOfOpenCredit < requestParameter.getLeaveUsed()) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorCode("001");
							response.setErrorMessage(CommonConstants.USED_LEAVES_EXECEEDED);
							return response;
						}
					} else if (open == -999 && credit == -999) {
						if (leaveCode.equals("COFF") && requestParameter.getLeaveUsed() > sumOfOpenCredit) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorCode("001");
							response.setErrorMessage(
									CommonConstants.USED_LEAVES_EXECEEDED);
							return response;
						}
						if (requestParameter.getLeaveUsed() > sumOfOpenCredit) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorCode("001");
							response.setErrorMessage(CommonConstants.USED_LEAVES_EXECEEDED);
							return response;
						}
					}
					if (leaveCode.equals("PL") || leaveCode.equals("PTO") || leaveCode.equals("APTO")) {
						UnFreezeResponseVO resultSet = leaveCreditService.insertUpdateLeave(requestParameter);
						if (resultSet.getResultCode().equals("ERROR-001") || resultSet.getResultCode().equals("ERROR-002")) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorCode("03");
							response.setErrorMessage(resultSet.getResultDesc());
						} else {
							response.setStatus(CommonConstants.SUCCESS);
							response.setSuccessMessage("Record is updated successfully");
						}
					} else {
						boolean decimalValue = false;
						if (requestParameter.getLeaveOpen() % 1 != 0 || requestParameter.getLeaveCredit() % 1 != 0 || requestParameter.getLeaveUsed() % 1 != 0)
							decimalValue = true;
						if (decimalValue) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorMessage("This leave type should not have decimal value");
							response.setErrorCode("001");
							return response;
						}
						UnFreezeResponseVO resultSet = leaveCreditService.insertUpdateLeave(requestParameter);
						if (resultSet.getResultCode().equals("ERROR-001")
								|| resultSet.getResultCode().equals("ERROR-002")) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorCode("03");
							response.setErrorMessage(resultSet.getResultDesc());
						} else {
							response.setStatus(CommonConstants.SUCCESS);
							response.setSuccessMessage("Record is updated successfully");
						}
					}
				} else {
					response.setStatus(CommonConstants.ERROR);
					response.setErrorCode("001");
					response.setErrorMessage("Leave Balance Data Empty");
					VmsLogging.logError(getClass(), "Leave Balance Data Empty", null);
				}
			} else {
				response.setStatus(CommonConstants.ERROR);
				response.setErrorCode("001");
				response.setErrorMessage(CommonConstants.INVALID_DATA);
				VmsLogging.logError(getClass(), CommonConstants.INVALID_DATA, null);
			}
			return response;
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Leave Insert Or update info Exception ",
					new VmsApplicationException(e.getMessage()));
			return null;
		}
	}

	@RequestMapping(value = "/bulkUpload", method = RequestMethod.POST)
	public SuccessResponseVO bulkUpload(@RequestParam("file") MultipartFile uploader,
			@RequestParam(value = "employeeNumber", defaultValue = "") String employeeNumber, ServletRequest req) {
		SuccessResponseVO response = new SuccessResponseVO();
		try {
			XSSFWorkbook offices = null;
			HSSFSheet sheet = null;
			XSSFSheet xssfSheet = null;
			Row row = null;
			int rowNo = 0;
			if (employeeNumber != null) {
				String[] fileName = uploader.getOriginalFilename().split("\\.");
				if (fileName[1].equals("xls")) {
					HSSFWorkbook workbook = new HSSFWorkbook(uploader.getInputStream());
					sheet = workbook.getSheetAt(0);
					rowNo = sheet.getLastRowNum();
				} else if(fileName[1].equals("xlsx")){
					offices = new XSSFWorkbook(uploader.getInputStream());
					xssfSheet = offices.getSheetAt(0);
					rowNo = xssfSheet.getLastRowNum();
				}else {
					response.setErrorCode("03");
					response.setErrorMessage("Invalid File format");
					response.setStatus(CommonConstants.ERROR);
					return response;
				}

				LeaveCreditDataVO addLeave = new LeaveCreditDataVO();
				addLeave.setLeaveCredit(new ArrayList<LeaveCreditVO>());
				for (int i = 1; i <= rowNo; i++) {
					if (fileName[1].equals("xls"))
						row = sheet.getRow(i);
					else
						row = xssfSheet.getRow(i);
				
					LeaveCreditVO leave = new LeaveCreditVO();
					Cell cellSno = row.getCell(0);
					if(cellSno == null || cellSno.toString().isEmpty()) {
						break;
					}
					String sno[] = cellSno.toString().split("\\.");
					Cell codeValue = row.getCell(1);
					Cell cellLeave = row.getCell(2);
					Cell cellDays = row.getCell(3);
					Cell cellDate = row.getCell(4);
					try {
						if (cellLeave.toString().equals("COFF")) {
							if(cellDate.getCellType() != Cell.CELL_TYPE_BLANK) {
								String date = dateTimeUtils.convertDateFormat(cellDate.getDateCellValue());
								if(date.equals("false")) {
									response.setStatus(CommonConstants.ERROR);
									response.setErrorMessage("Line "+sno[0]+"-Invalid date format.Format should be YYYY-MM-DD");
									response.setErrorCode("03");
									return response;
								}else {
									leave.setCoffdate(date);
								}
							}
						}
					}
					catch(Exception e) {
						response.setStatus(CommonConstants.ERROR);
						response.setErrorMessage("Line "+sno[0]+"-Invalid date format.Format should be YYYY-MM-DD");
						response.setErrorCode("03");
						return response;
					}
					String leaveType = cellLeave.toString();
					String[] splitCode = codeValue.toString().split("\\.");
					String employeeCode = null;
					if (splitCode.length > 0) {
						employeeCode = splitCode[0];
					} else {
						employeeCode = codeValue.toString();
					}
					String noOfDays = cellDays.toString();					
					leave.setId(String.valueOf(i));
					leave.setEmployeeid(employeeCode);
					leave.setLeavetype(leaveType);
					leave.setNoofdays(noOfDays);

					addLeave.getLeaveCredit().add(leave);
				}
				UnFreezeResponseVO resultSet = leaveCreditService.bulkUpload(employeeNumber, addLeave);
				if (resultSet.getLeaveCreditId() != null && resultSet.getResultCode().equals(CommonConstants.SUCCESS)) {

					List<LeaveCreditLog> leaveCreditLog = leaveCreditService
							.leaveCreditLog(resultSet.getLeaveCreditId());
					String csvFileStream = getCSVFileStream(leaveCreditLog);
					Map<String, String> employeeDetails = freezeDAOImpl.getEmployeeMail(employeeNumber);
					Map<String, Object> map = new HashMap<>();
					map.put("userName", employeeDetails.get("employeeName"));
					String messageBody = emailTemplateService.getEmailTemplate("bulkUploadTemplate.vm", map);
					emailService.sendEmailWithAttachement(employeeDetails.get("email"),
							CommonConstants.BULK_UPLOAD_SUBJECT, messageBody, csvFileStream);
					response.setStatus(CommonConstants.SUCCESS);
					response.setSuccessMessage("Leaves have been uploaded successfully");
				} else {
					response.setStatus(CommonConstants.ERROR);
					response.setErrorMessage(CommonConstants.INVALID_DATA);
					response.setErrorCode("03");
				}
			} else {
				response.setStatus(CommonConstants.ERROR);
				response.setErrorMessage(CommonConstants.INVALID_DATA);
				response.setErrorCode("03");
			}

		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Leave Insert Or update info Exception ",
					new VmsApplicationException(e.getMessage()));
			response.setStatus(CommonConstants.ERROR);
			response.setErrorMessage(CommonConstants.INVALID_DATA);
			response.setErrorCode("03");
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/leaveUpdateLW")
	public SuccessResponseVO leaveUpdateLW(@RequestBody LeaveUpdateLWRequest request, HttpServletRequest req) {
		SuccessResponseVO response = new SuccessResponseVO();
		try {
			String employeeNumber = request.getEmployeeCode();
			boolean decimalValue = false;
			if (employeeNumber != null) {
				request.setEmployeeCode(employeeNumber);
				request.setCompOffDate(null);
				if (request.getLeaveTypeId() != 0 && request.getLocationId() != 0 && request.getNoOfDays() != 0) {
					if(request.getLeaveTypeId() != 2 && request.getLeaveTypeId() != 4) {
						if(request.getNoOfDays()%1 != 0)
							decimalValue = true;
						if(decimalValue) {
							response.setStatus(CommonConstants.ERROR);
							response.setErrorMessage("This leave type should not have decimal value");
							response.setErrorCode("03");
							return response;
						}
					}
					UnFreezeResponseVO resultSet = leaveCreditService.leaveUpdateLW(request);
					VmsLogging.logInfo(getClass(), "resultSet " + resultSet.getResultCode());
					String[] resultCode = resultSet.getResultCode().split("-");
					if (resultCode[0].equals("ERROR")) {
						response.setStatus(CommonConstants.ERROR);
						response.setErrorMessage(CommonConstants.INVALID_DATA);
						response.setErrorCode("03");
					} else {
						response.setStatus(CommonConstants.SUCCESS);
						response.setSuccessMessage("Mass updation is done successfully");
					}
				} else {
					response.setStatus(CommonConstants.ERROR);
					response.setErrorMessage(CommonConstants.INVALID_DATA);
					response.setErrorCode("03");
					return response;
				}
			} else {
				response.setStatus(CommonConstants.ERROR);
				response.setErrorMessage(CommonConstants.INVALID_DATA);
				response.setErrorCode("03");
			}
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Location Wise - Leave update info Exception ",
					new VmsApplicationException(e.getMessage()));
			response.setStatus(CommonConstants.ERROR);
			response.setErrorCode("03");
		}
		return response;
	}

	public String getCSVFileStream(List<LeaveCreditLog> leaveCreditLog) {
		String streamOfData = null;
		StringWriter sw = new StringWriter();
		try {
			CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT.withHeader("SNO", "Employee Code",
					"Employee Name", "Leave Type", "Credit Value", "Coff Date", "Credited Date", "Upload Status", "Comments"));
			int i = 1;
			for (LeaveCreditLog leaveLog : leaveCreditLog) {
				csvPrinter.printRecord(String.valueOf(i), leaveLog.getEmployeeCode(), leaveLog.getEmployeeName(),
						leaveLog.getLeaveType(), leaveLog.getCreditValue(), leaveLog.getCompOffDate(),
						leaveLog.getCreditedDate(),leaveLog.getUploadStatus(), leaveLog.getComments());
				i++;
			}
			streamOfData = sw.toString();
		} catch (IOException e) {
			VmsLogging.logError(getClass(), "Leave Credit Log Excel info Exception ",
					new VmsApplicationException(e.getMessage()));
		}

		return streamOfData;
	}

	@RequestMapping(value = "/getAdminReport", method = RequestMethod.GET)
	public void getAdminReport(@RequestParam (value="employeeNumber", defaultValue="") String employeeNumber,@RequestParam (value="flag", defaultValue="") String flag,@RequestParam (value="fromDate", defaultValue="") String fromDate,@RequestParam (value="toDate", defaultValue="") String toDate, ServletRequest req,
			HttpServletResponse servletResponse) {
		try {
			
			VmsLogging.logInfo(getClass(), "Admin Report info " + employeeNumber);
			ArrayList<LeaveHistoryVO> response = leaveCreditService.getAdminReport(employeeNumber,flag,fromDate,toDate);
			pushDatatoCSV(response, servletResponse);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Admin - Report info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/getLeaveType")
	public LeaveTypeResponse getLeaveTypeByLocation(@RequestBody LocationIdRequestVO request, HttpServletRequest req ) {
		LeaveTypeResponse response = new LeaveTypeResponse();
		try {
			String employeeNumber = request.getEmployeeNumber();
			if(employeeNumber != null) {
				List<LeaveType> leaveType = leaveCreditService.getLeaveTypeByLocation(request.getLocationId());
				if(leaveType != null && !leaveType.isEmpty()) {
				response.setLeaveType(leaveType);
				response.setStatus("Success");
				}
				else {
					response.setStatus(CommonConstants.ERROR);
					response.setErrorMessage(CommonConstants.INVALID_DATA);
					response.setErrorCode("03");
				}
			}
			else {
				response.setStatus(CommonConstants.ERROR);
				response.setErrorMessage(CommonConstants.INVALID_DATA);
				response.setErrorCode("03");
			}
		}catch(Exception e) {
			VmsLogging.logError(getClass(), "Admin - Report info Exception ",
					new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	private void pushDatatoCSV(ArrayList<LeaveHistoryVO> reportGenerateVO, HttpServletResponse servletResponse) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			servletResponse.setContentType(CommonConstants.CONTENT_DOWNLOAD);
			servletResponse.setHeader(CommonConstants.CONTENT_DISPOSITION,
					"attachment; filename=Admin Report_" + formatter.format(new Date()) + ".csv");
			CSVPrinter csvPrinter = new CSVPrinter(servletResponse.getWriter(),
					CSVFormat.DEFAULT.withHeader("Sno", "Employee ID", "Employee Name","Location Name",
							"Updated By Admin Name", "Updated Date", "Leave Category",
							"Existing Value", "New Value", "Leave Code", "Leave Type", "Comments"));
				for (LeaveHistoryVO record : reportGenerateVO) {
					csvPrinter.printRecord(record.getSno(),record.getEmployeeId(),
							record.getEmployeeName(),record.getLocationName(),record.getCreatedByName(),
							record.getCreatedByDate(), record.getLeaveCategory(),
							record.getExistingValue(), record.getNewValue(), record.getLeaveCode(), record.getLeaveType(),
							record.getComments());
				}
				servletResponse.getWriter().flush();
				csvPrinter.flush();
				csvPrinter.close();
		} catch (FileNotFoundException e) {
			VmsLogging.logError(getClass(), "Report file not found Exception ",
					new VmsApplicationException(e.getMessage()));
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Generate Report Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	@RequestMapping(value = "/getLeaveCreditCOFFDate", method = RequestMethod.POST)
	public  Map<String, ArrayList<?>> getLeaveCreditCOFFDate(@RequestBody RequestParameterVO requestParameter, HttpServletRequest req ) {
		try {
			VmsLogging.logInfo(getClass(), "Leave Credit COFF Date info " + requestParameter.getEmployeeNumber());

			 Map<String, ArrayList<?>> response = leaveCreditService.getLeaveCreditCOFFDate(requestParameter.getEmployeeNumber());
			return response;
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Leave Credit COFF Date info Exception ",	new VmsApplicationException(e.getMessage()));
			return null;
		}
	}
	
	@RequestMapping(value = "/getAutoApprovalEmpDtl", method = RequestMethod.POST)
	public Map<String, ArrayList<?>> getAutoApprovalEmpDtl(@RequestBody RequestParameterVO requestParameter, HttpServletRequest req) {
		try {
			String employeeNumber = requestParameter.getEmployeeNumber();
			VmsLogging.logInfo(getClass(), "Employee - Leave info " + employeeNumber);

			Map<String, ArrayList<?>> response = leaveCreditService.getAutoApprovalEmpDtl();
			return response;
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Employee - Auto Approval info Exception ",
					new VmsApplicationException(e.getMessage()));
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/autoApproval")
	public SuccessResponseVO autoApproval(@RequestBody RequestParameterVO requestParameter, HttpServletRequest req) {
		SuccessResponseVO response = new SuccessResponseVO();
		try {
			String loginEmployeeNumber = requestParameter.getLoginEmployeeNumber();
			if(loginEmployeeNumber != null && requestParameter.getEmployeeNumber() != null) {
				UnFreezeResponseVO resultSet = leaveCreditService.autoApproval(loginEmployeeNumber, requestParameter.getEmployeeNumber(),requestParameter.getIsActive());
				response.setStatus(resultSet.getResultCode());
				if(resultSet.getResultCode().equals("SUCCESS"))
					response.setSuccessMessage(resultSet.getResultDesc());
				else
					response.setErrorMessage(resultSet.getResultDesc());
			}else {
				response.setStatus(CommonConstants.FAILURE);
				response.setErrorMessage(CommonConstants.INVALID_DATA);
				response.setErrorCode("03");
			}
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Add or Update Employee - Auto Approval info Exception ",new VmsApplicationException(e.getMessage()));
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getAutoApprovalEmployee")
	public AutoApprovalEmployeeVO getAutoApproval(HttpServletRequest req) {
		AutoApprovalEmployeeVO response = new AutoApprovalEmployeeVO();
		try {
			List<AutoApprovalEmpDtlVO> employee = leaveCreditService.getAutoApprovalEmployee();
			VmsLogging.logDebug(getClass(), "Get Employee List - Auto Approval");
			if(employee != null) {
				response.setEmployee(employee);
				response.setStatus(CommonConstants.SUCCESS);
			}
			else
				response.setStatus(CommonConstants.ERROR);
		}
		catch(Exception e) {
			VmsLogging.logError(getClass(), "Get Employee List - Auto Approval info Exception ",new VmsApplicationException(e.getMessage()));
			}
		return response;
	}
}
