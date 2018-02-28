package org.github.mervyn.xgxsbdtc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;
@Service("xGParseExcel")
public class XGParseExcel {
	private final static int DIVISOR = 10;
	
	private POIFSFileSystem fs;
    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private HSSFRow row;

    /**
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
        System.out.println("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = getCellFormatValue(row.getCell((short) i));
        }
        return title;
    }

    /*
     * 读取Excel数据内容readExcelContent(InputStream is)
     * 
     * */
    public Table readExcelContent(InputStream is){
    	Table table = null;
    	
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
		List<Integer> countOfColumnInGroupList = parseGroup(titles);
		//该table数据表共有rowNum行
        table = new Table(rowNum);
        for(int lineNum = 0; lineNum < rowNum; lineNum++){
        	short countOfCellNum = 0;
        	//该行有countOfColumnInGroupList.size()组
        	String str = null;
        	double temp = 0;
        	Line line = new Line(countOfColumnInGroupList.size(),lineNum);
			for(int groupNum = 0; groupNum < countOfColumnInGroupList.size(); groupNum++){
				//该组有countOfColumnInGroupList.get(groupNum)列
				Group group = new Group(countOfColumnInGroupList.get(groupNum));
				for(int columnNum = 0; columnNum < countOfColumnInGroupList.get(groupNum); columnNum++){
					//读取该行该组的所有列的数据
					//第0行不用解析，因为第0行是标题
					str = getCellFormatValue(sheet.getRow(lineNum+1).getCell(countOfCellNum)).trim();
					if(str == null || str.isEmpty()){
						temp = 0;
					}else{					
						temp = Double.parseDouble(str);
					}
					group.getColumnList().add(temp);
					countOfCellNum++;
				}
				line.getGroupList().add(group);
			}
			table.getLineList().add(line);
		}
    	return table;
    }
    
