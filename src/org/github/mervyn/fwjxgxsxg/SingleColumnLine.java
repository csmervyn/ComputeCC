package org.github.mervyn.fwjxgxsxg;

import java.util.ArrayList;
import java.util.List;


public class SingleColumnLine {
	//行号
	private int num;
	private List<Ceil> columnList;
	
	private double b; //Xi标*Xk标
	
	private double bBiao;//b标
	
	private double bBiaoMulXibiao;//B标*Xi标
	
  	public SingleColumnLine(int columnNum, int num){
		this.num = num;
		columnList = new ArrayList<Ceil>();
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<Ceil> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<Ceil> columnList) {
		this.columnList = columnList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < columnList.size()-1; i++){
			sb.append(columnList.get(i).toString() + ", ");
		}
		sb.append(columnList.get(columnList.size()-1).toString());
		return sb.toString();
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getbBiao() {
		return bBiao;
	}

	public void setbBiao(double bBiao) {
		this.bBiao = bBiao;
	}

	public double getbBiaoMulXibiao() {
		return bBiaoMulXibiao;
	}

	public void setbBiaoMulXibiao(double bBiaoMulXibiao) {
		this.bBiaoMulXibiao = bBiaoMulXibiao;
	}

	
	
}
