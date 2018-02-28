package org.github.mervyn.dataExtraction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.github.mervyn.utils.Group;
import org.github.mervyn.utils.Line;
import org.github.mervyn.utils.ParseExcel;
import org.github.mervyn.utils.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("extractionService")
public class ExtractionService {
	@Autowired
	ParseExcel parse;
	public Table parseTable1(String path) throws IOException{
		String urlStr = path + File.separator + "fivePointTable.xls";
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
	
	public Table parseTable2(String path) throws IOException{
		String urlStr = path + File.separator + "sevenPointTable.xls";
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
	public void mainFivePoint(Param param,Table bigTable,String path) throws IOException{
		Table smallTable = extraction(param,bigTable);
		exportTable1(smallTable,path + File.separator + "抽取出的五点量表数据.xls");
	}
	
	public void mainSevenPoint(Param param,Table bigTable,String path) throws IOException{
		Table smallTable = extraction(param,bigTable);
		exportTable2(smallTable,path+ File.separator + "抽取出的七点量表数据.xls");
	}
	
	public void exportTable2(Table table, String path) throws IOException{
		// 第一步，创建一个webbook，对应一个Excel文件  
        //HSSFWorkbook workbook = new HSSFWorkbook();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
     // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
		SXSSFSheet sheet = workbook.createSheet("抽取出的七点量表数据");
     // 设置表格默认列宽度为15个字节  
        sheet.setDefaultColumnWidth((short)10);  
        // 生成一个样式  
		CellStyle style = workbook.createCellStyle();
        // 设置这些样式  
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 生成一个字体  
		Font font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);  
        font.setFontHeightInPoints((short) 12);  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        // 把字体应用到当前的样式  
        style.setFont(font);  
        // 生成并设置另一个样式  
		CellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);  
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成另一个字体  
		Font font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        style2.setFont(font2);
        
        // 产生表格标题行  
		SXSSFRow row = sheet.createRow(0);
        int groupNum = table.getLineList().get(0).getGroupList().size();
		SXSSFCell cell = null;
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
                 XSSFRichTextString text = new XSSFRichTextString("" + line + column); 
                 cell.setCellValue(text);
                 cellNum++;
                 
        	}
        	//cell = row.createCell(cellNum);  
            //cell.setCellStyle(style);
           // XSSFRichTextString text = new XSSFRichTextString("第"+i+"平均值"); 
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
        			XSSFRichTextString text = new XSSFRichTextString(table.getLineList().get(line).getGroupList().get(group).getColumnList().get(column).toString()); 
        			cell.setCellValue(text);  
                    cell.setCellStyle(style2);
        			dataCellNum++;
        		}
        		//cell = row.createCell(dataCellNum);  
                //cell.setCellStyle(style2);
               // XSSFRichTextString text = new XSSFRichTextString("" + table.getLineList().get(line).getGroupList().get(group).getAverage()); 
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
	
 	public void exportTable1(Table table, String path) throws IOException{
		// 第一步，创建一个webbook，对应一个Excel文件  
        //HSSFWorkbook workbook = new HSSFWorkbook();

		SXSSFWorkbook workbook = new SXSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		SXSSFSheet sheet = workbook.createSheet("抽取出的五点量表数据");
     // 设置表格默认列宽度为15个字节  
        sheet.setDefaultColumnWidth((short)10);  
        // 生成一个样式  
        CellStyle style = workbook.createCellStyle();
        // 设置这些样式  
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 生成一个字体  
        Font font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);  
        font.setFontHeightInPoints((short) 12);  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        // 把字体应用到当前的样式  
        style.setFont(font);  
        // 生成并设置另一个样式  
		CellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);  
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成另一个字体  
		Font font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        style2.setFont(font2);
        
        // 产生表格标题行  
        SXSSFRow row = sheet.createRow(0);
        int groupNum = table.getLineList().get(0).getGroupList().size();
        SXSSFCell cell = null;
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
                 XSSFRichTextString text = new XSSFRichTextString("" + line + column); 
                 cell.setCellValue(text);
                 cellNum++;
                 
        	}
        	//cell = row.createCell(cellNum);  
            //cell.setCellStyle(style);
           // XSSFRichTextString text = new XSSFRichTextString("第"+i+"平均值"); 
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
        			XSSFRichTextString text = new XSSFRichTextString(table.getLineList().get(line).getGroupList().get(group).getColumnList().get(column).toString()); 
        			cell.setCellValue(text);  
                    cell.setCellStyle(style2);
        			dataCellNum++;
        		}
        		//cell = row.createCell(dataCellNum);  
                //cell.setCellStyle(style2);
               // XSSFRichTextString text = new XSSFRichTextString("" + table.getLineList().get(line).getGroupList().get(group).getAverage()); 
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
	
	public Table extraction(Param param,Table bigTable){
		Table smallTable = null;
		smallTable = new Table(param.getSampleSize());
		smallTable.setGroupNum(param.getGroupNum());
		
		//选择出组的列表
		List<Integer> targetGroupList = getTargetGroupList(param,bigTable);
		//选择出列的列表
		List<List<Integer>> targetColumnList = getTargetColumnList(param,bigTable,targetGroupList);
		
		for(int lineIndex = 0; lineIndex < param.getSampleSize(); lineIndex++){
			Line line = new Line(param.getGroupNum(),lineIndex);
			for(int groupIndex = 0; groupIndex < smallTable.getGroupNum(); groupIndex++){
				
				Group group = new Group(param.getList().get(groupIndex));
				List<Integer> usedLineNumList = new ArrayList<Integer>();
				for(int columnIndex = 0; columnIndex < param.getList().get(groupIndex); columnIndex++){
					//选择一个行
					int targetLineIndex = randomIntNotSetWithMaxLineNum(bigTable,usedLineNumList);
					usedLineNumList.add(targetLineIndex);
					long data = bigTable.getLineList().get(targetLineIndex)
										.getGroupList().get(targetGroupList.get(groupIndex))
										.getColumnList().get(targetColumnList.get(groupIndex).get(columnIndex));
					group.getColumnList().add((long)data);
				}
				line.getGroupList().add(group);
			}
			smallTable.getLineList().add(line);
		}
		
		return smallTable;
	}
	

	//选择出列的列表
	private List<List<Integer>> getTargetColumnList(Param param,Table bigTable,List<Integer> targetGroupList){
		List<List<Integer>> targetColumnList = new ArrayList<List<Integer>>();
		for(int groupIndex = 0; groupIndex < param.getGroupNum(); groupIndex++){
			List<Integer> usedColumnNumList = new ArrayList<Integer>();
			for(int columnIndex = 0; columnIndex < param.getList().get(groupIndex); columnIndex++){
				int targetGourp = targetGroupList.get(groupIndex);
				int tableColumnNum = bigTable.getLineList().get(0).getGroupList().get(targetGourp).getColumnList().size();
				int targetColumnIndex = 0;
				if(param.getList().get(groupIndex) > tableColumnNum){
					targetColumnIndex = randomInt(tableColumnNum);
				}else{
					//选择一个列
					targetColumnIndex = randomIntNotSet(usedColumnNumList,tableColumnNum);
				}
				usedColumnNumList.add(targetColumnIndex);
			}
			targetColumnList.add(usedColumnNumList);
		}
		return targetColumnList;
	}
	
	//选择出组的列表
 	private List<Integer> getTargetGroupList(Param param,Table bigTable){
		List<Integer> usedGroupNumList = new ArrayList<Integer>();
		for(int groupIndex = 0; groupIndex < param.getGroupNum(); groupIndex++){
			int targetGroupIndex = 0;
			if(param.getGroupNum() > bigTable.getGroupNum()){
				targetGroupIndex = randomInt(bigTable.getGroupNum());
			}else{
				//选择一个组
				targetGroupIndex = randomIntNotSet(usedGroupNumList,bigTable.getGroupNum());
			}
			usedGroupNumList.add(targetGroupIndex);
		}
		return usedGroupNumList;
	}
	
 	private int randomIntNotSetWithMaxLineNum(Table bigTable,List<Integer> list){
		boolean flag = Boolean.TRUE;
		int temp = 0;
		while(flag){
			temp = new Random().nextInt(bigTable.getLineList().size());
			if(!list.contains(temp)){
				flag = Boolean.FALSE;
			}
		}
		return temp;
	}

	private int randomInt(int bound){
		return new Random().nextInt(bound);
	}

 	private int randomIntNotSet(List<Integer> list,int bound){
		boolean flag = Boolean.TRUE;
		int temp = 0;
		while(flag){
			temp = new Random().nextInt(bound);
			if(!list.contains(temp)){
				flag = Boolean.FALSE;
			}
		}
		return temp;
	}
}
