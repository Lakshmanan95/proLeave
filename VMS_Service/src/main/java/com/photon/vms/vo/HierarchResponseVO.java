package com.photon.vms.vo;

import java.util.List;

public class HierarchResponseVO extends SuccessResponseVO{

	List<SearchHierarchy> searchList;

	public List<SearchHierarchy> getSearchList() {
		return searchList;
	}

	public void setSearchList(List<SearchHierarchy> searchList) {
		this.searchList = searchList;
	}
	
	
}
