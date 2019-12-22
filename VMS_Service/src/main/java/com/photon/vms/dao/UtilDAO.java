package com.photon.vms.dao;

import java.util.List;

import com.photon.vms.vo.AutoApprovalVO;

/**
 * @author sureshkumar_e
 *
 */
public interface UtilDAO {

	List<AutoApprovalVO> getAutoApprovalLeaves() throws Exception;

}
