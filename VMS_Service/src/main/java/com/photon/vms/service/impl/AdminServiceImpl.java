package com.photon.vms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.photon.vms.dao.AdminDAO;
import com.photon.vms.service.AdminService;
import com.photon.vms.vo.LeaveReminderInfo;
import com.photon.vms.vo.RevocationEmailVO;

@Service
public class AdminServiceImpl implements AdminService{
	@Autowired 
	AdminDAO adminDao;
	
	@Override
	public boolean isValidUnfrozenRecords(String fromdate, String toDate, int employeeId) throws Exception {
		return adminDao.isValidUnfrozenRecords(fromdate, toDate, employeeId);
	}

	@Override
	public List<LeaveReminderInfo> getReminderLeaveList() throws Exception {
		List<LeaveReminderInfo> response = adminDao.getReminderLeaveList();
		return response;
	}
	
	@Override
	public List<RevocationEmailVO> getRevocationListForAdmin() throws Exception {
		List<RevocationEmailVO> response = adminDao.getRevocationListForAdmin();
		return response;
	}
}
