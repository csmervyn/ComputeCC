package org.github.mervyn.dataExtraction;

import java.util.List;

public class Param {
	private int groupNum;
	private int sampleSize;
	private List<Integer> list;
	private int maxElement;
 	public int getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}
	public int getSampleSize() {
		return sampleSize;
	}
	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}
	public List<Integer> getList() {
		return list;
	}
	public void setList(List<Integer> list) {
		this.list = list;
	}
	public Param(int groupNum, int sampleSize) {
		super();
		this.groupNum = groupNum;
		this.sampleSize = sampleSize;
	}
	public int getMaxElement() {
		return maxElement;
	}
	public void setMaxElement(int maxElement) {
		this.maxElement = maxElement;
	}
	
}
