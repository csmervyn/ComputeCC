package org.github.mervyn.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.github.mervyn.utils.Compute;
import org.github.mervyn.utils.ComputeService;
import org.github.mervyn.utils.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller("uploadController")
public class UploadController {

	@Autowired
	private ComputeService service;
	
	@RequestMapping(value = "/upload.do")
	public ModelAndView upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) {  
        String path = request.getSession().getServletContext().getRealPath("upload");  
        String fileName = "originalTableName.xls";
        File targetFile = new File(path, fileName);  
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        ModelAndView mav = new ModelAndView();
        //保存  
        try {  
            file.transferTo(targetFile);  
        } catch (Exception e) {  
        	mav.setViewName("forward:/WEB-INF/upload.jsp");
			mav.addObject("error", "无法将上传文件保存到指定目录，有可能是存储空间目录已满");
			e.printStackTrace();
			return mav;
        }
        request.getSession().setAttribute("filePath", path); 
        Table table = null;
        try {
        	table = service.parseTable(path);
		} catch(IOException | NumberFormatException  e) {
			mav.setViewName("forward:/WEB-INF/upload.jsp");
			mav.addObject("error", "上传的excel格式不正确，无法正确解析该excel文件，请重新上传符合要求的excel文件");
			e.printStackTrace();
			return mav;
		}
        request.getSession().setAttribute("table", table);
       Compute compute = new  Compute();
       mav.addObject("compute", compute);
        mav.setViewName("computeCorrelationCoefficient"); 
        return mav;
    }
	
	@RequestMapping(value = "/goUploadPage.do")
	public ModelAndView goUploadPage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("upload");
		return mav;
	}
	
	@RequestMapping(value = "/goUploadPage1.do")
	public ModelAndView goUploadPage1(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("upload1");
		return mav;
	}

	@RequestMapping(value = "/upload1.do")
	public ModelAndView upload1(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) { 
		String path = request.getSession().getServletContext().getRealPath("upload");  
        String fileName = "originalTableName.xls";
        File targetFile = new File(path, fileName);  
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        ModelAndView mav = new ModelAndView();
        //保存  
        try {  
            file.transferTo(targetFile);  
        } catch (Exception e) {  
        	mav.setViewName("forward:/WEB-INF/upload1.jsp");
			mav.addObject("error", "无法将上传文件保存到指定目录，有可能是存储空间目录已满");
			return mav;
        }
        request.getSession().setAttribute("filePath", path); 
        Table table = null;
        try {
        	table = service.parseTable(path);
		} catch(IOException | NumberFormatException  e) {
			mav.setViewName("forward:/WEB-INF/upload1.jsp");
			mav.addObject("error", "上传的excel格式不正确，无法正确解析该excel文件，请重新上传符合要求的excel文件");
			return mav;
		}
        request.getSession().setAttribute("table", table);
        double percent = 0.03D;
		mav.addObject("PERCENT", percent);
        mav.setViewName("fiveStep"); 
        return mav;
    }

	@RequestMapping(value = "/fiveStep.do")
	public ModelAndView gofiveStepPage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("fiveStep");
		return mav;
	}
	
	
	
}
