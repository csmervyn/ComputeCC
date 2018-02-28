package org.github.mervyn.createShuLie;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private int groupNum;
	private List<Column> listData;
	private int maxLineNum;
	public Table(int groupNum){
		this.groupNum = groupNum;
		listData = new ArrayList<Column>();
	}
	public List<Column> getListData() {
		return listData;
	}
	public void setListData(List<Column> listData) {
		this.listData = listData;
	}
	public int getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}
	public int getMaxLineNum() {
		return maxLineNum;
	}
	public void setMaxLineNum(int maxLineNum) {
		this.maxLineNum = maxLineNum;
	}
	
	
	
}
