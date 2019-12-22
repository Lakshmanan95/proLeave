package com.photon.vms.vo;

public class PunchedHours {

	private String employeeCode;
	private String punchDate;
	private String prodMins;
	private String prdHrs;
	private int productionMins;
	
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getPunchDate() {
		return punchDate;
	}
	public void setPunchDate(String punchDate) {
		this.punchDate = punchDate;
	}
	public String getPrdHrs() {
		return prdHrs;
	}
	public void setPrdHrs(String prdHrs) {
		this.prdHrs = prdHrs;
	}
	public String getProdMins() {
		return prodMins;
	}
	public void setProdMins(String prodMins) {
		this.prodMins = prodMins;
	}
	public int getProductionMins() {
		return productionMins;
	}
	public void setProductionMins(int productionMins) {
		this.productionMins = productionMins;
	}
	
	
}
