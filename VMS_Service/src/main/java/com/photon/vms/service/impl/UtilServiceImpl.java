package com.photon.vms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.photon.vms.dao.UtilDAO;
import com.photon.vms.service.UtilService;
import com.photon.vms.vo.AutoApprovalVO;
/**
 * @author sureshkumar_e
 *
 */
@Service
public class UtilServiceImpl implements UtilService{

	@Autowired 
	UtilDAO utilDAO;
	
	@Override
	public List<AutoApprovalVO> getAutoApprovalLeaves() throws Exception{
		List<AutoApprovalVO> response = utilDAO.getAutoApprovalLeaves();
		return response;
	}

}
