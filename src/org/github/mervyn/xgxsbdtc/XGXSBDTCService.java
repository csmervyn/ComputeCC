package org.github.mervyn.xgxsbdtc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import org.github.mervyn.utils.ComputeCorrelationCoefficient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("xGXSBDTCService")
public class XGXSBDTCService {
	@Autowired
	XGParseExcel xGParseExcel;
	public Table parseTable(String path) throws IOException{
		String urlStr = path + File.separator + "originalTableName.xls";
		File file = new File(urlStr);
		InputStream in = new FileInputStream(file); 
		int groupNum = xGParseExcel.getGroupNum(in);
		
		//解析excel中的数据
		Table table = null;
		table = xGParseExcel.parse(in);
		
		table.setGroupNum(groupNum);
		in.close();
		return table;
	}
	
	
	public void compute(int formerGroup,int latterGroup,double parameter,Table table){
		BigDecimal litlleCC = new BigDecimal(parameter - 0.05);
		BigDecimal bigCC = new BigDecimal(parameter + 0.05);
		boolean flag = true;
		while(flag){
			List<Column> formerColumnList = getColumnListByGroupID(table,formerGroup);
			List<Column> latterColumnList = getColumnListByGroupID(table,latterGroup);
			List<Double> formerData = getAverage(formerColumnList);
			List<Double> latterData = getAverage(latterColumnList);	
			BigDecimal correlationCoefficient = new BigDecimal(ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(formerData,latterData));
			for(int line = 0; line < table.getLineList().size(); line++){
				double temp = getBiaoZhun(formerData.get(line), formerData) * getBiaoZhun(latterData.get(line), latterData);
				table.getLineList().get(line).getGroupList().get(formerGroup).setBiaoZhun(temp);
			}
			//Comparator<Line> comparator = new LineComparator8(formerGroup);
			Comparator<Line> comparator = new LineComparator8(formerGroup);
			if(correlationCoefficient.compareTo(bigCC) >0){
				Collections.sort(table.getLineList(), comparator);
				Collections.reverse(table.getLineList());
				int lineNum = (int) (table.getLineList().size() * 0.01);
				if(lineNum >0){
					for(int line = 0; line < lineNum; line++){
						table.getLineList().remove(line);
					}
				}else{
					flag = Boolean.FALSE;
				}
			}else if(correlationCoefficient.compareTo(litlleCC) <0){
				Collections.sort(table.getLineList(), comparator);
				int lineNum = (int) (table.getLineList().size() * 0.01);
				if(lineNum >0){
					for(int line = 0; line < lineNum; line++){
						table.getLineList().remove(line);
					}
				}else{
					flag = Boolean.FALSE;
				}
			}else{
				flag = Boolean.FALSE;
			}
		}
	}
	
