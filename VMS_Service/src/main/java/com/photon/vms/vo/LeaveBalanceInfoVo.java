package com.photon.vms.vo;

public class LeaveBalanceInfoVo {
	private int leaveTypeId;
	private String leaveCode;
	private String leaveTypeName;
	private Double openLeave;
	private Double creditedLeave;
	private Double usedLeave;
	private Double balanceLeave;
	private String locationCode;
	private String isDefault;
	private String isContinuous;
	private String disableFlag;
	private int maxLeaveOpen;
	private int maxLeaveCredit;
	
	public int getLeaveTypeId() {
		return leaveTypeId;
	}
	public void setLeaveTypeId(int leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}
	public String getLeaveCode() {
		return leaveCode;
	}
	public void setLeaveCode(String leaveCode) {
		this.leaveCode = leaveCode;
	}
	public String getLeaveTypeName() {
		return leaveTypeName;
	}
	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
	}
	public Double getOpenLeave() {
		return openLeave;
	}
	public void setOpenLeave(Double openLeave) {
		this.openLeave = openLeave;
	}
	public Double getCreditedLeave() {
		return creditedLeave;
	}
	public void setCreditedLeave(Double creditedLeave) {
		this.creditedLeave = creditedLeave;
	}
	public Double getUsedLeave() {
		return usedLeave;
	}
	public void setUsedLeave(Double usedLeave) {
		this.usedLeave = usedLeave;
	}
	public Double getBalanceLeave() {
		return balanceLeave;
	}
	public void setBalanceLeave(Double balanceLeave) {
		this.balanceLeave = balanceLeave;
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
	public String getIsContinuous() {
		return isContinuous;
	}
	public void setIsContinuous(String isContinuous) {
		this.isContinuous = isContinuous;
	}
	public String getDisableFlag() {
		return disableFlag;
	}
	public void setDisableFlag(String disableFlag) {
		this.disableFlag = disableFlag;
	}
	public int getMaxLeaveOpen() {
		return maxLeaveOpen;
	}
	public void setMaxLeaveOpen(int maxLeaveOpen) {
		this.maxLeaveOpen = maxLeaveOpen;
	}
	public int getMaxLeaveCredit() {
		return maxLeaveCredit;
	}
	public void setMaxLeaveCredit(int maxLeaveCredit) {
		this.maxLeaveCredit = maxLeaveCredit;
	}
}
