package org.github.mervyn.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.github.mervyn.dataExtraction.ExtractionService;
import org.github.mervyn.dataExtraction.Param;
import org.github.mervyn.utils.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("dataExtractionController")
@RequestMapping(value = "/DEC")
public class DataExtractionController {
	@Autowired
	private ExtractionService service;
	
	//downloadSevenPoint
	@RequestMapping("/downloadSevenPoint.do")    
    public ResponseEntity<byte[]> downloadSevenPoint(HttpServletRequest request) throws IOException {
    	String path = request.getSession().getServletContext().getRealPath("download");
        File file=new File(path+ File.separator  + "抽取出的七点量表数据.xls");  
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String("抽取出的七点量表数据.xls".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);    
    }  
	
	@RequestMapping(value = "/sevenDataExtraction.do")
	public ModelAndView sevenDataExtraction(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		Param param = (Param) request.getSession().getAttribute("param");
		String path = request.getSession().getServletContext().getRealPath("sourceDataOfDataExtraction"); 
		String downloadPath = request.getSession().getServletContext().getRealPath("download"); 
		List<Integer> list = null;
		try{
			list  = getColumnDataList(request);
			if(list != null){
				param.setList(list);
				Table bigTable;
				try {
					bigTable = service.parseTable2(path);
					service.mainSevenPoint(param, bigTable, downloadPath);
				} catch (IOException e) {
					mav.setViewName("forward:/WEB-INF/dataExtraction/downlaodSevenPoint.jsp");
					mav.addObject("error", "计算出现错误");
					return mav;
				}
				
			}else{
				mav.setViewName("forward:/WEB-INF/dataExtraction/downlaodSevenPoint.jsp");
				mav.addObject("error", "参数设置错误，请重新设置参数");
				return mav;
			}
		}catch(NumberFormatException e){
			mav.setViewName("forward:/WEB-INF/dataExtraction/downlaodSevenPoint.jsp");
				mav.addObject("error", "参数设置错误，请重新设置参数");
				return mav;
		}

		mav.setViewName("dataExtraction/downlaodSevenPoint");
		return mav;
	}

	@RequestMapping(value = "/sevenSetColumnNum.do")
	public ModelAndView sevenSetColumn(HttpServletRequest request){
		String groupNum = request.getParameter("groupNum").trim();
		String sampleSize = request.getParameter("sampleSize").trim();
		Param param = new Param(Integer.parseInt(groupNum),Integer.parseInt(sampleSize));
		request.getSession().setAttribute("param", param);
		ModelAndView mav = new ModelAndView();
		mav.addObject("groupNum", Integer.parseInt(groupNum));
		mav.setViewName("dataExtraction/sevenSetColumnNum");
		return mav;
	}
	
	@RequestMapping("/downloadFivePoint.do")    
    public ResponseEntity<byte[]> downloadFivePoint(HttpServletRequest request) throws IOException {
    	String path = request.getSession().getServletContext().getRealPath("download");
        File file=new File(path+ File.separator  + "抽取出的五点量表数据.xls");  
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String("抽取出的五点量表数据.xls".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);    
    }  
	//fiveDataExtraction
	@RequestMapping(value = "/fiveDataExtraction.do")
	public ModelAndView fiveDataExtraction(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		Param param = (Param) request.getSession().getAttribute("param");
		String path = request.getSession().getServletContext().getRealPath("sourceDataOfDataExtraction"); 
		String downloadPath = request.getSession().getServletContext().getRealPath("download"); 
		List<Integer> list = null;
		try{
			list  = getColumnDataList(request);
			if(list != null){
				param.setList(list);
				Table bigTable;
				try {
					bigTable = service.parseTable1(path);
					service.mainFivePoint(param, bigTable, downloadPath);
				} catch (IOException e) {
					mav.setViewName("forward:/WEB-INF/dataExtraction/downlaodFivePoint.jsp");
					mav.addObject("error", "计算出现错误");
					return mav;
				}
				
			}else{
				mav.setViewName("forward:/WEB-INF/dataExtraction/downlaodFivePoint.jsp");
				mav.addObject("error", "参数设置错误，请重新设置参数");
				return mav;
			}
		}catch(NumberFormatException e){
			mav.setViewName("forward:/WEB-INF/dataExtraction/downlaodFivePoint.jsp");
				mav.addObject("error", "参数设置错误，请重新设置参数");
				return mav;
		}

		mav.setViewName("dataExtraction/downlaodFivePoint");
		return mav;
	}
	
	private List<Integer> getColumnDataList(HttpServletRequest request){
		List<Integer> list = new ArrayList<Integer>();
		
		Param param = (Param) request.getSession().getAttribute("param");
		for(int i = 0; i < param.getGroupNum(); i++){
			String index = "column" + i;
			String str = request.getParameter(index).trim();
			if(str == null || str.isEmpty()){
				return null;
			}else{
				list.add(Integer.parseInt(str));
			}
		}
		
		return list;
	}
	
	@RequestMapping(value = "/fiveSetColumnNum.do")
	public ModelAndView fiveSetColumn(HttpServletRequest request){
		String groupNum = request.getParameter("groupNum").trim();
		String sampleSize = request.getParameter("sampleSize").trim();
		Param param = new Param(Integer.parseInt(groupNum),Integer.parseInt(sampleSize));
		request.getSession().setAttribute("param", param);
		ModelAndView mav = new ModelAndView();
		mav.addObject("groupNum", Integer.parseInt(groupNum));
		mav.setViewName("dataExtraction/fiveSetColumnNum");
		return mav;
	}
	
	@RequestMapping(value = "/goSelect.do")
	public ModelAndView goSelectPage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("dataExtraction/select");
		return mav;
	}
	
	@RequestMapping(value = "/goFivePointScale.do")
	public ModelAndView goFivePointScalePage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("dataExtraction/fivePointScale");
		return mav;
	}
	
	@RequestMapping(value = "/goSevenPointScale.do")
	public ModelAndView goSevenPointScalePage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("dataExtraction/sevenPointScale");
		return mav;
	}
	
}
