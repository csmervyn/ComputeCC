package org.github.mervyn.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.github.mervyn.utils.DTO;
import org.github.mervyn.utils.Result;
import org.github.mervyn.utils.Table;
import org.github.mervyn.wjxdxdtc.WJXDXDTCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("computeWJXDXDTCController")
@RequestMapping(value = "/CWCompute")
public class ComputeWJXDXDTCController {
	@Autowired
	private WJXDXDTCService wJXDXDTCService;
	
	@RequestMapping(value = "/CFCompute.do")
	 public ModelAndView recompute(HttpServletRequest request){
			ModelAndView mav = new ModelAndView();	 
			Table table = (Table) request.getSession().getAttribute("table");
			String parameter = request.getParameter("parameter").trim();
			boolean flag = false;
			try {
				flag = isParameterRight(parameter);
			} catch (NumberFormatException e) {
				 mav.setViewName("forward:/WEB-INF/fiveStep.jsp");
				 mav.addObject("error", "参数设置错误，请重新设置参数");
				 return mav;
			}
			
			//参数没有错误，可以进行计算了
			String path = (String) request.getSession().getAttribute("filePath");
			List<Result> resultList = null;
			try{
				resultList = wJXDXDTCService.compute(table, Double.parseDouble(parameter),path);	
			}catch(IOException e){
				mav.setViewName("CCerror");
				mav.addObject("error", "计算出现错误");
				return mav;
			}
			mav.addObject("url1", path+ File.separator  + "修改后的表格数据.xls");
			mav.addObject("url2", path+ File.separator  + "各组平均值相关系数表.xls");
			mav.addObject("resultList", resultList);
			mav.setViewName("downlaodWenJuanXinXiaoDuTuiChu");
			return mav;
			
		 } 

		private boolean isParameterRight(String parameter){
			 if(parameter == null || parameter.isEmpty()){
				return false;
			}
			BigDecimal little = new BigDecimal(-1.0D);
			BigDecimal big = new BigDecimal(1.0D);
			BigDecimal BDparameter = new BigDecimal(Double.parseDouble(parameter));
			if(!(BDparameter.compareTo(little)>=0 && BDparameter.compareTo(big) <=0)){
				return false;
			}
			return true;
		}
		
		
		
		
		
		
}
