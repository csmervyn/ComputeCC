package org.github.mervyn.domain;

public class NormalDistribution1 {
	private double mean;	//均值
	private double sd;		//标准差
	private int num;		//个数
	private int decimalPlace;//小数位的位置
	private double max;		//最大值
	private double min;		//最小值
	private boolean flag;	//标志max和min是否存在
	
 	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
	}
	public double getSd() {
		return sd;
	}
	public void setSd(double sd) {
		this.sd = sd;
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
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}
