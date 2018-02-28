package org.github.mervyn.xgxsbdtc;

import java.util.ArrayList;
import java.util.List;

public class Result {
	private List<Double> correlationCoefficientList;
	
	public Result(){
		correlationCoefficientList = new ArrayList<Double>();
	}

	public List<Double> getCorrelationCoefficientList() {
		return correlationCoefficientList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < correlationCoefficientList.size(); i++){
			sb.append(correlationCoefficientList.get(i) + ",");
		}
		return sb.toString();
	}
	
	
}
