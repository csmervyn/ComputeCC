package org.github.mervyn.dataExtraction1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.github.mervyn.dataExtraction.Param;
import org.github.mervyn.utils.Group;
import org.github.mervyn.utils.Line;
import org.github.mervyn.utils.Table;
import org.springframework.stereotype.Service;

@Service("dE1Service")
public class DE1Service {
	
	public void main(Param param, List<List<Integer>> percentListList,String path) throws IOException{
		Table table = createData(param, percentListList);
		 exportTable(table, path + File.separator + "抽取出的单组表数据.xls");
	}
	
	public Table createData(Param param, List<List<Integer>> percentListList){
		Table table = new Table(param.getSampleSize());
		table.setGroupNum(param.getGroupNum());
		List<List<Long>> listColumnList = getData(param,percentListList);
		
		for(int lineIndex = 0; lineIndex < param.getSampleSize(); lineIndex++){
			Line line = new Line(param.getGroupNum(),lineIndex);
			for(int groupIndex = 0; groupIndex < table.getGroupNum(); groupIndex++){
				
				Group group = new Group(1);
				
				for(int columnIndex = 0; columnIndex < 1; columnIndex++){
					long temp = listColumnList.get(groupIndex).get(lineIndex);
					group.getColumnList().add((long)temp);
				}
				line.getGroupList().add(group);
			}
			table.getLineList().add(line);
		}
		return table;
	}
	
	public void exportTable(Table table, String path) throws IOException{
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook workbook = new HSSFWorkbook();
     // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = workbook.createSheet("抽取出的单组表数据");  
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
				 } else{
                	 line = "Z";
                 }
                // int column = j + 1;
                 HSSFRichTextString text = new HSSFRichTextString("" + line); 
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
	private List<List<Long>> getData(Param param, List<List<Integer>> percentListList){
		List<List<Long>> listColumnList = new ArrayList<List<Long>>();
		for(int groupIndex = 0; groupIndex < param.getGroupNum(); groupIndex++){
			List<Long> columnList = getColumnList(param,percentListList,groupIndex);
			listColumnList.add(columnList);
		}
		return listColumnList;
	}
	
	private List<Long> getColumnList(Param param, List<List<Integer>> percentListList,int groupIndex){
		List<Long> columnList = new ArrayList<Long>();
		//for(int lineIndex = 0; lineIndex < param.getSampleSize(); lineIndex++){
			List<Integer> countList = percentListList.get(groupIndex);
			int fianlCount= param.getSampleSize();
			int tempCount = 0;
			for(int index = 0; index < countList.size(); index++){
				for(int count = 0; count < countList.get(index); count++){
					if(tempCount < fianlCount){
						columnList.add((long)(index+1));
						tempCount++;
					}else{
						break;
					}
					
				}
			}
			
			/*for(int valueIndex = 1; valueIndex <= param.getList().get(groupIndex); valueIndex++){
				List<Integer> countList = percentListList.get(groupIndex);
				for(int count = 0; count < countList.get(valueIndex-1); count++){
					columnList.add((long)valueIndex);
				}
			}*/
		//}
		//置乱
		Collections.shuffle(columnList);  
	    return columnList;
	}
	
}
