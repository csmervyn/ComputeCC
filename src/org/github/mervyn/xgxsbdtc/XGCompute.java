package org.github.mervyn.xgxsbdtc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.github.mervyn.utils.ComputeCorrelationCoefficient;
import org.junit.Test;


public class XGCompute {
	public final static Random RANDOM = new Random();
	public final static BigDecimal CCPARAMETER = new BigDecimal(0.5);
	private final  int GROUPNUM;	//每行中有多少组
	private final double PERCENT;	//关于前多少数据需要修改
	private final  double J2PARAMETER;	//关于j等于2的时候的参数
	private final  double J3PARAMETER;	//关于j等于3的时候的参数
	private final  double J4PARAMETER;	//关于j等于4的时候的参数

	private final double PARAMETER_OF_THREESTEP; //关于第三步的参数
	
	private final double PARAMETER_OF_FOUR_ONE_STEP; //关于第4.1步的参数
	
	private final int RANGE;	//关于第三不步数字的范围

	
	
	
	public XGCompute(int groupNum,double percent,double j2Parameter,double j3Parameter,double j4Parameter,double parameterOfThreeStep,double parameterOfFourOneStep,int range){
		this.GROUPNUM = groupNum;
		this.PERCENT = percent;
		this.J2PARAMETER = j2Parameter;
		this.J3PARAMETER = j3Parameter;
		this.J4PARAMETER = j4Parameter;
		this.PARAMETER_OF_THREESTEP = parameterOfThreeStep;
		this.PARAMETER_OF_FOUR_ONE_STEP = parameterOfFourOneStep;
		this.RANGE = range;
	}
	public XGCompute(int groupNum,int range){
		this.GROUPNUM = groupNum;
		this.J2PARAMETER = 0.85D;
		this.J3PARAMETER = 0.80D;
		this.J4PARAMETER = 0.78D;
		this.PERCENT = 0.05D;
		this.PARAMETER_OF_THREESTEP = 0.75D;
		this.PARAMETER_OF_FOUR_ONE_STEP = 0.0D;
		this.RANGE = range;
	}
	
	public XGCompute(){
		this.GROUPNUM = 0;
		this.PERCENT = 0.05D;
		this.J2PARAMETER = 0.85D;
		this.J3PARAMETER = 0.80D;
		this.J4PARAMETER = 0.78D;
		this.PARAMETER_OF_THREESTEP = 0.75D;
		this.PARAMETER_OF_FOUR_ONE_STEP = 0.0D;
		this.RANGE = 0;
	}
	
	public XGCompute(int groupNum,double percent,int range){
		this.GROUPNUM = groupNum;
		this.PERCENT = percent;
		this.RANGE = range;
		
		this.J2PARAMETER = 0.85D;
		this.J3PARAMETER = 0.80D;
		this.J4PARAMETER = 0.78D;
		this.PARAMETER_OF_THREESTEP = 0.75D;
		this.PARAMETER_OF_FOUR_ONE_STEP = 0.0D;
	}
	
	
	public XGCompute(int groupNum){
		this.GROUPNUM = groupNum;
		this.PERCENT = 0.05D;
		this.J2PARAMETER = 0.85D;
		this.J3PARAMETER = 0.80D;
		this.J4PARAMETER = 0.78D;
		this.PARAMETER_OF_THREESTEP = 0.75D;
		this.PARAMETER_OF_FOUR_ONE_STEP = 0.0D;
		this.RANGE = 0;
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
	
	
	
	
	
	
 	
	public void exportTable2(Table table, String path) throws IOException{
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook workbook = new HSSFWorkbook();
     // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = workbook.createSheet("修改后的表格数据");  
     // 设置表格默认列宽度为15个字节  
        sheet.setDefaultColumnWidth((short)20);  
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
        			String sstext = ""+table.getLineList().get(line).getGroupList().get(group).getColumnList().get(column).toString();
        			int index = sstext.lastIndexOf(".");
        			if(sstext.substring(index+1).equals("0")){
        				sstext = sstext.substring(0, index);
        			}
        			HSSFRichTextString text = new HSSFRichTextString(sstext); 
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
				double temp = table.getLineList().get(lineNum).getGroupList().get(groupID).getColumnList().get(columnIndex);
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
