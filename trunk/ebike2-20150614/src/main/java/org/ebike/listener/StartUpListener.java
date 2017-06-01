package org.ebike.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.ebike.bean.Application;
import org.ebike.tools.SpringBean;

/**
 * 
 * @ClassName: StartUpListener 
 * @Description: 应用器启动监听类
 * @author hzjintianfan
 * @date 2014-7-23 下午6:44:03
 */
public class StartUpListener implements ServletContextListener
{

	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		SpringBean.sc = event.getServletContext();
		Application application = (Application) SpringBean.getBean("application");
		application.setBathPath(SpringBean.sc.getRealPath("/"));
	}

	@Override
	public void contextDestroyed(ServletContextEvent event)
	{

	}

}
