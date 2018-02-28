package org.github.mervyn.domain;

import java.util.ArrayList;
import java.util.List;

public class GroupDYHGParam {
	private int num;
	private List<DYHGParameter> dyhgList  = new ArrayList<DYHGParameter>();
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public List<DYHGParameter> getDyhgList() {
		return dyhgList;
	}
	public void setDyhgList(List<DYHGParameter> dyhgList) {
		this.dyhgList = dyhgList;
	}
	
	
}
