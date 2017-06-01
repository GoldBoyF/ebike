package org.ebike.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.ebike.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @ClassName: UserController 
 * @Description: 用户控制器
 * @author hzjintianfan
 * @date 2014-7-23 下午6:36:54
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController
{
	@Autowired
	AccountService accountService;

	/**
	 * 发送用户注册验证码
	 * @param phone
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/phone/sendCode", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object sendCode(@RequestParam String phone, HttpSession session)
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
					data.put("sid", session.getId());
					data.put("success", true);
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
	 * 校验短信验证码
	 * @param code 短信验证码
	 * @param session 
	 * @return
	 */
	@RequestMapping(value = "/phone/verification", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object verifyPhoneCode(@RequestParam String code, HttpSession session)
	{
		Map data = new HashMap();
		if (accountService.verifyCode(session, code))
		{
			data.put("success", true);
			data.put("sid", session.getId());
		}
		else
		{
			data.put("success", false);
		}
		return data;
	}

	/**
	 * 用户注册
	 * @param password 密码
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/register", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object register(@RequestParam String password, HttpSession session)
	{
		Map data = new HashMap();
		Object phone = session.getAttribute("phone");
		if (phone != null)
		{
			accountService.createAccount(phone.toString(), password);
			data.put("success", true);
		}
		else
		{
			data.put("success", false);
		}
		return data;
	}

}
