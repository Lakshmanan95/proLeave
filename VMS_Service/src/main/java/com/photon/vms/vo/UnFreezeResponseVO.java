package com.photon.vms.vo;

public class UnFreezeResponseVO {

	private String resultCode;
	private String resultDesc;
	private boolean validation = true;
	private String email;
	private String leaveCreditId;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public boolean isValidation() {
		return validation;
	}
	public void setValidation(boolean validation) {
		this.validation = validation;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLeaveCreditId() {
		return leaveCreditId;
	}
	public void setLeaveCreditId(String leaveCreditId) {
		this.leaveCreditId = leaveCreditId;
	}
}
