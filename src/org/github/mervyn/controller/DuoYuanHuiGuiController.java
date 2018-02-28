package org.github.mervyn.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.github.mervy.dyhgyz.DYHGService;
import org.github.mervyn.domain.DYHGParameter;
import org.github.mervyn.domain.GroupDYHGParam;
import org.github.mervyn.utils.DTO;
import org.github.mervyn.utils.Result;
import org.github.mervyn.utils.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import Jama.Matrix;

@Controller("duoYuanHuiGuiController")
@RequestMapping("/DYHG")
public class DuoYuanHuiGuiController {
	
	@Autowired
	private DYHGService dYHGService;
	
	@RequestMapping(value = "/getParam.do")
	 public ModelAndView getParam(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		GroupDYHGParam  groupDyhgParam = new GroupDYHGParam();
		String str = request.getParameter("duoYuanNum").trim();
		if(str == null || str.isEmpty()){
			mav.setViewName("forward:/WEB-INF/duoYuanHuiGuiYanZheng/setNum.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}else{
			groupDyhgParam.setNum(Integer.parseInt(str));
		}
		request.getSession().setAttribute("groupDyhgParam", groupDyhgParam);
		mav.addObject("duoYuanNum", groupDyhgParam.getNum());
		mav.setViewName("duoYuanHuiGuiYanZheng/duoYuanHuiGuiYanZheng");
		return mav;
	 } 
	
	//继续进行多元回归验证
	@RequestMapping(value = "/JXduoYuanHuiGuiYanZheng.do")
	public ModelAndView JXduoYuanHuiGuiYanZheng(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		//mav.setViewName("duoYuanHuiGuiYanZheng/duoYuanHuiGuiYanZheng");
		Boolean  flag = (Boolean) request.getSession().getAttribute("flag");
		if(flag == null || !flag){	
			mav.setViewName("duoYuanHuiGuiYanZheng/setNum");
		}else{
			mav.setViewName("redirect:/DYHG/computeJX.do");
		}
		return mav;
	}
	//重新去往多元回归验证页面
	@RequestMapping(value = "/duoYuanHuiGuiYanZheng.do")
	public ModelAndView goduoYuanHuiGuiYanZhengPage(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		//mav.setViewName("duoYuanHuiGuiYanZheng/duoYuanHuiGuiYanZheng");
			
		mav.setViewName("duoYuanHuiGuiYanZheng/setNum");
		
		return mav;
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
	//继续
	@RequestMapping(value = "/computeJX.do")
	 public ModelAndView computeJX(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		Boolean  flag = (Boolean) request.getSession().getAttribute("flag");
		GroupDYHGParam temp = null;
		GroupDYHGParam groupDyhgParam = null;
		if(flag == null || !flag){	
			temp = (GroupDYHGParam) request.getSession().getAttribute("groupDyhgParam");
			groupDyhgParam = getGroupParam(request,temp);
		}else{
			groupDyhgParam = (GroupDYHGParam) request.getSession().getAttribute("groupDyhgParam");
		}
		/*GroupDYHGParam temp = (GroupDYHGParam) request.getSession().getAttribute("groupDyhgParam");
		GroupDYHGParam groupDyhgParam = getGroupParam(request,temp);
		*/
		if(groupDyhgParam == null){
			mav.setViewName("forward:/WEB-INF/duoYuanHuiGuiYanZheng/duoYuanHuiGuiYanZheng.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}
		Table table = (Table) request.getSession().getAttribute("table");
		//Matrix B = dYHGService.compute(table, parameter);
		List<Matrix> matrixList = dYHGService.groupCompute(table,groupDyhgParam);
		List<String>  strList = getFinalStrList(matrixList, groupDyhgParam);
		//String finalStr = getFinalStr(parameter,B);
		List<Result> resultList = (List<Result>) request.getSession().getAttribute("resultList");
		String path = (String) request.getSession().getAttribute("filePath");
		
		request.getSession().setAttribute("groupDyhgParam", groupDyhgParam);
		request.getSession().setAttribute("matrixList", matrixList);
		request.getSession().setAttribute("flag", Boolean.TRUE);
		
		mav.addObject("url1", path+ File.separator  + "修改后的表格数据.xls");
		mav.addObject("url2", path+ File.separator  + "各组平均值相关系数表.xls");
		mav.addObject("resultList", resultList);
		request.getSession().setAttribute("resultList", resultList);
		//mav.addObject("finalStr", finalStr);
		mav.addObject("strList", strList);
		//System.out.println(finalStr);
		mav.setViewName("downlaod1");
		return mav;
	 } 
	
	
	@RequestMapping(value = "/compute.do")
	 public ModelAndView compute(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		GroupDYHGParam temp = (GroupDYHGParam) request.getSession().getAttribute("groupDyhgParam");
		GroupDYHGParam groupDyhgParam = getGroupParam(request,temp);
		
		if(groupDyhgParam == null){
			mav.setViewName("forward:/WEB-INF/duoYuanHuiGuiYanZheng/duoYuanHuiGuiYanZheng.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}
		Table table = (Table) request.getSession().getAttribute("table");
		//Matrix B = dYHGService.compute(table, parameter);
		List<Matrix> matrixList = dYHGService.groupCompute(table,groupDyhgParam);
		List<String>  strList = getFinalStrList(matrixList, groupDyhgParam);
		//String finalStr = getFinalStr(parameter,B);
		List<Result> resultList = (List<Result>) request.getSession().getAttribute("resultList");
		String path = (String) request.getSession().getAttribute("filePath");
		
		request.getSession().setAttribute("groupDyhgParam", groupDyhgParam);
		request.getSession().setAttribute("matrixList", matrixList);
		request.getSession().setAttribute("flag", Boolean.TRUE);
		
		mav.addObject("url1", path+ File.separator  + "修改后的表格数据.xls");
		mav.addObject("url2", path+ File.separator  + "各组平均值相关系数表.xls");
		mav.addObject("resultList", resultList);
		request.getSession().setAttribute("resultList", resultList);
		//mav.addObject("finalStr", finalStr);
		mav.addObject("strList", strList);
		//System.out.println(finalStr);
		mav.setViewName("downlaod1");
		return mav;
	 } 
	public List<String> getFinalStrList(List<Matrix> list,GroupDYHGParam groupDyhgParam){
		List<String> mylist = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++){
			mylist.add(getFinalStr(groupDyhgParam.getDyhgList().get(i),list.get(i)));
		}
		return mylist;
	}
	
	private String getFinalStr(DYHGParameter parameter,Matrix matrixB){
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
			double newtemp2 = matrixB.get(lineNum, 0);
			BigDecimal bg2 = new BigDecimal(newtemp2);
			newtemp2 = bg2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			sb.append(newtemp2);
			sb.append("×");
			sb.append(numToStr(temp));
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
 		}
 		return str;
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
