package org.ebike.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.ebike.domian.RentOrder;
import org.ebike.entity.Account;
import org.ebike.service.AccountService;
import org.ebike.service.OrderService;
import org.ebike.tools.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 
 * @ClassName: SelfspaceController 
 * @Description: 个人中心控制器
 * @author hzjintianfan
 * @date 2014-7-23 下午6:28:39
 */
@Controller
@RequestMapping(value = "/selfspace")
public class SelfspaceController extends BaseController
{
	@Autowired
	private AccountService accountService;

	@Autowired
	private OrderService orderService;

	/**
	 * 个人信息设置
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object modify(@RequestParam String nickname, String render, MultipartFile photo, HttpSession session)
	{
		Map data = new HashMap();
		Object accountId = session.getAttribute("accountId");
		String photoPath = "";
		accountService.modifyAccount(accountId.toString(), nickname, render, photoPath);
		data.put("success", true);
		return data;
	}

	/**
	 * 设置昵称
	 * @param nickname 昵称
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/nickname", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object modifyNickname(@RequestParam String nickname, HttpSession session)
	{
		Map data = new HashMap();
		String accountId = session.getAttribute("accountId").toString();
		accountService.modifyNickname(accountId, nickname);
		data.put("success", true);
		return data;
	}

	/**
	 * 设置性别
	 * @param render 性别
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/render", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object modifyRender(@RequestParam String render, HttpSession session)
	{
		Map data = new HashMap();
		String accountId = session.getAttribute("accountId").toString();
		accountService.modifyRender(accountId, render);
		data.put("success", true);
		return data;
	}

	/**
	 * 设置照片
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/photo", method = RequestMethod.POST)
	@ResponseBody
	public Object modifyPhoto(MultipartHttpServletRequest request, HttpSession session)
	{
		Map data = new HashMap();
		String accountId = session.getAttribute("accountId").toString();
		//1. build an iterator
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		//2. get each file
		while (itr.hasNext())
		{
			mpf = request.getFile(itr.next());
			try
			{
				String filePath = accountService.copyFileToDestDir(mpf.getInputStream(), mpf.getOriginalFilename(),
						"/storage/person");
				Account account = baseDao.query(Account.class, accountId);
				account.setPhoto(filePath);
				baseDao.modify(account);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		data.put("success", true);
		return data;
	}

	/**
	 * 个人信息查看
	 * @return
	 */
	@RequestMapping(value = "", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Object view(HttpSession session)
	{
		Map data = new HashMap();
		Account account = getSessionAccount(session);
		data.put("nickname", account.getNickname());
		data.put("render", account.getRender());
		data.put("photo", account.getPhoto());
		return data;
	}

	/**
	 * 密码重置
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/password/reset", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object resetPassword(@RequestParam String oldPassword, String newPassword, HttpSession session)
	{
		Map data = new HashMap();
		Account account = getSessionAccount(session);
		if (account.getPassword().equals(MD5.getMD5(oldPassword)))
		{
			account.setPassword(MD5.getMD5(newPassword));
			baseDao.modify(account);
			data.put("success", true);
		}
		else
		{
			data.put("success", false);
		}
		return data;
	}

	/**
	 * 向旧手机发送验证码
	 * @return
	 */
	@RequestMapping(value = "/phone/oldPhone/sendCode", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object sendOldPhoneVerificationCode(HttpSession session)
	{
		Map data = new HashMap();
		Account account = getSessionAccount(session);
		if (accountService.canSendSMS(account.getUsername()))
		{
			accountService.sendSMS(session, account.getUsername());
			data.put("success", true);
		}
		else
		{
			data.put("success", false);
			data.put("msg", 1);//
		}
		return data;
	}

	/**
	 * 验证旧手机号码的验证码
	 * @param code 验证码
	 * @return
	 */
	@RequestMapping(value = "/phone/oldPhone/verification", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object verifyOldPhoneCode(@RequestParam String code, HttpSession session)
	{
		Map data = new HashMap();
		if (accountService.verifyCode(session, code))
		{
			data.put("success", true);
		}
		else
		{
			data.put("success", false);
		}
		return data;
	}

	/**
	 * 向新手机发送验证码
	 * @param phone 新手机号
	 * @return
	 */
	@RequestMapping(value = "/phone/newPhone/sendCode", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object sendNewPhoneVerificationCode(@RequestParam String phone, HttpSession session)
	{
		Map data = new HashMap();
		if (accountService.isValidPhoneNumber(phone))
		{
			if (accountService.isPhoneNumberExist(phone))
			{
				data.put("success", false);
				data.put("msg", 0); //号码被注册
			}
			else
			{
				if (accountService.canSendSMS(phone))
				{
					accountService.sendSMS(session, phone);
				}
				else
				{
					data.put("success", false);
					data.put("msg", 1); //限制发送
				}

			}
		}
		else
		{
			data.put("success", false);
			data.put("msg", 2); //不是手机号
		}
		return data;
	}

	/**
	 * 验证新手机号码的验证码，手机号修改成功
	 * @return
	 */
	@RequestMapping(value = "/phone/newPhone/verification", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object verifyNewPhoneCode(@RequestParam String code, HttpSession session)
	{
		Map data = new HashMap();
		if (accountService.verifyCode(session, code))
		{
			Account account = getSessionAccount(session);
			Object newPhone = session.getAttribute("phone");
			account.setUsername(newPhone.toString());
			baseDao.modify(account);
			data.put("success", true);
		}
		else
		{
			data.put("success", false);
		}
		return data;
	}

	/**
	 * 查看我的所有订单（只显示最近50条）
	 * @param status 订单状态
	 * @param page 当前页
	 * @param pageSize 每页显示记录数
	 * @return
	 */
	@RequestMapping(value = "/rentOrders", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Object getRentOrders(@RequestParam Integer status, Integer page, Integer pageSize, HttpSession session)
	{
		Map data = new HashMap();
		Object accountId = session.getAttribute("accountId");
		List list = accountService.getMyOrders(accountId.toString(), status, page, pageSize);
		long total = accountService.getMyOrdersCount(accountId.toString(), status);
		data.put("total", total);
		data.put("list", list);
		return data;
	}

	/**
	 * 我的订单详情
	 * @param id 订单ID
	 * @return
	 */
	@RequestMapping(value = "/rentOrders/{id}", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Object viewRentOrder(@PathVariable String id)
	{
		Map data = new HashMap();
		RentOrder rentOrder = orderService.getRentOrder(id);
		data.put("data", rentOrder);
		return data;
	}

	/**
	 * 订单评价
	 * @param id 订单ID
	 * @param star 评分(1到5)
	 * @param comment 评论
	 * @return
	 */
	@RequestMapping(value = "/rentOrders/{id}/grade", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object evalueOrder(@PathVariable String id, @RequestParam(required = false) Integer star, String comment)
	{
		Map data = new HashMap();
		orderService.evaluteOrder(id, star, comment);
		data.put("success", true);
		return data;
	}

}
