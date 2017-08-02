package com.automation.controller;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.automation.service.AutomationService;

@Controller
@RequestMapping("/automation")
public class AutomationController {
	
	@Autowired
	@Qualifier("automationServiceImpl")
	private AutomationService automationService;
	
	Logger log = Logger.getLogger(AutomationController.class);
	
	
	@RequestMapping("/upload")
	@ResponseBody
	public String uploadCSV(HttpServletRequest request,String type){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
		MultipartFile multipartFile = multipartRequest.getFile(type);
		String name = multipartFile.getOriginalFilename();
		System.out.println("name:"+name);
		System.out.println("type:"+type);
		InputStream in = null;
		try {
			in = multipartFile.getInputStream();
			if("customer".equals(type)){
				automationService.createCustomer(in,multipartFile.getOriginalFilename());
			}else if("event".equals(type)){
				automationService.customerEvent(in,multipartFile.getOriginalFilename());
			}else if("update".equals(type)){
				automationService.updataCustomer(in,multipartFile.getOriginalFilename());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "123";
	}
	
	
	
}
