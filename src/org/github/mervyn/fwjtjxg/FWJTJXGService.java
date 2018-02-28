package org.github.mervyn.fwjtjxg;

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
import org.github.mervyn.fwjxgxsxg.Ceil;
import org.github.mervyn.fwjxgxsxg.LineComparator7;
import org.github.mervyn.fwjxgxsxg.ParseExcelForFWJ;
import org.github.mervyn.fwjxgxsxg.SingleColumnLine;
import org.github.mervyn.fwjxgxsxg.SingleColumnTable;
import org.github.mervyn.utils.ComputeCorrelationCoefficient;
import org.github.mervyn.utils.Line;
import org.github.mervyn.utils.LineComparator4;
import org.github.mervyn.utils.Result;
import org.github.mervyn.utils.Table;
import org.github.mervyn.wjtj.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Jama.Matrix;

@Service("fWJTJXGService")
public class FWJTJXGService {
	
	@Autowired
	private ParseExcelForFWJ parseExcelForFWJ;
	
	private Matrix getX(SingleColumnTable table,DYHGParameter parameter){
		Matrix X = new  Matrix(table.getLineList().size(),parameter.getIndependentVarList().size()+2);
		for(int i = 0; i < table.getLineList().size();i++){
			X.set(i, 0, 1);
		}
		int column = 1;
		for(Integer i : parameter.getIndependentVarList()) {
			List<Double> doubleList = getColumnListByColumnID(i,table);	
			for(int line = 0; line < doubleList.size();line++){
				X.set(line, column, doubleList.get(line));
			}
			column++;
		}
		for(int line = 0; line < table.getLineList().size();line++){
			double b = table.getLineList().get(line).getB();
			X.set(line, column, b);
		}
		return X;
	}
	