    /**
     * 读取Excel数据内容
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
    /*public Map<Integer, String> readExcelContent(InputStream is) {
        Map<Integer, String> content = new HashMap<Integer, String>();
        String str = "";
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
                // str += getStringCellValue(row.getCell((short) j)).trim() +
                // "-";
                str += getCellFormatValue(row.getCell((short) j)).trim() + "    ";
                j++;
            }
            content.put(i, str);
            str = "";
        }
        return content;
    }
*/
    /**
     * 获取单元格数据内容为字符串类型的数据
     * 
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(HSSFCell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_STRING:
            strCell = cell.getStringCellValue();
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:
            strCell = String.valueOf(cell.getNumericCellValue());
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            strCell = String.valueOf(cell.getBooleanCellValue());
            break;
        case HSSFCell.CELL_TYPE_BLANK:
            strCell = "";
            break;
        default:
            strCell = "";
            break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
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
	
	public Table parse(InputStream in){
		Table table = null;
		
		table = readExcelContent(in);
		
		return table;
	}
	
	public int getGroupNum(InputStream in){
		String[] titles =  readExcelTitle(in);
		List<Integer> countOfColumnInGroupList = parseGroup(titles);
		return countOfColumnInGroupList.size();
	}
	
	/*
	private List<Integer> parseGroup(String[] titles){
		List<Integer> countOfColumnInGroup = new ArrayList<Integer>();
		int countOfColumnInGroup0 = 0;
		int countOfColumnInGroup1 = 0;
		int countOfColumnInGroup2 = 0;
		int countOfColumnInGroup3 = 0;
		int countOfColumnInGroup4 = 0;
		int countOfColumnInGroup5 = 0;
		int countOfColumnInGroup6 = 0;
		int countOfColumnInGroup7 = 0;
		int countOfColumnInGroup8 = 0;
		int countOfColumnInGroup9 = 0;
		int countOfColumnInGroup10 = 0;
		int countOfColumnInGroup11 = 0;
		int countOfColumnInGroup12 = 0;
		int countOfColumnInGroup13 = 0;
		int countOfColumnInGroup14 = 0;

		for(int i = 0; i < titles.length; i++){
			int temp = (int)Double.parseDouble(titles[i]);
			int remainder = temp / DIVISOR;
			if(remainder == 0){
				countOfColumnInGroup0++;
			}else if(remainder == 1){
				countOfColumnInGroup1++;
			}else if(remainder == 2){
				countOfColumnInGroup2++;
			}else if(remainder == 3){
				countOfColumnInGroup3++;
			}else if(remainder == 4){
				countOfColumnInGroup4++;
			}else if(remainder == 5){
				countOfColumnInGroup5++;
			}else if(remainder == 6){
				countOfColumnInGroup6++;
			}else if(remainder == 7){
				countOfColumnInGroup7++;
			}else if(remainder == 8){
				countOfColumnInGroup8++;
			}else if(remainder == 9){
				countOfColumnInGroup9++;
			}else if(remainder == 10){
				countOfColumnInGroup10++;
			}else if(remainder == 11){
				countOfColumnInGroup11++;
			}else if(remainder == 12){
				countOfColumnInGroup12++;
			}else if(remainder == 13){
				countOfColumnInGroup13++;
			}else if(remainder == 14){
				countOfColumnInGroup14++;
			}else{
				break;
			}
		}
		
		if(countOfColumnInGroup0 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup0);
		}
		if(countOfColumnInGroup1 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup1);
		}
		if(countOfColumnInGroup2 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup2);
		}
		if(countOfColumnInGroup3 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup3);
		}
		if(countOfColumnInGroup4 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup4);
		}
		if(countOfColumnInGroup5 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup5);
		}
		if(countOfColumnInGroup6 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup6);
		}
		if(countOfColumnInGroup7 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup7);
		}
		if(countOfColumnInGroup8 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup8);
		}
		if(countOfColumnInGroup9 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup9);
		}
		if(countOfColumnInGroup10 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup10);
		}
		if(countOfColumnInGroup11 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup11);
		}
		if(countOfColumnInGroup12 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup12);
		}
		if(countOfColumnInGroup13 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup13);
		}
		if(countOfColumnInGroup14 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup14);
		}
		
		return countOfColumnInGroup;
	}*/
	
