package com.photon.vms.vo;

import java.util.List;

public class UnFreezeRequestProcessVO {

	private String status;
	private String successMessage;
	private String errorCode;
	private String errorMessage;
	private String validationMessage;
	private List<Integer> duplicateDateIndex;
	private List<String> duplicateDates;
	
	public UnFreezeRequestProcessVO(String code,String message){
		this.errorCode = code;
		this.errorMessage = message;
	}
	
	public UnFreezeRequestProcessVO(){};
	
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
	
	public String getValidationMessage() {
		return validationMessage;
	}
	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}
	public List<Integer> getDuplicateDateIndex() {
		return duplicateDateIndex;
	}
	public void setDuplicateDateIndex(List<Integer> duplicateDateIndex) {
		this.duplicateDateIndex = duplicateDateIndex;
	}
	public List<String> getDuplicateDates() {
		return duplicateDates;
	}
	public void setDuplicateDates(List<String> duplicateDates) {
		this.duplicateDates = duplicateDates;
	}
}
