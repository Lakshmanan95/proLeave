package com.photon.vms.vo;

import java.util.Date;

public class HolidayListVO {
	private String holidayId;
	private Date holidayDate;
	private String holidayName;
	private String dayOfHoliday;
	private String locationName;
	
	public String getHolidayId() {
		return holidayId;
	}
	public void setHolidayId(String holidayId) {
		this.holidayId = holidayId;
	}
	public Date getHolidayDate() {
		return holidayDate;
	}
	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}
	public String getHolidayName() {
		return holidayName;
	}
	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}
	public String getDayOfHoliday() {
		return dayOfHoliday;
	}
	public void setDayOfHoliday(String dayOfHoliday) {
		this.dayOfHoliday = dayOfHoliday;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
}
