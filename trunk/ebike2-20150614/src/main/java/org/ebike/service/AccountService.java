package org.ebike.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.ebike.domian.RentOrder;
import org.ebike.entity.Account;
import org.ebike.entity.SmsHistory;
import org.ebike.tools.DateTool;
import org.ebike.tools.MD5;
import org.ebike.tools.RandTool;
import org.ebike.tools.SMSTool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @ClassName: AccountService 
 * @Description: 账号管理业务类
 * @author hzjintianfan
 * @date 2014-7-23 下午6:46:44
 */
@Service
@Transactional
public class AccountService extends BaseService
{
	/**
	 * 判断账号是否有效
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean isValidAccount(String username, String password)
	{
		Account account = (Account) baseDao.queryUnique("select a from Account a where a.username=?", username);
		if (account == null)
			return false;
		if (username.equals(account.getUsername()) && password.equals(account.getPassword()))
			return true;
		return false;
	}

	/**
	 * 根据账号名获取账号
	 * @param username
	 * @return
	 */
	public Account getAccountByUsername(String username)
	{
		return (Account) baseDao.queryUnique("select a from Account a where a.username=?", username);
	}

	/**
	 * 判断手机号是否有效
	 * @param phone
	 * @return
	 */
	public boolean isValidPhoneNumber(String phone)
	{
		if (phone == null || phone.isEmpty())
			return false;
		return phone.matches("^(13|15|17|18)\\d{9}$");
	}

	/**
	 * 判断手机号是否已存在
	 * @param phone
	 * @return
	 */
	public boolean isPhoneNumberExist(String phone)
	{
		if (phone == null || phone.isEmpty())
			return true;
		List list = baseDao.query("select a from Account a where a.username=?", phone);
		if (list.size() > 0)
			return true;
		else
			return false;
	}

	/**
	 * 判断密码格式是否符合规范
	 * @param password
	 * @return
	 */
	public boolean validatePassword(String password)
	{
		Pattern p = Pattern.compile("^[A-Za-z0-9]{6,20}$");
		Matcher m = p.matcher(password);
		return m.matches();
	}

	/**
	 * 判断该号码能否发送注册码(注册码每个号码1天不得超过20条)
	 * @param phone
	 * @return
	 */
	public boolean canSendSMS(String phone)
	{
		Date today = DateTool.getTodayDate();
		long count = baseDao.count("select count(*) from SmsHistory s where s.phone=? and s.date>?", phone, today);
		if (count > 20)
			return false;
		else
			return true;
	}

	/**
	 * 发送短信验证码
	 * @param session
	 */
	public void sendSMS(HttpSession session, String phone)
	{
		String code = RandTool.rand(6);
		boolean result = SMSTool.sendCode(phone, code);
		session.setAttribute("phone", phone);
		session.setAttribute("code", code);
		session.setAttribute("codeExpireDate", new Date(new Date().getTime() + 10 * 60 * 1000));
		//插入发送记录
		SmsHistory smsHistory = new SmsHistory();
		smsHistory.setDate(new Date());
		smsHistory.setPhone(phone);
		smsHistory.setText("发送验证码:" + code);
		if (result)
		{
			smsHistory.setStatus(0);
		}
		else
		{
			smsHistory.setStatus(1);
		}
		baseDao.add(smsHistory);
	}

	/**
	 * 检查验证码是否正确
	 * @param code
	 * @return
	 */
	public boolean verifyCode(HttpSession session, String code)
	{
		if (code == null || code.isEmpty())
			return false;
		Object realCode = session.getAttribute("code");
		Object expireDate = session.getAttribute("codeExpireDate");
		if (realCode != null && expireDate != null && realCode.toString().equals(code)
				&& ((Date) expireDate).getTime() > new Date().getTime())
			return true;
		return false;
	}

	/**
	 * 创建账号
	 * @param phone 手机号
	 * @param password 密码
	 * @return
	 */
	public String createAccount(String phone, String password)
	{
		Account account = new Account();
		account.setPassword(MD5.getMD5(password));
		account.setUsername(phone);
		account.setType(0);
		account.setDate(new Date());
		return baseDao.add(account);
	}

	/**
	 * 修改个人信息
	 * @param nickname 昵称
	 * @param render
	 * @param photoPath 
	 */
	public void modifyAccount(String accountId, String nickname, String render, String photoPath)
	{
		Account account = baseDao.query(Account.class, accountId);
		account.setNickname(nickname);
		account.setPhoto(photoPath);
		account.setRender(render);
		baseDao.modify(account);
	}

	/**
	 * 修改昵称
	 * @param accountId
	 * @param nickname
	 */
	public void modifyNickname(String accountId, String nickname)
	{
		Account account = baseDao.query(Account.class, accountId);
		account.setNickname(nickname);
		baseDao.modify(account);
	}

	/**
	 * 修改性别
	 * @param accountId
	 * @param render
	 */
	public void modifyRender(String accountId, String render)
	{
		Account account = baseDao.query(Account.class, accountId);
		account.setRender(render);
		baseDao.modify(account);
	}

	/**
	 * 获取我的订单列表
	 * @param accountId
	 * @param status
	 * @return
	 */
	public List<RentOrder> getMyOrders(String accountId, Integer status, Integer page, Integer pageSize)
	{
		Map params = new HashMap();
		params.put("accountId", accountId);
		params.put("status", status);
		List list = null;
		if (status == -1)
		{
			list = baseDao
					.query("select p.id,p.name,p.location,p.phone,o.description,p.photo,o.price,o.status,o.id from Order o left join o.account a left join o.provider p where a.id=:accountId ",
							params, page * pageSize, pageSize);
		}
		else
		{
			list = baseDao
					.query("select p.id,p.name,p.location,p.phone,o.description,p.photo,o.price,o.status,o.id from Order o left join o.account a left join o.provider p where a.id=:accountId and o.status=:status",
							params, page * pageSize, pageSize);
		}
		List<RentOrder> rentOrders = new ArrayList<RentOrder>();
		for (Object item : list)
		{
			RentOrder r = new RentOrder();
			Object[] o = (Object[]) item;
			r.setProviderId(o[0].toString());
			r.setProviderName(o[1].toString());
			r.setProviderLocation(o[2].toString());
			r.setProviderPhone(o[3].toString());
			r.setDescription(o[4].toString());
			if (o[5] != null)
			{
				r.setProviderPhoto(o[5].toString());
			}
			r.setPrice(Double.valueOf(o[6].toString()));
			r.setStatus(Integer.valueOf(o[7].toString()));
			r.setOrderId(o[8].toString());
			rentOrders.add(r);
		}
		return rentOrders;
	}

	/**
	 * 获取我的订单总数
	 * @param accountId
	 * @param status
	 * @return
	 */
	public long getMyOrdersCount(String accountId, Integer status)
	{
		Map params = new HashMap();
		params.put("accountId", accountId);
		params.put("status", status);
		long total = 0;
		if (status == -1)
		{
			total = baseDao
					.count("select p.id,p.name,p.location,p.phone,o.description,p.photo,o.price,o.status from Order o left join o.account a left join o.provider p where a.id=:accountId",
							params);
		}
		else
		{
			total = baseDao
					.count("select p.id,p.name,p.location,p.phone,o.description,p.photo,o.price,o.status from Order o left join o.account a left join o.provider p where a.id=:accountId and o.status=:status",
							params);
		}
		return total;
	}

}
