package com.photon.vms.vo;

import java.util.List;

public class AutoApprovalEmployeeVO extends SuccessResponseVO{

	List<AutoApprovalEmpDtlVO> employee;

	public List<AutoApprovalEmpDtlVO> getEmployee() {
		return employee;
	}

	public void setEmployee(List<AutoApprovalEmpDtlVO> employee) {
		this.employee = employee;
	}
}
