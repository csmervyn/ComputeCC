package org.github.mervyn.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.github.mervyn.dataExtraction.Param;
import org.github.mervyn.dataExtraction1.DE1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("dataExtractionController1")
@RequestMapping(value = "/DEC1")
public class DataExtraction1Controller {
	@Autowired
	private DE1Service service;
	@RequestMapping("/download.do")    
    public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException {
    	String path = request.getSession().getServletContext().getRealPath("download");
        File file=new File(path+ File.separator  + "抽取出的单组表数据.xls");  
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String("抽取出的单组表数据.xls".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);    
    }  
	//
	@RequestMapping(value = "/getData.do")
	public ModelAndView getData(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		List<List<Integer>> percentListList = null;
		Param param = (Param) request.getSession().getAttribute("DEC1param");
		String downloadPath = request.getSession().getServletContext().getRealPath("download"); 
		try{
			percentListList = getPercentListList(param,request);
			
		}catch(NumberFormatException e){
			mav.setViewName("forward:/WEB-INF/dataExtraction1/downlaod.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}
		try {
			service.main(param, percentListList,downloadPath);
		} catch (IOException e) {
			mav.setViewName("forward:/WEB-INF/dataExtraction1/downlaod.jsp");
			mav.addObject("error", "计算出现错误");
			e.printStackTrace();
			return mav;
		}
		
		mav.setViewName("dataExtraction1/downlaod");
		return mav;
	}
	
	
	private List<List<Integer>> getPercentListList(Param param,HttpServletRequest request){
		List<List<Integer>> percentListList = new ArrayList<List<Integer>>();
		for(int groupIndex = 0; groupIndex < param.getGroupNum(); groupIndex++){
			List<Integer> lineNumList = new ArrayList<Integer>();
			for(int itemIndex = 0; itemIndex < param.getList().get(groupIndex); itemIndex++){
				String index = "pecent_" + groupIndex + "_" + itemIndex;
				String str = request.getParameter(index).trim();
				double percent = 0;
				if(str == null || str.isEmpty()){
					return null;
				}else{
					percent = Double.parseDouble(str);
				}
				//四舍五入取整
				int  temp =  new BigDecimal(percent * param.getSampleSize()).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
				
				lineNumList.add(temp);
			}
			int sum = 0;
			for(int i = 0; i < lineNumList.size(); i++){
				sum += lineNumList.get(i);
			}
			if(sum < param.getSampleSize()){
				int need = param.getSampleSize() - lineNumList.size();
				int targetIndex = new Random().nextInt(param.getList().get(groupIndex));
				int realNum = lineNumList.get(targetIndex) + need;
				lineNumList.set(targetIndex, realNum);
			}
			percentListList.add(lineNumList);
		}
		return percentListList;
	}
	
 	private List<Integer> getMaxDataList(HttpServletRequest request){
		List<Integer> list = new ArrayList<Integer>();
		
		Param param = (Param) request.getSession().getAttribute("DEC1param");
		for(int i = 0; i < param.getGroupNum(); i++){
			String index = "max" + i;
			String str = request.getParameter(index).trim();
			if(str == null || str.isEmpty()){
				return null;
			}else{
				list.add(Integer.parseInt(str));
			}
		}
		
		return list;
	}
	
	
	//goSetPercentPage
	@RequestMapping(value = "/goSetPercentPage.do")
	public ModelAndView goSetPercentPage(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		List<Integer> maxList = null;
		Param param = (Param) request.getSession().getAttribute("DEC1param");
		try{
			maxList  = getMaxDataList(request);
			param.setList(maxList);
			
		}catch(NumberFormatException e){
			mav.setViewName("forward:/WEB-INF/dataExtraction1/setPercent.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}
		int maxElement = Integer.MIN_VALUE;
		for(int i = 0; i < maxList.size(); i++){
			if(maxList.get(i) > maxElement){
				maxElement = maxList.get(i);
			}
		}
		param.setMaxElement(maxElement);
		
		request.getSession().setAttribute("DEC1param", param);
		mav.addObject("maxElement", maxElement);
		mav.addObject("maxList", maxList);
		mav.addObject("groupNum", param.getGroupNum());
		mav.setViewName("dataExtraction1/setPercent");
		return mav;
	}
	
	
	
	//goNextPage
	@RequestMapping(value = "/goNextPage.do")
	public ModelAndView goNextPage(HttpServletRequest request){
		String groupNum = request.getParameter("groupNum").trim();
		String sampleSize = request.getParameter("sampleSize").trim();
		Param param = new Param(Integer.parseInt(groupNum),Integer.parseInt(sampleSize));
		request.getSession().setAttribute("DEC1param", param);
		ModelAndView mav = new ModelAndView();
		mav.addObject("groupNum", Integer.parseInt(groupNum));
		mav.setViewName("dataExtraction1/setMaxElementInOneGroup");
		return mav;
	}
	
	@RequestMapping(value = "/goSingleGroup.do")
	public ModelAndView goSelectPage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("dataExtraction1/setGroupNum");
		return mav;
	}
	
}
