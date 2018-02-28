package org.github.mervyn.controller;

import org.github.mervyn.utils.ComputeService;
import org.github.mervyn.utils.DTO;
import org.github.mervyn.utils.Result;
import org.github.mervyn.utils.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller("computeController")
@RequestMapping(value = "/CCCompute")
public class ComputeController {
	@Autowired
	private ComputeService service;
	@RequestMapping(value = "/compute.do")
	 public ModelAndView compute(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		DTO dto =  null;
		try{
			dto = getDTO(request);
		}catch(NumberFormatException e){
			mav.setViewName("forward:/WEB-INF/computeCorrelationCoefficient.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}
		if((dto == null) || ((dto != null) && (isRight(dto)))){
			String path = (String) request.getSession().getAttribute("filePath");
			Table table = (Table) request.getSession().getAttribute("table");
			List<Result> resultList = null;
			try{
				resultList = service.compute(table, dto,path);
			}catch(IOException e){
				mav.setViewName("CCerror");
				mav.addObject("error", "表格输出出现异常");
				return mav;
			}
			mav.addObject("url1", path+ File.separator  + "修改后的表格数据.xls");
			mav.addObject("url2", path+ File.separator  + "各组平均值相关系数表.xls");
			mav.addObject("resultList", resultList);
			mav.setViewName("downlaod");
			return mav;
		}else{
			mav.setViewName("forward:/WEB-INF/computeCorrelationCoefficient.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}
	 } 
	
	
	private DTO getDTO(HttpServletRequest request){
		DTO dto = new DTO();
		String str = request.getParameter("PERCENT").trim();
		
		if(str == null || str.isEmpty()){
			return null;
		}else{
			dto.setPERCENT(Double.parseDouble(str));
		}
		str = request.getParameter("J2PARAMETER").trim();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			dto.setJ2PARAMETER(Double.parseDouble(str));
		}

		str = request.getParameter("J3PARAMETER").trim();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			dto.setJ3PARAMETER(Double.parseDouble(str));
		}
		str = request.getParameter("NEW_J4PARAMETER").trim();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			dto.setNEW_J4PARAMETER(Double.parseDouble(str));
		}

		str = request.getParameter("NEW_J5PARAMETER").trim();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			dto.setNEW_J5PARAMETER(Double.parseDouble(str));
		}

		
		str = request.getParameter("J4PARAMETER").trim();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			dto.setJ4PARAMETER(Double.parseDouble(str));
		}

		str = request.getParameter("PARAMETER_OF_THREESTEP").trim();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			dto.setPARAMETER_OF_THREESTEP(Double.parseDouble(str));
		}
		
		str = request.getParameter("PARAMETER_OF_FOUR_ONE_STEP").trim();
		if(str == null || str.isEmpty()){
			return null;
		}else{
			dto.setPARAMETER_OF_FOUR_ONE_STEP(Double.parseDouble(str));
		}
		
		
		return dto;
	}
	
	
	private boolean isRight(DTO dto){
		BigDecimal little = new BigDecimal(0.0D);
		BigDecimal big = new BigDecimal(1.0D);
		BigDecimal negative = new BigDecimal(-1.0D);
		BigDecimal percent = new BigDecimal(dto.getPERCENT());
		 BigDecimal j2 = new BigDecimal(dto.getPERCENT());
		 BigDecimal j3 = new BigDecimal(dto.getJ2PARAMETER());
		 BigDecimal j4 = new BigDecimal(dto.getJ3PARAMETER());
		 BigDecimal threeStep = new BigDecimal(dto.getPARAMETER_OF_THREESTEP());
		 BigDecimal fourOneStep = new BigDecimal(dto.getPARAMETER_OF_FOUR_ONE_STEP());
		 if(!(percent.compareTo(little)>=0 && percent.compareTo(big) <=0)){
			 return false;
		 }
		 if(!(j2.compareTo(little)>=0 && j2.compareTo(big) <=0)){
			 return false;
		 }
		 if(!(j3.compareTo(little)>=0 && j3.compareTo(big) <=0)){
			 return false;
		 }
		 if(!(j4.compareTo(little)>=0 && j4.compareTo(big) <=0)){
			 return false;
		 }
		 if(!(threeStep.compareTo(negative)>=0 && threeStep.compareTo(big) <=0)){
			 return false;
		 }
		 if(!(fourOneStep.compareTo(negative)>=0 && fourOneStep.compareTo(big) <=0)){
			 return false;
		 }
		 
		 return true;
	}
	
	
	@RequestMapping(value = "/recompute.do")
	 public ModelAndView recompute(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();	 
		DTO dto =  null;
		try{
			dto = getDTO1(request);
		}catch(NumberFormatException e){
			mav.setViewName("forward:/WEB-INF/fiveStep.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}
		Table table = (Table) request.getSession().getAttribute("table");
		String former = request.getParameter("former").trim();
		former = getTrueNum(former);
		String latter = request.getParameter("latter").trim();
		latter = getTrueNum(latter);
		String parameter = request.getParameter("parameter").trim();
		boolean flag = false;
		try {
			flag = isParameterRight(former, latter, parameter, table);
		} catch (NumberFormatException e) {
			 mav.setViewName("forward:/WEB-INF/fiveStep.jsp");
			 mav.addObject("error", "参数设置错误，请重新设置参数");
			 return mav;
		}
		
		if(flag && (dto == null) || ((dto != null) && (isRight1(dto)))){
			int formerGroup = Integer.parseInt(former);
			int latterGroup = Integer.parseInt(latter);
			//参数没有错误，可以进行计算了
			String path = (String) request.getSession().getAttribute("filePath");
			List<Result> resultList = null;
			try{
				resultList = service.computeFiveStep(path, dto,formerGroup,latterGroup,Double.parseDouble(parameter),table);
			}catch(IOException e){
				mav.setViewName("CCerror");
				mav.addObject("error", "计算出现错误");
				return mav;
			}
			mav.addObject("url1", path+ File.separator  + "修改后的表格数据.xls");
			mav.addObject("url2", path+ File.separator  + "各组平均值相关系数表.xls");
			mav.addObject("resultList", resultList);
			request.getSession().setAttribute("resultList", resultList);
			mav.setViewName("downlaod1");
			return mav;
		}else{
			mav.setViewName("forward:/WEB-INF/fiveStep.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}
	 } 

	private boolean isParameterRight(String former,String latter,String parameter,Table table){
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
		if( formerGroupNum< 0 ||  formerGroupNum> table.getGroupNum()){
			return false;
		}
		int latterGroupNum = Integer.parseInt(latter);
		if( latterGroupNum< 0 || latterGroupNum > table.getGroupNum()){
			return false;
		}
		if(formerGroupNum == latterGroupNum){
			return false;
		}
		return true;
	}
	
	
	private DTO getDTO1(HttpServletRequest request){
		DTO dto = new DTO();
		String str = request.getParameter("PERCENT").trim();
		
		if(str == null || str.isEmpty()){
			return null;
		}else{
			dto.setPERCENT(Double.parseDouble(str));
		}
		return dto;
	}
	
	private boolean isRight1(DTO dto){
		BigDecimal little = new BigDecimal(0.0D);
		BigDecimal big = new BigDecimal(1.0D);
		BigDecimal percent = new BigDecimal(dto.getPERCENT()); 
		if(!(percent.compareTo(little)>=0 && percent.compareTo(big) <=0)){
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
			result = 24;
		}
		return "" + result;
	}
	
	

}
