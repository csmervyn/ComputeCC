package org.github.mervyn.fwjxgxsxgxin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.github.mervyn.domain.DYHGParameter;
import org.github.mervyn.domain.GroupDYHGParam;
import org.github.mervyn.fwjxgxsxg.Ceil;
import org.github.mervyn.fwjxgxsxg.LineComparator7;
import org.github.mervyn.fwjxgxsxg.ParseExcelForFWJ;
import org.github.mervyn.fwjxgxsxg.SingleColumnLine;
import org.github.mervyn.fwjxgxsxg.SingleColumnTable;
import org.github.mervyn.utils.ComputeCorrelationCoefficient;
import org.github.mervyn.utils.Result;
import org.github.mervyn.utils.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Jama.Matrix;

@Service("fWJXGXSXGXin")
public class FWJXGXSXGXin {
	@Autowired
	private ParseExcelForFWJ parseExcelForFWJ;
	
	public List<Matrix> groupCompute(SingleColumnTable table,GroupDYHGParam groupDyhgParam){
		List<Matrix> list = new ArrayList<Matrix>();
		for(int i = 0; i < groupDyhgParam.getDyhgList().size(); i++){
			list.add(computeDYHGYZ(table,groupDyhgParam.getDyhgList().get(i)));
		}
		return list;
	}
	
	
	public void computeBiaoZhun(SingleColumnTable table){
		for(int columnId = 0; columnId < table.getColumnNum(); columnId++){
			computeBiaoZhun(columnId,table);
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
	
	
	public List<Result> main(int former,int  latter,double parameter, SingleColumnTable table,String path) throws IOException{
		List<Double> shuLie =  first(latter,table);
		second(former,latter,parameter,table,shuLie);
		//先排序
		Comparator<SingleColumnLine> comparator = new LineComparator7();
		Collections.sort(table.getLineList(), comparator);
		List<Result> resultList = outputCorrelationCoefficient(table);
		//exportCorrelationCoefficientExcel(resultList,path+  File.separator + "FWJXGXSXGXin相关系数表.xls");
		//输出修改后的原始表格
		//exportTable2(table,path+ File.separator  + "非问卷相关系数修改后的表格数据.xls");
		//将修改后的原始表格输出到原始表格的位置
		exportTable2(table,path+ File.separator  + "FWJXGXSXGXin.xls");
		return resultList;
	}
	
 	private List<Double> first(int  latter,SingleColumnTable table){
		int ksize =  getColumnListByColumnID(latter,table).size();
		double kAverage = getAverage(latter,table);
		double ksd = getStandardDeviation(latter, table);
		double max = getMaxElementInColumnList(latter,table);
		double min = getMinElementInColumnList(latter,table);
		
		List<Double> resultList = new ArrayList<Double>();
		
		Random random = new Random(1);
		NormalDistribution normalDistributioin = new NormalDistribution(kAverage, ksd);
		
		for(int i = 0; i< ksize; i++){
			double temp = normalDistributioin.inverseCumulativeProbability(random.nextDouble());
			/*BigDecimal bg = new BigDecimal(temp);
			temp = bg.setScale(10, BigDecimal.ROUND_HALF_UP).doubleValue();*/
			if(temp >= min && temp <=max){
				resultList.add(temp);
			}else{
				i--;
			}
		}
		
		double nAverage = 0;
		for(double temp:resultList){
			nAverage += temp;
		}
		nAverage /= resultList.size();
		
		double deviation = 0;
		for(int i = 0; i < resultList.size(); i++){
			deviation = deviation + (resultList.get(i)-nAverage) * (resultList.get(i)-nAverage);
		}
		double sd = Math.sqrt(deviation/resultList.size());
		
		List<Double> finalList = new ArrayList<Double>();
		for(double temp:resultList){
			double data = ((temp - nAverage)/sd)*ksd+kAverage;
			if(data >= min && data <=max){
				finalList.add(data);
			}
		}
		return finalList;
	}
	
	private void secondOne(int former,int  latter,double parameter, SingleColumnTable table, List<Double> shuLie){
		boolean isEnd = Boolean.FALSE;
		while(!isEnd){
			List<Double> iList = getColumnListByColumnID(former,table);
			List<Double> kList = getColumnListByColumnID(latter,table);
			double cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(iList, kList);
			computeBiaoZhun(latter,table);
			if(cc < (parameter + 0.03)){
				//计算Xi标*Xk标
				for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){
					double temp = table.getLineList().get(lineNum).getColumnList().get(former).getBiaoZhun()
									*table.getLineList().get(lineNum).getColumnList().get(latter).getBiaoZhun();
					table.getLineList().get(lineNum).setB(temp);
				}
				Comparator<SingleColumnLine> comparator = new LineComparatorWithBiaoZhun();
				Collections.sort(table.getLineList(), comparator);
				if(table.getLineList().get(0).getColumnList().get(former).getBiaoZhun() > 0){
					Ceil ceil = table.getLineList().get(0).getColumnList().get(latter);
					double max = findRemoveMaxInShuLie(shuLie);
					if(ceil.isLong()){
						ceil.setLongValue((long)max);
					}else{	
						
						BigDecimal bg = new BigDecimal(max);
						max = bg.setScale(ceil.getDigit(), BigDecimal.ROUND_HALF_UP).doubleValue();
						ceil.setDoubleValue(max);
					}
					ceil.setAlter(Boolean.TRUE);
				}else if(table.getLineList().get(0).getColumnList().get(former).getBiaoZhun() < 0){
					Ceil ceil = table.getLineList().get(0).getColumnList().get(latter);
					double min = findRemoveMinInShuLie(shuLie);
					if(ceil.isLong()){
						ceil.setLongValue((long)min);
					}else{	
						BigDecimal bg = new BigDecimal(min);
						min = bg.setScale(ceil.getDigit(), BigDecimal.ROUND_HALF_UP).doubleValue();
						ceil.setDoubleValue(min);
					}
					ceil.setAlter(Boolean.TRUE);
				}else{
					//不操作
					;
				}
				
			}else{
				isEnd = Boolean.TRUE;
			}
		}
	}
	
	private void secondTwo(int former,int  latter,double parameter, SingleColumnTable table, List<Double> shuLie){
		boolean isEnd = Boolean.FALSE;
		while(!isEnd){
			List<Double> iList = getColumnListByColumnID(former,table);
			List<Double> kList = getColumnListByColumnID(latter,table);
			double cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(iList, kList);
			computeBiaoZhun(latter,table);
			if(cc > (parameter-0.03)){
				//计算Xi标*Xk标
				for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){
					double temp = table.getLineList().get(lineNum).getColumnList().get(former).getBiaoZhun()
									*table.getLineList().get(lineNum).getColumnList().get(latter).getBiaoZhun();
					table.getLineList().get(lineNum).setB(temp);
				}
				Comparator<SingleColumnLine> comparator = new LineComparatorWithBiaoZhun();
				Collections.sort(table.getLineList(), comparator);
				Collections.reverse(table.getLineList());
				if(table.getLineList().get(0).getColumnList().get(former).getBiaoZhun() > 0){
					Ceil ceil = table.getLineList().get(0).getColumnList().get(latter);
					double min = findRemoveMinInShuLie(shuLie);
					if(ceil.isLong()){
						ceil.setLongValue((long)min);
					}else{	
						BigDecimal bg = new BigDecimal(min);
						min = bg.setScale(ceil.getDigit(), BigDecimal.ROUND_HALF_UP).doubleValue();
						ceil.setDoubleValue(min);
					}
					ceil.setAlter(Boolean.TRUE);
				}else if(table.getLineList().get(0).getColumnList().get(former).getBiaoZhun() < 0){
					Ceil ceil = table.getLineList().get(0).getColumnList().get(latter);
					double max = findRemoveMaxInShuLie(shuLie);
					if(ceil.isLong()){
						ceil.setLongValue((long)max);
					}else{	
						BigDecimal bg = new BigDecimal(max);
						max = bg.setScale(ceil.getDigit(), BigDecimal.ROUND_HALF_UP).doubleValue();
						ceil.setDoubleValue(max);
					}
					ceil.setAlter(Boolean.TRUE);
				}else{
					//不操作
					;
				}
			}else{
				isEnd = Boolean.TRUE;
			}
		}
	}
	
