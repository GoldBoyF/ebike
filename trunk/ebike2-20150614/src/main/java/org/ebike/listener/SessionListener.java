package org.ebike.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 
 * @ClassName: SessionListener 
 * @Description: session监听类
 * @author hzjintianfan
 * @date 2014-7-23 下午6:43:35
 */
public class SessionListener implements HttpSessionListener
{

	/* 监听session创建 */
	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent)
	{

	}

	/* 监听session销毁 */
	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent)
	{

	}

}
