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
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @ClassName: MyHttpClient 
 * @Description: Http工具类，支持cookie，目前已经废除，最好用SimpleHttpClient
 * @author hzjintianfan
 * @date 2014-7-23 下午7:23:44
 */
@Deprecated
public class MyHttpClient
{
	private CookieStore cookie;
	private String domain;

	public MyHttpClient(String domain)
	{
		this.domain = domain;
		//client = new DefaultHttpClient();
	}

	public JSONObject post(String path, Map params)
	{
		DefaultHttpClient client = new DefaultHttpClient();
		if (cookie != null)
		{
			client.setCookieStore(cookie);
		}
		String response = null;
		try
		{
			HttpPost post = new HttpPost(domain + path);
			post.setEntity(map2HttpEntity(params));
			HttpResponse res = client.execute(post);
			List<org.apache.http.cookie.Cookie> cookies = client.getCookieStore().getCookies();
			if (cookies.size() > 0)
			{
				cookie = client.getCookieStore();
			}
			System.out.println("COOKIE:" + cookie);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(), charset);
				EntityUtils.consumeQuietly(entity);
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
		if (cookie != null)
		{
			client.setCookieStore(cookie);
		}
		String response = null;
		try
		{
			HttpPut put = new HttpPut(domain + path);
			put.setEntity(map2HttpEntity(params));
			HttpResponse res = client.execute(put);
			List<org.apache.http.cookie.Cookie> cookies = client.getCookieStore().getCookies();
			if (cookies.size() > 0)
			{
				cookie = client.getCookieStore();
			}
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(), charset);
				EntityUtils.consumeQuietly(entity);
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
		if (cookie != null)
		{
			client.setCookieStore(cookie);
		}
		String response = null;
		try
		{
			HttpGet get = new HttpGet(domain + path);
			HttpResponse res = client.execute(get);
			List<org.apache.http.cookie.Cookie> cookies = client.getCookieStore().getCookies();
			if (cookies.size() > 0)
			{
				cookie = client.getCookieStore();
			}
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(), charset);
				EntityUtils.consumeQuietly(entity);
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
		if (cookie != null)
		{
			client.setCookieStore(cookie);
		}
		String response = null;
		try
		{
			HttpDelete delete = new HttpDelete(domain + path);
			HttpResponse res = client.execute(delete);
			List<org.apache.http.cookie.Cookie> cookies = client.getCookieStore().getCookies();
			if (cookies.size() > 0)
			{
				cookie = client.getCookieStore();
			}
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(), charset);
				EntityUtils.consumeQuietly(entity);
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