	private void second(int former,int  latter,double parameter, SingleColumnTable table, List<Double> shuLie){
		List<Double> iList = getColumnListByColumnID(former,table);
		computeBiaoZhun(former,table);
		List<Double> kList = getColumnListByColumnID(latter,table);
		
		double cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(iList, kList);
		if(cc < parameter){
			secondOne(former,latter,parameter,table,shuLie);
		}else if(cc > parameter){
			secondTwo(former,latter,parameter,table,shuLie);
		}
	}
	
	public double findRemoveMaxInShuLie(List<Double> shuLie){
		double max = Double.MIN_VALUE;
		int index = 0;
		for(int i = 0; i< shuLie.size(); i++){
			if(shuLie.get(i) > max){
				max = shuLie.get(i);
				index = i;
			}
		}
		shuLie.remove(index);
		return max;
	}
	
	public double findRemoveMinInShuLie(List<Double> shuLie){
		double min = Double.MAX_VALUE;
		int index = 0;
		for(int i = 0; i< shuLie.size(); i++){
			if(shuLie.get(i) < min){
				min = shuLie.get(i);
				index = i;
			}
		}
		shuLie.remove(index);
		return min;
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
	
	public List<Double> getBiaoZhun(int columnNum, SingleColumnTable table){
		List<Double> list = new ArrayList<Double>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){
			Ceil ceil = table.getLineList().get(lineNum).getColumnList().get(columnNum);
			list.add(ceil.getBiaoZhun());
		}
		return list;
	}
	
