package com.photon.vms.vo;

public class LeaveTypeVO {
	
	private String leaveTypeId;
	private String leaveCode;
	private String leaveType;
	private String locationCode;
	private String isDefault;
	private String isContinuous;
	
	public String getIsContinuous() {
		return isContinuous;
	}
	public void setIsContinuous(String isContinuous) {
		this.isContinuous = isContinuous;
	}
	public String getLeaveTypeId() {
		return leaveTypeId;
	}
	public void setLeaveTypeId(String leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}
	public String getLeaveCode() {
		return leaveCode;
	}
	public void setLeaveCode(String leaveCode) {
		this.leaveCode = leaveCode;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
}
