package org.github.mervyn.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.github.mervyn.domain.DYHGParameter;
import org.github.mervyn.domain.GroupDYHGParam;
import org.github.mervyn.fwjxgxsxg.FWJService;
import org.github.mervyn.fwjxgxsxg.SingleColumnTable;
import org.github.mervyn.utils.Result;
import org.github.mervyn.utils.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import Jama.Matrix;

@Controller("computeFWJXGXSXGController")
@RequestMapping(value = "/CFCompute")
public class ComputeFWJXGXSXGController {

	@Autowired
	private FWJService fWJService;
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
	
	
	@RequestMapping(value = "/compute.do")
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
		List<Matrix> matrixList = fWJService.groupCompute(table,groupDyhgParam);
		List<String>  strList = getFinalStrList(matrixList, groupDyhgParam);
		//String finalStr = getFinalStr(parameter,B);
		List<Result> resultList = (List<Result>) request.getSession().getAttribute("resultList");
		String path = (String) request.getSession().getAttribute("filePath");
		
		request.getSession().setAttribute("groupDyhgParam", groupDyhgParam);
		request.getSession().setAttribute("matrixList", matrixList);

		
		//request.getSession().setAttribute("resultList", resultList);
		
		mav.addObject("url1", path+ File.separator  + "修改后的表格数据.xls");
		mav.addObject("url2", path+ File.separator  + "相关系数表.xls");
		mav.addObject("resultList", resultList);
		mav.addObject("strList", strList);
		mav.setViewName("downlaodFeiWenJuanXiangGuanXiShuXiuGai");
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
	
	//getParam.do
	@RequestMapping(value = "/getParam.do")
	 public ModelAndView getParam(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		GroupDYHGParam  groupDyhgParam = new GroupDYHGParam();
		String str = request.getParameter("duoYuanNum").trim();
		if(str == null || str.isEmpty()){
			mav.setViewName("forward:/WEB-INF/fwjxgxsxg/setNum.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}else{
			groupDyhgParam.setNum(Integer.parseInt(str));
		}
		request.getSession().setAttribute("groupDyhgParam", groupDyhgParam);
		mav.addObject("duoYuanNum", groupDyhgParam.getNum());
		mav.setViewName("fwjxgxsxg/duoYuanHuiGuiYanZheng");
		return mav;
	 } 
	
	
	//duoYuanHuiGuiYanZheng.do
	@RequestMapping(value = "/duoYuanHuiGuiYanZheng.do")
	public ModelAndView goduoYuanHuiGuiYanZhengPage(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("fwjxgxsxg/setNum");
		return mav;
	}
	
	
	@RequestMapping(value = "/recompute.do")
	 public ModelAndView recompute(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();	 
		
		SingleColumnTable table = (SingleColumnTable) request.getSession().getAttribute("singleColumnTable");
		//System.out.println(table);
		String former = request.getParameter("former").trim();
		former = getTrueNum(former);
		String latter = request.getParameter("latter").trim();
		latter = getTrueNum(latter);
		String parameter = request.getParameter("parameter").trim();
		boolean flag = false;
		try {
			flag = isParameterRight(former, latter, parameter, table);
		} catch (NumberFormatException e) {
			 mav.setViewName("forward:/WEB-INF/feiWenJuanXiangGuanXiShuXiuGai.jsp");
			 mav.addObject("error", "参数设置错误，请重新设置参数");
			 return mav;
		}
		
		
		int formerColumn = Integer.parseInt(former);
		int latterColumn = Integer.parseInt(latter);
		//参数没有错误，可以进行计算了
		String path = (String) request.getSession().getAttribute("filePath");
		List<Result> resultList = null;
		try{
			resultList = fWJService.computeFiveStep(path,formerColumn,latterColumn,Double.parseDouble(parameter),table);
		}catch(IOException e){
			mav.setViewName("CCerror");
			mav.addObject("error", "计算出现错误");
			return mav;
		}
		
		request.getSession().setAttribute("resultList", resultList);
		
		mav.addObject("url1", path+ File.separator  + "修改后的表格数据.xls");
		mav.addObject("url2", path+ File.separator  + "相关系数表.xls");
		mav.addObject("resultList", resultList);
		mav.setViewName("downlaodFeiWenJuanXiangGuanXiShuXiuGai");
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
		}
		return "" + result;
	}
}
