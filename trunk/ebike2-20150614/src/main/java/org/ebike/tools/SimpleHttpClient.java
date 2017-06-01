package org.ebike.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @ClassName: SimpleHttpClient 
 * @Description: Http请求工具类
 * @author hzjintianfan
 * @date 2014-7-23 下午7:32:44
 */
public class SimpleHttpClient
{
	private String sid; //存储cookie值
	//private CookieStore cookie;
	private String domain; //服务器网址

	public SimpleHttpClient(String domain, String sid)
	{
		this.domain = domain;
		this.sid = sid;
	}

	public SimpleHttpClient(String domain)
	{
		this.domain = domain;
	}

	public JSONObject post(String path, Map params)
	{
		DefaultHttpClient client = new DefaultHttpClient();
		String response = null;
		try
		{
			HttpPost post = new HttpPost(domain + path);
			if (sid != null)
			{
				post.setHeaders(buildCookieHeader());
			}
			post.setEntity(map2HttpEntity(params));
			HttpResponse res = client.execute(post);
			List<org.apache.http.cookie.Cookie> cookies = client.getCookieStore().getCookies();
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(), charset);
			}
			else if (res.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY)
				return post(getRedirectPath(res), new HashMap());
			return JSONObject.fromObject(response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public JSONObject put(String path, Map params)
	{
		DefaultHttpClient client = new DefaultHttpClient();
		String response = null;
		try
		{
			HttpPut put = new HttpPut(domain + path);
			if (sid != null)
			{
				put.setHeaders(buildCookieHeader());
			}
			put.setEntity(map2HttpEntity(params));
			HttpResponse res = client.execute(put);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(), charset);
			}
			else if (res.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY)
				return post(getRedirectPath(res), new HashMap());
			return JSONObject.fromObject(response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public JSONObject get(String path)
	{
		DefaultHttpClient client = new DefaultHttpClient();
		String response = null;
		try
		{
			HttpGet get = new HttpGet(domain + path);
			if (sid != null)
			{
				get.setHeaders(buildCookieHeader());
			}
			HttpResponse res = client.execute(get);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(), charset);
			}
			else if (res.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY)
				return get(getRedirectPath(res));
			return JSONObject.fromObject(response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public JSONObject delete(String path)
	{
		DefaultHttpClient client = new DefaultHttpClient();
		String response = null;
		try
		{
			HttpDelete delete = new HttpDelete(domain + path);
			if (sid != null)
			{
				delete.setHeaders(buildCookieHeader());
			}
			HttpResponse res = client.execute(delete);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(), charset);
			}
			else if (res.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY)
				return get(getRedirectPath(res));
			return JSONObject.fromObject(response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/*public JSONObject postFile(String path,String filePath){	
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
		    //插入文件
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
			return JSONObject.fromObject(response);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/

	private String stream2string(InputStream is, String encode)
	{
		if (is != null)
		{
			try
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, encode));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null)
				{
					sb.append(line + "\n");
				}
				is.close();
				return sb.toString();
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 有session返回带session的Header数组，没session返回null
	 * @return
	 */
	private Header[] buildCookieHeader()
	{
		Map<String, String> header = new HashMap<String, String>();
		if (sid != null && !sid.isEmpty())
		{
			header.put("Cookie", "JSESSIONID=" + sid);
			return buildHeader(header);
		}
		else
			return null;

	}

	private Header[] buildHeader(Map<String, String> params)
	{
		Header[] headers = null;
		if (params != null && params.size() > 0)
		{
			headers = new BasicHeader[params.size()];
			int i = 0;
			for (Map.Entry<String, String> entry : params.entrySet())
			{
				headers[i] = new BasicHeader(entry.getKey(), entry.getValue());
				i++;
			}
		}
		return headers;
	}

	private UrlEncodedFormEntity map2HttpEntity(Map params) throws UnsupportedEncodingException
	{
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		Set keys = params.keySet();
		for (Object key : keys)
		{
			if (null != params.get(key))
			{
				pairs.add(new BasicNameValuePair((String) key, params.get(key).toString()));
			}
			else
			{
				pairs.add(new BasicNameValuePair((String) key, ""));
			}
		}
		return new UrlEncodedFormEntity(pairs, "UTF-8");
	}

	private String getRedirectPath(HttpResponse res)
	{
		Header[] headers = res.getHeaders("Location");
		if (headers != null && headers.length > 0)
		{
			String redirectUrl = headers[0].getValue();
			redirectUrl = redirectUrl.replace(" ", "%20");
			if (redirectUrl.contains(domain))
			{
				redirectUrl = redirectUrl.substring(domain.length());
			}
			return redirectUrl;
		}
		return null;
	}

}
