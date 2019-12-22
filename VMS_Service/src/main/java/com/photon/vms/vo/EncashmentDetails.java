package com.photon.vms.vo;

import java.util.List;

public class EncashmentDetails extends SuccessResponseVO{

	List<EncashmentPolicyAndOption> encashmentPolicy;
	List<EncashmentPolicyAndOption> encashmentOption;
	List<EncashmentDays> encashmentDays;
	
	public List<EncashmentPolicyAndOption> getEncashmentPolicy() {
		return encashmentPolicy;
	}
	public void setEncashmentPolicy(List<EncashmentPolicyAndOption> encashmentPolicy) {
		this.encashmentPolicy = encashmentPolicy;
	}
	public List<EncashmentPolicyAndOption> getEncashmentOption() {
		return encashmentOption;
	}
	public void setEncashmentOption(List<EncashmentPolicyAndOption> encashmentOption) {
		this.encashmentOption = encashmentOption;
	}
	public List<EncashmentDays> getEncashmentDays() {
		return encashmentDays;
	}
	public void setEncashmentDays(List<EncashmentDays> encashmentDays) {
		this.encashmentDays = encashmentDays;
	}
	
	
}
