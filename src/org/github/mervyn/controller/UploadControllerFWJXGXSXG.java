package org.github.mervyn.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.github.mervyn.fwjxgxsxg.FWJService;
import org.github.mervyn.fwjxgxsxg.SingleColumnTable;
import org.github.mervyn.utils.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller("uploadControllerFWJXGXSXG")
public class UploadControllerFWJXGXSXG {
	@Autowired
	private FWJService fWJService;
	
	@RequestMapping(value = "/goUploadFWJXGXSXG.do")
	public ModelAndView goUploadFWJXGXSXG(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("uploadFWJXGXSXG");
		return mav;
	}
	
	@RequestMapping(value = "/uploadFWJXGXSXG.do")
	public ModelAndView uploadFWJXGXSXG(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) { 
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
        	mav.setViewName("forward:/WEB-INF/uploadFWJXGXSXG.jsp");
			mav.addObject("error", "无法将上传文件保存到指定目录，有可能是存储空间目录已满");
			return mav;
        }
        request.getSession().setAttribute("filePath", path); 
        SingleColumnTable table = null;
        try {
        	table = fWJService.parseTable(path);
        	//System.out.println(table);
		} catch(IOException | NumberFormatException  e) {
			mav.setViewName("forward:/WEB-INF/uploadFWJXGXSXG.jsp");
			mav.addObject("error", "上传的excel格式不正确，无法正确解析该excel文件，请重新上传符合要求的excel文件");
			return mav;
		}
        request.getSession().setAttribute("singleColumnTable", table);   
        mav.setViewName("feiWenJuanXiangGuanXiShuXiuGai"); 
        return mav;
    }
	
	
	@RequestMapping(value = "/feiWenJuanXiangGuanXiShuXiuGai.do")
	public ModelAndView gofiveStepPage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("feiWenJuanXiangGuanXiShuXiuGai");
		return mav;
	}
}
