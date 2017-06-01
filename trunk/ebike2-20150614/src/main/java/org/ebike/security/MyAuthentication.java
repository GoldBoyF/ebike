package org.ebike.security;

import javax.servlet.http.HttpServletRequest;

import org.ebike.dao.HibernateBaseDao;
import org.ebike.entity.Account;
import org.ebike.service.AccountService;
import org.ebike.tools.MD5;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @ClassName: MyAuthentication 
 * @Description: 自定义认证逻辑，供认证过滤器MyAuthenticationFilter调用
 * @author hzjintianfan
 * @date 2014-7-23 下午6:44:30
 */
public class MyAuthentication
{

	@Autowired
	protected AccountService accountService;

	@Autowired
	protected HibernateBaseDao baseDao;

	/**
	 * 获取授权状态
	 * @param request
	 * @return 状态吗
	 */
	public int getAuthenticationStatus(HttpServletRequest request)
	{
		// 获得请求的参数		
		String username = request.getParameter("j_username");//此处必须使用j_username，在spring框架内部定义了一个常量
		String password = request.getParameter("j_password");

		if (username == null || username.trim().isEmpty())
			return 1;// 用户名为空

		if (password == null || password.isEmpty())
			return 2;// 密码为空

		password = MD5.getMD5(password);
		if (!accountService.isValidAccount(username, password))
			return 4; //用户名或密码错误

		Account account = accountService.getAccountByUsername(username);
		request.getSession().setAttribute("accountId", account.getId());

		return 0; //认证通过
	}
}
