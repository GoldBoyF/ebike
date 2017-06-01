package org.ebike.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 
 * @ClassName: SMSTool 
 * @Description: 短信发送工具类
 * @author hzjintianfan
 * @date 2014-7-23 下午7:27:25
 */
public class SMSTool
{

	/**
	 * 短信验证码发送接口
	 * @param phone 手机号
	 * @param random 验证码
	 * @return 发送成功或失败
	 */
	public static boolean sendCode(String phone, String random)
	{
		HttpURLConnection httpURLConnection = null;
		String result = null;
		try
		{
			//发送POST请求
			StringBuffer urlBuffer = new StringBuffer();
			urlBuffer.append("http://118.145.30.35:9999/sms.aspx");
			urlBuffer.append("?action=send&userid=300&account=");
			String userName = URLEncoder.encode("袁", "UTF-8");
			urlBuffer.append(userName);
			urlBuffer.append("&password=123456&mobile=");
			urlBuffer.append(phone);
			urlBuffer.append("&content=您正在使用“易租车”软件，您的验证码为：");
			urlBuffer.append(random);
			urlBuffer.append("。请妥善保管好您的验证码。");
			String temp = URLEncoder.encode("【易租车】", "UTF-8");
			urlBuffer.append(temp);
			urlBuffer.append("&sendTime=&extno=");
			URL url = new URL(urlBuffer.toString());
			System.out.println("发送的url:" + urlBuffer.toString());
			httpURLConnection = (HttpURLConnection) url.openConnection();
			InputStream inputStream = httpURLConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
					httpURLConnection.getInputStream(), "UTF-8"));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = bufferedReader.read()) > -1)
			{
				sb.append((char) ch);
			}
			System.out.println(sb.toString());
			String returnstatus = XMLTool.getElementText(sb.toString(), "returnstatus");
			bufferedReader.close();
			if (returnstatus.equals("Success"))
				return true;
			else
				return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (httpURLConnection != null)
			{
				httpURLConnection.disconnect();
				httpURLConnection = null;
			}
		}
		return false;
	}

}