  	public double getMaxElementInColumnList(int columnNum, SingleColumnTable table){
		BigDecimal max = new BigDecimal(Double.MIN_VALUE);
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){
			BigDecimal temp;
			if(table.getLineList().get(lineNum).getColumnList().get(columnNum).isLong()){
				temp = new BigDecimal(table.getLineList().get(lineNum).getColumnList().get(columnNum).getLongValue());
			}else{
				temp = new BigDecimal(table.getLineList().get(lineNum).getColumnList().get(columnNum).getDoubleValue());
			}
			if(temp.compareTo(max) > 0){
				max = temp;
			}
		}
		return max.doubleValue();	
	}
	
	public double getMinElementInColumnList(int columnNum, SingleColumnTable table){
		BigDecimal min = new BigDecimal(Double.MAX_VALUE);
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){
			BigDecimal temp;
			if(table.getLineList().get(lineNum).getColumnList().get(columnNum).isLong()){
				temp = new BigDecimal(table.getLineList().get(lineNum).getColumnList().get(columnNum).getLongValue());
			}else{
				temp = new BigDecimal(table.getLineList().get(lineNum).getColumnList().get(columnNum).getDoubleValue());
			}
			if(temp.compareTo(min) < 0){
				min = temp;
			}
		}
		return min.doubleValue();	
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

 	public List<Ceil> getCeilListByColumnID(int columnNum, SingleColumnTable table){
 		List<Ceil> columnList = new ArrayList<Ceil>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){
			columnList.add(table.getLineList().get(lineNum).getColumnList().get(columnNum));
		}
		return columnList;
 	}
 	
 	public List<Result> outputCorrelationCoefficient(SingleColumnTable table){
		List<Result> resultList = new ArrayList<Result>();
		for(int formerIndexOfColumn = 0; formerIndexOfColumn < table.getColumnNum()-1; formerIndexOfColumn++){
			Result result = new Result();
			for(int latterIndexOfColumn = formerIndexOfColumn+1; latterIndexOfColumn < table.getColumnNum();latterIndexOfColumn++){
				List<Double> averageListFirst = getColumnListByColumnID(formerIndexOfColumn,table);
				List<Double> averageListSecond = getColumnListByColumnID(latterIndexOfColumn,table);
				double correlationCoefficient = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(averageListFirst, averageListSecond);
				BigDecimal   b   =   new   BigDecimal(correlationCoefficient);  
				correlationCoefficient   = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
				result.getCorrelationCoefficientList().add(correlationCoefficient);
			}
			resultList.add(result);
		}
		return resultList;
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
	
 	public SingleColumnTable parseTable(String path) throws IOException{
		String urlStr = path + File.separator + "FWJXGXSXGXin.xls";
		File file = new File(urlStr);
		InputStream in = new FileInputStream(file); 
		
		//解析excel中的数据
		SingleColumnTable table = null;
		table = parseExcelForFWJ.parse(in);
		in.close();
		return table;
	}

 	public void exportTable2(SingleColumnTable table, String path) throws IOException{
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
        
        // 生成并设置另一个样式  
        HSSFCellStyle style3 = workbook.createCellStyle();  
        style3.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);  
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style3.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成另一个字体  
        HSSFFont font3 = workbook.createFont();  
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        style2.setFont(font3); 
        
        // 产生表格标题行  
        HSSFRow row = sheet.createRow(0);
        //int groupNum = table.getLineList().get(0).getGroupList().size();
        int columnNum = table.getColumnNum();
        HSSFCell cell = null;
        int cellNum = 0;
        for(short i = 0; i < columnNum; i++){
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
            
            HSSFRichTextString text = new HSSFRichTextString("" + line); 
            cell.setCellValue(text);
            cellNum++;
        }
        
      //产生数据
        
        for (int line = 0; line < table.getLineList().size(); line++){
        	row = sheet.createRow(line+1);
        	int dataCellNum = 0;
        	for(int column = 0; column < table.getLineList().get(line).getColumnList().size(); column++){
    			cell = row.createCell((short)(dataCellNum));
    			String str = null;
    			if(table.getLineList().get(line).getColumnList().get(column).isLong()){
    				str = "" + table.getLineList().get(line).getColumnList().get(column).getLongValue();
    			}else{
    				double temp = table.getLineList().get(line).getColumnList().get(column).getDoubleValue();
    				String weiShuStr = "%." + table.getLineList().get(line).getColumnList().get(column).getDigit() + "f";
    				
    				str = "" + subZeroAndDot(String .format(weiShuStr,temp)); 
    			}
    
    			HSSFRichTextString text = new HSSFRichTextString(str); 
    			cell.setCellValue(text);
    			if(table.getLineList().get(line).getColumnList().get(column).isAlter()){    				
    				cell.setCellStyle(style3);
    			}else{
    				cell.setCellStyle(style2);
    			}
    			dataCellNum++;
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
 	
 	public static String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }  
}
