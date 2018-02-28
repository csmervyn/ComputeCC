package org.github.mervy.dyhgyz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.github.mervyn.domain.DYHGParameter;
import org.github.mervyn.domain.GroupDYHGParam;
import org.github.mervyn.utils.Column;
import org.github.mervyn.utils.Table;
import org.springframework.stereotype.Service;

import Jama.Matrix;

@Service("dYHGService")
public class DYHGService {
	
	
	public List<Matrix> groupCompute(Table table,GroupDYHGParam groupDyhgParam){
		List<Matrix> list = new ArrayList<Matrix>();
		for(int i = 0; i < groupDyhgParam.getDyhgList().size(); i++){
			list.add(compute(table,groupDyhgParam.getDyhgList().get(i)));
		}
		return list;
	}
	
	public Matrix compute(Table table,DYHGParameter parameter){
		computeBiaoZhun(table);
		Matrix B = new  Matrix(table.getLineList().size(),1);
		Matrix X = getX(table,parameter);
		Matrix Y = getY(table,parameter);
		//转置
		Matrix XT = X.transpose();
		//乘积
		Matrix product = XT.times(X);
		//求逆
		Matrix inverse = product.inverse();
		
		Matrix product2 = inverse.times(XT);
		Matrix product3 =  product2.times(Y);
		B = product3;
		return B;
	}
	
	public Matrix computeWithB(Table table,DYHGParameter parameter){
		computeBiaoZhun(table);
		Matrix B = new  Matrix(table.getLineList().size(),1);
		Matrix X = getXWithB(table,parameter);
		Matrix Y = getY(table,parameter);
		//转置
		Matrix XT = X.transpose();
		//乘积
		Matrix product = XT.times(X);
		//求逆
		Matrix inverse = product.inverse();
		
		Matrix product2 = inverse.times(XT);
		Matrix product3 =  product2.times(Y);
		B = product3;
		return B;
	}
	
	private Matrix getXWithB(Table table,DYHGParameter parameter){
		Matrix X = new  Matrix(table.getLineList().size(),parameter.getIndependentVarList().size()+2);
		for(int i = 0; i < table.getLineList().size();i++){
			X.set(i, 0, 1);
		}
		int column = 1;
		for(Integer i : parameter.getIndependentVarList()) {
			List<Double> doubleList = getBiaoZhunList(table,i);
			for(int line = 0; line < doubleList.size();line++){
				X.set(line, column, doubleList.get(line));
			}
			column++;
		}
		for(int line = 0; line <table.getLineList().size();line++){
			double b = table.getLineList().get(line).getB();
			X.set(line, column, b);
		}
		return X;
	}
	
	
	private Matrix getX(Table table,DYHGParameter parameter){
		
		Matrix X = new  Matrix(table.getLineList().size(),parameter.getIndependentVarList().size()+1);
		for(int i = 0; i < table.getLineList().size();i++){
			X.set(i, 0, 1);
		}
		int column = 1;
		for(Integer i : parameter.getIndependentVarList()) {
			List<Double> doubleList = getBiaoZhunList(table,i);
			for(int line = 0; line < doubleList.size();line++){
				X.set(line, column, doubleList.get(line));
			}
			column++;
		}
		return X;
	}
	
	private Matrix getY(Table table,DYHGParameter parameter){
		List<Double> averageList = getBiaoZhunList(table,parameter.getDependentVar());		
		Matrix Y = new  Matrix(averageList.size(),1);
		for(int i =0; i < averageList.size(); i++){
			Y.set(i, 0, averageList.get(i));
		}
		return Y;
	}
	
	public void computeBiaoZhun(Table table){
		reComputeAverage(table);
		for(int groupId = 0; groupId < table.getGroupNum(); groupId++){
			List<Double> averageListFirst = getAverageListByGroupID(table,groupId);
			for(int line =0; line < averageListFirst.size(); line++){
				double biaozhun = getBiaoZhun(averageListFirst.get(line),averageListFirst);
				table.getLineList().get(line).getGroupList().get(groupId).setBiaoZhun(biaozhun);
			}
		}
	}
	
	public List<Double> getBiaoZhunList(Table table,int groupId){
		List<Double> biaoZhunList = new ArrayList<Double>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){		
			biaoZhunList.add(table.getLineList().get(lineNum).getGroupList().get(groupId).getBiaoZhun());
		}
		return biaoZhunList;
	}
	
	private static double getBiaoZhun(double input, List<Double> list){
		
		double sum = 0;
		for(int i = 0; i < list.size(); i++){
			sum += list.get(i);
		}
		double average = sum / list.size();
		double deviation = 0;
		for(int i = 0; i < list.size(); i++){
			deviation = deviation + (list.get(i)-average) * (list.get(i)-average);
		}
		double sd = Math.sqrt(deviation/list.size());
		
		double target =  ((input - average)/sd);
		return target;
	}
	
	private static List<Double> getAverageListByGroupID(Table table, int groupID){
		List<Double> averageList = new ArrayList<Double>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){		
			averageList.add(table.getLineList().get(lineNum).getGroupList().get(groupID).getAverage());
		}
		return averageList;
	}
	
	public void reComputeAverage(Table table){
		int numOfGroup = table.getLineList().get(0).getGroupList().size();
		for(int countOfGroupNum = 0; countOfGroupNum < numOfGroup; countOfGroupNum++){
			average(table,countOfGroupNum);
		}
	}
	
	private void average(Table table,int groupId){
		//取出第i组数据进行计算
		List<Column> columnList = getColumnListByGroupID(table,groupId);
		//计算每行每组中的平均值
		for(int indexOfLine = 0; indexOfLine<table.getLineList().size(); indexOfLine++){
			long sum = 0;
			//计算同一行中几个列数字的和
			for(int indexOfColumn = 0; indexOfColumn < columnList.size(); indexOfColumn++){
				sum += columnList.get(indexOfColumn).getColumnData().get(indexOfLine);
			}
			//计算平均值
			BigDecimal   b   =   new   BigDecimal((double)sum/columnList.size());  
			double average   =   b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
			table.getLineList().get(indexOfLine).getGroupList().get(groupId).setAverage(average);
		}
	}
	
	private static List<Column> getColumnListByGroupID(Table table, int groupID){
		List<Column> columnList = new ArrayList<Column>();
		//int maxColumnNum = table.getLineList().get(0).getGroupList().get(0).getColumnList().size();
		int maxColumnNum = table.getLineList().get(0).getGroupList().get(groupID).getColumnList().size();
		//初始化
		for(int columnIndex = 0; columnIndex < maxColumnNum; columnIndex++){
			columnList.add(new Column());
		}
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){
			for(int columnIndex = 0; columnIndex < maxColumnNum; columnIndex++){
				long temp = table.getLineList().get(lineNum).getGroupList().get(groupID).getColumnList().get(columnIndex);
				columnList.get(columnIndex).getColumnData().add(temp);
			}
		}
		return columnList;
	}
}