	private Matrix getY(SingleColumnTable table,DYHGParameter parameter){
		List<Double> averageList = 	getColumnListByColumnID(parameter.getDependentVar(),table);	
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
	
	
	public List<Result> main(SingleColumnTable table,Parameter param,String path) throws IOException{
		List<Double> shuLie =  first(param.getAdjustVar(),table);
		compute(table,param,shuLie);
		//后续计算多元回归需要
		computeB(table,param);
		Comparator<SingleColumnLine> comparator = new LineComparator7();
		Collections.sort(table.getLineList(), comparator);
		List<Result> resultList = computeCorrelationCoefficient(table,param);
		//输出修改后的数据
		exportTable2(table,path+ File.separator  + "FWJTJXG.xls");	
		return resultList;
	}
	
	
	public List<Result> computeCorrelationCoefficient(SingleColumnTable table,Parameter param){
		List<Result> resultList = new ArrayList<Result>();
		
		List<Double> iAverageList = getColumnListByColumnID(param.getDependentVar(),table);
		List<Double> kAverageList = getColumnListByColumnID(param.getIndependentVar(),table);
		List<Double> jAverageList = getColumnListByColumnID(param.getAdjustVar(),table);
		List<Double> bAverageList = getBList(table);
		
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
	public List<Result> outputCorrelationCoefficient(SingleColumnTable table){
		List<Result> resultList = new ArrayList<Result>();
		for(int formerIndexOfColumn = 0; formerIndexOfColumn < table.getColumnNum()-1; formerIndexOfColumn++){
			Result result = new Result();
			for(int latterIndexOfColumn = formerIndexOfColumn+1; latterIndexOfColumn < table.getColumnNum();latterIndexOfColumn++){
				List<Double> averageListFirst = getColumnListByColumnID(formerIndexOfColumn,table);
				List<Double> averageListSecond = getColumnListByColumnID(latterIndexOfColumn,table);
				double correlationCoefficient = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(averageListFirst, averageListSecond);
				result.getCorrelationCoefficientList().add(correlationCoefficient);
			}
			resultList.add(result);
		}
		return resultList;
	}
	private void secondOne(SingleColumnTable table, Parameter param,List<Double> shuLie){
		
		BigDecimal setCC = new BigDecimal(param.getCorrelationCoefficient());
		boolean isEnd = Boolean.FALSE;
		computeBiaoZhun(param.getIndependentVar(),table);
		int lineNum = 0;
		while(!isEnd){
			computeBiaoZhun(param.getAdjustVar(),table);
			computeB(table,param);
			List<Double> bList = getBList(table);
			List<Double> iList = getColumnListByColumnID(param.getDependentVar(),table);
			double cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(bList, iList);
			BigDecimal realCC = new BigDecimal(cc);
			Comparator<SingleColumnLine> comparator = new LineComparator();
			if(realCC.compareTo(setCC) <0){
				computeBiaoZhun(param.getDependentVar(),table);
				computeBBiaoMulXibiao(table,param);
				Collections.sort(table.getLineList(), comparator);
				//Collections.reverse(table.getLineList());
				double XkBiao;
				List<Ceil> columnList = table.getLineList().get(lineNum).getColumnList();
				XkBiao = columnList.get(param.getIndependentVar()).getBiaoZhun();
				double XjBiao;
				XjBiao = columnList.get(param.getAdjustVar()).getBiaoZhun();
				
				double XiBiao = columnList.get(param.getDependentVar()).getBiaoZhun();
				if(XiBiao> 0 && XkBiao > 0 && XjBiao < 0){
					
					Ceil ceil = columnList.get(param.getAdjustVar());
					double max = findRemoveMaxInShuLie(shuLie);
					if(ceil.isLong()){
						ceil.setLongValue((long)max);
					}else{	
						
						BigDecimal bg = new BigDecimal(max);
						max = bg.setScale(ceil.getDigit(), BigDecimal.ROUND_HALF_UP).doubleValue();
						ceil.setDoubleValue(max);
					}
					ceil.setAlter(Boolean.TRUE);
					
					
				}else if(XiBiao> 0 && XkBiao < 0 && XjBiao > 0){
					Ceil ceil = columnList.get(param.getAdjustVar());
					double min = findRemoveMinInShuLie(shuLie);
					if(ceil.isLong()){
						ceil.setLongValue((long)min);
					}else{	
						
						BigDecimal bg = new BigDecimal(min);
						min = bg.setScale(ceil.getDigit(), BigDecimal.ROUND_HALF_UP).doubleValue();
						ceil.setDoubleValue(min);
					}
					ceil.setAlter(Boolean.TRUE);
				}else if(XiBiao< 0 && XkBiao > 0 && XjBiao > 0){
					Ceil ceil = columnList.get(param.getAdjustVar());
					double min = findRemoveMinInShuLie(shuLie);
					if(ceil.isLong()){
						ceil.setLongValue((long)min);
					}else{	
						
						BigDecimal bg = new BigDecimal(min);
						min = bg.setScale(ceil.getDigit(), BigDecimal.ROUND_HALF_UP).doubleValue();
						ceil.setDoubleValue(min);
					}
					ceil.setAlter(Boolean.TRUE);
				}else if(XiBiao < 0 && XkBiao < 0 && XjBiao < 0){
					Ceil ceil = columnList.get(param.getAdjustVar());
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
				}
			}else if(realCC.compareTo(setCC) > 0){
				isEnd = Boolean.TRUE;
			}else{
				//不操作
			}
			lineNum++;
			
		}
	}
	

	private void secondTwo(SingleColumnTable table, Parameter param,List<Double> shuLie){
		BigDecimal setCC = new BigDecimal(param.getCorrelationCoefficient());
		boolean isEnd = Boolean.FALSE;
		int lineNum = 0;
		computeBiaoZhun(param.getIndependentVar(),table);
		while(!isEnd){
			computeBiaoZhun(param.getAdjustVar(),table);
			computeB(table,param);
			List<Double> bList = getBList(table);
			List<Double> iList = getColumnListByColumnID(param.getDependentVar(),table);
			double cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(bList, iList);
			BigDecimal realCC = new BigDecimal(cc);
			Comparator<SingleColumnLine> comparator = new LineComparator();
			if(realCC.compareTo(setCC) >0){
				computeBiaoZhun(param.getDependentVar(),table);
				computeBBiaoMulXibiao(table,param);
				Collections.sort(table.getLineList(), comparator);
				Collections.reverse(table.getLineList());
				List<Ceil> columnList = table.getLineList().get(lineNum).getColumnList();
				double XkBiao;
				XkBiao = columnList.get(param.getIndependentVar()).getBiaoZhun();
				double XjBiao;
				XjBiao = columnList.get(param.getAdjustVar()).getBiaoZhun();
				
				double XiBiao = columnList.get(param.getDependentVar()).getBiaoZhun();
				if(XiBiao> 0 && XkBiao > 0 && XjBiao > 0){
					Ceil ceil = columnList.get(param.getAdjustVar());
					double min = findRemoveMinInShuLie(shuLie);
					if(ceil.isLong()){
						ceil.setLongValue((long)min);
					}else{	
						
						BigDecimal bg = new BigDecimal(min);
						min = bg.setScale(ceil.getDigit(), BigDecimal.ROUND_HALF_UP).doubleValue();
						ceil.setDoubleValue(min);
					}
					ceil.setAlter(Boolean.TRUE);
				}else if(XiBiao> 0 && XkBiao < 0 && XjBiao < 0){
					Ceil ceil = columnList.get(param.getAdjustVar());
					double max = findRemoveMaxInShuLie(shuLie);
					if(ceil.isLong()){
						ceil.setLongValue((long)max);
					}else{	
						
						BigDecimal bg = new BigDecimal(max);
						max = bg.setScale(ceil.getDigit(), BigDecimal.ROUND_HALF_UP).doubleValue();
						ceil.setDoubleValue(max);
					}
					ceil.setAlter(Boolean.TRUE);
				}else if(XiBiao< 0 && XkBiao > 0 && XjBiao < 0){
					Ceil ceil = columnList.get(param.getAdjustVar());
					double max = findRemoveMaxInShuLie(shuLie);
					if(ceil.isLong()){
						ceil.setLongValue((long)max);
					}else{	
						
						BigDecimal bg = new BigDecimal(max);
						max = bg.setScale(ceil.getDigit(), BigDecimal.ROUND_HALF_UP).doubleValue();
						ceil.setDoubleValue(max);
					}
					ceil.setAlter(Boolean.TRUE);
				}else if(XiBiao< 0 && XkBiao < 0 && XjBiao > 0){
					Ceil ceil = columnList.get(param.getAdjustVar());
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
				}
			}else if(realCC.compareTo(setCC) < 0){
				isEnd = Boolean.TRUE;
			}else{
				//不操作
			}
			lineNum++;
		}
	}
	
	public void compute(SingleColumnTable table, Parameter param,List<Double> shuLie){
		BigDecimal setCC = new BigDecimal(param.getCorrelationCoefficient());
		computeB(table,param);
		List<Double> bList = getBList(table);
		List<Double> iList = getColumnListByColumnID(param.getDependentVar(),table);
		double cc = ComputeCorrelationCoefficient.getCorrelationCoefficientDouble(bList, iList);
		BigDecimal realCC = new BigDecimal(cc);
		//Comparator<SingleColumnLine> comparator = new LineComparator();
		if(realCC.compareTo(new BigDecimal(setCC.doubleValue() +0.03)) <0){
			secondOne(table,param,shuLie);
		}else if(realCC.compareTo(new BigDecimal(setCC.doubleValue() -0.03)) > 0){
			secondTwo(table,param,shuLie);
		}else{
			//待添加
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
	
	private void computeBBiaoMulXibiao(SingleColumnTable table,Parameter param){
		computeBBiaoZhun(table);
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){		
			double temp = table.getLineList().get(lineNum).getbBiao()
							* table.getLineList().get(lineNum).getColumnList().get(param.getDependentVar()).getBiaoZhun();
			table.getLineList().get(lineNum).setbBiaoMulXibiao(temp);
		}
	}
	
	

	private List<Double> getBBiaoZhunList(SingleColumnTable table){
		
		List<Double> bList = new ArrayList<Double>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){		
			double temp = table.getLineList().get(lineNum).getbBiao();
			bList.add(temp);
		}
		return bList;
	}
	
	private void computeB(SingleColumnTable table,Parameter param){
		computeBiaoZhun(param.getIndependentVar(),table);
		computeBiaoZhun(param.getAdjustVar(),table);
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){		
			double temp = table.getLineList().get(lineNum).getColumnList().get(param.getIndependentVar()).getBiaoZhun()
							* table.getLineList().get(lineNum).getColumnList().get(param.getAdjustVar()).getBiaoZhun();
			table.getLineList().get(lineNum).setB(temp);
		}
	}
	
	private List<Double> getBList(SingleColumnTable table){
		
		List<Double> bList = new ArrayList<Double>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){		
			double temp = table.getLineList().get(lineNum).getB();
			bList.add(temp);
		}
		return bList;
	}

