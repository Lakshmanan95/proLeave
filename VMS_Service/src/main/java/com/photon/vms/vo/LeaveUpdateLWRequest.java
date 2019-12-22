package com.photon.vms.vo;

public class LeaveUpdateLWRequest {

	private String employeeCode;
	private int locationId;
	private int leaveTypeId;
	private double noOfDays;
	private String compOffDate;
	
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public int getLeaveTypeId() {
		return leaveTypeId;
	}
	public void setLeaveTypeId(int leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}
	public double getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(double noOfDays) {
		this.noOfDays = noOfDays;
	}
	public String getCompOffDate() {
		return compOffDate;
	}
	public void setCompOffDate(String compOffDate) {
		this.compOffDate = compOffDate;
	}
}