	public List<Result> main(String path,int formerGroup,int latterGroup,double parameter,Table table) throws IOException{
		//复制表
        Table oldTable = new Table(table.getLineList().size());
        for(int lineNum = 0; lineNum < table.getLineList().size(); lineNum++){
        	
        	Line line = new Line(table.getLineList().get(lineNum).getGroupList().size(),table.getLineList().get(lineNum).getNum());
			for(int groupNum = 0; groupNum < table.getLineList().get(lineNum).getGroupList().size(); groupNum++){
				//该组有countOfColumnInGroupList.get(groupNum)列
				Group group = new Group(table.getLineList().get(lineNum).getGroupList().get(groupNum).getColumnList().size());
				for(int columnNum = 0; columnNum < table.getLineList().get(lineNum).getGroupList().get(groupNum).getColumnList().size(); columnNum++){
					//读取该行该组的所有列的数据
					group.getColumnList().add(table.getLineList().get(lineNum).getGroupList().get(groupNum).getColumnList().get(columnNum));
					
				}
				line.getGroupList().add(group);
			}
			oldTable.getLineList().add(line);
		}

        //处理
		compute(formerGroup,latterGroup,parameter,table);
		

		//恢复已经删除的行
		Line line = new Line(table.getLineList().get(0).getGroupList().size(),-1);
		for(int groupNum = 0; groupNum < table.getLineList().get(0).getGroupList().size(); groupNum++){
			//该组有countOfColumnInGroupList.get(groupNum)列
			Group group = new Group(table.getLineList().get(0).getGroupList().get(0).getColumnList().size());
			for(int columnNum = 0; columnNum < table.getLineList().get(0).getGroupList().get(0).getColumnList().size(); columnNum++){
				//读取该行该组的所有列的数据
				//第0行不用解析，因为第0行是标题			
				group.getColumnList().add(0D);
			}
			line.getGroupList().add(group);
		}
		
		//先排序
		Comparator<Line> comparator = new LineComparator7();
		Collections.sort(table.getLineList(), comparator);
		
		
		/*for(int lineNum = 0,newLine = 0; lineNum < oldTable.getLineList().size(); lineNum++){
			if(lineNum != table.getLineList().get(newLine).getNum()){
				oldTable.getLineList().remove(lineNum);
				line.setNum(lineNum);
				oldTable.getLineList().add(lineNum, line);
			}else{
				newLine++;
			}
		}*/
		
		int newLine = 0;
		for(int lineNum = 0; lineNum < table.getLineList().size();){
			if(!table.getLineList().get(lineNum).equals(oldTable.getLineList().get(newLine))){
				List<Group> groupList = oldTable.getLineList().get(newLine).getGroupList();
				for(int groupIndex = 0; groupIndex < groupList.size(); groupIndex++){
					List<Double> columnList = groupList.get(groupIndex).getColumnList();
					for(int columnIndex = 0; columnIndex < columnList.size(); columnIndex++){
						columnList.set(columnIndex, 0D);
					}
				}
				newLine++;
			}else{
				lineNum++;
				newLine++;
			}
		}
		while(newLine < oldTable.getLineList().size()){
			List<Group> groupList = oldTable.getLineList().get(newLine).getGroupList();
			for(int groupIndex = 0; groupIndex < groupList.size(); groupIndex++){
				List<Double> columnList = groupList.get(groupIndex).getColumnList();
				for(int columnIndex = 0; columnIndex < columnList.size(); columnIndex++){
					columnList.set(columnIndex, 0D);
				}
			}
			newLine++;
		}
    	
		
		//计算各组的平均值
		XGCompute compute = new XGCompute(table.getGroupNum());
		compute.reComputeAverage(table);
		
		//得到相关系数的矩阵
		List<Result> resultList = compute.outputCorrelationCoefficient(table);
		//输出相关系数的excel表
		compute.exportCorrelationCoefficientExcel(resultList,path + File.separator + "各组平均值相关系数表.xls");
		//输出修改后的原始表格
		compute.exportTable2(oldTable,path+ File.separator  + "剔除后的表格数据.xls");
		//将修改后的原始表格输出到原始表格的位置
		compute.exportTable2(table,path+ File.separator  + "originalTableName.xls");
		return resultList;
		
			
		
		
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
				double temp = table.getLineList().get(lineNum).getGroupList().get(groupID).getColumnList().get(columnIndex);
				columnList.get(columnIndex).getColumnData().add(temp);
			}
		}
		return columnList;
	}

	private static List<Double> getAverage(List<Column> columnList){
		List<Double> data = new ArrayList<Double>();
		if(columnList.size() > 1){
			for(int column = 0; column < columnList.size(); column++){
				for(int line = 0; line < columnList.get(column).getColumnData().size(); line++){
					if(column == 0){
						data.add(columnList.get(column).getColumnData().get(line));
					}else{
						double temp = data.get(line)+ columnList.get(column).getColumnData().get(line);
						data.set(line, temp);
					}
				}
			}
			for(int line = 0; line < data.size(); line++){
				double temp = data.get(line)/columnList.size();
				data.set(line, temp);
			}
		}else{
			data = columnList.get(0).getColumnData();
		}
		return data;
	}
	
	private static double getBiaoZhun(double input, List<Double> list){
		/*List<Long> list = new ArrayList<Long>();
		for(int column = 0; column < columnList.size(); column++){
			for(int line = 0; line < columnList.get(column).getColumnData().size(); line++){
				list.add(columnList.get(column).getColumnData().get(line));
			}
		}*/
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
		
		double target = (input - average)/sd;
		return target;
	}
}
