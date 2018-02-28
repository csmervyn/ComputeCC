package org.github.mervyn.wjtj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.github.mervyn.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wJTJService")
public class WJTJService {
	
	@Autowired
	ParseExcel parse;
	public List<Result> main(Table table,Parameter param,String path) throws IOException{
		//compute(table,param);
		//computexin(table,param);
		computeXinXin(table,param);
		Comparator<Line> comparator = new LineComparator7();
		//重新排序
		Collections.sort(table.getLineList(), comparator);
		//后续计算多元回归需要
		getB(table,param);
		List<Result> resultList = computeCorrelationCoefficient(table,param);
		//输出修改后的数据
		exportTable2(table,path+ File.separator  + "wjtj.xls");	
		return resultList;
	}
	
	public Table parseTable(String path) throws IOException{
		String urlStr = path + File.separator + "wjtj.xls";
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
	
	
	public List<Result> computeCorrelationCoefficient(Table table,Parameter param){
		List<Result> resultList = new ArrayList<Result>();
		
		List<Double> iAverageList = getDoubleAverageListByGroupID(table,param.getDependentVar());
		List<Double> kAverageList = getDoubleAverageListByGroupID(table,param.getIndependentVar());
		List<Double> jAverageList = getDoubleAverageListByGroupID(table,param.getAdjustVar());
		List<Double> bAverageList = getB(table,param);
		
		
		Result iResult = new Result();
		double correlationCoefficient = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(iAverageList, kAverageList);
		BigDecimal   b   =   new   BigDecimal(correlationCoefficient);  
		correlationCoefficient   = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
		iResult.getCorrelationCoefficientList().add(correlationCoefficient);
		
		correlationCoefficient = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(iAverageList, jAverageList);
		BigDecimal   b1   =   new   BigDecimal(correlationCoefficient);  
		correlationCoefficient   = b1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
		
		iResult.getCorrelationCoefficientList().add(correlationCoefficient);
		
		correlationCoefficient = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(iAverageList, bAverageList);
		BigDecimal   b2   =   new   BigDecimal(correlationCoefficient);  
		correlationCoefficient   = b2.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
		iResult.getCorrelationCoefficientList().add(correlationCoefficient);
		resultList.add(iResult);
		
		Result kResult = new Result();
		correlationCoefficient = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(kAverageList, jAverageList);
		BigDecimal   b3   =   new   BigDecimal(correlationCoefficient);  
		correlationCoefficient   = b3.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
		kResult.getCorrelationCoefficientList().add(correlationCoefficient);
		
		correlationCoefficient = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(kAverageList, bAverageList);
		BigDecimal   b4   =   new   BigDecimal(correlationCoefficient);  
		correlationCoefficient   = b4.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
		kResult.getCorrelationCoefficientList().add(correlationCoefficient);
		resultList.add(kResult);
		
		Result jResult = new Result();
		correlationCoefficient = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(jAverageList, bAverageList);
		BigDecimal   b5   =   new   BigDecimal(correlationCoefficient);  
		correlationCoefficient   = b5.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
		jResult.getCorrelationCoefficientList().add(correlationCoefficient);
		resultList.add(jResult);
		
		return resultList;
	}
	
	public void second(Table table, Parameter param){
		reComputeAverage(table);
		BigDecimal setCC = new BigDecimal(param.getCorrelationCoefficient());
		BigDecimal litlleCC = new BigDecimal(param.getCorrelationCoefficient() - 0.03);
		BigDecimal bigCC = new BigDecimal(param.getCorrelationCoefficient() + 0.03);
		Comparator<Line> comparator = new   LineComparator4(param.getDependentVar());
		Random rand = new Random();
		Random rand1 = new Random();
		
		boolean addOrSubOne;
		boolean former;
		List<Double> iList = getDoubleAverageListByGroupID(table,param.getDependentVar());
		List<Double> BList =  getB(table,param);
		double cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(BList, iList);
		BigDecimal realCC = new BigDecimal(cc);
		while(realCC.compareTo(bigCC) > 0){
			former = rand.nextBoolean();
			addOrSubOne = rand1.nextBoolean();
			Collections.sort(table.getLineList(), comparator);
			if(former){
				int alterIndex = (int) Math.ceil(param.getPercent() * table.getLineList().size());
				if(alterIndex>=1){
					for(int line = 0; line<alterIndex; line++){
						double XkBiao = table.getLineList().get(line).getGroupList().get(param.getIndependentVar()).getBiaoZhun();
						if(XkBiao > 0){
							if(addOrSubOne){
								maxSetInOneLine(table, param, line);
							}else{
								maxSubOneSetInOneLine(table, param, line);
							}
							
						}else if(XkBiao < 0){
							if(addOrSubOne){
								minSetInOneLine(table,param, line);
							}else{
								minAddOneSetInOneLine(table, param, line);
							}
						}else{
							//不操作
						}
					}
				}else{
					//不做修改
				}
			}else{
				Collections.reverse(table.getLineList());
				int alterIndex = (int) Math.ceil(param.getPercent() * table.getLineList().size());
				if(alterIndex>=1){
					for(int line = 0; line<alterIndex; line++){
						double XkBiao = table.getLineList().get(line).getGroupList().get(param.getIndependentVar()).getBiaoZhun();
						if(XkBiao > 0){
							if(addOrSubOne){
								minSetInOneLine(table,param, line);
							}else{
								minAddOneSetInOneLine(table, param, line);
							}
							
						}else if(XkBiao <0){
							if(addOrSubOne){
								maxSetInOneLine(table, param, line);
							}else{
								maxSubOneSetInOneLine(table, param, line);
							}
						}else{
							//不操作
						}
					}
				}else{
					//不做修改
				}
			}
			reComputeAverage(table);
			iList = getDoubleAverageListByGroupID(table,param.getDependentVar());
			BList =  getB(table,param);
			cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(BList, iList);
			realCC = new BigDecimal(cc);
		}
	}
	
	
	public void first(Table table, Parameter param){
		reComputeAverage(table);
		BigDecimal setCC = new BigDecimal(param.getCorrelationCoefficient());
		BigDecimal litlleCC = new BigDecimal(param.getCorrelationCoefficient() - 0.03);
		BigDecimal bigCC = new BigDecimal(param.getCorrelationCoefficient() + 0.03);
		Comparator<Line> comparator = new   LineComparator4(param.getDependentVar());
		Random rand = new Random();
		Random rand1 = new Random();
		
		boolean former;
		boolean addOrSubOne;
		List<Double> iList = getDoubleAverageListByGroupID(table,param.getDependentVar());
		List<Double> BList =  getB(table,param);
		double cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(BList, iList);
		BigDecimal realCC = new BigDecimal(cc);
		while(realCC.compareTo(litlleCC) < 0){
			Collections.sort(table.getLineList(), comparator);
			former = rand.nextBoolean();
			addOrSubOne = rand1.nextBoolean();
			if(former){
				int alterIndex = (int) Math.ceil(param.getPercent() * table.getLineList().size());
				if(alterIndex>=1){
					for(int line = 0; line<alterIndex; line++){
						double XkBiao = table.getLineList().get(line).getGroupList().get(param.getIndependentVar()).getBiaoZhun();
						if(XkBiao > 0){
							if(addOrSubOne){
								minSetInOneLine(table,param, line);
							}else{
								minAddOneSetInOneLine(table, param, line);
							}
							
						}else if(XkBiao < 0){
							if(addOrSubOne){
								maxSetInOneLine(table, param, line);
							}else{
								maxSubOneSetInOneLine(table, param, line);
							}
						}else{
							//不操作
						}
					}
				}else{
					//不做修改
				}
			}else{
				Collections.reverse(table.getLineList());
				int alterIndex = (int) Math.ceil(param.getPercent() * table.getLineList().size());
				if(alterIndex>=1){
					for(int line = 0; line<alterIndex; line++){
						double XkBiao = table.getLineList().get(line).getGroupList().get(param.getIndependentVar()).getBiaoZhun();
						if(XkBiao > 0){
							if(addOrSubOne){
								maxSetInOneLine(table, param, line);
							}else{
								maxSubOneSetInOneLine(table, param, line);
							}
						}else if(XkBiao <0){
							if(addOrSubOne){
								minSetInOneLine(table,param, line);
							}else{
								minAddOneSetInOneLine(table, param, line);
							}
						}else{
							//不操作
						}
					}
				}else{
					//不做修改
				}
			}
			reComputeAverage(table);
			iList = getDoubleAverageListByGroupID(table,param.getDependentVar());
			BList =  getB(table,param);
			cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(BList, iList);
			realCC = new BigDecimal(cc);
		}
	}
	
	
	
	public void computeXinXin(Table table, Parameter param){
		reComputeAverage(table);
		BigDecimal setCC = new BigDecimal(param.getCorrelationCoefficient());
		//BigDecimal litlleCC = new BigDecimal(param.getCorrelationCoefficient() - 0.05);
		//BigDecimal bigCC = new BigDecimal(param.getCorrelationCoefficient() + 0.05);

		List<Double> iList = getDoubleAverageListByGroupID(table,param.getDependentVar());
		List<Double> BList =  getB(table,param);
		double cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(BList, iList);
		BigDecimal realCC = new BigDecimal(cc);
		if(realCC.compareTo(setCC) < 0){
			first(table,param);
		}
		if(realCC.compareTo(setCC) > 0){
			second(table,param);
		}

	}

	public void computexin(Table table, Parameter param){
		boolean isEnd = Boolean.TRUE;
		reComputeAverage(table);
		BigDecimal setCC = new BigDecimal(param.getCorrelationCoefficient());
		BigDecimal litlleCC = new BigDecimal(param.getCorrelationCoefficient() - 0.05);
		BigDecimal bigCC = new BigDecimal(param.getCorrelationCoefficient() + 0.05);
		Comparator<Line> comparator = new   LineComparator4(param.getDependentVar());
		Random rand = new Random();
		boolean former;
		while(isEnd){
			reComputeAverage(table);
			Collections.sort(table.getLineList(), comparator);
			Collections.reverse(table.getLineList());
			List<Double> iList = getDoubleAverageListByGroupID(table,param.getDependentVar());
			List<Double> BList =  getB(table,param);
			double cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(BList, iList);
			BigDecimal realCC = new BigDecimal(cc);
			if(realCC.compareTo(litlleCC) < 0){
				//第一步
				former = rand.nextBoolean();
				if(former){
					int alterIndex = (int) Math.ceil(param.getPercent() * table.getLineList().size());
					if(alterIndex>=1){
						for(int line = 0; line<alterIndex; line++){
							double XkBiao = table.getLineList().get(line).getGroupList().get(param.getIndependentVar()).getBiaoZhun();
							if(XkBiao > 0){
								maxSetInOneLine(table, param, line);
							}else if(XkBiao < 0){
								minSetInOneLine(table,param, line);
							}else{
								//不操作
							}
						}
					}else{
						//不做修改
					}
				}else{
					int alterIndex = (int) Math.ceil((1-param.getPercent()) * table.getLineList().size());
					if(alterIndex>=1){
						for(int indexOfLine = alterIndex-1; indexOfLine < table.getLineList().size(); indexOfLine++){
							double XkBiao = table.getLineList().get(indexOfLine).getGroupList().get(param.getIndependentVar()).getBiaoZhun();
							if(XkBiao > 0){
								minSetInOneLine(table,param, indexOfLine);
							}else if(XkBiao <0){
								maxSetInOneLine(table, param, indexOfLine);
							}else{
								//不操作
							}
						}
					}else{
						//不操作
					}
				}
			}else if(realCC.compareTo(bigCC) > 0){
				//第二步
				former = rand.nextBoolean();
				if(former){
					int alterIndex = (int) Math.ceil(param.getPercent() * table.getLineList().size());
					if(alterIndex>=1){
						for(int line = 0; line<alterIndex; line++){
							double XkBiao = table.getLineList().get(line).getGroupList().get(param.getIndependentVar()).getBiaoZhun();
							if(XkBiao > 0){
								minSetInOneLine(table,param, line);
							}else if(XkBiao < 0){
								maxSetInOneLine(table, param, line);
							}else{
								//不操作
							}
						}
					}else{
						//不操作
					}
				}else{
					int alterIndex = (int) Math.ceil((1-param.getPercent()) * table.getLineList().size());
					if(alterIndex>=1){
						for(int indexOfLine = alterIndex-1; indexOfLine < table.getLineList().size(); indexOfLine++){
							double XkBiao = table.getLineList().get(indexOfLine).getGroupList().get(param.getIndependentVar()).getBiaoZhun();
							if(XkBiao > 0){
								maxSetInOneLine(table, param, indexOfLine);
	
							}else if(XkBiao < 0){
								minSetInOneLine(table,param, indexOfLine);
							}else{
								//不操作
							}
						}
					}else{
						//不操作
					}
				}
				
			}else{
				isEnd = Boolean.FALSE;
			}
		}
	}
	
	
	private void maxSetInOneLine(Table table, Parameter param, int lineId){
		int maxColumn = table.getLineList().get(lineId).getGroupList().get(param.getAdjustVar()).getColumnList().size();
		long maxElement = maxElementInGroup(table,param.getAdjustVar());
		for(int columnId = 0; columnId < maxColumn; columnId++){
			table.getLineList().get(lineId).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, maxElement);				
		}
	}
	
