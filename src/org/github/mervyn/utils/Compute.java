package org.github.mervyn.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


public class Compute {
	public final static Random RANDOM = new Random();
	public final static BigDecimal CCPARAMETER = new BigDecimal(0.5);
	private final  int GROUPNUM;	//每行中有多少组
	private final double PERCENT;	//关于前多少数据需要修改
	//private  double PERCENT;
	private final  double J2PARAMETER;	//关于j等于2的时候的参数
	private final  double J3PARAMETER;	//关于j等于3的时候的参数
	private final  double J4PARAMETER;	//关于j等于4的时候的参数

	private  double NEW_J4PARAMETER;
	private  double NEW_J5PARAMETER;

	private final double PARAMETER_OF_THREESTEP; //关于第三步的参数
	
	private final double PARAMETER_OF_FOUR_ONE_STEP; //关于第4.1步的参数


	
	private final int RANGE;	//关于第三不步数字的范围

	
	
	
	public Compute(int groupNum,double percent,double j2Parameter,double j3Parameter,double j4Parameter,double parameterOfThreeStep,double parameterOfFourOneStep,int range){
		this.GROUPNUM = groupNum;
		this.PERCENT = percent;
		this.J2PARAMETER = j2Parameter;
		this.J3PARAMETER = j3Parameter;
		this.J4PARAMETER = j4Parameter;
		this.PARAMETER_OF_THREESTEP = parameterOfThreeStep;
		this.PARAMETER_OF_FOUR_ONE_STEP = parameterOfFourOneStep;
		this.RANGE = range;
	}
	public Compute(int groupNum,int range){
		this.GROUPNUM = groupNum;
		/*this.J2PARAMETER = 0.75D;
		this.J3PARAMETER = 0.7D;
		this.J4PARAMETER = 0.5D;
		this.NEW_J4PARAMETER = 0.68D;
		this.PERCENT = 0.03D;
		this.PARAMETER_OF_THREESTEP = 0.7D;
		this.PARAMETER_OF_FOUR_ONE_STEP = 0.0D;*/
		this.PERCENT = 0.03D;
		this.J2PARAMETER = 0.85D;
		this.J3PARAMETER = 0.73D;

		this.NEW_J4PARAMETER = 0.68D;
		this.NEW_J5PARAMETER = 0.6D;

		this.J4PARAMETER = 0.5D;
		this.PARAMETER_OF_THREESTEP = 0.7D;
		this.PARAMETER_OF_FOUR_ONE_STEP = 0.2D;
		this.RANGE = range;
	}
	
	public Compute(){
		this.GROUPNUM = 0;
		this.PERCENT = 0.03D;
		this.J2PARAMETER = 0.85D;
		this.J3PARAMETER = 0.73D;

		this.NEW_J4PARAMETER = 0.68D;
		this.NEW_J5PARAMETER = 0.6D;

		this.J4PARAMETER = 0.5D;
		this.PARAMETER_OF_THREESTEP = 0.7D;
		this.PARAMETER_OF_FOUR_ONE_STEP = 0.2D;
		this.RANGE = 0;
	}
	
