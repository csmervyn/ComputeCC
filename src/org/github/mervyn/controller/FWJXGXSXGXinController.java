package org.github.mervyn.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.github.mervyn.domain.DYHGParameter;
import org.github.mervyn.domain.GroupDYHGParam;
import org.github.mervyn.fwjxgxsxg.SingleColumnTable;
import org.github.mervyn.fwjxgxsxgxin.FWJXGXSXGXin;
import org.github.mervyn.utils.Result;
import org.github.mervyn.utils.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import Jama.Matrix;

@Controller("fWJXGXSXGXinController")
@RequestMapping("/fWJXGXSXGXin")
public class FWJXGXSXGXinController {
	@Autowired
	private FWJXGXSXGXin fWJXGXSXGXin;
	
	//继续进行多元回归验证
	@RequestMapping(value = "/JXduoYuanHuiGuiYanZheng.do")
	public ModelAndView JXduoYuanHuiGuiYanZheng(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		//mav.setViewName("duoYuanHuiGuiYanZheng/duoYuanHuiGuiYanZheng");
		Boolean  flag = (Boolean) request.getSession().getAttribute("myFlag");
		if(flag == null || !flag){	
			mav.setViewName("duoYuanHuiGuiYanZheng/setNum");
		}else{
			mav.setViewName("redirect:/fWJXGXSXGXin/computeJX.do");
		}
		return mav;
	}
	
	//继续
		@RequestMapping(value = "/computeJX.do")
		 public ModelAndView computeJX(HttpServletRequest request){
			ModelAndView mav = new ModelAndView();
			Boolean  flag = (Boolean) request.getSession().getAttribute("myFlag");
			GroupDYHGParam temp = null;
			GroupDYHGParam groupDyhgParam = null;
			if(flag == null || !flag){	
				temp = (GroupDYHGParam) request.getSession().getAttribute("groupDyhgParam");
				groupDyhgParam = getGroupParam(request,temp);
			}else{
				groupDyhgParam = (GroupDYHGParam) request.getSession().getAttribute("groupDyhgParam");
			}
			
			if(groupDyhgParam == null){
				mav.setViewName("forward:/WEB-INF/fwjxgxsxg/duoYuanHuiGuiYanZheng.jsp");
				mav.addObject("error", "参数设置错误，请重新设置参数");
				return mav;
			}
			SingleColumnTable table = (SingleColumnTable) request.getSession().getAttribute("singleColumnTable");
			
			List<Matrix> matrixList = fWJXGXSXGXin.groupCompute(table,groupDyhgParam);
			List<String>  strList = getFinalStrList(matrixList, groupDyhgParam);
			
			List<Result> resultList = (List<Result>) request.getSession().getAttribute("resultList");
			String path = (String) request.getSession().getAttribute("filePath");
			
			request.getSession().setAttribute("groupDyhgParam", groupDyhgParam);
			request.getSession().setAttribute("matrixList", matrixList);
			request.getSession().setAttribute("myFlag", Boolean.TRUE);
			
			mav.addObject("url1", path+ File.separator  + "修改后的表格数据.xls");
			mav.addObject("url2", path+ File.separator  + "相关系数表.xls");
			mav.addObject("resultList", resultList);
			mav.addObject("strList", strList);
			mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/downlaodFWJXGXSXGXin");
			return mav;
		 }
	
