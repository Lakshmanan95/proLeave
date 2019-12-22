package com.photon.vms.service;

import java.util.List;

import com.photon.vms.vo.AutoApprovalVO;

/**
 * @author sureshkumar_e
 *
 */
public interface UtilService {

	List<AutoApprovalVO> getAutoApprovalLeaves() throws Exception;

}