	public Compute(int groupNum,double percent,int range){
		this.GROUPNUM = groupNum;
		this.PERCENT = percent;
		this.RANGE = range;

		this.J2PARAMETER = 0.85D;
		this.J3PARAMETER = 0.73D;

		this.NEW_J4PARAMETER = 0.68D;
		this.NEW_J5PARAMETER = 0.6D;

		this.J4PARAMETER = 0.5D;
		this.PARAMETER_OF_THREESTEP = 0.7D;
		this.PARAMETER_OF_FOUR_ONE_STEP = 0.2D;
		/*this.J2PARAMETER = 0.85D;
		this.J3PARAMETER = 0.80D;
		this.J4PARAMETER = 0.78D;
		this.PARAMETER_OF_THREESTEP = 0.75D;*/
		/*this.J2PARAMETER = 0.75D;
		this.J3PARAMETER = 0.7D;
		this.J4PARAMETER = 0.5D;
		this.PARAMETER_OF_THREESTEP = 0.7D;
		this.PARAMETER_OF_FOUR_ONE_STEP = 0.0D;*/
	}
	
	
	public Compute(int groupNum){
		this.GROUPNUM = groupNum;
		this.PERCENT = 0.03D;
/*		this.J2PARAMETER = 0.85D;
		this.J3PARAMETER = 0.80D;
		this.J4PARAMETER = 0.78D;
		this.PARAMETER_OF_THREESTEP = 0.75D;
		this.PARAMETER_OF_FOUR_ONE_STEP = 0.0D;
		this.RANGE = 0;*/
		this.J2PARAMETER = 0.75D;
		this.J3PARAMETER = 0.7D;
		this.J4PARAMETER = 0.5D;
		this.PARAMETER_OF_THREESTEP = 0.7D;
		this.PARAMETER_OF_FOUR_ONE_STEP = 0.0D;
		this.RANGE = 0;
		
	}
	
	
 	public void  mainCompute(Table table){	
		firstStep(table);
		secondStep(table);
		thirdStep(table);
		fourOneStep(table);
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

	public void firstStep(Table table){
		long minRange = Table.minElementInTable(table);
		for(int groupId = 0; groupId < GROUPNUM; groupId++){
			//取出第i组数据进行计算
			List<Column> columnList = getColumnListByGroupID(table,groupId);
			
			//判断任意两列的相关系数是否均大于0.5
			boolean flag = true;
			for(int formerIndexOfColumn = 0; flag && (formerIndexOfColumn < columnList.size()-1); formerIndexOfColumn++){
				for(int latterIndexOfColumn = formerIndexOfColumn+1; latterIndexOfColumn < columnList.size();latterIndexOfColumn++){
					BigDecimal correlationCoefficient = new BigDecimal(ComputeCorrelationCoefficient.getCorrelationCoefficient(columnList.get(formerIndexOfColumn).getColumnData(), columnList.get(latterIndexOfColumn).getColumnData()));
					if(correlationCoefficient.compareTo(CCPARAMETER) < 0){
						flag = false;
						break;
					}
				}	
			}

			if(!flag){
				//修改该组的数据
				//计算两两数据间的绝对值
				for(int indexOfLine = 0; indexOfLine<table.getLineList().size(); indexOfLine++){
					long tempAbs = 0;
					for(int formerIndexOfColumn = 0; formerIndexOfColumn < columnList.size()-1; formerIndexOfColumn++){
						for(int latterIndexOfColumn = formerIndexOfColumn+1; latterIndexOfColumn < columnList.size();latterIndexOfColumn++){
							tempAbs += Math.abs(columnList.get(formerIndexOfColumn).getColumnData().get(indexOfLine)-columnList.get(latterIndexOfColumn).getColumnData().get(indexOfLine));
						}
					}
					table.getLineList().get(indexOfLine).getGroupList().get(groupId).setAbsValue(tempAbs);
				}
				//排序
				LineComparator lineComparator = new LineComparator(groupId);
				//按升序排列
				Collections.sort(table.getLineList(), lineComparator);
				//将升序变成逆序
				Collections.reverse(table.getLineList());
				//计算该组的平均值
				average(table,groupId);
				//修改前5%的数据
				int alterIndex = (int) Math.floor(PERCENT * table.getLineList().size());
				if(alterIndex>=1){
					for(int i = 0; i<alterIndex; i++){
						int maxColumnNumInGroup = table.getLineList().get(i).getGroupList().get(groupId).getColumnList().size();
						//赋均值
						table.getLineList().get(i).getGroupList().get(groupId).getColumnList().clear();
						double average = table.getLineList().get(i).getGroupList().get(groupId).getAverage();
						
						for(int j = 0; j < maxColumnNumInGroup; j++){
							int tempFlag = RANDOM.nextInt(3);
							if(tempFlag == 0){								
								table.getLineList().get(i).getGroupList().get(groupId).getColumnList().add((long)average);
							}else if(tempFlag == 1){
								if(average+1 >= RANGE){
									table.getLineList().get(i).getGroupList().get(groupId).getColumnList().add((long)RANGE);
								}else{
									table.getLineList().get(i).getGroupList().get(groupId).getColumnList().add((long)(average+1));
								}
							}else{
								if(average-1 <= minRange){
									table.getLineList().get(i).getGroupList().get(groupId).getColumnList().add((long)minRange);
								}else{
									table.getLineList().get(i).getGroupList().get(groupId).getColumnList().add((long)(average-1));
								}
							}
						}
					}
					//计算每行每组中的平均值
					average(table,groupId);
					groupId = groupId-1;
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
	
	private void average2(Table table,int groupId,int columnID){
		//取出第i组数据进行计算
		List<Column> columnList = getColumnListByGroupID(table,groupId);
		//计算每行每组中的平均值
		for(int indexOfLine = 0; indexOfLine<table.getLineList().size(); indexOfLine++){
			long sum = 0;
			//计算同一行中几个列数字的和
			for(int indexOfColumn = 0; indexOfColumn < columnList.size(); indexOfColumn++){
				if(indexOfColumn != columnID){				
					sum += columnList.get(indexOfColumn).getColumnData().get(indexOfLine);
				}
			}
			//计算平均值
			long average = Math.round((double)sum/columnList.size());
			table.getLineList().get(indexOfLine).getGroupList().get(groupId).setAverage2(average);
		}
	}
	
	public void reComputeAverage(Table table){
		int numOfGroup = table.getLineList().get(0).getGroupList().size();
		for(int countOfGroupNum = 0; countOfGroupNum < numOfGroup; countOfGroupNum++){
			average(table,countOfGroupNum);
		}
	}
	
	//测试
 	public long getSumOfAverage(List<Long> list){
		long  sum = 0;
		for(int i = 0; i < list.size(); i++){
			sum += list.get(i);
		}
		return sum;
	}
	
	public void secondStep(Table table){
		for(int groupId = 0; groupId < GROUPNUM; groupId++){

			//计算第i组数据
			//取出第i组数据进行计算
			List<Column> columnList = getColumnListByGroupID(table,groupId);
			List<Double> correlationCoefficientList = new ArrayList<Double>(columnList.size());
			List<Double> averageList = null;
			if(2 == columnList.size()){
				average(table,groupId);

				averageList = getAverageListByGroupID(table,groupId);
				//List<Double> correlationCoefficientList = new ArrayList<Double>(columnList.size());
				//计算相关系数
				for(int columnIndex = 0; columnIndex < columnList.size(); columnIndex++){
					correlationCoefficientList.add(ComputeCorrelationCoefficient.getCorrelationCoefficientLongDouble(columnList.get(columnIndex).getColumnData(),averageList));
				}
			}else{
				average(table,groupId);
				//修改成不含k列的i组平面值
				for(int columnId = 0; columnId < table.getLineList().get(0).getGroupList().get(groupId).getColumnList().size(); columnId++){
					average2(table,groupId,columnId);
				}
				averageList = getAverage2ListByGroupID(table,groupId);

				//计算相关系数
				for(int columnIndex = 0; columnIndex < columnList.size(); columnIndex++){
					correlationCoefficientList.add(ComputeCorrelationCoefficient.getCorrelationCoefficientLongDouble(columnList.get(columnIndex).getColumnData(),averageList));
				}
			}
			//计算相关系数的最小值
			int indexOfMin = indexOfMin(correlationCoefficientList);
			//按照行数修改对应的参数
			double parameter = 0.0D;
			if(columnList.size() <= 2){
				parameter = J2PARAMETER;
			}else if(columnList.size() == 3){
				parameter = J3PARAMETER;
			}else if(4 == columnList.size()){
				parameter = NEW_J4PARAMETER;
			}else if(5 == columnList.size()){
				parameter = NEW_J5PARAMETER;
			}else{
				parameter = J4PARAMETER;
			}
			if(correlationCoefficientList.get(indexOfMin) <= parameter){
				//修改该组数据
				//找到相关系数最小的一列
				for(int lineNum = 0; lineNum < table.getLineList().size(); lineNum++){
					//long tempAbs = Math.abs(columnList.get(indexOfMin).getColumnData().get(lineNum)-averageList.get(indexOfMin));
					double tempAbs = Math.abs(columnList.get(indexOfMin).getColumnData().get(lineNum)-averageList.get(lineNum));
					table.getLineList().get(lineNum).getGroupList().get(groupId).setAbs2Value(tempAbs);
				}
				Comparator<Line> comparator = new LineComparator2(groupId);
				//升序排序
				Collections.sort(table.getLineList(), comparator);
				//将升序排序的改成降序排序的
				Collections.reverse(table.getLineList());
				
				//计算不包含k列的平均值
				average2(table,groupId,indexOfMin);
				
				
				//修改前5%的数据
				int alterIndex = (int) Math.floor(PERCENT * table.getLineList().size());
				if(alterIndex>=1){					
					for(int indexLine = 0; indexLine<alterIndex; indexLine++){
						double average = table.getLineList().get(indexLine).getGroupList().get(groupId).getAverage2();
						table.getLineList().get(indexLine).getGroupList().get(groupId).getColumnList().set(indexOfMin, (long)average);
					}
					//重新计算该组的平均值
					average(table,groupId);
					//修改后重新计算该组数据
					groupId = groupId -1;
				}
			}
			
			
		}
		
		//第2步，计算完，进行第3步
	}

	
	
	
	
	
 	public void thirdStep(Table table){
 		long minRange = Table.minElementInTable(table);
		boolean isASC = true;
		for(int formerIndexOfGroup = 0; formerIndexOfGroup < GROUPNUM-1; formerIndexOfGroup++){
			for(int latterIndexOfGroup = formerIndexOfGroup+1; latterIndexOfGroup < GROUPNUM;latterIndexOfGroup++){
				reComputeAverage(table);
				List<Double> averageListFirst = getAverageListByGroupID(table,formerIndexOfGroup);
				List<Double> averageListSecond = getAverageListByGroupID(table,latterIndexOfGroup);
				BigDecimal POTBig = new BigDecimal(PARAMETER_OF_THREESTEP);
				BigDecimal CCBig = new BigDecimal(ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(averageListFirst,averageListSecond));
				if(POTBig.compareTo(BigDecimal.ZERO) >=0 ){
					//相关系数大于指定值改变数据
					if(CCBig.compareTo(POTBig) >= 0){
						//进行修改数据操作
						//设置绝对值
						for(int indexOfLine = 0; indexOfLine < table.getLineList().size(); indexOfLine++){
							table.getLineList().get(indexOfLine).getGroupList().get(formerIndexOfGroup).setAbs3Value(Math.abs(averageListFirst.get(indexOfLine)-averageListSecond.get(indexOfLine)));
						}
						//按latterIndexOfGroup组平均值进行升序或降序（一次升序，一次降序交替进行）排序
						Comparator<Line> comparator0 = new LineComparator4(latterIndexOfGroup);
						Collections.sort(table.getLineList(), comparator0);
						if(isASC){
							isASC = false;
						}else{
							Collections.reverse(table.getLineList());
							isASC = true;
						}
						
						//按绝对值排序
						Comparator<Line> comparator = new LineComparator3(formerIndexOfGroup);
						Collections.sort(table.getLineList(), comparator);
						//按绝对值排序逆序
						Collections.reverse(table.getLineList());
						//修改后5%的数据
						int alterIndex = (int) Math.ceil((1.0D - PERCENT) * table.getLineList().size());
						if(alterIndex>=1){				
							for(int indexOfLine = alterIndex-1; indexOfLine < table.getLineList().size(); indexOfLine++){
								int maxIndexOfColumn = table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().size();
								for(int indexOfColumn = 0; indexOfColumn < maxIndexOfColumn; indexOfColumn++){
									long temp = table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().get(indexOfColumn);
									long randomInt = (long) (RANDOM.nextInt(RANGE-1) + 1);
									while(randomInt == temp){
										randomInt = (long) (RANDOM.nextInt(RANGE-1) + 1);
									}
									table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().set(indexOfColumn, randomInt);
								}
							}
							//修改indexOfGroup+1组的平均值
							average(table,latterIndexOfGroup);			
						}
						//重新对第formerIndexOfGroup和latterIndexOfGroup的相关系数进行计算
						latterIndexOfGroup = latterIndexOfGroup -1;
					}
				}else{
					//相关系数小于指定值改变数据
					if(CCBig.compareTo(POTBig) <= 0){
						//进行修改数据操作
						//设置绝对值
						for(int indexOfLine = 0; indexOfLine < table.getLineList().size(); indexOfLine++){
							table.getLineList().get(indexOfLine).getGroupList().get(formerIndexOfGroup).setAbs3Value(Math.abs(averageListFirst.get(indexOfLine)-averageListSecond.get(indexOfLine)));
						}
						//按latterIndexOfGroup组平均值进行逆序序排序
						Comparator<Line> comparator0 = new LineComparator4(latterIndexOfGroup);
						Collections.sort(table.getLineList(), comparator0);
						if(isASC){
							isASC = false;
						}else{
							Collections.reverse(table.getLineList());
							isASC = true;
						}
						
						
						//按绝对值排序
						Comparator<Line> comparator = new LineComparator3(formerIndexOfGroup);
						Collections.sort(table.getLineList(), comparator);
						//按绝对值排序逆序
						Collections.reverse(table.getLineList());
						
						//修改前5%的数据
						int alterIndex = (int) Math.ceil(PERCENT * table.getLineList().size());
						if(alterIndex>=1){	
							for(int i = 0; i<alterIndex; i++){
								//赋均值
								int maxColumnNumInGroup = table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().size();
								table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().clear();
								double average = table.getLineList().get(i).getGroupList().get(formerIndexOfGroup).getAverage();
								
								
								for(int j = 0; j < maxColumnNumInGroup; j++){
									int tempFlag = RANDOM.nextInt(3);
									if(tempFlag == 0){								
										table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average);
									}else if(tempFlag == 1){
										if(average+1 >= RANGE){
											table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)RANGE);
										}else{
											table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average+1);
										}
									}else{
										if(average-1 <= minRange){
											table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)minRange);
										}else{
											table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average-1);
										}
									}
								}
							}
							//修改indexOfGroup+1组的平均值
							average(table,latterIndexOfGroup);	
						}
						//重新对第formerIndexOfGroup和latterIndexOfGroup的相关系数进行计算
						latterIndexOfGroup = latterIndexOfGroup -1;
					}
				}
			}	
		}
	}

 	public void fourOneStep(Table table){
 		BigDecimal target = new BigDecimal(PARAMETER_OF_FOUR_ONE_STEP);
 		for(int formerIndexOfGroup = 0; formerIndexOfGroup < GROUPNUM-1; formerIndexOfGroup++){
 			reComputeAverage(table);
			for(int latterIndexOfGroup = formerIndexOfGroup+1; latterIndexOfGroup < GROUPNUM;latterIndexOfGroup++){
				List<Double> averageListSecond = getAverageListByGroupID(table,latterIndexOfGroup);
				int maxIndexOfColumn  = getColumnListByGroupID(table,formerIndexOfGroup).size();

				for(int indexOfColumn = 0; indexOfColumn < maxIndexOfColumn; indexOfColumn++){
					average(table,formerIndexOfGroup);
					List<Column> columnList =  getColumnListByGroupID(table,formerIndexOfGroup);
					List<Double> averageListFirst = getAverageListByGroupID(table,formerIndexOfGroup);
					double  firstABS = Math.abs(ComputeCorrelationCoefficient.getCorrelationCoefficientLongDouble(columnList.get(indexOfColumn).getColumnData(),averageListFirst));
					double  secondABS = Math.abs(ComputeCorrelationCoefficient.getCorrelationCoefficientLongDouble(columnList.get(indexOfColumn).getColumnData(),averageListSecond));
					BigDecimal result = new BigDecimal(Math.abs(firstABS -secondABS));
					if(result.compareTo(target) < 0){
						//计算|Xij -Xk|
						for(int lineNum = 0; lineNum < table.getLineList().size(); lineNum++){
							double temp = Math.abs(columnList.get(indexOfColumn).getColumnData().get(lineNum)-averageListSecond.get(lineNum));
							table.getLineList().get(lineNum).getGroupList().get(formerIndexOfGroup).setAbs6Value(temp);
						}
						
						//计算|Xij - Xi|
						for(int lineNum = 0; lineNum < table.getLineList().size(); lineNum++){
							double temp = Math.abs(columnList.get(indexOfColumn).getColumnData().get(lineNum)-averageListFirst.get(lineNum));
							table.getLineList().get(lineNum).getGroupList().get(formerIndexOfGroup).setAbs5Value(temp);
						}
						
						//对|Xij -Xk|按升序排列
						Comparator<Line> comparator6 = new LineComparator6(formerIndexOfGroup);
						Collections.sort(table.getLineList(), comparator6);
						
						//对|Xij - Xi|按降序排列
						Comparator<Line> comparator5 = new LineComparator5(formerIndexOfGroup);
						Collections.sort(table.getLineList(), comparator5);
						Collections.reverse(table.getLineList());
						
						//计算不包含k列的平均值
						average2(table,formerIndexOfGroup,indexOfColumn);
						
						
						//修改前5%的数据
						int alterIndex = (int) Math.ceil(PERCENT * table.getLineList().size());
						if(alterIndex>=1){	
							for(int i = 0; i<alterIndex; i++){
								//给这一列赋均值
								double average = table.getLineList().get(i).getGroupList().get(formerIndexOfGroup).getAverage2();
								table.getLineList().get(i).getGroupList().get(formerIndexOfGroup).getColumnList().set(indexOfColumn, (long)average);
							}
							//修改indexOfGroup+1组的平均值
							average(table,formerIndexOfGroup);	
						}
						//重新对第formerIndexOfGroup组从第0列开始进行队相关系数进行计算
						indexOfColumn = -1;	
					}
				}
			}	
		}
 	}

 	
 	public void fiveStep(Table table, int formerIndexOfGroup, int latterIndexOfGroup, double correlationCoefficient){
 		//double store = PERCENT;
 		//PERCENT = 0.05;
 		long minRange = Table.minElementInTable(table);
		BigDecimal setCC = new BigDecimal(correlationCoefficient);
		BigDecimal litlleCC = new BigDecimal(correlationCoefficient - 0.03);
		BigDecimal bigCC = new BigDecimal(correlationCoefficient + 0.03);
		//
		boolean isASC = true;
		//重新计算这两列formerIndexOfGroup、latterIndexOfGroup平均值
		average(table,formerIndexOfGroup);
		average(table,latterIndexOfGroup);
		boolean flag = true;		
		while(flag){
			List<Double> averageListFirst = getAverageListByGroupID(table,formerIndexOfGroup);
			List<Double> averageListSecond = getAverageListByGroupID(table,latterIndexOfGroup);
			BigDecimal cC = new BigDecimal(ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(averageListFirst,averageListSecond));
			if(setCC.compareTo(BigDecimal.ZERO) >= 0){
				if(cC.compareTo(litlleCC) >=0 && cC.compareTo(bigCC) <= 0){
					flag = false;
				}else{
					//进行修改数据操作
					//设置绝对值
					for(int indexOfLine = 0; indexOfLine < table.getLineList().size(); indexOfLine++){
						double abs3Value = getBiaoZhun(averageListFirst.get(indexOfLine),averageListFirst) - getBiaoZhun(averageListSecond.get(indexOfLine),averageListSecond);
						table.getLineList().get(indexOfLine).getGroupList().get(formerIndexOfGroup).setAbs3Value(Math.abs(abs3Value));
						//table.getLineList().get(indexOfLine).getGroupList().get(formerIndexOfGroup).setAbs3Value(Math.abs(averageListFirst.get(indexOfLine)-averageListSecond.get(indexOfLine)));
					}
					
					//按latterIndexOfGroup组平均值进行升序或降序（一次升，一次降）排序
					Comparator<Line> comparator0 = new LineComparator4(latterIndexOfGroup);
					Collections.sort(table.getLineList(), comparator0);
					boolean tempBooleanFlag = true;
					if(isASC){
						tempBooleanFlag = true;
						isASC = false;
					}else{
						Collections.reverse(table.getLineList());
						isASC = true;
						tempBooleanFlag = false;
					}

					//排序
					Comparator<Line> comparator = new LineComparator3(formerIndexOfGroup);
					Collections.sort(table.getLineList(), comparator);
					//逆序
					Collections.reverse(table.getLineList());
					
					if(cC.compareTo(bigCC) > 0){
						//修改后5%的数据
						int alterIndex = (int) Math.ceil((1.0D - PERCENT) * table.getLineList().size());
						if(alterIndex>=1){				
							for(int indexOfLine = alterIndex-1; indexOfLine < table.getLineList().size(); indexOfLine++){
								int maxIndexOfColumn = table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().size();
								for(int indexOfColumn = 0; indexOfColumn < maxIndexOfColumn; indexOfColumn++){
									long temp = table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().get(indexOfColumn);
									long randomInt = (long) (RANDOM.nextInt(RANGE-1) + 1);
									while(randomInt == temp){
										randomInt = (long) (RANDOM.nextInt(RANGE-1) + 1);
									}
									table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().set(indexOfColumn, randomInt);
								}
							}
							//修改indexOfGroup+1组的平均值
							average(table,latterIndexOfGroup);	
						}
					}else{
						//修改前5%的数据
						int alterIndex = (int) Math.ceil(PERCENT * table.getLineList().size());
						if(alterIndex>=1){	
							for(int i = 0; i<alterIndex; i++){
								//赋均值
								int maxIndexOfColumn = table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().size();
								table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().clear();
								double average = table.getLineList().get(i).getGroupList().get(formerIndexOfGroup).getAverage();
								
								if(tempBooleanFlag){
									//升序
									for(int j = 0; j < maxIndexOfColumn; j++){
										int tempFlag = RANDOM.nextInt(2);
										if(tempFlag == 0){								
											table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average);
										}else{
											if(average+1 >= RANGE){
												table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)RANGE);
											}else{
												table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average+1);
											}
										}
									}
								}else{
									//降序
									for(int j = 0; j < maxIndexOfColumn; j++){
										int tempFlag = RANDOM.nextInt(2);
										if(tempFlag == 0){								
											table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average);
										}else{
											if(average-1 <= minRange){
												table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)minRange);
											}else{
												table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average-1);
											}
										}
									}
								}
							}
							//修改indexOfGroup+1组的平均值
							average(table,latterIndexOfGroup);	
						}	
					}
					//重新对第formerIndexOfGroup和latterIndexOfGroup的相关系数进行计算
				}
				
			}else{
				//进行修改数据操作
				//设置绝对值
				for(int indexOfLine = 0; indexOfLine < table.getLineList().size(); indexOfLine++){
					double abs3Value = getBiaoZhun(averageListFirst.get(indexOfLine),averageListFirst) - getBiaoZhun(averageListSecond.get(indexOfLine),averageListSecond);
					table.getLineList().get(indexOfLine).getGroupList().get(formerIndexOfGroup).setAbs3Value(Math.abs(abs3Value));
					//table.getLineList().get(indexOfLine).getGroupList().get(formerIndexOfGroup).setAbs3Value(Math.abs(averageListFirst.get(indexOfLine)-averageListSecond.get(indexOfLine)));
				}
				
				
				//按latterIndexOfGroup组平均值进行升序排序
				Comparator<Line> comparator0 = new LineComparator4(latterIndexOfGroup);
				Collections.sort(table.getLineList(), comparator0);
				boolean tempBooleanFlag = true;
				if(isASC){
					isASC = false;
					tempBooleanFlag = true;
				}else{
					Collections.reverse(table.getLineList());
					isASC = true;
					tempBooleanFlag = false;
				}
				
				
				
				//排序
				Comparator<Line> comparator = new LineComparator3(formerIndexOfGroup);
				Collections.sort(table.getLineList(), comparator);
				//逆序
				Collections.reverse(table.getLineList());
				
				if(cC.compareTo(bigCC) > 0){
					//修改后5%的数据
					int alterIndex = (int) Math.ceil((1.0D - PERCENT) * table.getLineList().size());
					if(alterIndex>=1){
						long maxElementInThisColumn = Compute.maxElementInGroup(table,latterIndexOfGroup);
						for(int indexOfLine = alterIndex-1; indexOfLine < table.getLineList().size(); indexOfLine++){
							//写
							int maxIndexOfColumn = table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().size();
							table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().clear();
							double average = table.getLineList().get(indexOfLine).getGroupList().get(formerIndexOfGroup).getAverage();
							average = maxElementInThisColumn+1-average;
							if(tempBooleanFlag){
								//升序
								for(int j = 0; j < maxIndexOfColumn; j++){
									int tempFlag = RANDOM.nextInt(2);
									if(tempFlag == 0){								
										table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average);
									}else{
										if(average-1 <= minRange){
											table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)minRange);
										}else{
											table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average-1);
										}
									}
								}
							}else{
								//降序
								for(int j = 0; j < maxIndexOfColumn; j++){
									int tempFlag = RANDOM.nextInt(2);
									if(tempFlag == 0){								
										table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average);
									}else if(tempFlag == 1){
										if(average+1 >= RANGE){
											table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)RANGE);
										}else{
											table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average+1);
										}
									}
								}
							}
							/*for(int j = 0; j < maxIndexOfColumn; j++){
								table.getLineList().get(indexOfLine).getGroupList().get(latterIndexOfGroup).getColumnList().add(maxElementInThisColumn+1-average);
							}*/
						}
						//修改indexOfGroup+1组的平均值
						average(table,latterIndexOfGroup);	
					}
				}else if(cC.compareTo(litlleCC) < 0){
					//修改前5%的数据
					int alterIndex = (int) Math.ceil(PERCENT * table.getLineList().size());
					if(alterIndex>=1){	
						for(int i = 0; i<alterIndex; i++){
							//赋均值
							int maxIndexOfColumn = table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().size();
							table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().clear();
							double average = table.getLineList().get(i).getGroupList().get(formerIndexOfGroup).getAverage();
							if(tempBooleanFlag){
								//升序
								for(int j = 0; j < maxIndexOfColumn; j++){
									int tempFlag = RANDOM.nextInt(2);
									if(tempFlag == 0){								
										table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average);
									}else{
										if(average-1 <= minRange){
											table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)minRange);
										}else{
											table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average-1);
										}
									}
								}
							}else{
								//降序
								for(int j = 0; j < maxIndexOfColumn; j++){
									int tempFlag = RANDOM.nextInt(2);
									if(tempFlag == 0){								
										table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average);
									}else{
										if(average+1 >= RANGE){
											table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)RANGE);
										}else{
											table.getLineList().get(i).getGroupList().get(latterIndexOfGroup).getColumnList().add((long)average+1);
										}
									}
								}
							}
						}
						//修改indexOfGroup+1组的平均值
						average(table,latterIndexOfGroup);	
					}	
				}else{
					//满足条件了
					flag = false;
				}
			}
		}
		
		//PERCENT = store;
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
 	
 	
	public void exportTable(Table table, String path) throws IOException{
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook workbook = new HSSFWorkbook();
     // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = workbook.createSheet("修改后的表格数据");  
     // 设置表格默认列宽度为15个字节  
        sheet.setDefaultColumnWidth((short)10);  
        // 生成一个样式  
        HSSFCellStyle style = workbook.createCellStyle();  
        // 设置这些样式  
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 生成一个字体  
        HSSFFont font = workbook.createFont();  
        font.setColor(HSSFColor.VIOLET.index);  
        font.setFontHeightInPoints((short) 12);  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        // 把字体应用到当前的样式  
        style.setFont(font);  
        // 生成并设置另一个样式  
        HSSFCellStyle style2 = workbook.createCellStyle();  
        style2.setFillForegroundColor(HSSFColor.WHITE.index);  
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成另一个字体  
        HSSFFont font2 = workbook.createFont();  
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        style2.setFont(font2);
        
        // 产生表格标题行  
        HSSFRow row = sheet.createRow(0);
        int groupNum = table.getLineList().get(0).getGroupList().size();
        HSSFCell cell = null;
        int cellNum = 0;
        for (short i = 0; i < groupNum; i++)  
        {  
        	for(int j = 0; j < table.getLineList().get(0).getGroupList().get(i).getColumnList().size(); j++){
        		 cell = row.createCell(cellNum);  
                 cell.setCellStyle(style);
                 int line = i;
                 int column = j;
                 HSSFRichTextString text = new HSSFRichTextString("" + line + column); 
                 cell.setCellValue(text);
                 cellNum++;
                 
        	}
        	//cell = row.createCell(cellNum);  
            //cell.setCellStyle(style);
           // HSSFRichTextString text = new HSSFRichTextString("第"+i+"平均值"); 
            //cell.setCellValue(text);
            //cellNum++;
        }
        
      //产生数据
        
        for (int line = 0; line < table.getLineList().size(); line++){
        	row = sheet.createRow(line+1);
        	int dataCellNum = 0;
        	for(int group = 0; group < table.getLineList().get(line).getGroupList().size();group++){
        		for(int column = 0; column < table.getLineList().get(line).getGroupList().get(group).getColumnList().size(); column++){
        			cell = row.createCell((short)(dataCellNum));
        			HSSFRichTextString text = new HSSFRichTextString(table.getLineList().get(line).getGroupList().get(group).getColumnList().get(column).toString()); 
        			cell.setCellValue(text);  
                    cell.setCellStyle(style2);
        			dataCellNum++;
        		}
        		//cell = row.createCell(dataCellNum);  
                //cell.setCellStyle(style2);
               // HSSFRichTextString text = new HSSFRichTextString("" + table.getLineList().get(line).getGroupList().get(group).getAverage()); 
               // cell.setCellValue(text);
                //dataCellNum++;
        	}
        }
        FileOutputStream fout = null;
        try {
        	fout = new FileOutputStream(path);
			workbook.write(fout);
			
		} finally {
			if(fout != null){
				fout.close();
			}
		}    
	}
	
	public void exportTable2(Table table, String path) throws IOException{
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook workbook = new HSSFWorkbook();
     // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = workbook.createSheet("修改后的表格数据");  
     // 设置表格默认列宽度为15个字节  
        sheet.setDefaultColumnWidth((short)10);  
        // 生成一个样式  
        HSSFCellStyle style = workbook.createCellStyle();  
        // 设置这些样式  
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 生成一个字体  
        HSSFFont font = workbook.createFont();  
        font.setColor(HSSFColor.VIOLET.index);  
        font.setFontHeightInPoints((short) 12);  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        // 把字体应用到当前的样式  
        style.setFont(font);  
        // 生成并设置另一个样式  
        HSSFCellStyle style2 = workbook.createCellStyle();  
        style2.setFillForegroundColor(HSSFColor.WHITE.index);  
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成另一个字体  
        HSSFFont font2 = workbook.createFont();  
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        style2.setFont(font2);
        
        // 产生表格标题行  
        HSSFRow row = sheet.createRow(0);
        int groupNum = table.getLineList().get(0).getGroupList().size();
        HSSFCell cell = null;
        int cellNum = 0;
        for (short i = 0; i < groupNum; i++)  
        {  
        	for(int j = 0; j < table.getLineList().get(0).getGroupList().get(i).getColumnList().size(); j++){
        		 cell = row.createCell(cellNum);  
                 cell.setCellStyle(style);
                 String line = "";
                 if(i == 0){
                	 line = "A";
                 }else if(i == 1){
                	 line = "B";
                 }else if(i == 2){
                	 line = "C";
                 }else if(i == 3){
                	 line = "D";
                 }else if(i == 4){
                	 line = "E";
                 }else if(i == 5){
                	 line = "F";
                 }else if(i == 6){
                	 line = "G";
                 }else if(i == 7){
                	 line = "H";
                 }else if(i == 8){
                	 line = "I";
                 }else if(i == 9){
                	 line = "J";
                 }else if(i == 10){
                	 line = "K";
                 }else if(i == 11){
                	 line = "L";
                 }else if(i == 12){
                	 line = "M";
                 }else if(i == 13){
                	 line = "N";
                 }else if(i == 14){
                	 line = "O";
                 }else if(i == 15){
					 line = "P";
				 }else if(i == 16){
					 line = "Q";
				 }else if(i == 17){
					 line = "R";
				 }else if(i == 18){
					 line = "S";
				 }else if(i == 19){
					 line = "T";
				 }else if(i == 20){
					 line = "U";
				 }else if(i == 21){
					 line = "V";
				 }else if(i == 22){
					 line = "W";
				 }else if(i == 23){
					 line = "X";
				 }else if(i == 24){
					 line = "Y";
				 }else if(i == 25){
					 line = "Z";
				 }
                 int column = j + 1;
                 HSSFRichTextString text = new HSSFRichTextString("" + line + column); 
                 cell.setCellValue(text);
                 cellNum++;
                 
        	}
        	//cell = row.createCell(cellNum);  
            //cell.setCellStyle(style);
           // HSSFRichTextString text = new HSSFRichTextString("第"+i+"平均值"); 
            //cell.setCellValue(text);
            //cellNum++;
        }
        
      //产生数据
        
        for (int line = 0; line < table.getLineList().size(); line++){
        	row = sheet.createRow(line+1);
        	int dataCellNum = 0;
        	for(int group = 0; group < table.getLineList().get(line).getGroupList().size();group++){
        		for(int column = 0; column < table.getLineList().get(line).getGroupList().get(group).getColumnList().size(); column++){
        			cell = row.createCell((short)(dataCellNum));
        			HSSFRichTextString text = new HSSFRichTextString(table.getLineList().get(line).getGroupList().get(group).getColumnList().get(column).toString()); 
        			cell.setCellValue(text);  
                    cell.setCellStyle(style2);
        			dataCellNum++;
        		}
        		//cell = row.createCell(dataCellNum);  
                //cell.setCellStyle(style2);
               // HSSFRichTextString text = new HSSFRichTextString("" + table.getLineList().get(line).getGroupList().get(group).getAverage()); 
               // cell.setCellValue(text);
                //dataCellNum++;
        	}
        }
        FileOutputStream fout = null;
        try {
        	fout = new FileOutputStream(path);
			workbook.write(fout);
			
		} finally {
			if(fout != null){
				fout.close();
			}
		}    
	}
	
	
	public void exportCorrelationCoefficientExcel(List<Result> resultList, String path) throws IOException{
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook workbook = new HSSFWorkbook();
     // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = workbook.createSheet("各组平均值相关系数表");  
     // 设置表格默认列宽度为15个字节  
        sheet.setDefaultColumnWidth((short) 15);  
        // 生成一个样式  
        HSSFCellStyle style = workbook.createCellStyle();  
        // 设置这些样式  
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 生成一个字体  
        HSSFFont font = workbook.createFont();  
        font.setColor(HSSFColor.VIOLET.index);  
        font.setFontHeightInPoints((short) 12);  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        // 把字体应用到当前的样式  
        style.setFont(font);  
        // 生成并设置另一个样式  
        HSSFCellStyle style2 = workbook.createCellStyle();  
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);  
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成另一个字体  
        HSSFFont font2 = workbook.createFont();  
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        style2.setFont(font2); 
        
        // 产生表格标题行  
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell0 = row.createCell(0);  
        cell0.setCellStyle(style);
        HSSFRichTextString text0 = new HSSFRichTextString("组间相关系数");  
        cell0.setCellValue(text0); 
        for (short i = 0; i < resultList.size()+1; i++)  
        {  
            HSSFCell cell = row.createCell(i+1);  
            cell.setCellStyle(style);
            int group = i+1;
            HSSFRichTextString text = new HSSFRichTextString("第"+i+"组");  
            cell.setCellValue(text);  
        }  
        
        //产生数据
        for (int i = 0; i < resultList.size(); i++)  
        {  
            row = sheet.createRow(i+1);
            HSSFCell cell = row.createCell((short)(0));
       	 	int group = i+1;
       	 	HSSFRichTextString text = new HSSFRichTextString("第"+i+"组");  
            cell.setCellValue(text);  
            cell.setCellStyle(style);
            for(int j = 0; j < resultList.get(i).getCorrelationCoefficientList().size(); j++){
            	
            	cell = row.createCell((short)(i+j+1+1));
                 cell.setCellValue(resultList.get(i).getCorrelationCoefficientList().get(j).doubleValue());
                 cell.setCellStyle(style2);
            }
        }
        //最后再加一行的第0列
        row = sheet.createRow(resultList.size()+1);
        HSSFCell cell = row.createCell((short)(0));
   	 	int group = resultList.size()+1;
   	 	HSSFRichTextString text = new HSSFRichTextString("第"+resultList.size()+"组");  
        cell.setCellValue(text);  
        cell.setCellStyle(style);
        FileOutputStream fout = null;
        try {
        	fout = new FileOutputStream(path);
			workbook.write(fout);
			
		} finally {
			if(fout != null){
				fout.close();
			}
		}  
	}
	
 	public List<Result> outputCorrelationCoefficient(Table table){
		List<Result> resultList = new ArrayList<Result>();
		for(int formerIndexOfGroup = 0; formerIndexOfGroup < GROUPNUM-1; formerIndexOfGroup++){
			Result result = new Result();
			for(int latterIndexOfGroup = formerIndexOfGroup+1; latterIndexOfGroup < GROUPNUM;latterIndexOfGroup++){
				List<Double> averageListFirst = getAverageListByGroupID(table,formerIndexOfGroup);
				List<Double> averageListSecond = getAverageListByGroupID(table,latterIndexOfGroup);
				double correlationCoefficient = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(averageListFirst, averageListSecond);
				BigDecimal   b   =   new   BigDecimal(correlationCoefficient);  
				correlationCoefficient   = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
				
				result.getCorrelationCoefficientList().add(correlationCoefficient);
			}
			resultList.add(result);
		}
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
				long temp = table.getLineList().get(lineNum).getGroupList().get(groupID).getColumnList().get(columnIndex);
				columnList.get(columnIndex).getColumnData().add(temp);
			}
		}
		return columnList;
	}
	private static List<Double> getAverageListByGroupID(Table table, int groupID){
		List<Double> averageList = new ArrayList<Double>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){		
			averageList.add(table.getLineList().get(lineNum).getGroupList().get(groupID).getAverage());
		}
		return averageList;
	}
	
	private static List<Double> getAverage2ListByGroupID(Table table, int groupID){
		List<Double> averageList = new ArrayList<Double>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){		
			averageList.add(table.getLineList().get(lineNum).getGroupList().get(groupID).getAverage2());
		}
		return averageList;
	}
	
	private static int indexOfMin(List<Double> correlationCoefficientList){
		double min = correlationCoefficientList.get(0);
		int index = 0;
		for(int i = 1; i < correlationCoefficientList.size(); i++){
			if(correlationCoefficientList.get(i) < min){
				min = correlationCoefficientList.get(i);
				index = i;
			}
		}
		return index;
	}
	
	private static long maxElementInGroup(Table table, int groupId){
		List<Column> columnList =  getColumnListByGroupID(table,groupId);
		long maxElement = 0;
		for(int colNum = 0; colNum < columnList.size(); colNum++){	
			for(int line = 0; line < columnList.get(colNum).getColumnData().size(); line++){
				long temp = columnList.get(colNum).getColumnData().get(line);
				if(temp > maxElement){
					maxElement = temp;
				}
			}
		}
		return maxElement;
	}
	public double getPERCENT() {
		return PERCENT;
	}
	public double getJ2PARAMETER() {
		return J2PARAMETER;
	}
	public double getJ3PARAMETER() {
		return J3PARAMETER;
	}
	public double getJ4PARAMETER() {
		return J4PARAMETER;
	}
	public double getPARAMETER_OF_THREESTEP() {
		return PARAMETER_OF_THREESTEP;
	}
	public double getPARAMETER_OF_FOUR_ONE_STEP() {
		return PARAMETER_OF_FOUR_ONE_STEP;
	}

	
	
	
}