	private List<Integer> parseGroup(String[] titles){
		List<Integer> countOfColumnInGroup = new ArrayList<Integer>();
		int countOfColumnInGroup0 = 0;
		int countOfColumnInGroup1 = 0;
		int countOfColumnInGroup2 = 0;
		int countOfColumnInGroup3 = 0;
		int countOfColumnInGroup4 = 0;
		int countOfColumnInGroup5 = 0;
		int countOfColumnInGroup6 = 0;
		int countOfColumnInGroup7 = 0;
		int countOfColumnInGroup8 = 0;
		int countOfColumnInGroup9 = 0;
		int countOfColumnInGroup10 = 0;
		int countOfColumnInGroup11 = 0;
		int countOfColumnInGroup12 = 0;
		int countOfColumnInGroup13 = 0;
		int countOfColumnInGroup14 = 0;
		int countOfColumnInGroup15 = 0;
		int countOfColumnInGroup16 = 0;
		int countOfColumnInGroup17 = 0;
		int countOfColumnInGroup18 = 0;
		int countOfColumnInGroup19 = 0;
		int countOfColumnInGroup20 = 0;
		int countOfColumnInGroup21 = 0;
		int countOfColumnInGroup22 = 0;
		int countOfColumnInGroup23 = 0;
		int countOfColumnInGroup24 = 0;
		int countOfColumnInGroup25 = 0;

		for(int i = 0; i < titles.length; i++){
			
			String letter = titles[i].substring(0, 1);
			if(letter.equalsIgnoreCase("A")){
				countOfColumnInGroup0++;
			}else if(letter.equalsIgnoreCase("B")){
				countOfColumnInGroup1++;
			}else if(letter.equalsIgnoreCase("C")){
				countOfColumnInGroup2++;
			}else if(letter.equalsIgnoreCase("D")){
				countOfColumnInGroup3++;
			}else if(letter.equalsIgnoreCase("E")){
				countOfColumnInGroup4++;
			}else if(letter.equalsIgnoreCase("F")){
				countOfColumnInGroup5++;
			}else if(letter.equalsIgnoreCase("G")){
				countOfColumnInGroup6++;
			}else if(letter.equalsIgnoreCase("H")){
				countOfColumnInGroup7++;
			}else if(letter.equalsIgnoreCase("I")){
				countOfColumnInGroup8++;
			}else if(letter.equalsIgnoreCase("J")){
				countOfColumnInGroup9++;
			}else if(letter.equalsIgnoreCase("K")){
				countOfColumnInGroup10++;
			}else if(letter.equalsIgnoreCase("L")){
				countOfColumnInGroup11++;
			}else if(letter.equalsIgnoreCase("M")){
				countOfColumnInGroup12++;
			}else if(letter.equalsIgnoreCase("N")){
				countOfColumnInGroup13++;
			}else if(letter.equalsIgnoreCase("O")){
				countOfColumnInGroup14++;
			}else if(letter.equalsIgnoreCase("P")){
				countOfColumnInGroup15++;
			}else if(letter.equalsIgnoreCase("Q")){
				countOfColumnInGroup16++;
			}else if(letter.equalsIgnoreCase("R")){
				countOfColumnInGroup17++;
			}else if(letter.equalsIgnoreCase("S")){
				countOfColumnInGroup18++;
			}else if(letter.equalsIgnoreCase("T")){
				countOfColumnInGroup19++;
			}else if(letter.equalsIgnoreCase("U")){
				countOfColumnInGroup20++;
			}else if(letter.equalsIgnoreCase("V")){
				countOfColumnInGroup21++;
			}else if(letter.equalsIgnoreCase("W")){
				countOfColumnInGroup22++;
			}else if(letter.equalsIgnoreCase("X")){
				countOfColumnInGroup23++;
			}else if(letter.equalsIgnoreCase("Y")){
				countOfColumnInGroup24++;
			}else if(letter.equalsIgnoreCase("Z")){
				countOfColumnInGroup25++;
			}else{
				break;
			}
		}
		
		if(countOfColumnInGroup0 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup0);
		}
		if(countOfColumnInGroup1 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup1);
		}
		if(countOfColumnInGroup2 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup2);
		}
		if(countOfColumnInGroup3 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup3);
		}
		if(countOfColumnInGroup4 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup4);
		}
		if(countOfColumnInGroup5 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup5);
		}
		if(countOfColumnInGroup6 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup6);
		}
		if(countOfColumnInGroup7 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup7);
		}
		if(countOfColumnInGroup8 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup8);
		}
		if(countOfColumnInGroup9 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup9);
		}
		if(countOfColumnInGroup10 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup10);
		}
		if(countOfColumnInGroup11 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup11);
		}
		if(countOfColumnInGroup12 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup12);
		}
		if(countOfColumnInGroup13 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup13);
		}
		if(countOfColumnInGroup14 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup14);
		}
		if(countOfColumnInGroup15 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup15);
		}
		if(countOfColumnInGroup16 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup16);
		}
		if(countOfColumnInGroup17 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup17);
		}
		if(countOfColumnInGroup18 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup18);
		}
		if(countOfColumnInGroup19 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup19);
		}
		if(countOfColumnInGroup20 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup20);
		}
		if(countOfColumnInGroup21 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup21);
		}
		if(countOfColumnInGroup22 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup22);
		}
		if(countOfColumnInGroup23 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup23);
		}
		if(countOfColumnInGroup24 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup24);
		}
		if(countOfColumnInGroup25 != 0){
			countOfColumnInGroup.add(countOfColumnInGroup25);
		}
		return countOfColumnInGroup;
	}
	
}
