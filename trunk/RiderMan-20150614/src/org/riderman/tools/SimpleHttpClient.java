package org.riderman.tools;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.riderman.exception.NetException;

public class SimpleHttpClient {
	//public static String DOMAIN = "http://etotech.net:5000/ebike"; 
	public static String DOMAIN = "http://223.252.196.240:8080/ebike"; 
	
	private String sid;
	//private CookieStore cookie;
	private String domain;
	
	public SimpleHttpClient(String domain,String sid){
		this.domain = domain;
		this.sid = sid;
	}
	
	public SimpleHttpClient(String domain){
		this.domain = domain;
	}
	
	public JSONObject post(String path,Map params) throws NetException{	
		DefaultHttpClient client = new DefaultHttpClient();
		String response = null;
		try {
			HttpPost post =  new HttpPost( domain+path);
			if(sid!=null){
				post.setHeaders(buildCookieHeader());
			}
		    post.setEntity(map2HttpEntity(params));  
			HttpResponse res = client.execute(post);
			List<org.apache.http.cookie.Cookie> cookies = client.getCookieStore().getCookies();
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(),charset);
			}else if(res.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {			 
                return post(getRedirectPath(res),new HashMap());   
			}
			return new JSONObject(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NetException();
		}
	}
	
	public JSONObject put(String path,Map params) throws NetException{	
		DefaultHttpClient client = new DefaultHttpClient();
		String response = null;
		try {
			HttpPut put =  new HttpPut( domain+path);
			if(sid!=null){
				put.setHeaders(buildCookieHeader());
			}
			put.setEntity(map2HttpEntity(params));  
			HttpResponse res = client.execute(put);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(),charset);
			}else if(res.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {			 
                return post(getRedirectPath(res),new HashMap());   
			}
			return new JSONObject(response);
		} catch (Exception e) {
			throw new NetException();
		}
	}
	
	public JSONObject postFile(String path,String filePath) throws NetException{	
		if(!new File(filePath).exists()){
			return null;
		}
		DefaultHttpClient client = new DefaultHttpClient();
		String response = null;
		try {
			HttpPost post =  new HttpPost( domain+path);
			if(sid!=null){
				post.setHeaders(buildCookieHeader());
			}
		    //²åÈëÎÄ¼þ
		    FileBody fileBody = new FileBody(new File(filePath));  
	        MultipartEntity ety = new MultipartEntity();  
	        ety.addPart("file", fileBody);  
	        post.setEntity(ety);  
			HttpResponse res = client.execute(post);
			List<org.apache.http.cookie.Cookie> cookies = client.getCookieStore().getCookies();
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(),charset);
			}else if(res.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {			 
                return post(getRedirectPath(res),new HashMap());   
			}
			return new JSONObject(response);
		} catch (Exception e) {
			throw new NetException();
		}
	}
	
	public JSONObject get(String path) throws NetException{	
		DefaultHttpClient client = new DefaultHttpClient();
		String response = null;
		try {
			HttpGet get =  new HttpGet( domain+path); 
			if(sid!=null){
				get.setHeaders(buildCookieHeader());
			}
			HttpResponse res = client.execute(get);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(),charset);
			}else if(res.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {			 
                return get(getRedirectPath(res));   
			}
			return new JSONObject(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NetException();
		}
	}
	
	public JSONObject delete(String path) throws NetException{	
		DefaultHttpClient client = new DefaultHttpClient();
		String response = null;
		try {
			HttpDelete delete =  new HttpDelete( domain+path); 
			if(sid!=null){
				delete.setHeaders(buildCookieHeader());
			}
			HttpResponse res = client.execute(delete);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(),charset);
			}else if(res.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {			 
                return get(getRedirectPath(res));   
			}
			return new JSONObject(response);
		} catch (Exception e) {
			throw new NetException();
		}
	}
	
	
	private  String stream2string(InputStream is, String encode) {
        if (is != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, encode));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                return sb.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
	


	private Header[] buildCookieHeader(){
		Map<String,String> header = new HashMap<String,String>();
		if(sid!=null && !sid.isEmpty()){
			header.put("Cookie", "JSESSIONID="+sid);
			return buildHeader(header);
		}else {
			return null;
		}
		
	}
	
	private Header[] buildHeader(Map<String,String> params){
    	Header[] headers = null;
    	if(params != null && params.size() > 0){
    		headers = new BasicHeader[params.size()];
    		int i  = 0;
    		for (Map.Entry<String, String> entry:params.entrySet()) {
    			headers[i] = new BasicHeader(entry.getKey(),entry.getValue());
    			i++;
    		}
    	}
    	return headers;
    }
	
	private UrlEncodedFormEntity map2HttpEntity(Map params) throws UnsupportedEncodingException{
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();  
		Set keys = params.keySet();
		for(Object key:keys){
			if(null != params.get(key)){
				pairs.add(new BasicNameValuePair((String)key,params.get(key).toString())); 					
			}else {
				pairs.add(new BasicNameValuePair((String)key,"")); 					
			}	
		}
	    return new UrlEncodedFormEntity(pairs,"UTF-8");  
	}
	
	private String getRedirectPath(HttpResponse res){
		Header[] headers = res.getHeaders("Location");
		if(headers!=null && headers.length>0){
			String redirectUrl = headers[0].getValue();
			redirectUrl = redirectUrl.replace(" ", "%20");
			if(redirectUrl.contains(domain)){
				redirectUrl = redirectUrl.substring(domain.length());
			}
			return redirectUrl;
		}
		return null;	
	}
	
	public static void main(String[] args) throws NetException {
		String domain = "http://localhost:8080/ebike";
		SimpleHttpClient client = new SimpleHttpClient(domain);
		System.out.println(client.get("/providers/detail/007"));;
	}
	
}
