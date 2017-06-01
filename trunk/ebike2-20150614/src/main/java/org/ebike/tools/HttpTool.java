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

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @ClassName: HttpTool 
 * @Description: Http请求工具类，不支持cookie
 * @author hzjintianfan
 * @date 2014-7-23 下午7:07:18
 */
public class HttpTool
{

	public static String post(String url, Map params)
	{
		DefaultHttpClient client = new DefaultHttpClient();
		String response = null;
		try
		{
			HttpPost post = new HttpPost(url);
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
			UrlEncodedFormEntity e = new UrlEncodedFormEntity(pairs, "UTF-8");
			post.setEntity(e);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				HttpEntity entity = res.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				response = stream2string(entity.getContent(), charset);
			}
			else if (res.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY)
			{
				Header[] headers = res.getHeaders("Location");
				if (headers != null && headers.length > 0)
				{
					String redirectUrl = headers[0].getValue();
					System.out.println("重定向的URL:" + redirectUrl);
					redirectUrl = redirectUrl.replace(" ", "%20");
					return post(redirectUrl, new HashMap());
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return response;
	}

	/**
	 * 把输入流转为字符串
	 * @param is
	 * @param encode
	 * @return
	 */
	private static String stream2string(InputStream is, String encode)
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

}
