package org.github.mervyn.createShuLie;

import java.util.ArrayList;
import java.util.List;

public class Column {
	private List<Double> columnData;
	private int num;		//个数
	private int decimalPlace;//小数位的位置
	private double average;
	private double sd;
	
	private double targetAverage;
	private double targetSD;
	
 	public Column(){
		columnData = new ArrayList<Double>();
	}
	public List<Double> getColumnData() {
		return columnData;
	}
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getDecimalPlace() {
		return decimalPlace;
	}
	public void setDecimalPlace(int decimalPlace) {
		this.decimalPlace = decimalPlace;
	}
	public void setColumnData(List<Double> columnData) {
		this.columnData = columnData;
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
	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
	public double getSd() {
		return sd;
	}
	public void setSd(double sd) {
		this.sd = sd;
	}
	public double getTargetAverage() {
		return targetAverage;
	}
	public void setTargetAverage(double targetAverage) {
		this.targetAverage = targetAverage;
	}
	public double getTargetSD() {
		return targetSD;
	}
	public void setTargetSD(double targetSD) {
		this.targetSD = targetSD;
	}
	
	
}
