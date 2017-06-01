package org.ebike.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ebike.bean.JsonData;
import org.ebike.entity.Account;
import org.ebike.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @ClassName: MainController 
 * @Description: 主页面控制器
 * @author hzjintianfan
 * @date 2014-7-23 下午5:35:35
 */
@Controller
@RequestMapping(value = "/")
public class MainController extends BaseController
{
	@Autowired
	private BaseService baseService;

	@RequestMapping(value = "main")
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response)
	{
		return new ModelAndView("main");
	}

	/**
	 * 心跳请求
	 * @return
	 */
	@RequestMapping(value = "beat", produces = "application/json")
	@ResponseBody
	public Object beat()
	{
		return new JsonData();
	}

	/**
	 * 登陆成功后返回JSON
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "dispatch", produces = "application/json")
	@ResponseBody
	public Object dispatch(HttpServletRequest request, HttpSession session)
	{
		Map map = new HashMap();
		map.put("success", true);
		map.put("sid", session.getId());

		Account account = getSessionAccount(session);
		map.put("nickname", account.getNickname());
		map.put("render", account.getRender());
		map.put("photo", account.getPhoto());
		return map;
	}

	/**
	 * 登录失败后返回JSON
	 * @param error
	 * @return
	 */
	@RequestMapping(value = "failLogin", produces = "application/json")
	@ResponseBody
	public Object failLogin(@RequestParam String error)
	{

		JsonData jsonData = new JsonData();
		jsonData.success = false;
		jsonData.msg = error;
		return jsonData;
	}

}
