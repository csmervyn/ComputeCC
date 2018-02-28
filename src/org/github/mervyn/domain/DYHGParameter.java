package org.github.mervyn.domain;

import java.util.ArrayList;
import java.util.List;

public class DYHGParameter {
	private int dependentVar;	//因变量
	private List<Integer> independentVarList; //自变量
	

	public DYHGParameter() {
		independentVarList = new ArrayList<Integer>();
	}


	public int getDependentVar() {
		return dependentVar;
	}


	public void setDependentVar(int dependentVar) {
		this.dependentVar = dependentVar;
	}


	public List<Integer> getIndependentVarList() {
		return independentVarList;
	}


	public void setIndependentVarList(List<Integer> independentVarList) {
		this.independentVarList = independentVarList;
	}
	
}
