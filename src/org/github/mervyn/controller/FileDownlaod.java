package org.github.mervyn.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("fileDownlaod")
@RequestMapping("/downloadFile") 
public class FileDownlaod {
	@RequestMapping("/alterTable.do")    
    public ResponseEntity<byte[]> downloadAlterTable(HttpServletRequest request) throws IOException {
    	String path = request.getSession().getServletContext().getRealPath("upload");
        File file=new File(path+ File.separator  + "修改后的表格数据.xls");  
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String("修改后的表格数据.xls".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);    
    }  
    
    
    	@RequestMapping("/correlationCoefficient.do")    
    public ResponseEntity<byte[]> downloadorrelationCoefficient(HttpServletRequest request) throws IOException {
    	String path = request.getSession().getServletContext().getRealPath("upload");
        File file=new File(path+ File.separator  + "各组平均值相关系数表.xls");  
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String("各组平均值相关系数表.xls".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);    
    }
    
    
    @RequestMapping("/XGXSBDTCTable.do")    
    public ResponseEntity<byte[]> downloadXGXSBDTCTable(HttpServletRequest request) throws IOException {
    	String path = request.getSession().getServletContext().getRealPath("upload");
        File file=new File(path+ File.separator  + "剔除后的表格数据.xls");  
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String("剔除后的表格数据.xls".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);    
    }
    
    @RequestMapping("/WJXDXDTCTable.do")    
    public ResponseEntity<byte[]> downloadWJXDXDTCTable(HttpServletRequest request) throws IOException {
    	String path = request.getSession().getServletContext().getRealPath("upload");
        File file=new File(path+ File.separator  + "剔除后的表格数据.xls");  
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String("剔除后的表格数据.xls".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);    
    }  

    @RequestMapping("/alterFWJTable.do")    
    public ResponseEntity<byte[]> downloadFWJTable(HttpServletRequest request) throws IOException {
    	String path = request.getSession().getServletContext().getRealPath("upload");
        File file=new File(path+ File.separator  + "修改后的表格数据.xls");  
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String("修改后的表格数据.xls".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);    
    }  

}
