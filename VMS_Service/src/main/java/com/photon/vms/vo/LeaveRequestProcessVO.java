package com.photon.vms.vo;

public class LeaveRequestProcessVO {
	
	private String status;
	private String successMessage;
	private String errorCode;
	private String errorMessage;
	
	public LeaveRequestProcessVO(String code,String message){
		this.errorCode = code;
		this.errorMessage = message;
	}
	public LeaveRequestProcessVO(){};
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
}