	private void maxSubOneSetInOneLine(Table table, Parameter param, int lineId){
		int maxColumn = table.getLineList().get(lineId).getGroupList().get(param.getAdjustVar()).getColumnList().size();
		long maxElement = maxElementInGroup(table,param.getAdjustVar());
		for(int columnId = 0; columnId < maxColumn; columnId++){
			table.getLineList().get(lineId).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, maxElement-1);				
		}
	}
	
	private void minSetInOneLine(Table table, Parameter param, int lineId){
		int maxColumn = table.getLineList().get(lineId).getGroupList().get(param.getAdjustVar()).getColumnList().size();
		long minElement = minElementInGroup(table,param.getAdjustVar());
		for(int columnId = 0; columnId < maxColumn; columnId++){
			table.getLineList().get(lineId).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, minElement);			
		}
	}
	
	private void minAddOneSetInOneLine(Table table, Parameter param, int lineId){
		int maxColumn = table.getLineList().get(lineId).getGroupList().get(param.getAdjustVar()).getColumnList().size();
		long minElement = minElementInGroup(table,param.getAdjustVar());
		for(int columnId = 0; columnId < maxColumn; columnId++){
			table.getLineList().get(lineId).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, minElement+1);			
		}
	}
	
 	public void compute(Table table,Parameter param){
		boolean isEnd = Boolean.TRUE;
		reComputeAverage(table);
		List<Double> iList = getDoubleAverageListByGroupID(table,param.getDependentVar());
		BigDecimal setCC = new BigDecimal(param.getCorrelationCoefficient());
		BigDecimal litlleCC = new BigDecimal(param.getCorrelationCoefficient() - 0.05);
		BigDecimal bigCC = new BigDecimal(param.getCorrelationCoefficient() + 0.05);
		Comparator<Line> comparator = new   LineComparator4(param.getDependentVar());
		boolean former = Boolean.TRUE;
		while(isEnd){
			reComputeAverage(table);
			List<Double> BList =  getB(table,param);
			double cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(BList, iList);
			BigDecimal realCC = new BigDecimal(cc);
			Collections.sort(table.getLineList(), comparator);
			if(realCC.compareTo(litlleCC) < 0){
				if(former){
					int alterIndex = (int) Math.ceil(param.getPercent() * table.getLineList().size());
					if(alterIndex>=1){		
						for(int line = 0; line<alterIndex; line++){
							double XkBiao = table.getLineList().get(line).getGroupList().get(param.getIndependentVar()).getBiaoZhun();
							double XjBiao = table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getBiaoZhun();
							if(XkBiao > 0 && XjBiao < 0){						
								int maxColumn = table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getColumnList().size();
								long maxElement = maxElementInGroup(table,param.getAdjustVar());
								boolean flag = Boolean.TRUE;
								for(int columnId = 0; columnId < maxColumn; columnId++){
									if(flag){
										table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, maxElement);
										flag = Boolean.FALSE;
									}else{
										table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, maxElement-1);
										flag = Boolean.TRUE;
									}
								}
							}else if(XkBiao < 0 && XjBiao > 0){
								int maxColumn = table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getColumnList().size();
								long minElement = minElementInGroup(table,param.getAdjustVar());
								boolean flag = Boolean.TRUE;
								for(int columnId = 0; columnId < maxColumn; columnId++){
									if(flag){
										table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, minElement);
										flag = Boolean.FALSE;
									}else{
										table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, minElement+1);
										flag = Boolean.TRUE;
									}
								}
							}else{
								continue;
							}
						}
					}
					former = Boolean.FALSE;
				}else{
					int alterIndex = (int) Math.ceil((1-param.getPercent()) * table.getLineList().size());
					if(alterIndex>=1){	
						for(int indexOfLine = alterIndex-1; indexOfLine < table.getLineList().size(); indexOfLine++){
							double XkBiao = table.getLineList().get(indexOfLine).getGroupList().get(param.getIndependentVar()).getBiaoZhun();
							double XjBiao = table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getBiaoZhun();
							if(XkBiao > 0 && XjBiao > 0){
								int maxColumn = table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getColumnList().size();
								long minElement = minElementInGroup(table,param.getAdjustVar());
								boolean flag = Boolean.TRUE;
								for(int columnId = 0; columnId < maxColumn; columnId++){
									if(flag){
										table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, minElement);
										flag = Boolean.FALSE;
									}else{
										table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, minElement+1);
										flag = Boolean.TRUE;
									}
								}
							}else if(XkBiao < 0 && XjBiao < 0){
								int maxColumn = table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getColumnList().size();
								long maxElement = maxElementInGroup(table,param.getAdjustVar());
								boolean flag = Boolean.TRUE;
								for(int columnId = 0; columnId < maxColumn; columnId++){
									if(flag){
										table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, maxElement);
										flag = Boolean.FALSE;
									}else{
										table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, maxElement-1);
										flag = Boolean.TRUE;
									}
								}
							}else{
								continue;
							}	
						}
					}
					former = Boolean.TRUE;
				}
			}else if(realCC.compareTo(bigCC) > 0){
				if(former){	
					int alterIndex = (int) Math.ceil(param.getPercent() * table.getLineList().size());
					if(alterIndex>=1){	
						for(int line = 0; line<alterIndex; line++){
							double XkBiao = table.getLineList().get(line).getGroupList().get(param.getIndependentVar()).getBiaoZhun();
							double XjBiao = table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getBiaoZhun();
							if(XkBiao > 0 && XjBiao > 0){
								int maxColumn = table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getColumnList().size();
								long minElement = minElementInGroup(table,param.getAdjustVar());
								boolean flag = Boolean.TRUE;
								for(int columnId = 0; columnId < maxColumn; columnId++){
									if(flag){
										table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, minElement);
										flag = Boolean.FALSE;
									}else{
										table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, minElement+1);
										flag = Boolean.TRUE;
									}
								}
							}else if(XkBiao < 0 && XjBiao < 0){
								int maxColumn = table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getColumnList().size();
								long maxElement = maxElementInGroup(table,param.getAdjustVar());
								boolean flag = Boolean.TRUE;
								for(int columnId = 0; columnId < maxColumn; columnId++){
									if(flag){
										table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, maxElement);
										flag = Boolean.FALSE;
									}else{
										table.getLineList().get(line).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, maxElement-1);
										flag = Boolean.TRUE;
									}
								}
							}else{
								continue;
							}
						}
					}
					former = Boolean.FALSE;
				}else{
					int alterIndex = (int) Math.ceil((1-param.getPercent()) * table.getLineList().size());
					if(alterIndex>=1){	
						for(int indexOfLine = alterIndex-1; indexOfLine < table.getLineList().size(); indexOfLine++){
							double XkBiao = table.getLineList().get(indexOfLine).getGroupList().get(param.getIndependentVar()).getBiaoZhun();
							double XjBiao = table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getBiaoZhun();
							if(XkBiao > 0 && XjBiao < 0){
								int maxColumn = table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getColumnList().size();
								long maxElement = maxElementInGroup(table,param.getAdjustVar());
								boolean flag = Boolean.TRUE;
								for(int columnId = 0; columnId < maxColumn; columnId++){
									if(flag){
										table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, maxElement);
										flag = Boolean.FALSE;
									}else{
										table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, maxElement-1);
										flag = Boolean.TRUE;
									}
								}
							}else if(XkBiao < 0 && XjBiao > 0){
								int maxColumn = table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getColumnList().size();
								long minElement = minElementInGroup(table,param.getAdjustVar());
								boolean flag = Boolean.TRUE;
								for(int columnId = 0; columnId < maxColumn; columnId++){
									if(flag){
										table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, minElement);
										flag = Boolean.FALSE;
									}else{
										table.getLineList().get(indexOfLine).getGroupList().get(param.getAdjustVar()).getColumnList().set(columnId, minElement+1);
										flag = Boolean.TRUE;
									}
								}
							}else{
								continue;
							}	
						}
					}
					former = Boolean.TRUE;
				}
			}else{
				isEnd = Boolean.FALSE;
			}
		}
	}
	
	
	private static long minElementInGroup(Table table, int groupId){
		List<Column> columnList =  getColumnListByGroupID(table,groupId);
		long minElement = Long.MAX_VALUE;
		for(int colNum = 0; colNum < columnList.size(); colNum++){	
			for(int line = 0; line < columnList.get(colNum).getColumnData().size(); line++){
				long temp = columnList.get(colNum).getColumnData().get(line);
				if(temp < minElement){
					minElement = temp;
				}
			}
		}
		return minElement;
	}
	
	
	private static long maxElementInGroup(Table table, int groupId){
		List<Column> columnList =  getColumnListByGroupID(table,groupId);
		long maxElement = Long.MIN_VALUE;
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
	
	private List<Double> getB(Table table,Parameter param){
		computeBiaoZhun(table);
		List<Double> kList = getBiaoZhunList(table,param.getIndependentVar());
		List<Double> jList = getBiaoZhunList(table,param.getAdjustVar());
		List<Double> list  = new ArrayList<Double>();
		for(int i = 0; i < kList.size(); i++){
			double result = kList.get(i)*jList.get(i);
			table.getLineList().get(i).setB(result);
			list.add(result);
		}
		return list;
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
			biaoZhunList.add((table.getLineList().get(lineNum).getGroupList().get(groupId).getBiaoZhun()));
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
		
		double target =   (((double)input - average)/sd);
		return target;
	}
	
	private static List<Double> getDoubleAverageListByGroupID(Table table, int groupID){
		List<Double> averageList = new ArrayList<Double>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){		
			averageList.add((double)table.getLineList().get(lineNum).getGroupList().get(groupID).getAverage());
		}
		return averageList;
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
