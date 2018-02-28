package org.github.mervyn.utils;

public class DTO {
	private  double PERCENT;	//关于前多少数据需要修改
	private   double J2PARAMETER;	//关于j等于2的时候的参数
	private   double J3PARAMETER;	//关于j等于3的时候的参数
	private   double J4PARAMETER;	//关于j等于4的时候的参数

	private  double NEW_J4PARAMETER;
	private  double NEW_J5PARAMETER;


	private  double PARAMETER_OF_THREESTEP; //关于第三步的参数
	private  double PARAMETER_OF_FOUR_ONE_STEP; //关于第4.1步的参数
	
	public double getPERCENT() {
		return PERCENT;
	}
	public void setPERCENT(double pERCENT) {
		PERCENT = pERCENT;
	}
	public double getJ2PARAMETER() {
		return J2PARAMETER;
	}
	public void setJ2PARAMETER(double j2parameter) {
		J2PARAMETER = j2parameter;
	}
	public double getJ3PARAMETER() {
		return J3PARAMETER;
	}
	public void setJ3PARAMETER(double j3parameter) {
		J3PARAMETER = j3parameter;
	}
	public double getJ4PARAMETER() {
		return J4PARAMETER;
	}
	public void setJ4PARAMETER(double j4parameter) {
		J4PARAMETER = j4parameter;
	}
	public double getPARAMETER_OF_THREESTEP() {
		return PARAMETER_OF_THREESTEP;
	}
	public void setPARAMETER_OF_THREESTEP(double pARAMETER_OF_THREESTEP) {
		PARAMETER_OF_THREESTEP = pARAMETER_OF_THREESTEP;
	}
	public double getPARAMETER_OF_FOUR_ONE_STEP() {
		return PARAMETER_OF_FOUR_ONE_STEP;
	}
	public void setPARAMETER_OF_FOUR_ONE_STEP(double pARAMETER_OF_FOUR_ONE_STEP) {
		PARAMETER_OF_FOUR_ONE_STEP = pARAMETER_OF_FOUR_ONE_STEP;
	}

	public double getNEW_J4PARAMETER() {
		return NEW_J4PARAMETER;
	}

	public void setNEW_J4PARAMETER(double NEW_J4PARAMETER) {
		this.NEW_J4PARAMETER = NEW_J4PARAMETER;
	}

	public double getNEW_J5PARAMETER() {
		return NEW_J5PARAMETER;
	}

	public void setNEW_J5PARAMETER(double NEW_J5PARAMETER) {
		this.NEW_J5PARAMETER = NEW_J5PARAMETER;
	}
}
