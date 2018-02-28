package org.github.mervyn.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("service")
public class ComputeService {
	@Autowired
	ParseExcel parse;
	
	public Table parseTable(String path) throws IOException{
		String urlStr = path + File.separator + "originalTableName.xls";
		File file = new File(urlStr);
		InputStream in = new FileInputStream(file); 
		int groupNum = parse.getGroupNum(in);
		
		//解析excel中的数据
		Table table = null;
		table = parse.parse(in);
		
		table.setGroupNum(groupNum);
		in.close();
		return table;
	}
	
	public List<Result> compute(Table table,DTO dto,String path) throws IOException{
		int groupNum = table.getGroupNum();
		Compute compute = null;
		if(dto == null){		
			compute = new Compute(groupNum,Table.maxElementInTable(table));
		}else{
			//public Compute(int groupNum,double percent,double j2Parameter,double j3Parameter,double j4Parameter,double parameterOfThreeStep,int range)
			compute = new Compute(groupNum,dto.getPERCENT(),dto.getJ2PARAMETER(),dto.getJ3PARAMETER(),dto.getJ4PARAMETER(),dto.getPARAMETER_OF_THREESTEP(),dto.getPARAMETER_OF_FOUR_ONE_STEP(),Table.maxElementInTable(table));
		}
		//通过3步计算
		compute.mainCompute(table);
		
		//先排序
		Comparator<Line> comparator = new LineComparator7();
		Collections.sort(table.getLineList(), comparator);
		//计算各组的平均值
		compute.reComputeAverage(table);
		
		//得到相关系数的矩阵
		List<Result> resultList = compute.outputCorrelationCoefficient(table);
		//输出相关系数的excel表
		compute.exportCorrelationCoefficientExcel(resultList,path + File.separator + "各组平均值相关系数表.xls");
		
		
		//输出修改后的原始表格
		compute.exportTable2(table,path+ File.separator  + "修改后的表格数据.xls");	
		return resultList;
	}
	
	
	public List<Result> computeFiveStep(String path,DTO dto,int formerGroupNum, int latterGroupNum, double parameter,Table table) throws IOException{
		
		int groupNum = table.getGroupNum();
		Compute compute = null;
		if(dto == null){		
			compute = new Compute(groupNum,Table.maxElementInTable(table));
		}else{
			//public Compute(int groupNum,double percent,double j2Parameter,double j3Parameter,double j4Parameter,double parameterOfThreeStep,int range)
			compute = new Compute(groupNum,dto.getPERCENT(),Table.maxElementInTable(table));
		}
		
		//计算第五步
		compute.fiveStep(table, formerGroupNum, latterGroupNum, parameter);
		
		//先排序
		Comparator<Line> comparator = new LineComparator7();
		Collections.sort(table.getLineList(), comparator);
		//计算各组的平均值
		compute.reComputeAverage(table);
		
		//得到相关系数的矩阵
		List<Result> resultList = compute.outputCorrelationCoefficient(table);
		//输出相关系数的excel表
		compute.exportCorrelationCoefficientExcel(resultList,path + File.separator + "各组平均值相关系数表.xls");
		//输出修改后的原始表格
		compute.exportTable2(table,path+ File.separator  + "修改后的表格数据.xls");
		//将修改后的原始表格输出到原始表格的位置
		compute.exportTable2(table,path+ File.separator  + "originalTableName.xls");
		return resultList;
	}
	
	
}
