package org.github.mervyn.utils;

import java.util.ArrayList;
import java.util.List;

public class Line {
	//行号
	private int num;
	private List<Group> groupList;
	private boolean isDelete;
	
	private double b;//Xk标*Xj标
	


	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public Line(int groupNum,int num){
		groupList = new ArrayList<Group>(groupNum);
		this.num = num;
	}

	public List<Group> getGroupList() {
		return groupList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < groupList.size(); i++){
			sb.append(groupList.get(i).toString() + " ");
		}
		return sb.toString();
	}

	public int getNum() {
		return num;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + num;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (num != other.num)
			return false;
		return true;
	}

	
	
}
