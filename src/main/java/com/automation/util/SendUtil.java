package com.automation.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;


import net.sf.json.JSONObject;

public class SendUtil {

	
	public static boolean isMobile(String mobileStr){
		boolean flag = true;
		try {
			Long.valueOf(mobileStr);
			//String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";  
			String regExp = "^[1]([3][0-9]{1}|59|58|88|89|78)[0-9]{8}$";
			Pattern p = Pattern.compile(regExp);  
			Matcher m = p.matcher(mobileStr);   
			flag = m.find();  
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	public static boolean isEmail(String emailStr){
		String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(RULE_EMAIL);
        Matcher m = p.matcher(emailStr);
		return m.matches();
	}
	
	public static boolean isGender(String genderStr){
		boolean flag = true;
		try {
			int gender = Integer.valueOf(genderStr);
			if(gender == 0 || gender ==1 || gender == 2){
				flag = true;
			}else{
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	public static boolean isInteger(String employeeNumberStr){
		boolean flag = true;
		try {
			Integer.valueOf(employeeNumberStr);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	public static boolean isBoolean(String str){
		boolean flag = false;
		if("true".equals(str) || "false".equals(str)){
			flag = true;
		}
		return flag;
	}
	
	
	public static boolean isDate(String dateStr,String type){
		boolean flag = true;
		/*String regExp = "^((([1-2]){1}[0-9]{3}[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(([1-2]{1})[0-9]{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		//String regExp1 = "^((([1-2]){1}[0-9]{3}[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(dateStr);
		return m.matches();*/
		Date date = null;
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(dateStr.contains("Z") && dateStr.contains("T")){
				dateStr = dateStr.replace("Z"," ");
				dateStr = dateStr.replace("T"," ");
				date = sim.parse(dateStr);
			}else{
				date = sim.parse(dateStr);
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	
	public static boolean isBigDecimal(String str){
		boolean flag = true;
		try {
			Double.valueOf(str);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	
	public static boolean isChinese(String str){
		String regExp = "[^\u4e00-\u9fa5]+";
		//String regExp1 = "[\u4e00-\u9fa5]+";
		Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
		return m.matches();
	}
	
	
	public static void writeCSV(List<String> list,String fileName){
        FileOutputStream fos = null;
        OutputStreamWriter os = null;
        BufferedWriter bw = null;
        String path = "C:/Users/lx/Desktop/";
        File file = new File(path);
        if(!file.exists()){
        	file.mkdirs();
        }
        try {
			fos = new FileOutputStream(path+fileName,true);
			os = new OutputStreamWriter(fos,"utf-8");
			bw = new BufferedWriter(os);
			if(null != list && list.size()>0){
				for(int i= 0;i<list.size();i++){
					String str = list.get(i);
					bw.write(str);
					bw.write("\n");
				}
				bw.close();
				os.close();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
	 public static String sendGet(String url, String param) {
	        String result = "";
	        BufferedReader inpo = null;
	        boolean isInt = false;
	        int i = 1;
	        while(isInt==false) {
	        	if(i>=5){
	        		break;
	        	}
	            try {
	                Thread.sleep(100);
	                isInt = true;
	                String urlNameString = url + "?" + param;
	                System.out.println(urlNameString);
	                trustAllHosts();
	                URL realUrl = new URL(urlNameString);
	                // 打开和URL之间的连接
	                URLConnection connection = realUrl.openConnection();
	                // 设置通用的请求属性
	                connection.setRequestProperty("accept", "*/*");
	                connection.setRequestProperty("connection", "Keep-Alive");
	                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	                // 建立实际的连接
	                connection.connect();
	                // 获取所有响应头字段
	                Map<String, List<String>> map = connection.getHeaderFields();
	                // 遍历所有的响应头字段
	                for (String key : map.keySet()) {
	                	System.out.println(key + "--->" + map.get(key));
	                }
	                // 定义 BufferedReader输入流来读取URL的响应
	                inpo = new BufferedReader(new InputStreamReader(
	                        connection.getInputStream()));
	                String line;
	                while ((line = inpo.readLine()) != null) {
	                    result += line;
	                }
	            } catch (Exception e) {
	                isInt=false;
	                System.out.println("发送GET请求出现异常！" + e + "结尾");
	                e.printStackTrace();
	                result = "";
	            }
	            // 使用finally块来关闭输入流
	            finally {
	                try {
	                    if (inpo != null) {
	                        inpo.close();
	                    }
	                } catch (Exception e2) {
	                    e2.printStackTrace();
	                }
	            }
	            i++;
	        }
	        return result;
	    }

	
	
	public static String post(String URL,String json) {

	 trustAllHosts();
     HttpClient client = new DefaultHttpClient();
     client = WebClientDevWrapper.wrapClient(client);
     HttpPost post = new HttpPost(URL);

     post.setHeader("Content-Type", "text/json");
     post.addHeader("Authorization", "Basic YWRtaW46");
     String result = "error";

     try {

         StringEntity s = new StringEntity(json, "utf-8");
         s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                 "application/json"));
         post.setEntity(s);

         // 发送请求
         HttpResponse httpResponse = client.execute(post);
         if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
             System.out.print("请求服务器成功，做相应处理");
         } else {
             System.out.print("请求服务端失败");
         }
         // 获取响应输入流
         InputStream inStream = httpResponse.getEntity().getContent();
         BufferedReader reader = new BufferedReader(new InputStreamReader(
                 inStream, "utf-8"));
         StringBuilder strber = new StringBuilder();
         String line = null;
         while ((line = reader.readLine()) != null)
             strber.append(line + "\n");
         inStream.close();

         result = strber.toString();
         System.out.print(result);

     } catch (Exception e) {
         System.out.print("请求异常");
         throw new RuntimeException(e);
     }

     return result;
 }
	
	
	public static String HttpPut(String postUrl,String postEntity) throws Exception {  
		trustAllHosts();
		URL postURL = new URL(postUrl); 
     HttpURLConnection httpURLConnection = (HttpURLConnection) postURL.openConnection(); 
     httpURLConnection.setDoOutput(true);                 
     httpURLConnection.setDoInput(true); 
     httpURLConnection.setRequestMethod("PUT"); 
     httpURLConnection.setUseCaches(false); 
     httpURLConnection.setInstanceFollowRedirects(true); 
     //application/json x-www-form-urlencoded
     //httpURLConnection.setRequestProperty("Content-Type",  "application/x-www-form-urlencoded");//表单上传的模式
     httpURLConnection.setRequestProperty("Content-Type",  "application/json;charset=utf-8");//json格式上传的模式
     StringBuilder sbStr = new StringBuilder();
     /*if(postHeaders != null) {
         for(String pKey : postHeaders.keySet()) {
             httpURLConnection.setRequestProperty(pKey, postHeaders.get(pKey));
         }
     }*/
     if(postEntity != null) {
         JSONObject obj = JSONObject.fromObject(postEntity);
         PrintWriter out = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(),"utf-8"));   
         out.println(obj);  
         out.close();  
         BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection  
                 .getInputStream()));  
           
         String inputLine; 
         while ((inputLine = in.readLine()) != null) {  
             sbStr.append(inputLine);  
         }  
         in.close();  
     }
     httpURLConnection.disconnect(); 
     return new String(sbStr.toString().getBytes(),"utf-8");
 }  

	
	
	
	private static void trustAllHosts() {  
	  	  
     // Create a trust manager that does not validate certificate chains  
     TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {  
   
         public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
             return new java.security.cert.X509Certificate[] {};  
         }  
   
         public void checkClientTrusted(X509Certificate[] chain, String authType)  {  
               
         }  
   
         public void checkServerTrusted(X509Certificate[] chain, String authType) {  
               
         }  
     } };  
   
     // Install the all-trusting trust manager  
     try {  
         SSLContext sc = SSLContext.getInstance("TLS");  
         sc.init(null, trustAllCerts, new java.security.SecureRandom());  
         HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());  
     } catch (Exception e) {  
         e.printStackTrace();  
     }  
 }  
	
}