	public List<Double> getBiaoZhunList(SingleColumnTable table,int groupId){
		List<Double> biaoZhunList = new ArrayList<Double>();
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){		
			biaoZhunList.add((table.getLineList().get(lineNum).getColumnList().get(groupId).getBiaoZhun()));
		}
		return biaoZhunList;
	}
	
	public void computeBiaoZhun(SingleColumnTable table){
		for(int columnId = 0; columnId < table.getColumnNum(); columnId++){
			computeBiaoZhun(columnId,table);
		}
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
	
	private void computeBBiaoZhun(SingleColumnTable table){
		List<Double> bList = getBList(table);
		double average = getAverage(bList);
		double sd = getStandardDeviation(bList);
		for(int lineNum = 0; lineNum< table.getLineList().size(); lineNum++){
			double bTemp = table.getLineList().get(lineNum).getB();
			bTemp = (bTemp-average)/sd;
			table.getLineList().get(lineNum).setbBiao(bTemp);
		}
	}
	
 	private List<Double> first(int  latter,SingleColumnTable table){
		int ksize =  (int) (getColumnListByColumnID(latter,table).size()*1.5);
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

	private double getAverage(List<Double> list){
		double sum = 0;
		for(double temp : list){
			sum += temp;
		}
		return sum/list.size();
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

 	private double getStandardDeviation(List<Double> list){
 		double average = getAverage(list);
 		double deviation = 0;
		for(int i = 0; i < list.size(); i++){
			deviation = deviation + (list.get(i)-average) * (list.get(i)-average);
		}
		double sd = Math.sqrt(deviation/list.size());
		return sd;
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
	public SingleColumnTable parseTable(String path) throws IOException{
		String urlStr = path + File.separator + "FWJTJXG.xls";
		File file = new File(urlStr);
		InputStream in = new FileInputStream(file); 
		
		//解析excel中的数据
		SingleColumnTable table = null;
		table = parseExcelForFWJ.parse(in);
		in.close();
		return table;
	}
}
