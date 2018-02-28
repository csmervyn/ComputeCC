package org.github.mervyn.fwjxgxsxg;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import org.springframework.stereotype.Service;

@Service("parseExcelForFWJ")
public class ParseExcelForFWJ {
	
	private POIFSFileSystem fs;
    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private HSSFRow row;
	
	public int getGroupNum(InputStream in){
		String[] titles =  readExcelTitle(in);
		return titles.length;
	}
	
	/*
	 * 读取Excel表格表头的内容
     * @param InputStream
     * @return String 表头内容的数组
     */
    public String[] readExcelTitle(InputStream is) { 
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
           // e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
 
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = getCellFormatValue(row.getCell((short) i));
        }
        return title;
    }
    
    
    /**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_NUMERIC:
            case HSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                    
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
	
    public SingleColumnTable parse(InputStream in){
    	SingleColumnTable table = null;
		
		table = readExcelContent(in);
		
		return table;
	}
    
    /*
     * 读取Excel数据内容readExcelContent(InputStream is)
     * 
     * */
    public SingleColumnTable readExcelContent(InputStream is){
    	SingleColumnTable table = null;
    	
        try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        String[] titles =  readExcelTitle(is);

		//该table数据表共有rowNum行
        table = new SingleColumnTable(rowNum);
        for(int lineNum = 0; lineNum < rowNum; lineNum++){
        	//short countOfCellNum = 0;
        	String str = null;
        	Ceil temp = null;
        	SingleColumnLine line = new SingleColumnLine(titles.length,lineNum);
			for(int columnNum = 0; columnNum < titles.length; columnNum++){
				//读取该行该组的所有列的数据
				//第0行不用解析，因为第0行是标题
				str = getCellFormatValue(sheet.getRow(lineNum+1).getCell(columnNum)).trim();
				temp = new Ceil();
				if(str == null || str.isEmpty()){
					temp.setLong(true);
					temp.setLongValue(0);
				}else{
					int index = str.lastIndexOf(".");
					if(index == -1){
						temp.setLong(true);
						temp.setLongValue(Integer.parseInt(str));
					}else{
						int indexValue = str.length() - str.lastIndexOf(".")-1;
						if(indexValue == 1 && str.substring(index+1).equals("0")){
							temp.setLong(true);
							temp.setLongValue(Integer.parseInt(str.substring(0, index)));
						}else{
							temp.setLong(false);
							temp.setDoubleValue(Double.parseDouble(str));
							temp.setDigit(str.length() - str.lastIndexOf(".")-1);
						}
						
					}
					
				}
				line.getColumnList().add(temp);
			}
			table.getLineList().add(line);
		}
        table.setColumnNum(titles.length);
    	return table;
    }
}
