package org.github.mervyn.xgxsbdtc;


import java.util.ArrayList;
import java.util.List;

public class Group implements Comparable<Group>{
	private List<Double> columnList;
	private double average;
	private long average2; //代表不含有k列的i组数据均值
	private long absValue;	//代表|X11-X12|+|X11-X13|+|X12-X13|
	private long abs2Value;	//代表|X11-X1|

	private long abs3Value;	//代表|X1-X2|
	
	private long abs5Value; //代表|Xij - Xi|
	
	private long abs6Value; //代表|Xij -Xk|
	
	private double biaoZhun;
	
	private double mulBiaoZhun;
	
 	public Group(int column){
		columnList = new ArrayList<Double>(column);
	}

	public double getAverage() {
		return average;
	}


	public void setAverage(double average) {
		this.average = average;
	}

	public List<Double> getColumnList() {
		return columnList;
	}

	public long getAbsValue() {
		return absValue;
	}

	public void setAbsValue(long absValue) {
		this.absValue = absValue;
	}

	
	
	public void setAbs3Value(long abs3Value) {
		this.abs3Value = abs3Value;
	}

	public long getAbs2Value() {
		return abs2Value;
	}

	public void setAbs2Value(long abs2Value) {
		this.abs2Value = abs2Value;
	}

	public long getAbs3Value() {
		return abs3Value;
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

	public long getAbs5Value() {
		return abs5Value;
	}

	public void setAbs5Value(long abs5Value) {
		this.abs5Value = abs5Value;
	}

	public long getAbs6Value() {
		return abs6Value;
	}

	public void setAbs6Value(long abs6Value) {
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

	public long getAverage2() {
		return average2;
	}

	public void setAverage2(long average2) {
		this.average2 = average2;
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
