package org.github.mervyn.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.github.mervyn.createShuLie.CSLService;
import org.github.mervyn.domain.NormalDistribution1;
import org.github.mervyn.fwjxgxsxg.SingleColumnTable;
import org.github.mervyn.utils.DTO;
import org.github.mervyn.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("createShuLieController")
@RequestMapping(value = "/createShuLie")
public class CreateShuLieController {
	@Autowired
	private CSLService cSLService;
	
	@RequestMapping(value = "/goCreate.do")
	 public ModelAndView goCreate(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("create/create");
		return mav;
	 }
	
	@RequestMapping(value = "/getShuLieNum.do")
	 public ModelAndView getShuLieNum(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		String str = request.getParameter("shuLieNum").trim();
		Integer shuLieNum = 0;
		if(str == null || str.isEmpty()){
			mav.setViewName("forward:/WEB-INF/create/create.jsp");
			mav.addObject("error", "参数设置错误，请重新设置参数");
			return mav;
		}else{
			shuLieNum = Integer.parseInt(str);
		}
		mav.addObject("shuLieNum", shuLieNum);
		mav.setViewName("create/create1");
		return mav;
	 }
	
	
	@RequestMapping(value = "/create.do")
	 public ModelAndView create(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		String str = request.getParameter("shuLieNum").trim();
		int shuLieNum =  Integer.parseInt(str);
		List<NormalDistribution1> list =  null;
		try{
			list = getData(request,shuLieNum);
		}catch(NumberFormatException e){
			mav.addObject("error", "参数设置错误，请重新设置参数");
			mav.addObject("shuLieNum", shuLieNum);
			mav.setViewName("create/create1");
			return mav;
		}
		String path = request.getSession().getServletContext().getRealPath("upload");  
		String realPath = path+ File.separator  + "数列生成.xls";
		try {
			cSLService.create(list,realPath);
		} catch (IOException e) {
			mav.addObject("error", "参数设置错误，请重新设置参数");
			mav.addObject("shuLieNum", shuLieNum);
			mav.setViewName("create/create1");
			
			e.printStackTrace();
			return mav;
		}
		mav.setViewName("create/downlaod");
		return mav;
	 }
	
	 @RequestMapping("/download.do")    
	    public ResponseEntity<byte[]> downloadFWJTable(HttpServletRequest request) throws IOException {
	    	String path = request.getSession().getServletContext().getRealPath("upload");
	        File file=new File(path+ File.separator  + "数列生成.xls");  
	        HttpHeaders headers = new HttpHeaders();    
	        String fileName=new String("数列生成.xls".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
	        headers.setContentDispositionFormData("attachment", fileName);   
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
	        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
	                                          headers, HttpStatus.CREATED);    
	    }  
	
	private List<NormalDistribution1> getData(HttpServletRequest request,int shuLieNum){
		List<NormalDistribution1> list = new ArrayList<NormalDistribution1>();
		for(int i = 0; i < shuLieNum; i++){
			NormalDistribution1 entity = new NormalDistribution1();
			int j = i+1;
			String index = "mean" + j;
			String str = request.getParameter(index).trim();
			if(str == null || str.isEmpty()){
				return null;
			}else{
				entity.setMean(Double.parseDouble(str));
			}
			
			index = "sd" + j;
			str = request.getParameter(index).trim();
			if(str == null || str.isEmpty()){
				return null;
			}else{
				entity.setSd(Double.parseDouble(str));
			}
			
			index = "num" + j;
			str = request.getParameter(index).trim();
			if(str == null || str.isEmpty()){
				return null;
			}else{
				entity.setNum(Integer.parseInt(str));
			}
			
			index = "decimalPlace" + j;
			str = request.getParameter(index).trim();
			if(str == null || str.isEmpty()){
				return null;
			}else{
				entity.setDecimalPlace(Integer.parseInt(str));
			}
			
			index = "min" + j;
			str = request.getParameter(index).trim();
			if(str == null){
				return null;
			}else if(str.isEmpty()){
				entity.setFlag(Boolean.FALSE);
			}else{
				entity.setFlag(Boolean.TRUE);
				entity.setMin(Double.parseDouble(str));
			}
			
			index = "max" + j;
			str = request.getParameter(index).trim();
			if(str == null){
				return null;
			}else if(str.isEmpty()){
				entity.setFlag(Boolean.FALSE);
			}else{
				entity.setFlag(Boolean.TRUE);
				entity.setMax(Double.parseDouble(str));
			}
			
			
			list.add(entity);
		}
		return list;
	}
}
