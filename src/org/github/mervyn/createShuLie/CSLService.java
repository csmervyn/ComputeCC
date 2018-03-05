package org.github.mervyn.createShuLie;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.github.mervyn.domain.NormalDistribution1;

import org.springframework.stereotype.Service;

@Service("cSLService")
public class CSLService {
	public void create(List<NormalDistribution1> list,String path) throws IOException{
		int maxLine = 0;
		for(NormalDistribution1 nd:list){
			if(nd.getNum() > maxLine){	
				maxLine = nd.getNum();
			}
		}
		Table table = new Table(list.size());
		table.setMaxLineNum(maxLine);
		//List<Column> resultList = getFinalData(createData(list));
		//List<Column> resultList = createSecondData(createData(list));
		List<Column> resultList =createData(list);
		table.setListData(resultList);
		exportTable(table, path);
	}
	
	private List<Column> getFinalData(List<Column> resultList){
		List<Column> finalResultList = new ArrayList<Column>();
		for(int index = 0; index < resultList.size(); index++){
			Column col = resultList.get(index);
			List<Double> itemList = col.getColumnData();
			Column resultCol = new Column();
			resultCol.setNum(col.getNum());
			resultCol.setDecimalPlace(col.getDecimalPlace());
			resultCol.setTargetAverage(col.getTargetAverage());
			resultCol.setTargetSD(col.getTargetSD());
			
			double average = average(itemList);
			col.setAverage(average);
			double sd =  getSD(itemList);
			col.setSd(sd);
			
			for(double item : itemList){
				double temp = ((item - average)/sd)*resultCol.getTargetSD() + resultCol.getTargetAverage();
				BigDecimal bg = new BigDecimal(temp);
				temp = bg.setScale(resultCol.getDecimalPlace(), BigDecimal.ROUND_HALF_UP).doubleValue();
				resultCol.getColumnData().add(temp);
			}
			
			double average1 = average(resultCol.getColumnData());
			resultCol.setAverage(average);
			double sd1 =  getSD(resultCol.getColumnData());
			resultCol.setSd(sd);
			finalResultList.add(resultCol);
		}
		return finalResultList;
	}
	
	private static double getSD(List<Double> list){
		double average = average(list);
		double deviation = 0;
		for(int i = 0; i < list.size(); i++){
			deviation = deviation + (list.get(i)-average) * (list.get(i)-average);
		}
		double sd = Math.sqrt(deviation/list.size());
		return sd;
	}

	private static double minSD(double min,double average,double sd){
		return (min-average)/sd;
	}

	private static double maxSD(double max,double average,double sd){
		return (max-average)/sd;
	}
	
	
	private static double average(List<Double> list){
		double sum = 0;
		for(int i = 0; i < list.size(); i++){
			sum += list.get(i);
		}
		double average = sum / list.size();
		return average;
	}

	private List<Column> createSecondData(List<Column> list){
		List<Column> resultList = new ArrayList<Column>();
		for(int index = 0; index < list.size(); index++){
			Column col = list.get(index);
			List<Double> itemList = col.getColumnData();
			Column resultCol = new Column();
			resultCol.setNum(col.getNum());
			resultCol.setDecimalPlace(col.getDecimalPlace());
			resultCol.setTargetAverage(col.getTargetAverage());
			resultCol.setTargetSD(col.getTargetSD());
			double average = average(itemList);
			col.setAverage(average);
			double sd =  getSD(itemList);
			col.setSd(sd);
			for(double item : itemList){
				double temp = ((item - average)/sd);
				BigDecimal bg = new BigDecimal(temp);
				temp = bg.setScale(resultCol.getDecimalPlace(), BigDecimal.ROUND_HALF_UP).doubleValue();
				resultCol.getColumnData().add(temp);
			}

			double average1 = average(resultCol.getColumnData());
			resultCol.setAverage(average);
			double sd1 =  getSD(resultCol.getColumnData());
			resultCol.setSd(sd);
			resultList.add(resultCol);
		}

		return resultList;
	}


	private List<Column> createData(List<NormalDistribution1> list){
		List<Column> resultList = new ArrayList<Column>();
		for(NormalDistribution1 nd:list){
			Column result = new Column();
			result.setNum(nd.getNum());
			result.setDecimalPlace(nd.getDecimalPlace());
			result.setTargetAverage(nd.getMean());
			result.setTargetSD(nd.getSd());
			Random random = new Random(1);
			NormalDistribution normalDistributioin = new NormalDistribution(nd.getMean(), nd.getSd());
			for(int i = 0; i< nd.getNum(); i++){
				double temp = normalDistributioin.inverseCumulativeProbability(random.nextDouble());
				BigDecimal bg = new BigDecimal(temp);
				temp = bg.setScale(nd.getDecimalPlace(), BigDecimal.ROUND_HALF_UP).doubleValue();
				if(nd.isFlag()){
					double minSD = minSD(nd.getMin(),nd.getMean(),nd.getSd());
					double maxSD = maxSD(nd.getMax(),nd.getMean(),nd.getSd());
					if(temp >= minSD && temp <=maxSD){
						result.getColumnData().add(temp);
					}else{
						i--;
					}
				}else{
					double tempSd = (temp - nd.getMean())/nd.getSd();
					if(tempSd >= -2.3 && tempSd <=2.3){
						result.getColumnData().add(temp);
					}else{
						i--;
					}
				}
			}
			resultList.add(result);
		}
		return resultList;
	}

	public void exportTable(Table table, String path) throws IOException{
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook workbook = new HSSFWorkbook();
     // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = workbook.createSheet("修改后的表格数据");  
     // 设置表格默认列宽度为15个字节  
        sheet.setDefaultColumnWidth((short)15);  
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
        int groupNum = table.getGroupNum();
        HSSFCell cell = null;
        int cellNum = 0;
        for (short i = 0; i < groupNum; i++)  
        {  
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
                 HSSFRichTextString text = new HSSFRichTextString("" + line); 
                 cell.setCellValue(text);
                 cellNum++;
        }
        
      //产生数据
      //先列后行
        for(int line = 0; line < table.getMaxLineNum(); line++){
        	row = sheet.createRow(line+1);
        	for (int column = 0; column < table.getGroupNum(); column++){
        		cell = row.createCell((short)(column));
        		if(line >=table.getListData().get(column).getColumnData().size()){
        			
        		}else{
        			double  temp = table.getListData().get(column).getColumnData().get(line);
        			
        			String weiShuStr = "%." + table.getListData().get(column).getDecimalPlace() + "f";
    				
    				String str = "" + subZeroAndDot(String .format(weiShuStr,temp)); 
        			HSSFRichTextString text = new HSSFRichTextString(str); 
        			cell.setCellValue(text);  
                    cell.setCellStyle(style2);
        		}
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
