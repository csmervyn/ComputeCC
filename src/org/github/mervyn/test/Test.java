package org.github.mervyn.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class Test {

	@org.junit.Test
	public void test() {
		
		double mean = 50;
		double sd = 1.2;
		RandomGenerator random = new JDKRandomGenerator(1);
		NormalDistribution normalDistributioin = new NormalDistribution(mean, sd);
		double result = normalDistributioin.inverseCumulativeProbability(0.001713643);
		double result2 = normalDistributioin.inverseCumulativeProbability(0.001713643);
		System.out.println(result+"  "+result2);
	/*	PoissonDistribution dist = new PoissonDistributionImpl(4.0);
		 try {
		 System.out.println("P(X<=2.0) = "+dist.cumulativeProbability(2));
		System.out.println("mean value is "+dist.getMean());
		System.out.println("P(X=1.0) = "+dist.probability(1));
		System.out.println("P(X=x)=0.8 where x = "+dist.inverseCumulativeProbability(0.8));
		} catch (MathException e) {
		 // TODO Auto-generated catch block
		e.printStackTrace();
		}*/
		/* HSSFWorkbook workbook = new HSSFWorkbook();
	     // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = workbook.createSheet("修改后的表格数据");  
	        
	        // 产生表格标题行  
	        HSSFRow row = sheet.createRow(0);
	        HSSFCell cell = null;
	        cell = row.createCell(0);  
	        String formula ="NORMINV(0.99957,35,0.9)";
	        cell.setCellFormula(formula);
	        
            HSSFRichTextString text = new HSSFRichTextString(""); 
            cell.setCellValue(text);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            evaluator.evaluate(row.getCell((short)0));
            double s = evaluator.evaluate(cell).getNumberValue();
            System.out.println(s);*/
		
	}
	
	/*public String getCellValue(HSSFCell cell) {
		String value = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_FORMULA:
				// cell.getCellFormula();
				try {
					value = String.valueOf(cell.getNumericCellValue());
				} catch (IllegalStateException e) {
					value = String.valueOf(cell.getRichStringCellValue());
				}
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				value = String.valueOf(cell.getNumericCellValue());
				break;
			case HSSFCell.CELL_TYPE_STRING:
				value = String.valueOf(cell.getRichStringCellValue());
				break;
			}
		}

		return value;
	}*/

}
