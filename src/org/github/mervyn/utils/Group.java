package org.github.mervyn.utils;


import java.util.ArrayList;
import java.util.List;

public class Group implements Comparable<Group>{
	private List<Long> columnList;
	private double average;
	private double average2; //代表不含有k列的i组数据均值
	private long absValue;	//代表|X11-X12|+|X11-X13|+|X12-X13|
	private double abs2Value;	//代表|X11-X1|

	private double abs3Value;	//代表|X1-X2|
	
	private double abs5Value; //代表|Xij - Xi|
	
	private double abs6Value; //代表|Xij -Xk|
	
	private double biaoZhun; //（y-均值）/标准差
	
	private double mulBiaoZhun;
	
 	public Group(int column){
		columnList = new ArrayList<Long>(column);
	}

	

	public double getAverage() {
		return average;
	}



	public void setAverage(double average) {
		this.average = average;
	}



	public List<Long> getColumnList() {
		return columnList;
	}

	public long getAbsValue() {
		return absValue;
	}

	public void setAbsValue(long absValue) {
		this.absValue = absValue;
	}

	
	
	

	public double getAbs3Value() {
		return abs3Value;
	}



	public void setAbs3Value(double abs3Value) {
		this.abs3Value = abs3Value;
	}



	

	@Override
	public int compareTo(Group group) {
		if(group == null){
			return 0;
		}else{
			if(absValue == group.getAbsValue()){
				return 0;
			}else if(absValue < group.getAbsValue()){
				return -1;
			}else{
				return 1;
			}
		}
	}

	

	public double getAbs5Value() {
		return abs5Value;
	}



	public void setAbs5Value(double abs5Value) {
		this.abs5Value = abs5Value;
	}



	
	public double getAbs6Value() {
		return abs6Value;
	}



	public void setAbs6Value(double abs6Value) {
		this.abs6Value = abs6Value;
	}



	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < columnList.size(); i++){
			sb.append(columnList.get(i) + " ");
		}
		//b.append("average=" + average +" ");
		//sb.append("absValue=" + absValue + " ** ");
		//sb.append("abs2Value=" + abs2Value+" ");
		//sb.append("abs3Value=" + abs3Value+" ");
		return sb.toString();
	}

	
	public double getAverage2() {
		return average2;
	}



	public void setAverage2(double average2) {
		this.average2 = average2;
	}



	public double getAbs2Value() {
		return abs2Value;
	}



	public void setAbs2Value(double abs2Value) {
		this.abs2Value = abs2Value;
	}



	public double getBiaoZhun() {
		return biaoZhun;
	}

	public void setBiaoZhun(double biaoZhun) {
		this.biaoZhun = biaoZhun;
	}

	public double getMulBiaoZhun() {
		return mulBiaoZhun;
	}

	public void setMulBiaoZhun(double mulBiaoZhun) {
		this.mulBiaoZhun = mulBiaoZhun;
	}
	
	
	
}