	///DYHGYZ.do
	@RequestMapping(value = "/DYHGYZ.do")
	 public ModelAndView compute(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		GroupDYHGParam temp = (GroupDYHGParam) request.getSession().getAttribute("groupDyhgParam");
		GroupDYHGParam groupDyhgParam = getGroupParam(request,temp);
		
		if(groupDyhgParam == null){
			mav.setViewName("forward:/WEB-INF/fwjxgxsxg/duoYuanHuiGuiYanZheng.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}
		SingleColumnTable table = (SingleColumnTable) request.getSession().getAttribute("singleColumnTable");
		//Matrix B = dYHGService.compute(table, parameter);
		List<Matrix> matrixList = fWJXGXSXGXin.groupCompute(table,groupDyhgParam);
		List<String>  strList = getFinalStrList(matrixList, groupDyhgParam);
		//String finalStr = getFinalStr(parameter,B);
		List<Result> resultList = (List<Result>) request.getSession().getAttribute("resultList");
		String path = (String) request.getSession().getAttribute("filePath");
		
		request.getSession().setAttribute("groupDyhgParam", groupDyhgParam);
		request.getSession().setAttribute("matrixList", matrixList);
		request.getSession().setAttribute("myFlag", Boolean.TRUE);
		
		//request.getSession().setAttribute("resultList", resultList);
		
		mav.addObject("url1", path+ File.separator  + "修改后的表格数据.xls");
		mav.addObject("url2", path+ File.separator  + "相关系数表.xls");
		mav.addObject("resultList", resultList);
		mav.addObject("strList", strList);
		mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/downlaodFWJXGXSXGXin");
		return mav;
	 }
	
	public List<String> getFinalStrList(List<Matrix> list,GroupDYHGParam groupDyhgParam){
		List<String> mylist = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++){
			mylist.add(getFinalStr(groupDyhgParam.getDyhgList().get(i),list.get(i)));
		}
		return mylist;
	}
	
	private GroupDYHGParam getGroupParam(HttpServletRequest request,GroupDYHGParam param){
		GroupDYHGParam needParam = new GroupDYHGParam();
		needParam.setNum(param.getNum());
		for(int j = 0; j < param.getNum(); j++){
			
			String str = request.getParameter("dependentVar"+j).trim();
			DYHGParameter parameter = new DYHGParameter();
			if(str == null || str.isEmpty()){
				return null;
			}else{
				parameter.setDependentVar(Integer.parseInt(getTrueNum(str)));
			}
			str = request.getParameter("independentVar"+j).trim();
			if(str == null || str.isEmpty()){
				return null;
			}else{
				char[] chArray = str.toCharArray();
				for(int i = 0; i < chArray.length ; i++){
					Character temp = new Character(chArray[i]);
					if(!temp.equals(',')){						
						parameter.getIndependentVarList().add(Integer.parseInt(getTrueNum(temp.toString())));
					}
				}
			}
			
			needParam.getDyhgList().add(parameter);
		}
		return needParam;
	}
	/*@RequestMapping(value = "/DYHGYZ.do")
	 public ModelAndView duoYuanHuiGuiYanZheng(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		DYHGParameter parameter = getParameter(request);
		if(parameter == null){
			mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/duoYuanHuiGuiYanZheng");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}
		SingleColumnTable table = (SingleColumnTable) request.getSession().getAttribute("singleColumnTable");
		Matrix B = fWJXGXSXGXin.computeDYHGYZ(table,parameter);
		String finalStr = getFinalStr(parameter,B);
		//List<Result> resultList = (List<Result>) request.getSession().getAttribute("resultList");
		String path = (String) request.getSession().getAttribute("filePath");
		//mav.addObject("resultList", resultList);
		mav.addObject("finalStr", finalStr);
		mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/downlaodFWJXGXSXGXin");
		return mav;
	 } */
	private String numToStr(int num){
 		String str = null;
 		if(num ==0){
 			str = "A";
 		}else if(num == 1){
 			str = "B";
 		}else if(num == 2){
 			str = "C";
 		}else if(num == 3){
 			str = "D";
 		}else if(num == 4){
 			str = "E";
 		}else if(num == 5){
 			str = "F";
 		}else if(num == 6){
 			str = "G";
 		}else if(num == 7){
 			str = "H";
 		}else if(num == 8){
 			str = "I";
 		}else if(num == 9){
 			str = "J";
 		}else if(num == 10){
 			str = "K";
 		}else if(num == 11){
 			str = "L";
 		}else if(num == 12){
 			str = "M";
 		}else if(num == 13){
 			str = "N";
 		}else if(num == 14){
 			str = "O";
 		}else if(num == 15){
			str = "P";
		}else if(num == 16){
			str = "Q";
		}else if(num == 17){
			str = "R";
		}else if(num == 18){
			str = "S";
		}else if(num == 19){
			str = "T";
		}else if(num == 20){
			str = "U";
		}else if(num == 21){
			str = "V";
		}else if(num == 22){
			str = "W";
		}else if(num == 23){
			str = "X";
		}else if(num == 24){
			str = "Y";
		}else if(num == 25){
			str = "Z";
		}
 		return str;
 	}
	private String getFinalStr(DYHGParameter parameter,Matrix matrixB){
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");//格式化设置  
		StringBuilder sb = new StringBuilder();
		sb.append(numToStr(parameter.getDependentVar()));
		sb.append("=");
		int lineNum = 0;
		double newtemp = matrixB.get(lineNum, 0);
		BigDecimal bg = new BigDecimal(newtemp);
		newtemp = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		sb.append(decimalFormat.format(newtemp));
		lineNum++;
		for(Integer temp : parameter.getIndependentVarList()){
			sb.append("+");
			boolean flag = Boolean.FALSE;
			double newtemp2 = matrixB.get(lineNum, 0);
			
			BigDecimal bg2 = new BigDecimal(newtemp2);
			if(bg2.compareTo(BigDecimal.ZERO) <0){
				flag = Boolean.TRUE;
			}
			newtemp2 = bg2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if(flag){
				sb.append("(");
			}
			sb.append(decimalFormat.format(newtemp2));
			sb.append("×");
			sb.append(numToStr(temp));
			if(flag){
				sb.append(")");
			}
			lineNum++;
		}
		return sb.toString();
	}
	
	
	private DYHGParameter getParameter(HttpServletRequest request){
		String str = request.getParameter("dependentVar").trim();
		DYHGParameter parameter = new DYHGParameter();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			parameter.setDependentVar(Integer.parseInt(getTrueNum(str)));
		}
		str = request.getParameter("independentVar").trim();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			char[] chArray = str.toCharArray();
			for(int i = 0; i < chArray.length ; i++){
				Character temp = new Character(chArray[i]);
				parameter.getIndependentVarList().add(Integer.parseInt(getTrueNum(temp.toString())));
			}
		}
		return parameter;
	}
	
	
	
	
	@RequestMapping("/alterFWJXinTable.do")    
	public ResponseEntity<byte[]> alterFWJXinTable(HttpServletRequest request) throws IOException {
		String path = request.getSession().getServletContext().getRealPath("upload");
	    File file=new File(path+ File.separator  + "FWJXGXSXGXin.xls");  
	    HttpHeaders headers = new HttpHeaders();    
	    String fileName=new String("非问卷相关系数修改后的表格数据.xls".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
	    headers.setContentDispositionFormData("attachment", fileName);   
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
	                                      headers, HttpStatus.CREATED);    
	}  

	
	
	@RequestMapping(value = "/compute.do")
	 public ModelAndView recompute(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();	 
		
		SingleColumnTable table = (SingleColumnTable) request.getSession().getAttribute("singleColumnTable");
		String former = request.getParameter("former").trim();
		former = getTrueNum(former);
		String latter = request.getParameter("latter").trim();
		latter = getTrueNum(latter);
		String parameter = request.getParameter("parameter").trim();
		boolean flag = false;
		try {
			flag = isParameterRight(former, latter, parameter, table);
		} catch (NumberFormatException e) {
			 mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/FWJXGXSXGXin");
			 mav.addObject("error", "参数设置错误，请重新设置参数");
			 return mav;
		}
		
		
		int formerColumn = Integer.parseInt(former);
		int latterColumn = Integer.parseInt(latter);
		//参数没有错误，可以进行计算了
		String path = (String) request.getSession().getAttribute("filePath");
		List<Result> resultList = null;
		try{
			resultList = fWJXGXSXGXin.main(formerColumn, latterColumn, Double.parseDouble(parameter), table, path);
		}catch(IOException e){
			mav.setViewName("CCerror");
			mav.addObject("error", "计算出现错误");
			return mav;
		}
		mav.addObject("resultList", resultList);
		request.getSession().setAttribute("resultList", resultList);
		mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/downlaodFWJXGXSXGXin");
		return mav;
		
	 }
	
	private boolean isParameterRight(String former,String latter,String parameter,SingleColumnTable table){
		if(former == null || former.isEmpty()){
			return false;
		}
		 if(latter == null || latter.isEmpty()){
			return false;
		}
		 if(parameter == null || parameter.isEmpty()){
			return false;
		}
		BigDecimal little = new BigDecimal(-1.0D);
		BigDecimal big = new BigDecimal(1.0D);
		BigDecimal BDparameter = new BigDecimal(Double.parseDouble(parameter));
		if(!(BDparameter.compareTo(little)>=0 && BDparameter.compareTo(big) <=0)){
			return false;
		}
		int formerGroupNum = Integer.parseInt(former);
		if( formerGroupNum< 0 ||  formerGroupNum> table.getColumnNum()){
			return false;
		}
		int latterGroupNum = Integer.parseInt(latter);
		if( latterGroupNum< 0 || latterGroupNum > table.getColumnNum()){
			return false;
		}
		if(formerGroupNum == latterGroupNum){
			return false;
		}
		return true;
	}
	
	private String getTrueNum(String str){
		int result = 0;
		if(str.equalsIgnoreCase("A")){
			result = 0;
		}else if(str.equalsIgnoreCase("B")){
			result = 1;
		}else if(str.equalsIgnoreCase("C")){
			result = 2;
		}else if(str.equalsIgnoreCase("D")){
			result = 3;
		}else if(str.equalsIgnoreCase("E")){
			result = 4;
		}else if(str.equalsIgnoreCase("F")){
			result = 5;
		}else if(str.equalsIgnoreCase("G")){
			result = 6;
		}else if(str.equalsIgnoreCase("H")){
			result = 7;
		}else if(str.equalsIgnoreCase("I")){
			result = 8;
		}else if(str.equalsIgnoreCase("J")){
			result = 9;
		}else if(str.equalsIgnoreCase("K")){
			result = 10;
		}else if(str.equalsIgnoreCase("L")){
			result = 11;
		}else if(str.equalsIgnoreCase("M")){
			result = 12;
		}else if(str.equalsIgnoreCase("N")){
			result = 13;
		}else if(str.equalsIgnoreCase("O")){
			result = 14;
		}else if(str.equalsIgnoreCase("P")){
			result = 15;
		}else if(str.equalsIgnoreCase("Q")){
			result = 16;
		}else if(str.equalsIgnoreCase("R")){
			result = 17;
		}else if(str.equalsIgnoreCase("S")){
			result = 18;
		}else if(str.equalsIgnoreCase("T")){
			result = 19;
		}else if(str.equalsIgnoreCase("U")){
			result = 20;
		}else if(str.equalsIgnoreCase("V")){
			result = 21;
		}else if(str.equalsIgnoreCase("W")){
			result = 22;
		}else if(str.equalsIgnoreCase("X")){
			result = 23;
		}else if(str.equalsIgnoreCase("Y")){
			result = 24;
		}else if(str.equalsIgnoreCase("Z")){
			result = 25;
		}
		return "" + result;
	}
	
	
	@RequestMapping(value = "/uploadFWJXGXSXG.do")
	public ModelAndView uploadFWJXGXSXG(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) { 
		String path = request.getSession().getServletContext().getRealPath("upload");  
        String fileName = "FWJXGXSXGXin.xls";
        File targetFile = new File(path, fileName);  
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        ModelAndView mav = new ModelAndView();
        //保存  
        try {  
            file.transferTo(targetFile);  
        } catch (Exception e) {  
        	mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/uploadFWJXGXSXG");
			mav.addObject("error", "无法将上传文件保存到指定目录，有可能是存储空间目录已满");
			return mav;
        }
        request.getSession().setAttribute("filePath", path); 
        SingleColumnTable table = null;
        try {
        	table = fWJXGXSXGXin.parseTable(path);
        	//System.out.println(table);
		} catch(IOException | NumberFormatException  e) {
			mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/uploadFWJXGXSXG");
			mav.addObject("error", "上传的excel格式不正确，无法正确解析该excel文件，请重新上传符合要求的excel文件");
			return mav;
		}
        request.getSession().setAttribute("singleColumnTable", table);   
        mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/FWJXGXSXGXin"); 
        return mav;
    }
	
	
	@RequestMapping(value = "/goUploadPage.do")
	public ModelAndView goUploadPage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/uploadFWJXGXSXG");
		return mav;
	}
	
	@RequestMapping(value = "/goFWJXGXSXGXinPage.do")
	public ModelAndView goFWJXGXSXGXinPage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/FWJXGXSXGXin");
		return mav;
	}

	@RequestMapping(value = "/goDYHUYZPage.do")
	public ModelAndView goDYHUYZPage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/duoYuanHuiGuiYanZheng");
		return mav;
	}
	
	@RequestMapping(value = "/goSetNum.do")
	public ModelAndView goduoYuanHuiGuiYanZhengPage(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/setNum");
		return mav;
	}
	
	
	@RequestMapping(value = "/getParam.do")
	 public ModelAndView getParam(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		GroupDYHGParam  groupDyhgParam = new GroupDYHGParam();
		String str = request.getParameter("duoYuanNum").trim();
		if(str == null || str.isEmpty()){
			mav.setViewName("forward:/WEB-INF/feiWenJuanXiangGuanXiShuXiuGaiXin/setNum.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}else{
			groupDyhgParam.setNum(Integer.parseInt(str));
		}
		request.getSession().setAttribute("groupDyhgParam", groupDyhgParam);
		mav.addObject("duoYuanNum", groupDyhgParam.getNum());
		mav.setViewName("feiWenJuanXiangGuanXiShuXiuGaiXin/duoYuanHuiGuiYanZheng");
		return mav;
	 } 

}
