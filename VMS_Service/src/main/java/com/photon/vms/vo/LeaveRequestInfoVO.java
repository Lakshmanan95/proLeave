package com.photon.vms.vo;

public class LeaveRequestInfoVO {
	
	private String status;
	private String errorCode;
	private String errorMessage;
	
	public LeaveRequestInfoVO(String code,String message){
		this.errorCode = code;
		this.errorMessage = message;
	}
	
	public LeaveRequestInfoVO() {}

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
}
