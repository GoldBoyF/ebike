package org.ebike.controller;

import javax.servlet.http.HttpSession;

import org.ebike.dao.HibernateBaseDao;
import org.ebike.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @ClassName: BaseController 
 * @Description: 控制器基类
 * @author hzjintianfan
 * @date 2014-7-23 下午5:28:48
 */
public class BaseController
{
	@Autowired
	protected HibernateBaseDao baseDao;

	/**
	 * 根据sessionId获取账号对象
	 * @param session
	 * @return
	 */
	protected Account getSessionAccount(HttpSession session)
	{
		Object accountId = session.getAttribute("accountId");
		return baseDao.query(Account.class, accountId.toString());
	}
}
