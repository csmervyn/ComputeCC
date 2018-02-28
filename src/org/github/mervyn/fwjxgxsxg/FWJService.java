package org.github.mervyn.fwjxgxsxg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.github.mervyn.domain.DYHGParameter;
import org.github.mervyn.domain.GroupDYHGParam;
import org.github.mervyn.utils.Result;
import org.github.mervyn.utils.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Jama.Matrix;

@Service("fWJService")
public class FWJService {

	@Autowired
	private ParseExcelForFWJ parseExcelForFWJ;
	
	public List<Matrix> groupCompute(SingleColumnTable table,GroupDYHGParam groupDyhgParam){
		List<Matrix> list = new ArrayList<Matrix>();
		for(int i = 0; i < groupDyhgParam.getDyhgList().size(); i++){
			list.add(computeDYHGYZ(table,groupDyhgParam.getDyhgList().get(i)));
		}
		return list;
	}
	
 	public SingleColumnTable parseTable(String path) throws IOException{
		String urlStr = path + File.separator + "originalTableName.xls";
		File file = new File(urlStr);
		InputStream in = new FileInputStream(file); 
		
		//解析excel中的数据
		SingleColumnTable table = null;
		table = parseExcelForFWJ.parse(in);
		in.close();
		return table;
	}
	
	public List<Result> computeFiveStep(String path,int formerColumnNum, int latterColumnNum, double parameter,SingleColumnTable table) throws IOException{
			
			int columnNum = table.getColumnNum();
			
			
			FWJCompute fWJCompute = null;
			fWJCompute = new FWJCompute(formerColumnNum,latterColumnNum,parameter);
			
			
			
			
			
			//计算第五步
			fWJCompute.first(table);
			
			//先排序
			Comparator<SingleColumnLine> comparator = new LineComparator7();
			Collections.sort(table.getLineList(), comparator);
			
			
			//得到相关系数的矩阵
			List<Result> resultList = fWJCompute.outputCorrelationCoefficient(table);
			//输出相关系数的excel表
			fWJCompute.exportCorrelationCoefficientExcel(resultList,path + File.separator + "相关系数表.xls");
			//输出修改后的原始表格
			fWJCompute.exportTable2(table,path+ File.separator  + "修改后的表格数据.xls");
			//将修改后的原始表格输出到原始表格的位置
			fWJCompute.exportTable2(table,path+ File.separator  + "originalTableName.xls");
			return resultList;
		}

	public void computeBiaoZhun(SingleColumnTable table){
		for(int columnId = 0; columnId < table.getColumnNum(); columnId++){
			computeBiaoZhun(columnId,table);
		}
	}
	public List<Double> getColumnListByColumnID(int columnNum, SingleColumnTable table){
		List<Double> columnList = new ArrayList<Double>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){
			double temp;
			if(table.getLineList().get(lineNum).getColumnList().get(columnNum).isLong()){
				temp = table.getLineList().get(lineNum).getColumnList().get(columnNum).getLongValue();
			}else{
				temp = table.getLineList().get(lineNum).getColumnList().get(columnNum).getDoubleValue();
			}
			columnList.add(temp);
		}
		return columnList;
	}
	public double getStandardDeviation(int columnNum, SingleColumnTable table){
		List<Double> list = getColumnListByColumnID(columnNum,table);
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
		return sd;
	}
	public double getAverage(int columnNum, SingleColumnTable table){
		List<Double> list = getColumnListByColumnID(columnNum, table);
		double sum = 0;
		for(int i = 0; i < list.size(); i++){
			sum += list.get(i);
		}
		double average = sum / list.size();
		return average;
	}
	public void computeBiaoZhun(int columnNum, SingleColumnTable table){
		double average = getAverage(columnNum,table);
		double sd = getStandardDeviation(columnNum, table);
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){
			Ceil ceil = table.getLineList().get(lineNum).getColumnList().get(columnNum);
			if(ceil.isLong()){
				double temp = (double)(ceil.getLongValue()-average)/sd;
				ceil.setBiaoZhun(temp);
			}else{
				double temp = (ceil.getDoubleValue()-average)/sd;
				ceil.setBiaoZhun(temp);
			}
		}
	}
	private Matrix getX(SingleColumnTable table,DYHGParameter parameter){
		Matrix X = new  Matrix(table.getLineList().size(),parameter.getIndependentVarList().size()+1);
		for(int i = 0; i < table.getLineList().size();i++){
			X.set(i, 0, 1);
		}
		int column = 1;
		for(Integer i : parameter.getIndependentVarList()) {
			List<Double> doubleList = getBiaoZhun(i,table);
					//getColumnListByColumnID(i,table);	
			for(int line = 0; line < doubleList.size();line++){
				X.set(line, column, doubleList.get(line));
			}
			column++;
		}
		return X;
	}
	public List<Double> getBiaoZhun(int columnNum, SingleColumnTable table){
		List<Double> list = new ArrayList<Double>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){
			Ceil ceil = table.getLineList().get(lineNum).getColumnList().get(columnNum);
			list.add(ceil.getBiaoZhun());
		}
		return list;
	}
	private Matrix getY(SingleColumnTable table,DYHGParameter parameter){
		List<Double> averageList = 	getBiaoZhun(parameter.getDependentVar(),table);
				//getColumnListByColumnID(parameter.getDependentVar(),table);	
		Matrix Y = new  Matrix(averageList.size(),1);
		for(int i =0; i < averageList.size(); i++){
			Y.set(i, 0, averageList.get(i));
		}
		return Y;
	}
	
	
	public Matrix computeDYHGYZ(SingleColumnTable table,DYHGParameter parameter){
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
}
