package com.photon.vms.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
@XmlAccessorType (XmlAccessType.FIELD)
public class LeaveCreditDataVO {

	@XmlElement(name = "lmscoff")
	private List<LeaveCreditVO> leaveCredit = null;

	public List<LeaveCreditVO> getLeaveCredit() {
		return leaveCredit;
	}

	public void setLeaveCredit(List<LeaveCreditVO> leaveCredit) {
		this.leaveCredit = leaveCredit;
	}
}
