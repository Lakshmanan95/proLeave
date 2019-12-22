package com.photon.vms.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lmscoff")
@XmlAccessorType (XmlAccessType.FIELD)
//@XmlCustomizer(XmlCustomiserUtil.class)
public class LeaveCreditVO {

	@XmlAttribute(name = "id")
	@XmlID
	private String id;
	@XmlElement(nillable=true)
	private String employeeid;
	@XmlElement(nillable=true)
	private String leavetype;
	@XmlElement(nillable=true)
	private String noofdays;
	@XmlElement(nillable=true)
	private String coffdate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}
	public String getLeavetype() {
		return leavetype;
	}
	public void setLeavetype(String leavetype) {
		this.leavetype = leavetype;
	}
	public String getNoofdays() {
		return noofdays;
	}
	public void setNoofdays(String noofdays) {
		this.noofdays = noofdays;
	}
	public String getCoffdate() {
		return coffdate;
	}
	public void setCoffdate(String coffdate) {
		this.coffdate = coffdate;
	}
}
