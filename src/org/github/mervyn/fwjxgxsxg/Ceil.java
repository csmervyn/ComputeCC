package org.github.mervyn.fwjxgxsxg;

public class Ceil {
	private boolean isLong;
	private long longValue;
	private double doubleValue;
	
	private int digit;
	
	private boolean isAlter;
	
	private double biaoZhun;

 	public boolean isLong() {
		return isLong;
	}

	public void setLong(boolean isLong) {
		this.isLong = isLong;
	}

	public long getLongValue() {
		return longValue;
	}

	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public int getDigit() {
		return digit;
	}

	public void setDigit(int digit) {
		this.digit = digit;
	}

	public boolean isAlter() {
		return isAlter;
	}

	public void setAlter(boolean isAlter) {
		this.isAlter = isAlter;
	}

	@Override
	public String toString() {
		if(isLong){
			return ""+longValue;
		}else{
			return ""+doubleValue;
		}
	}

	public double getBiaoZhun() {
		return biaoZhun;
	}

	public void setBiaoZhun(double biaoZhun) {
		this.biaoZhun = biaoZhun;
	}
	
	
	
}
