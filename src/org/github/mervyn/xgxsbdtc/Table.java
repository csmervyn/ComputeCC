package org.github.mervyn.xgxsbdtc;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private List<Line> lineList;
	
	private int groupNum;
	
	public Table(int lineNum){
		lineList = new ArrayList<Line>(lineNum);
	}
	public List<Line> getLineList() {
		return lineList;
	}

	public int getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
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
	
	public static double maxElementInTable(Table table){
		double max = 0;
		for(int lineNum = 0; lineNum < table.getLineList().size(); lineNum++){
			int maxGroupNum = table.getLineList().get(lineNum).getGroupList().size();
			for(int groupNum = 0; groupNum < maxGroupNum; groupNum++){
				int maxColumnNum = table.getLineList().get(lineNum).getGroupList().get(groupNum).getColumnList().size();
				for(int columnNum = 0; columnNum < maxColumnNum; columnNum++){
					double temp = table.getLineList().get(lineNum).getGroupList().get(groupNum).getColumnList().get(columnNum);
					if(temp > max){
						max = temp;
					}
				}
			}
		}
		return max;
	}
	
	public static double minElementInTable(Table table){
		double min = Long.MAX_VALUE;
		for(int lineNum = 0; lineNum < table.getLineList().size(); lineNum++){
			int maxGroupNum = table.getLineList().get(lineNum).getGroupList().size();
			for(int groupNum = 0; groupNum < maxGroupNum; groupNum++){
				int maxColumnNum = table.getLineList().get(lineNum).getGroupList().get(groupNum).getColumnList().size();
				for(int columnNum = 0; columnNum < maxColumnNum; columnNum++){
					double temp = table.getLineList().get(lineNum).getGroupList().get(groupNum).getColumnList().get(columnNum);
					if(temp < min){
						min = temp;
					}
					
				}
			}
		}
		return (int) min;
	}
	
	
	
}
