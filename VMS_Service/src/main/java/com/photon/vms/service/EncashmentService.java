package com.photon.vms.service;

import com.photon.vms.vo.EncashmentDetails;
import com.photon.vms.vo.SuccessResponseVO;

public interface EncashmentService {

	public EncashmentDetails getEncashDetails(String employeeNumber) throws Exception;
	
	public SuccessResponseVO leaveEncashmentProcess(String employeeNumber, int leaveEncashId) throws Exception;
}
