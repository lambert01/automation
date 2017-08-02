package com.automation.service;

import java.io.InputStream;

public interface AutomationService {

	//创建用户
	public void createCustomer(InputStream in,String fileName);
	
	public void customerEvent(InputStream in,String fileName);
	
	public void updataCustomer(InputStream in,String fileName);
}
