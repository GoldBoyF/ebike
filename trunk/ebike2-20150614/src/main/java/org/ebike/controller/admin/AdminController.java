package org.ebike.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @ClassName: AdminController 
 * @Description: 后台管理控制器
 * @author hzjintianfan
 * @date 2014-7-23 下午6:38:19
 */
@Controller
@RequestMapping(value = "/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController
{

	/**
	 * 跳转至后台管理主页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/main")
	public ModelAndView mainPage(HttpServletRequest request, HttpServletResponse response)
	{
		return new ModelAndView("/admin/main");
	}

}
