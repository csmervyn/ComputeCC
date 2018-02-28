package org.github.mervyn.wjxdxdtc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.github.mervyn.utils.Column;
import org.github.mervyn.utils.Compute;
import org.github.mervyn.utils.ComputeCorrelationCoefficient;
import org.github.mervyn.utils.Group;
import org.github.mervyn.utils.Line;
import org.github.mervyn.utils.LineComparator;
import org.github.mervyn.utils.LineComparator7;
import org.github.mervyn.utils.ParseExcel;
import org.github.mervyn.utils.Result;
import org.github.mervyn.utils.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wJXDXDTCService")
public class WJXDXDTCService {
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
	
	public List<Result> compute(Table table, double parameter, String path)  throws IOException{
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
		
		firstStep(table);
		secondStep(table);
		thirdStep(table,parameter);
		
		
		//恢复已经删除的行
		Line line = new Line(table.getLineList().get(0).getGroupList().size(),-1);
		for(int groupNum = 0; groupNum < table.getLineList().get(0).getGroupList().size(); groupNum++){
			//该组有countOfColumnInGroupList.get(groupNum)列
			Group group = new Group(table.getLineList().get(0).getGroupList().get(0).getColumnList().size());
			for(int columnNum = 0; columnNum < table.getLineList().get(0).getGroupList().get(0).getColumnList().size(); columnNum++){
				//读取该行该组的所有列的数据
				//第0行不用解析，因为第0行是标题			
				group.getColumnList().add((long)0);
			}
			line.getGroupList().add(group);
		}
		//先排序
		Comparator<Line> comparator = new LineComparator7();
		Collections.sort(table.getLineList(), comparator);
		
		
		int newLine = 0;
		for(int lineNum = 0; lineNum < table.getLineList().size();){
			if(!table.getLineList().get(lineNum).equals(oldTable.getLineList().get(newLine))){
				/*oldTable.getLineList().remove(newLine);
				line.setNum(newLine);
				oldTable.getLineList().add(newLine, line);*/
				List<Group> groupList = oldTable.getLineList().get(newLine).getGroupList();
				for(int groupIndex = 0; groupIndex < groupList.size(); groupIndex++){
					List<Long> columnList = groupList.get(groupIndex).getColumnList();
					for(int columnIndex = 0; columnIndex < columnList.size(); columnIndex++){
						columnList.set(columnIndex, 0L);
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
				List<Long> columnList = groupList.get(groupIndex).getColumnList();
				for(int columnIndex = 0; columnIndex < columnList.size(); columnIndex++){
					columnList.set(columnIndex, 0L);
				}
			}
			newLine++;
		}
		
		
		/*int lineNum = 0;
		
		for(; lineNum < oldTable.getLineList().size() && newLine < table.getLineList().size(); lineNum++){
			if(lineNum != table.getLineList().get(newLine).getNum()){
				oldTable.getLineList().remove(lineNum);
				line.setNum(lineNum);
				oldTable.getLineList().add(lineNum, line);
			}
		}*/
		
		oldTable.setGroupNum(table.getGroupNum());
		//计算各组的平均值
		Compute compute = new Compute(table.getGroupNum());
		System.out.println(table.getGroupNum());
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
	
	private void thirdStep(Table table, double parameter){
		double PERCENT = 0.01;
		BigDecimal bigCC = new BigDecimal(parameter);
		BigDecimal litlleCC = new BigDecimal(0 - parameter);
		for(int formerIndexOfGroup = 0; formerIndexOfGroup < table.getGroupNum()-1; formerIndexOfGroup++){
			for(int latterIndexOfGroup = formerIndexOfGroup+1; latterIndexOfGroup < table.getGroupNum();latterIndexOfGroup++){
				average(table,formerIndexOfGroup);
				average(table,latterIndexOfGroup);
				List<Double> averageListFirst = getAverageListByGroupID(table,formerIndexOfGroup);
				List<Double> averageListSecond = getAverageListByGroupID(table,latterIndexOfGroup);
				BigDecimal value = new BigDecimal(ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(averageListFirst, averageListSecond)); 
				if(value.compareTo(bigCC) > 0){
					setAllMulBiaoZhun(table,formerIndexOfGroup,averageListFirst,averageListSecond);
					Comparator<Line> comparator = new LineComparator2(formerIndexOfGroup);
					Collections.sort(table.getLineList(), comparator);
					Collections.reverse(table.getLineList());
					//修改前1%的数据
					int alterIndex = (int) Math.floor(PERCENT * table.getLineList().size());
					if(alterIndex>=1){
						for(int i = 0; i<alterIndex; i++){						
							table.getLineList().remove(i);
						}
						latterIndexOfGroup = latterIndexOfGroup -1;
					}
				}else if(value.compareTo(litlleCC) < 0){
					setAllMulBiaoZhun(table,formerIndexOfGroup,averageListFirst,averageListSecond);
					Comparator<Line> comparator = new LineComparator2(formerIndexOfGroup);
					Collections.sort(table.getLineList(), comparator);
					//修改前1%的数据
					int alterIndex = (int) Math.floor(PERCENT * table.getLineList().size());
					if(alterIndex>=1){
						for(int i = 0; i<alterIndex; i++){						
							table.getLineList().remove(i);
						}
						latterIndexOfGroup = latterIndexOfGroup -1;
					}
				}
			}
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
			long average = Math.round((double)sum/columnList.size());
			table.getLineList().get(indexOfLine).getGroupList().get(groupId).setAverage(average);
		}
	}
	
	private static List<Double> getAverageListByGroupID(Table table, int groupID){
		List<Double> averageList = new ArrayList<Double>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){		
			averageList.add(table.getLineList().get(lineNum).getGroupList().get(groupID).getAverage());
		}
		return averageList;
	}
	
 	private void secondStep(Table table){
		double PERCENT = 0.01;
		BigDecimal target = new BigDecimal(0.5);
		for(int groupNum = 0; groupNum < table.getGroupNum(); groupNum++){
			List<Column> columnList = getColumnListByGroupID(table,groupNum);
			for(int columnNum = 0; columnNum < table.getLineList().get(0).getGroupList().get(groupNum).getColumnList().size(); columnNum++){
				BigDecimal value = new BigDecimal(ComputeCorrelationCoefficient.getCorrelationCoefficientLongDouble(columnList.get(columnNum).getColumnData(), averageBesidesOneColumn(table,groupNum,columnNum)));
				if(value.compareTo(target) <= 0){
					setAllMulBiaoZhunLongDouble(table,groupNum,columnList.get(columnNum).getColumnData(),averageBesidesOneColumn(table,groupNum,columnNum));
					Comparator<Line> comparator = new LineComparator2(groupNum);
					Collections.sort(table.getLineList(), comparator);
					//修改前1%的数据
					int alterIndex = (int) Math.floor(PERCENT * table.getLineList().size());
					if(alterIndex>=1){
						for(int i = 0; i<alterIndex; i++){						
							table.getLineList().remove(i);
						}
						columnNum = columnNum -1;
					}
				}
			}
		}
	}
	
	private void setAllMulBiaoZhun(Table table, int groupNum, List<Double> xElements, List<Double> yElements){
		for(int line = 0; line < xElements.size(); line++){
			double temp = getBiaoZhun(xElements.get(line),xElements) * getBiaoZhun(yElements.get(line),yElements);
			table.getLineList().get(line).getGroupList().get(groupNum).setMulBiaoZhun(temp);
		}
	}
	
	private void setAllMulBiaoZhunLongDouble(Table table, int groupNum, List<Long> xElements, List<Double> yElements){
		for(int line = 0; line < xElements.size(); line++){
			double temp = getBiaoZhunLong(xElements.get(line),xElements) * getBiaoZhun(yElements.get(line),yElements);
			table.getLineList().get(line).getGroupList().get(groupNum).setMulBiaoZhun(temp);
		}
	}

	private static double getBiaoZhunLong(long input, List<Long> list){
		double sum = 0;
		for(int i = 0; i < list.size(); i++){
			sum += list.get(i);
		}
		double average = sum / list.size();
		double deviation = 0;
		for(int i = 0; i < list.size(); i++){
			deviation = deviation + (list.get(i)-average) * (list.get(i)-average);
		}
		double sd = Math.sqrt(deviation);
		
		double target = (input - average)/sd;
		return target;
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
		
		double target = (input - average)/sd;
		return target;
	}
	
	private List<Double> averageBesidesOneColumn(Table table, int groupNum, int targetColumn){
		List<Double> list = new ArrayList<Double>();
		//初始化
		for(int lineNum = 0; lineNum < table.getLineList().size(); lineNum++){
			list.add(0D);
		}
		List<Column> columnList = getColumnListByGroupID(table,groupNum);
		
		for(int i = 0; i< columnList.size(); i++){
			if(i != targetColumn){
				for(int lineNum = 0; lineNum < table.getLineList().size(); lineNum++){
					double temp = list.get(lineNum) + columnList.get(i).getColumnData().get(lineNum);
					list.set(lineNum, temp);
				}
			}
		}
		for(int lineNum = 0; lineNum < table.getLineList().size(); lineNum++){
			double temp = list.get(lineNum)/(columnList.size() -1);
			list.set(lineNum, temp);
		}
		return list;
	}
	
 	private void firstStep(Table table){
		double PERCENT = 0.01;
		BigDecimal target = new BigDecimal(0.7);
		for(int groupNum = 0; groupNum < table.getGroupNum(); groupNum++){
			BigDecimal value = new BigDecimal(formula1(table,groupNum));
			if(value.compareTo(target) <= 0){
				//计算并设置两两列直接的绝对值之和
				computeAbsValue(table,groupNum);
				//排序
				LineComparator lineComparator = new LineComparator(groupNum);
				//按升序排列
				Collections.sort(table.getLineList(), lineComparator);
				//将升序变成逆序
				Collections.reverse(table.getLineList());
				//修改前1%的数据
				int alterIndex = (int) Math.floor(PERCENT * table.getLineList().size());
				if(alterIndex>=1){
					for(int i = 0; i<alterIndex; i++){						
						table.getLineList().remove(i);
					}
					groupNum = groupNum -1;
				}
			}	
		}
	}
	

	private void computeAbsValue(Table table, int groupNum){
		List<Column> columnList = getColumnListByGroupID(table,groupNum);
		//计算两两数据间的绝对值
		for(int indexOfLine = 0; indexOfLine<table.getLineList().size(); indexOfLine++){
			long tempAbs = 0;
			for(int formerIndexOfColumn = 0; formerIndexOfColumn < columnList.size()-1; formerIndexOfColumn++){
				for(int latterIndexOfColumn = formerIndexOfColumn+1; latterIndexOfColumn < columnList.size();latterIndexOfColumn++){
					tempAbs += Math.abs(columnList.get(formerIndexOfColumn).getColumnData().get(indexOfLine)-columnList.get(latterIndexOfColumn).getColumnData().get(indexOfLine));
				}
			}
			table.getLineList().get(indexOfLine).getGroupList().get(groupNum).setAbsValue(tempAbs);
		}
	}
	
	private double formula1(Table table, int groupNum){
		List<Column> columnList = getColumnListByGroupID(table,groupNum);
		double result = (double)columnList.size()/(columnList.size() - 1)*(1.0D - sumOfAllSignleColumnDeviation(table,groupNum)/allColumnDeviation(table,groupNum));
		return result;
	}
	
	private double sumOfAllSignleColumnDeviation(Table table, int groupNum){
		List<Column> columnList = getColumnListByGroupID(table,groupNum);
		double sum = 0;
		for(int columnNum = 0; columnNum< columnList.size(); columnNum++){			
			sum += oneColumnDeviation(table,groupNum,columnNum);
		}
		return sum;
	}
	
	private double allColumnDeviation(Table table, int groupNum){
		List<Column> columnList = getColumnListByGroupID(table,groupNum);
		List<Long> list = new ArrayList<Long>();
		for(int i = 0; i< columnList.size(); i++){			
			list.addAll(columnList.get(i).getColumnData());
		}
		double sum = 0;
		for(int i = 0; i < list.size(); i++){
			sum += list.get(i);
		}
		double average = sum / list.size();
		double deviation = 0;
		for(int i = 0; i < list.size(); i++){
			deviation = deviation + (list.get(i)-average) * (list.get(i)-average);
		}
		return deviation;
	}
	
 	private double oneColumnDeviation(Table table, int groupNum, int columnNum){
		List<Long> list =  getColumnListByGroupID(table,groupNum).get(columnNum).getColumnData();
		double sum = 0;
		for(int i = 0; i < list.size(); i++){
			sum += list.get(i);
		}
		double average = sum / list.size();
		double deviation = 0;
		for(int i = 0; i < list.size(); i++){
			deviation = deviation + (list.get(i)-average) * (list.get(i)-average);
		}
		return deviation;
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
