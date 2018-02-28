package org.github.mervyn.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.github.mervyn.xgxsbdtc.Result;
import org.github.mervyn.xgxsbdtc.Table;
import org.github.mervyn.xgxsbdtc.XGXSBDTCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("computeXGXSBDTCController")
@RequestMapping(value = "/CXCompute")
public class ComputeXGXSBDTCController {
	@Autowired
	private XGXSBDTCService xGXSBDTCService;
	
	@RequestMapping(value = "/recompute.do")
	 public ModelAndView recompute(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();	 
		
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
			 mav.setViewName("forward:/WEB-INF/xiangGuanXiShuBianDongTuiChu.jsp");
			 mav.addObject("error", "参数设置错误，请重新设置参数");
			 return mav;
		}
		
		int formerGroup = Integer.parseInt(former);
		int latterGroup = Integer.parseInt(latter);
		//参数没有错误，可以进行计算了
		String path = (String) request.getSession().getAttribute("filePath");
		List<Result> resultList = null;
		try{
			resultList = xGXSBDTCService.main(path,formerGroup,latterGroup,Double.parseDouble(parameter),table);
		}catch(IOException e){
			mav.setViewName("CCerror");
			mav.addObject("error", "计算出现错误");
			return mav;
		}
		mav.addObject("url1", path+ File.separator  + "剔除后的表格数据.xls");
		mav.addObject("url2", path+ File.separator  + "各组平均值相关系数表.xls");
		mav.addObject("resultList", resultList);
		mav.setViewName("downlaodXiangGuanXiShuBianDongTiChu");
		return mav;
		
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
		}
		return "" + result;
	}
}
