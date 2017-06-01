package org.ebike.bean;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Service;

/**
 * 
 * @ClassName: Application 
 * @Description: 存储web应用的全局信息
 * @author hzjintianfan
 * @date 2014-7-28 下午2:19:45
 */
@Service
public class Application
{

	private Properties prop;

	private String bathPath;

	public Application() throws Exception
	{
		loadAppProperties();
	}

	/**
	 * 加载application.properties属性文件
	 */
	public void loadAppProperties()
	{
		try
		{
			prop = new Properties();//属性集合对象 

			InputStream path = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("application.properties");//获取路径并转换成流
			prop.load(path);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public String getParameter(String key)
	{
		return prop.getProperty(key);
	}

	public void setParameter(String key, String value)
	{
		prop.setProperty(key, value);
		try
		{
			prop.store(new FileOutputStream("application.properties"), "app");
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getBathPath()
	{
		return bathPath;
	}

	public void setBathPath(String bathPath)
	{
		this.bathPath = bathPath;
	}

}
