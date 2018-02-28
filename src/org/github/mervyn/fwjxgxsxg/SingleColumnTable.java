package org.github.mervyn.fwjxgxsxg;

import java.util.ArrayList;
import java.util.List;



public class SingleColumnTable {
	private List<SingleColumnLine> lineList;
	private int columnNum;
	
	
	public SingleColumnTable(int lineNum){
		lineList = new ArrayList<SingleColumnLine>(lineNum);
	}
	public List<SingleColumnLine> getLineList() {
		return lineList;
	}
	public int getColumnNum() {
		return columnNum;
	}
	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < lineList.size()-1; i++){
			sb.append(lineList.get(i).toString() + "\n");
		}
		sb.append(lineList.get(lineList.size()-1).toString());
		return sb.toString();
	}
	
	
}
