package org.github.mervyn.fwjxgxsxg;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.github.mervyn.utils.Column;
import org.github.mervyn.utils.ComputeCorrelationCoefficient;
import org.github.mervyn.utils.Result;
import org.github.mervyn.utils.Table;

public class FWJCompute {
	private int formerColumn;
	private int latterColumn;
	private double parameter;
	
	
	
	public FWJCompute(int formerColumn, int latterColumn, double parameter){
		this.formerColumn = formerColumn;
		this.latterColumn = latterColumn;
		this.parameter = parameter;
	}
	public void first(SingleColumnTable table){
		//算法可能有问题
		BigDecimal litlleCC = new BigDecimal(parameter - 0.05);
		BigDecimal bigCC = new BigDecimal(parameter + 0.05);
		Comparator<SingleColumnLine> comparatorFormer = new SingleColumnLineComparator(formerColumn);
		Comparator<SingleColumnLine> comparatorLatter = new SingleColumnLineComparator(latterColumn);
		int lineNum = table.getLineList().size();
		int formerLineCount = 0;
		int latterLineCount = 0; 
		boolean topEndFlag = true;
		
		List<Double> formerColumnListm = getColumnListByColumnID(formerColumn,table);
		List<Double> latterColumnListm = getColumnListByColumnID(latterColumn,table);
		BigDecimal correlationCoefficientm = new BigDecimal(ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(formerColumnListm, latterColumnListm));
		if(correlationCoefficientm.compareTo(bigCC) >0){
			while(formerLineCount + latterLineCount < lineNum){
				List<Double> formerColumnList = getColumnListByColumnID(formerColumn,table);
				List<Double> latterColumnList = getColumnListByColumnID(latterColumn,table);
				BigDecimal correlationCoefficient = new BigDecimal(ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(formerColumnList, latterColumnList));
				if(correlationCoefficient.compareTo(bigCC) >0){
					Collections.sort(table.getLineList(), comparatorFormer);
					Collections.sort(table.getLineList(), comparatorLatter);
					//Collections.reverse(table.getLineList());
					double result = 0;
					if(topEndFlag){
						double max = getMaxElementInColumnList(latterColumn,table);
						double standardDeviation = getStandardDeviation(latterColumn,table);
						result = max - (2 * standardDeviation / lineNum) * formerLineCount;
						formerLineCount++;
						topEndFlag = false;
						
						if(table.getLineList().get(formerLineCount).getColumnList().get(latterColumn).isLong()){
							Ceil temp = table.getLineList().get(formerLineCount).getColumnList().get(latterColumn);
							temp.setLongValue((long)result);
							temp.setAlter(Boolean.TRUE);
							table.getLineList().get(formerLineCount).getColumnList().set(latterColumn, temp);
						}else{
							Ceil temp = table.getLineList().get(formerLineCount).getColumnList().get(latterColumn);
							temp.setAlter(Boolean.TRUE);
							BigDecimal bg = new BigDecimal(result).setScale(temp.getDigit(), RoundingMode.UP);
							temp.setDoubleValue(bg.doubleValue());
							table.getLineList().get(formerLineCount).getColumnList().set(latterColumn, temp);
						}
						
						
						
					}else{
						double min = getMinElementInColumnList(latterColumn,table);
						double standardDeviation = getStandardDeviation(latterColumn,table);
						result = min + (2 * standardDeviation / lineNum) * latterLineCount;
						latterLineCount++;
						topEndFlag = true;
						
						
						if(table.getLineList().get(lineNum-1-latterLineCount).getColumnList().get(latterColumn).isLong()){
							Ceil temp = table.getLineList().get(lineNum-1-latterLineCount).getColumnList().get(latterColumn);
							temp.setLongValue((long)result);
							temp.setAlter(Boolean.TRUE);
							table.getLineList().get(lineNum-1-latterLineCount).getColumnList().set(latterColumn, temp);
						}else{
							Ceil temp = table.getLineList().get(lineNum-1-latterLineCount).getColumnList().get(latterColumn);
							temp.setAlter(Boolean.TRUE);
							BigDecimal bg = new BigDecimal(result).setScale(temp.getDigit(), RoundingMode.UP);
							temp.setDoubleValue(bg.doubleValue());
							table.getLineList().get(lineNum-1-latterLineCount).getColumnList().set(latterColumn, temp);
						}
						
						
					}
				}else{
					break;
				}
			}
		}else if(correlationCoefficientm.compareTo(litlleCC) <0){
			while(formerLineCount + latterLineCount < lineNum){
				List<Double> formerColumnList = getColumnListByColumnID(formerColumn,table);
				List<Double> latterColumnList = getColumnListByColumnID(latterColumn,table);
				BigDecimal correlationCoefficient = new BigDecimal(ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(formerColumnList, latterColumnList));
				if(correlationCoefficient.compareTo(litlleCC) <0){
					Collections.sort(table.getLineList(), comparatorFormer);
					
					Collections.sort(table.getLineList(), comparatorLatter);
					Collections.reverse(table.getLineList());
					double result = 0;
					if(!topEndFlag){
						double max = getMaxElementInColumnList(latterColumn,table);
						double standardDeviation = getStandardDeviation(latterColumn,table);
						result = max - (2 * standardDeviation / lineNum) * formerLineCount;
						formerLineCount++;
						topEndFlag = true;
						
						
						if(table.getLineList().get(formerLineCount).getColumnList().get(latterColumn).isLong()){
							Ceil temp = table.getLineList().get(formerLineCount).getColumnList().get(latterColumn);
							temp.setLongValue((long)result);
							temp.setAlter(Boolean.TRUE);
							table.getLineList().get(formerLineCount).getColumnList().set(latterColumn, temp);
						}else{
							Ceil temp = table.getLineList().get(formerLineCount).getColumnList().get(latterColumn);
							temp.setAlter(Boolean.TRUE);
							BigDecimal bg = new BigDecimal(result).setScale(temp.getDigit(), RoundingMode.UP);
							temp.setDoubleValue(bg.doubleValue());
							table.getLineList().get(formerLineCount).getColumnList().set(latterColumn, temp);
						}
						
						
					}else{
						double min = getMinElementInColumnList(latterColumn,table);
						double standardDeviation = getStandardDeviation(latterColumn,table);
						result = min + (2 * standardDeviation / lineNum) * latterLineCount;
						latterLineCount++;
						topEndFlag = false;
						
						if(table.getLineList().get(lineNum-1-latterLineCount).getColumnList().get(latterColumn).isLong()){
							Ceil temp = table.getLineList().get(lineNum-1-latterLineCount).getColumnList().get(latterColumn);
							temp.setLongValue((long)result);
							temp.setAlter(Boolean.TRUE);
							table.getLineList().get(lineNum-1-latterLineCount).getColumnList().set(latterColumn, temp);
						}else{
							Ceil temp = table.getLineList().get(lineNum-1-latterLineCount).getColumnList().get(latterColumn);
							temp.setAlter(Boolean.TRUE);
							BigDecimal bg = new BigDecimal(result).setScale(temp.getDigit(), RoundingMode.UP);
							temp.setDoubleValue(bg.doubleValue());
							table.getLineList().get(lineNum-1-latterLineCount).getColumnList().set(latterColumn, temp);
						}
						
					}
					
				}else{
					break;
				}
			}
		}
		
		/*while(formerLineCount + latterLineCount < lineNum){
			List<Double> formerColumnList = getColumnListByColumnID(formerColumn,table);
			List<Double> latterColumnList = getColumnListByColumnID(latterColumn,table);
			BigDecimal correlationCoefficient = new BigDecimal(ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(formerColumnList, latterColumnList));
			
			
			if(correlationCoefficient.compareTo(bigCC) >0){
				Collections.sort(table.getLineList(), comparatorFormer);
				Collections.sort(table.getLineList(), comparatorLatter);
				//Collections.reverse(table.getLineList());
				double result = 0;
				if(topEndFlag){
					double max = getMaxElementInColumnList(latterColumn,table);
					double standardDeviation = getStandardDeviation(latterColumn,table);
					result = max - (2 * standardDeviation / lineNum) * formerLineCount;
					formerLineCount++;
					topEndFlag = false;
					
					if(table.getLineList().get(formerLineCount).getColumnList().get(latterColumn).isLong()){
						Ceil temp = table.getLineList().get(formerLineCount).getColumnList().get(latterColumn);
						temp.setLongValue((long)result);
						temp.setAlter(Boolean.TRUE);
						table.getLineList().get(formerLineCount).getColumnList().set(latterColumn, temp);
					}else{
						Ceil temp = table.getLineList().get(formerLineCount).getColumnList().get(latterColumn);
						temp.setAlter(Boolean.TRUE);
						BigDecimal bg = new BigDecimal(result).setScale(temp.getDigit(), RoundingMode.UP);
						temp.setDoubleValue(bg.doubleValue());
						table.getLineList().get(formerLineCount).getColumnList().set(latterColumn, temp);
					}
					
					
					
				}else{
					double min = getMinElementInColumnList(latterColumn,table);
					double standardDeviation = getStandardDeviation(latterColumn,table);
					result = min + (2 * standardDeviation / lineNum) * latterLineCount;
					latterLineCount++;
					topEndFlag = true;
					
					
					if(table.getLineList().get(lineNum-1-latterLineCount).getColumnList().get(latterColumn).isLong()){
						Ceil temp = table.getLineList().get(lineNum-1-latterLineCount).getColumnList().get(latterColumn);
						temp.setLongValue((long)result);
						temp.setAlter(Boolean.TRUE);
						table.getLineList().get(lineNum-1-latterLineCount).getColumnList().set(latterColumn, temp);
					}else{
						Ceil temp = table.getLineList().get(lineNum-1-latterLineCount).getColumnList().get(latterColumn);
						temp.setAlter(Boolean.TRUE);
						BigDecimal bg = new BigDecimal(result).setScale(temp.getDigit(), RoundingMode.UP);
						temp.setDoubleValue(bg.doubleValue());
						table.getLineList().get(lineNum-1-latterLineCount).getColumnList().set(latterColumn, temp);
					}
					
					
				}
				
				
			}else if(correlationCoefficient.compareTo(litlleCC) <0){
				Collections.sort(table.getLineList(), comparatorFormer);
				
				Collections.sort(table.getLineList(), comparatorLatter);
				Collections.reverse(table.getLineList());
				double result = 0;
				if(!topEndFlag){
					double max = getMaxElementInColumnList(latterColumn,table);
					double standardDeviation = getStandardDeviation(latterColumn,table);
					result = max - (2 * standardDeviation / lineNum) * formerLineCount;
					formerLineCount++;
					topEndFlag = true;
					
					
					if(table.getLineList().get(formerLineCount).getColumnList().get(latterColumn).isLong()){
						Ceil temp = table.getLineList().get(formerLineCount).getColumnList().get(latterColumn);
						temp.setLongValue((long)result);
						temp.setAlter(Boolean.TRUE);
						table.getLineList().get(formerLineCount).getColumnList().set(latterColumn, temp);
					}else{
						Ceil temp = table.getLineList().get(formerLineCount).getColumnList().get(latterColumn);
						temp.setAlter(Boolean.TRUE);
						BigDecimal bg = new BigDecimal(result).setScale(temp.getDigit(), RoundingMode.UP);
						temp.setDoubleValue(bg.doubleValue());
						table.getLineList().get(formerLineCount).getColumnList().set(latterColumn, temp);
					}
					
					
				}else{
					double min = getMinElementInColumnList(latterColumn,table);
					double standardDeviation = getStandardDeviation(latterColumn,table);
					result = min + (2 * standardDeviation / lineNum) * latterLineCount;
					latterLineCount++;
					topEndFlag = false;
					
					if(table.getLineList().get(lineNum-1-latterLineCount).getColumnList().get(latterColumn).isLong()){
						Ceil temp = table.getLineList().get(lineNum-1-latterLineCount).getColumnList().get(latterColumn);
						temp.setLongValue((long)result);
						temp.setAlter(Boolean.TRUE);
						table.getLineList().get(lineNum-1-latterLineCount).getColumnList().set(latterColumn, temp);
					}else{
						Ceil temp = table.getLineList().get(lineNum-1-latterLineCount).getColumnList().get(latterColumn);
						temp.setAlter(Boolean.TRUE);
						BigDecimal bg = new BigDecimal(result).setScale(temp.getDigit(), RoundingMode.UP);
						temp.setDoubleValue(bg.doubleValue());
						table.getLineList().get(lineNum-1-latterLineCount).getColumnList().set(latterColumn, temp);
					}
					
				}
				
			}else{
				break;
			}
			
		}*/

		
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
		double sd = Math.sqrt(deviation);
		return sd;
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
    				str = "" + table.getLineList().get(line).getColumnList().get(column).getDoubleValue();
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
}
