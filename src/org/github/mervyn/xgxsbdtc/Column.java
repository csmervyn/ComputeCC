package org.github.mervyn.xgxsbdtc;

import java.util.ArrayList;
import java.util.List;

public class Column {
	private List<Double> columnData;
	public Column(){
		columnData = new ArrayList<Double>();
	}
	public List<Double> getColumnData() {
		return columnData;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < columnData.size()-1; i++){
			sb.append(columnData.get(i)+",");
			
		}
		sb.append(columnData.get(columnData.size()-1));
		return sb.toString();
	}
	
}
