package com.photon.vms.vo;

import java.util.List;

public class EmployeeAdminLocResponse extends SuccessResponseVO {

	List<LocationAdminDetails> adminDetails;

	public List<LocationAdminDetails> getAdminDetails() {
		return adminDetails;
	}

	public void setAdminDetails(List<LocationAdminDetails> adminDetails) {
		this.adminDetails = adminDetails;
	}
	
	
}
