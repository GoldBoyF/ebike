package org.ebike.tools;

import javax.servlet.ServletContext;

import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * @ClassName: SpringBean 
 * @Description: SpringBean管理工具类
 * 
 * @author hzjintianfan
 * @date 2014-7-23 下午7:29:29
 */
public class SpringBean
{
	public static ServletContext sc;

	/**
	 * 获取spring中bean的实例
	 * @param beanName
	 * @param sc
	 * @return
	 */
	public static Object getBean(String beanName, ServletContext sc)
	{
		return WebApplicationContextUtils.getWebApplicationContext(sc).getBean(beanName);
	}

	/**
	 * 获取spring中bean的实例
	 * @param beanName
	 * @param sc
	 * @return
	 */
	public static Object getBean(String beanName)
	{
		return getBean(beanName, sc);
	}

}
