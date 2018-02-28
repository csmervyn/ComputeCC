package org.github.mervyn.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.github.mervyn.domain.DYHGParameter;
import org.github.mervyn.fwjtjxg.FWJTJXGService;
import org.github.mervyn.fwjxgxsxg.SingleColumnTable;
import org.github.mervyn.utils.Result;
import org.github.mervyn.wjtj.Parameter;
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

@Controller("FWJTJXG")
@RequestMapping("/FWJTJXG")
public class FWJTJXGController {
	@Autowired
	private FWJTJXGService fWJTJXGService;
	
	@RequestMapping(value = "/compute.do")
	 public ModelAndView compute(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		Parameter param = new Parameter();
		try{
			param = getParam(request);
		}catch(NumberFormatException e){
			mav.setViewName("fwjtjxg/fwjtjxgsetParameter");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}
		if(param == null){
			mav.setViewName("fwjtjxg/fwjtjxgsetParameter");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}else{
			String path = (String) request.getSession().getAttribute("filePath");
			SingleColumnTable table = (SingleColumnTable) request.getSession().getAttribute("singleColumnTable");
			List<Result> resultList = null;
			String finalStr ="";
			try{
				resultList = fWJTJXGService.main(table,param,path);
				DYHGParameter parameter = new DYHGParameter();
				parameter.setDependentVar(param.getDependentVar());
				parameter.getIndependentVarList().add(param.getIndependentVar());
				parameter.getIndependentVarList().add(param.getAdjustVar());
				Matrix B = fWJTJXGService.computeDYHGYZ(table,parameter);
				finalStr = getFinalStrWithB(parameter,B);
			}catch(IOException e){
				mav.setViewName("fwjtjxg/fwjtjxgsetParameter");
				mav.addObject("error", "参数设置错误，请重新设置参数");
				return mav;
			}
			
			mav.addObject("resultList", resultList);
			request.getSession().setAttribute("table", table);
			mav.addObject("finalStr", finalStr);
			mav.setViewName("fwjtjxg/downlaodFWJTJXG");
			return mav;
		}
	 } 
	
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
	private String getFinalStrWithB(DYHGParameter parameter,Matrix matrixB){
		StringBuilder sb = new StringBuilder();
		sb.append(numToStr(parameter.getDependentVar()));
		sb.append("=");
		int lineNum = 0;
		double newtemp = matrixB.get(lineNum, 0);
		BigDecimal bg = new BigDecimal(newtemp);
		newtemp = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		sb.append(newtemp);
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
			sb.append(newtemp2);
			sb.append("×");
			sb.append(numToStr(temp));
			if(flag){
				sb.append(")");
			}
			lineNum++;
		}
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
		sb.append(newtemp2);
		sb.append("×");
		sb.append("交互项B");
		if(flag){
			sb.append(")");
		}
		return sb.toString();
	}
	
	
	private Parameter getParam(HttpServletRequest request){
		Parameter param = new Parameter();
		String str = request.getParameter("dependentVar").trim();
		
		if(str == null || str.isEmpty()){
			return null;
		}else{
			param.setDependentVar(Integer.parseInt(getTrueNum(str)));
		}
		
		str = request.getParameter("independentVar").trim();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			param.setIndependentVar(Integer.parseInt(getTrueNum(str)));
		}
		
		str = request.getParameter("adjustVar").trim();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			param.setAdjustVar(Integer.parseInt(getTrueNum(str)));
		}
		
		str = request.getParameter("correlationCoefficient").trim();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			param.setCorrelationCoefficient(Double.parseDouble(str));
		}
		
		str = request.getParameter("percent").trim();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			param.setPercent(Double.parseDouble(str));
		}
		return param;
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
	@RequestMapping(value = "/uploadFWJTJXG.do")
	public ModelAndView uploadFWJTJXG(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) { 
		String path = request.getSession().getServletContext().getRealPath("upload");  
        String fileName = "FWJTJXG.xls";
        File targetFile = new File(path, fileName);  
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        ModelAndView mav = new ModelAndView();
        //保存  
        try {  
            file.transferTo(targetFile);  
        } catch (Exception e) {  
        	mav.setViewName("fwjtjxg/uploadFWJTJXG");
			mav.addObject("error", "无法将上传文件保存到指定目录，有可能是存储空间目录已满");
			return mav;
        }
        request.getSession().setAttribute("filePath", path); 
        SingleColumnTable table = null;
        try {
        	table = fWJTJXGService.parseTable(path);
        	//System.out.println(table);
		} catch(IOException | NumberFormatException  e) {
			mav.setViewName("fwjtjxg/uploadFWJTJXG");
			mav.addObject("error", "上传的excel格式不正确，无法正确解析该excel文件，请重新上传符合要求的excel文件");
			return mav;
		}
        request.getSession().setAttribute("singleColumnTable", table);   
        mav.setViewName("fwjtjxg/fwjtjxgsetParameter"); 
        return mav;
    }
	
	@RequestMapping("/download.do")    
    public ResponseEntity<byte[]> downloadFWJTable(HttpServletRequest request) throws IOException {
    	String path = request.getSession().getServletContext().getRealPath("upload");
        File file=new File(path+ File.separator  + "FWJTJXG.xls");  
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String("非问卷调节修改后的数据表格.xls".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);    
    }
	
	
	@RequestMapping(value = "/goSetParameterPage.do")
	public ModelAndView goSetParameterPage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("fwjtjxg/fwjtjxgsetParameter");
		return mav;
	}
	
	
	@RequestMapping(value = "/goFWJTJXGUploadPage.do")
	public ModelAndView goUploadPage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("fwjtjxg/uploadFWJTJXG");
		return mav;
	}
	
}
