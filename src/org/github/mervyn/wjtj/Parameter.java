package org.github.mervyn.wjtj;

public class Parameter {
	private int dependentVar;
	private int independentVar;
	private int adjustVar;
	private double correlationCoefficient;
	private double percent;
	public int getDependentVar() {
		return dependentVar;
	}
	public void setDependentVar(int dependentVar) {
		this.dependentVar = dependentVar;
	}
	public int getIndependentVar() {
		return independentVar;
	}
	public void setIndependentVar(int independentVar) {
		this.independentVar = independentVar;
	}
	public int getAdjustVar() {
		return adjustVar;
	}
	public void setAdjustVar(int adjustVar) {
		this.adjustVar = adjustVar;
	}
	public double getCorrelationCoefficient() {
		return correlationCoefficient;
	}
	public void setCorrelationCoefficient(double correlationCoefficient) {
		this.correlationCoefficient = correlationCoefficient;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public Parameter() {
		super();
		percent = 0.01D;
	}
	
}
