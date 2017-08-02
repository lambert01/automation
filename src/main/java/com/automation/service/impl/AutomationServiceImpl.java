package com.automation.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.automation.service.AutomationService;
import com.automation.util.Constant;
import com.automation.util.SendUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("automationServiceImpl")
public class AutomationServiceImpl implements AutomationService{

	Logger log = Logger.getLogger(AutomationServiceImpl.class);
	
	
	//创建带有身份信息的客户
	public void createCustomer(InputStream in,String fileName) {
		InputStreamReader isr = null;
        BufferedReader br = null;
        String str = "";
        JSONObject customerjson = new JSONObject();
        JSONObject identityjson = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        int k = 0;
        Map<Integer, String> map = new HashMap<Integer, String>();
        //获取access_token
        String access_token = getAccessToken();
        List<String> list = new ArrayList<String>();
        boolean flag = true;
        //截取上传文件的名称
        String name = fileName.substring(0,fileName.lastIndexOf("."));
		try {
			isr = new InputStreamReader(in,"UTF-8");// InputStreamReader 是字节流通向字符流的桥梁,
	        br = new BufferedReader(isr);
	        //循环csv文件
	        while ((str = br.readLine()) != null) {
	        	customerjson.clear();
	        	identityjson.clear();
	        	json.clear();
	        	jsonArray.clear();
	        	if(null != str && !"".equals(str)){
		        	 ++k;
		        	 if(null != str && !"".equals(str)){
		        		 if(k ==1){
		        			//把表头信息放到list集合中
		        			list.add("行号:"+str);
		        			//获取csv文件中的表头信息
		        			map = getHead(str);
			        		continue;
			        	 }
		        		 String[] customerArray = str.split(",");
		        		 //第二行开始，获取csv文件中的相对应数据
		        		 for(int j = 0;j<customerArray.length;j++){
		        			//添加身份信息
		        			 if(map.get(j).contains(Constant.IDENTITY_)){
		        				 identityjson.put("identityType", map.get(j).substring(map.get(j).lastIndexOf("_")+1,map.get(j).length()));
		        				 identityjson.put("identityValue",isQuotes(customerArray[j]));
		        				 identityjson.put("identityName","测试19");
		        				 jsonArray.add(identityjson);
		        			 }else if(map.get(j).contains("_wechat")){//添加身份信息
		        				 identityjson.put("identityType","wechat");
		        				 identityjson.put("identityValue",isQuotes(customerArray[j]));
		        				 identityjson.put("identityName","测试19");
		        				 jsonArray.add(identityjson);
		        			 }else{
		        				 if(!"displayName".equals(map.get(j)) && !Constant.LASTUPDATED.equals(map.get(j)) && !"dateCreated".equals(map.get(j))){
		        					 //当表头信息为mobile时进行数据验证
		        					 if("mobile".equals(map.get(j))){
		        						 //验证数据的正确性
		        						 flag = SendUtil.isMobile(isQuotes(customerArray[j]));
		        						 if(!flag){
		        							 //如果不对，打印log日志
		        							 log.debug(isQuotes(customerArray[j])+":"+"手机号出现问题debug");
		        							 //把错误的数据放到集合中，一并进行处理
		        							 list.add(k+":"+str);
		        							 break;
		        						 }
		        					 } //当表头信息为email时进行数据验证
		        					 if("email".equals(map.get(j))){
		        						 flag = SendUtil.isEmail(isQuotes(customerArray[j]));
		        						 if(!flag){
		        							 log.debug(map.get(j)+"出现问题"+":"+isQuotes(customerArray[j]));
		        							 list.add(k+":"+str);
		        							 break;
		        						 }
		        					 }
		        					//当表头信息为gender时进行数据验证
		        					 if("gender".equals(map.get(j))){
		        						 flag = SendUtil.isGender(isQuotes(customerArray[j]));
		        						 if(!flag){
		        							 log.debug(map.get(j)+"出现问题"+":"+isQuotes(customerArray[j]));
		        							 list.add(k+":"+str);
		        							 break;
		        						 }
		        					 }
		        					//当表头信息为birthday时进行数据验证
		        					 if("birthday".equals(map.get(j))){
		        						 flag = SendUtil.isDate(isQuotes(customerArray[j]),map.get(j));
		        						 if(!flag){
		        							 log.debug(map.get(j)+"出现问题"+":"+isQuotes(customerArray[j]));
		        							 list.add(k+":"+str);
		        							 break;
		        						 }
		        					 }//当表头信息为emailVerified时进行数据验证
		        					 if("emailVerified".equals(map.get(j))){
		        						 flag = SendUtil.isBoolean(isQuotes(customerArray[j]));
		        						 if(!flag){
		        							 log.debug(map.get(j)+"出现问题"+":"+isQuotes(customerArray[j]));
		        							 list.add(k+":"+str);
		        							 break;
		        						 }
		        					 }//当表头信息为employeeNumber时进行数据验证
		        					 if("employeeNumber".equals(map.get(j))){
		        						 flag = SendUtil.isInteger(isQuotes(customerArray[j]));
		        						 if(!flag){
		        							 log.debug(map.get(j)+"出现问题"+":"+isQuotes(customerArray[j]));
		        							 list.add(k+":"+str);
		        							 break;
		        						 }
		        					 }//当表头信息为isEmployee时进行数据验证
		        					 if("isEmployee".equals(map.get(j))){
		        						 flag = SendUtil.isBoolean(isQuotes(customerArray[j]));
		        						 if(!flag){
		        							 log.info(map.get(j)+"出现问题"+":"+isQuotes(customerArray[j]));
		        							 log.debug(map.get(j)+"出现问题"+":"+isQuotes(customerArray[j]));
		        							 list.add(k+":"+str);
		        							 break;
		        						 }
		        					 }//当表头信息为annualRevenue时进行数据验证
		        					 if("annualRevenue".equals(map.get(j))){
		        						 flag = SendUtil.isBigDecimal(isQuotes(customerArray[j]));
		        						 if(!flag){
		        							 log.info(map.get(j)+"出现问题"+":"+isQuotes(customerArray[j]));
		        							 log.debug(map.get(j)+"出现问题"+":"+isQuotes(customerArray[j]));
		        							 list.add(k+":"+str);
		        							 break;
		        						 }
		        					 }//当表头信息为dateJoin时进行数据验证
		        					 if("dateJoin".equals(map.get(j))){
		        						 flag = SendUtil.isDate(isQuotes(customerArray[j]),map.get(j));
		        						 if(!flag){
		        							 log.info(map.get(j)+"出现问题"+":"+isQuotes(customerArray[j]));
		        							 log.debug(map.get(j)+"出现问题"+":"+isQuotes(customerArray[j]));
		        							 list.add(k+":"+str);
		        							 break;
		        						 }
		        					 }
		        					 //把数据放到json中
		        					 customerjson.put(map.get(j), isQuotes(customerArray[j]));
		        				 }
		        			 }
		        		 }
		        		 json.put("customer", customerjson);
		        		 json.put("customerIdentities", jsonArray);
		        		 System.out.println("customer:"+customerjson);
		        		 System.out.println("customerIdentities:"+jsonArray);
		        		 System.out.println("json:"+json);
		        	 }
		        	 try {
		        		 if(flag){
		        			 //调用创建客户的api
		        			 String result = SendUtil.post(Constant.CUSTOMERANDIDENTITIES+"?access_token="+access_token,json.toString());
		        		 }
					} catch (Exception e) {
						e.printStackTrace();
					}
	        	}
	        }
	        if(list.size()>1){
	        	//把数据格式不对的数据写入到另外一个csv文件中
	        	SendUtil.writeCSV(list, name+getDateStr()+".csv");
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//完善客户身份、客户信息
	public void updataCustomer(InputStream in,String fileName) {
        InputStreamReader isr = null;
        BufferedReader br = null;
        String str = "";
        int k = 0;
        JSONObject json = new JSONObject();
        JSONObject jsonIdentities = new JSONObject();
        Integer customerId = null;
        JSONArray jsonArray = new JSONArray();
        Map<Integer, String> map = new HashMap<Integer, String>();
        //获取access_token
        String access_token = getAccessToken();
        //截取文件名称
        String name = fileName.substring(0,fileName.lastIndexOf("."));
        List<String> list = new ArrayList<String>();
        boolean flag = true;
        try {
     		 isr = new InputStreamReader(in,"UTF-8");// InputStreamReader 是字节流通向字符流的桥梁,
             br = new BufferedReader(isr);
             while ((str = br.readLine()) != null) {
            	 if(null != str && !"".equals(str)){
            		 ++k;
                	 if(k ==1){
                		 list.add("行号:"+str);
                		 //获取csv文件的表头信息
		        		 map = getHead(str);
    	        		 continue;
    	        	 }
                	 //从第二行开始，截取数据
                	 String[] updateCustomer = str.split(",");
                	 for(int i = 0;i<updateCustomer.length;i++){
                		 //如果数据类型为mobile或者email调用查询客户信息的api获取相对应的customerId
                		 if(map.get(i).equals("mobile") || map.get(i).equals("email")){
                			 json.put(map.get(i), isQuotes(updateCustomer[i]));
                			 if(customerId == null){
                				 String mobileOremail = isQuotes(updateCustomer[i]);
                    			 String identity = "";
                    			 //如果获取的数据对应的表头信息是手机号，则验证手机号的正确性
                    			 if(map.get(i).equals("mobile")){
                    				 flag = SendUtil.isMobile(isQuotes(updateCustomer[i]));
                    				 if(!flag){
                    					 //如果不对则把整行数据放入list集合中
                    					 list.add(k+":"+str);
                    					 log.debug(map.get(i)+"出现问题:"+isQuotes(updateCustomer[i]));
                    					 break;
                    				 }
                    			 }
                    			 //如果获取的数据对应的表头信息是邮箱，则验证手机号的正确性
                    			 if(map.get(i).equals("email")){
                    				 flag = SendUtil.isEmail(isQuotes(updateCustomer[i]));
                    				 if(!flag){
                    					 //如果不对则把整行数据放入list集合中
                    					 list.add(k+":"+str);
                    					 log.debug(map.get(i)+"出现问题:"+isQuotes(updateCustomer[i]));
                    					 break;
                    				 }
                    			 }
                				 if(null != mobileOremail && !"".equals(mobileOremail)){
                					 //调用查询客户列表的openAPI
                					 identity = SendUtil.sendGet("http://api.convertwork.cn/v1/customers","access_token=" + access_token+"&"+map.get(i)+"="+mobileOremail);
                				 }
                				 if(null != identity && !"".equals(identity)){
                					 JSONArray a = (JSONArray)JSONObject.fromObject(identity).get("rows");
                					 if(null != a && a.size()>0){
                						 //获取customerId
                						 customerId = (Integer)JSONObject.fromObject(JSONArray.fromObject(a).get(0)).get("id");
                					 }
                					 
                				 }
                			 }
                		 }else if(map.get(i).contains("identity_")){//如果相对应的字段类型是身份，才调用查询客户身份的API
                			 String identityType = map.get(i).substring(map.get(i).lastIndexOf("_")+1,map.get(i).length());
            				 String identityValue = isQuotes(updateCustomer[i]);
            				 String identity = "";
            				 if(null != identityType && !"".equals(identityType) && null != identityValue && !"".equals(identityValue) ){
            					 //调用查询客户信息的openAPI
            					 identity = SendUtil.sendGet("https://api.convertwork.cn/v1/customeridentities","access_token=" + access_token+"&identityType="+identityType+"&identityValue="+identityValue);
            				 }
            				 //如果可以查询出相对应的数据，则获取查询出来的customerid
            				 if(null != identity && !"".equals(identity) && !"[]".equals(identity)){
            					 customerId = Integer.valueOf((String)JSONObject.fromObject(JSONArray.fromObject(identity).get(0)).get("customerId"));
            				 }else{//如果查询不到数据，则把身份信息放到json数组中
            					 jsonIdentities.put("identityType", identityType);
            					 jsonIdentities.put("identityValue", identityValue);
            					 jsonIdentities.put("identityName","");
                				 jsonArray.add(jsonIdentities);
            				 }
            				 continue;
                		 }
                		 if("displayName".equals(map.get(i)) || "lastUpdated".equals(map.get(i)) || "dateCreated".equals(map.get(i))){
                			 continue; 
                		 }
                		 //如果获取的数据对应的表头信息是gender，则验证手机号的正确性
                		 if("gender".equals(map.get(i))){
    						 flag = SendUtil.isGender(isQuotes(updateCustomer[i]));
    						 if(!flag){
    							 log.debug(map.get(i)+"出现问题"+":"+isQuotes(updateCustomer[i]));
    							 list.add(k+":"+str);
    							 break;
    						 }
    					 }
                		 //如果获取的数据对应的表头信息是birthday，则验证手机号的正确性
    					 if("birthday".equals(map.get(i))){
    						 flag = SendUtil.isDate(isQuotes(updateCustomer[i]),map.get(i));
    						 if(!flag){
    							 log.debug(map.get(i)+"出现问题"+":"+isQuotes(updateCustomer[i]));
    							 list.add(k+":"+str);
    							 break;
    						 }
    					 }
    					 //如果获取的数据对应的表头信息是emailVerified，则验证手机号的正确性
    					 if("emailVerified".endsWith(map.get(i))){
    						 flag = SendUtil.isBoolean(isQuotes(updateCustomer[i]));
    						 if(!flag){
    							 log.debug(map.get(i)+"出现问题"+":"+isQuotes(updateCustomer[i]));
    							 list.add(k+":"+str);
    							 break;
    						 }
    					 }
    					 //如果获取的数据对应的表头信息是employeeNumber，则验证手机号的正确性
    					 if("employeeNumber".endsWith(map.get(i))){
    						 flag = SendUtil.isInteger(isQuotes(updateCustomer[i]));
    						 if(!flag){
    							 log.debug(map.get(i)+"出现问题"+":"+isQuotes(updateCustomer[i]));
    							 list.add(k+":"+str);
    							 break;
    						 }
    					 }
    					 //如果获取的数据对应的表头信息是isEmployee，则验证手机号的正确性
    					 if("isEmployee".endsWith(map.get(i))){
    						 flag = SendUtil.isBoolean(isQuotes(updateCustomer[i]));
    						 if(!flag){
    							 log.debug(map.get(i)+"出现问题"+":"+isQuotes(updateCustomer[i]));
    							 list.add(k+":"+str);
    							 break;
    						 }
    					 }
    					 //如果获取的数据对应的表头信息是annualRevenue，则验证手机号的正确性
    					 if("annualRevenue".endsWith(map.get(i))){
    						 flag = SendUtil.isBigDecimal(isQuotes(updateCustomer[i]));
    						 if(!flag){
    							 log.debug(map.get(i)+"出现问题"+":"+isQuotes(updateCustomer[i]));
    							 list.add(k+":"+str);
    							 break;
    						 }
    					 }
    					 //如果获取的数据对应的表头信息是dateJoin，则验证手机号的正确性
    					 if("dateJoin".endsWith(map.get(i))){
    						 flag = SendUtil.isDate(isQuotes(updateCustomer[i]),map.get(i));
    						 if(!flag){
    							 log.debug(map.get(i)+"出现问题"+":"+isQuotes(updateCustomer[i]));
    							 list.add(k+":"+str);
    							 break;
    						 }
    					 }
                		 json.put(map.get(i), isQuotes(updateCustomer[i]));
                	 } 
            	 }
            	if(customerId != null){
            		//如果json数组不为空，则说明需要给当前用户添加身份
            		if(null != jsonArray && jsonArray.size()>0){
                		for(int a = 0;a<jsonArray.size();a++){
                			jsonIdentities = JSONObject.fromObject(jsonArray.get(a));
                			jsonIdentities.put("customerId", customerId);
                			//调用添加客户身份的openAPI
                			String result = SendUtil.post("https://api.convertwork.cn/v1/customeridentities?access_token="+access_token,jsonIdentities.toString());
                			System.out.println("arrayIdentities:"+jsonIdentities);
                		}
                	}
            		
            		json.put("customerId", customerId);
            		if(flag){
            			//调用更新客户信息的openAPI
            			String result = SendUtil.HttpPut("https://api.convertwork.cn/v1/customers/"+customerId+"?access_token="+access_token, json.toString());
            		}
            		
            	}
            	System.out.println("jsonIdentities:"+jsonIdentities);
            	System.out.println("json:"+json);
             }
             if(list.size()>1){
            	 //如果list集合中的数据大于1，则把错误数据写到csv文件中
            	 SendUtil.writeCSV(list, name+getDateStr()+".csv");
             }
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	
	//创建客户事件
	public void customerEvent(InputStream in,String fileName) {
        InputStreamReader isr = null;
        BufferedReader br = null;
        String str = "";
        int k = 0;
        JSONObject json = new JSONObject();
        Integer customerId = null;
        Map<Integer, String> map = new HashMap<Integer, String>();
        //生成access_token
        String access_token = getAccessToken();
        boolean flag = true;
        List<String> list = new ArrayList<String>();
        //截取csv文件名称
        String name = fileName.substring(0,fileName.lastIndexOf("."));
        try {
        	//fis = new FileInputStream("C:\\Users\\lx\\Desktop\\customerEvent1.csv");
     		 isr = new InputStreamReader(in,"UTF-8");// InputStreamReader 是字节流通向字符流的桥梁,
             br = new BufferedReader(isr);
             while ((str = br.readLine()) != null) {
            	++k;
 	        	if(null != str && !"".equals(str)){
	        		 if(k ==1){
	        			 //把表头放入list集合中
	        			 list.add("行号:"+str);
	        			 //生成表头信息
		        		 map = getHead(str);
		        		 continue;
		        	 }
	        		 String[] customerEvent = str.split(",");
	        		 for(int j=0;j<customerEvent.length;j++){
	        			 if(customerId == null){
	        				 //如果数据相对应的类型为身份类型则调用查询客户身份的api获取相对应的customerId
	        				 if(map.get(j).contains("identity_")){
		        				 String identityType = map.get(j).substring(map.get(j).lastIndexOf("_")+1,map.get(j).length());
		        				 String identityValue = isQuotes(customerEvent[j]);
		        				 String identity = "";
		        				 if(null != identityType && !"".equals(identityType) && null != identityValue && !"".equals(identityValue) ){
		        					 //调用查询客户信息的openAPI
		        					 identity = SendUtil.sendGet("http://api.convertwork.cn/v1/customeridentities","access_token=" + access_token+"&identityType="+identityType+"&identityValue="+identityValue);
		        				 }
		        				 if(null != identity && !"".equals(identity) && !"[]".equals(identity)){
		        					 //获取customerId
		        					 customerId = Integer.valueOf((String)JSONObject.fromObject(JSONArray.fromObject(identity).get(0)).get("customerId"));
		        				 }
		        				 continue;
		        			 }else if(map.get(j).equals("mobile") || map.get(j).equals("email")){//如果数据相对应的类型为手机号或者邮箱，则调用查询客户列表信息的openAPI或许customerId
		        				 String mobileOremail = isQuotes(customerEvent[j]);
		        				 String identity = "";
		        				 if(null != mobileOremail && !"".equals(mobileOremail)){
		        					 //调用查询客户列表的openAPI
		        					 identity = SendUtil.sendGet("http://api.convertwork.cn/v1/customers","access_token=" + access_token+"&"+map.get(j)+"="+mobileOremail);
		        				 }
		        				 if(null != identity && !"".equals(identity) && !"[]".equals(identity)){
		        					 //获取customerId
		        					 customerId = (Integer)JSONObject.fromObject(JSONArray.fromObject(JSONObject.fromObject(identity).get("rows")).get(0)).get("id");
		        				 }
		        				 continue;
		        				
		        			 } 
	        			 }else{
	        				 json.put("customerId", customerId);
	        				 if("mobile".equals(map.get(j)) || "email".equals(map.get(j)) || map.get(j).contains("identity_") || "lastUpdated".equals(map.get(j))){
	        					 continue;
	        				 }else{
	        					 //当获取的数据类型是score时则进行数据验证
	        					 if("score".equals(map.get(j))){
	        						 flag = SendUtil.isInteger(isQuotes(customerEvent[j]));
	        						 if(!flag){
	        							 //如果数据不对，则把整行数据放入list集合中，并且跳出当前循环
	        							 list.add(k+":"+str);
	        							 log.debug(map.get(j)+"出现错误:"+isQuotes(customerEvent[j]));
	        							 break;
	        						 }
	        					 }
	        					//当获取的数据类型是date时则进行数据验证
	        					 if("date".equals(map.get(j))){
	        						 flag = SendUtil.isDate(isQuotes(customerEvent[j]), map.get(j));
	        						 if(!flag){
	        							//如果数据不对，则把整行数据放入list集合中，并且跳出当前循环
	        							 list.add(k+":"+str);
	        							 log.debug(map.get(j)+"出现错误:"+isQuotes(customerEvent[j]));
	        							 break;
	        						 }
	        					 }
	        					//当获取的数据类型是event时则进行数据验证
	        					 if("event".equals(map.get(j))){
	        						 flag = SendUtil.isChinese(isQuotes(customerEvent[j]));
	        						 if(!flag){
	        							//如果数据不对，则把整行数据放入list集合中，并且跳出当前循环
	        							 list.add(k+":"+str);
	        							 log.debug(map.get(j)+"出现错误:"+isQuotes(customerEvent[j]));
	        							 break;
	        						 }
	        					 }
	        					//当获取的数据类型是campaign时则进行数据验证
	        					 if("campaign".equals(map.get(j))){
	        						 flag = SendUtil.isChinese(isQuotes(customerEvent[j]));
	        						 if(!flag){
	        							//如果数据不对，则把整行数据放入list集合中，并且跳出当前循环
	        							 list.add(k+":"+str);
	        							 log.debug(map.get(j)+"出现错误:"+isQuotes(customerEvent[j]));
	        							 break;
	        						 }
	        					 }
	        					//当获取的数据类型是targetId时则进行数据验证
	        					 if("targetId".equals(map.get(j))){
	        						 flag = SendUtil.isChinese(isQuotes(customerEvent[j]));
	        						 if(!flag){
	        							//如果数据不对，则把整行数据放入list集合中，并且跳出当前循环
	        							 list.add(k+":"+str);
	        							 log.debug(map.get(j)+"出现错误:"+isQuotes(customerEvent[j]));
	        							 break;
	        						 }
	        					 }
	        					 
	        					 json.put(map.get(j), isQuotes(customerEvent[j]));
	        				 }
	        				 
	        			 }
	        		 }
 	        	}
 	        	if(flag){
 	        		//调用创建客户事件的openAPI
 	        		String result = SendUtil.post("https://api.convertwork.cn/v1/customerevents?access_token="+access_token,json.toString());
 	        	}
 	        	customerId = null;
 	        	System.out.println("event:"+json);
 	        }
             if(list.size()>1){
            	 //如果集合大于1，则把数据写入到csv文件中
 	        	SendUtil.writeCSV(list, name+getDateStr()+".csv");
 	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//获取表头信息
	public Map<Integer,String> getHead(String str){
		String[] head = str.split(",");
		Map<Integer,String> map = new HashMap<Integer, String>();
		for(int i=0;i<head.length;i++){
			map.put(i, head[i]);
		}
		return map;
 	}
	
	
	public String getAccessToken(){
		String access = SendUtil.sendGet(Constant.ACCESSTOKENURL, "grant_type=client_credentials&appid=" + Constant.APPID + "&secret=" + Constant.SECRET + "");
		Map<String,Object> res = parseData(access);
		String access_token = res.get("access_token").toString();
		return access_token;
	}
	
	public static String isQuotes(String quotes){
		if(null != quotes && !"".equals(quotes)){
			String object = quotes.contains("\"") ? quotes.replace("\"","") : quotes;
			return object;
		}
		return "";
	}
	
	public static Map<String, Object> parseData(String data){
        return JSONObject.fromObject(data);
    }
	
	public static String getDateStr(){
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sim.format(new Date());
		return str;
	}
}
