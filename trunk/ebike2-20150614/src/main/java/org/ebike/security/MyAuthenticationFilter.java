package org.ebike.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @ClassName: MyAuthenticationFilter 
 * @Description: 自定义过滤器
 * @author hzjintianfan
 * @date 2014-7-23 下午6:45:12
 */
public class MyAuthenticationFilter implements Filter
{

	private MyAuthentication myAuthentication;// 自定义认证功能类

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String relativeUrl = request.getRequestURI();/* 以根开头的URL */
		String path = request.getContextPath();/* 获取客户端请求的上下文根 */

		/**
		 * 如果是登录请求，则进行登录认证。
		 * 如果是其它请求，则进行IP绑定匹配。
		 */
		if (relativeUrl.replaceAll(path, "").equals("/user/login"))
		{// 登录请求，登录认证			
			response.setContentType("application/json;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");

			if (request.getMethod().equals("POST"))
			{// 登录参数必须通过post方式传过来，security要求的
				int status = myAuthentication.getAuthenticationStatus(request);// 调用认证逻辑
				if (status == 0)
				{// 通过认证，则保存登录IP到session，并通过此过滤器
					chain.doFilter(request, response);
				}
				else
				{// 未通过认证，则拒绝登录，并返回登录页面提示相关信息
					req.getRequestDispatcher("/failLogin?error=" + status).forward(req, res);
				}
			}
			else
			{// 如果不是POST方式，则返回登录页面，并提示信息
				req.getRequestDispatcher("/failLogin?error=9").forward(req, res);
				//response.sendRedirect(path + "/user/login.jsp?error=9");// 登录必须用POST方式
			}
		}
		else
		{
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy()
	{
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	public void setMyAuthentication(MyAuthentication myAuthentication)
	{
		this.myAuthentication = myAuthentication;
	}
}
