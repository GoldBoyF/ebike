package org.ebike.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @ClassName: ErrorController 
 * @Description: 错误请求处理控制器
 * @author hzjintianfan
 * @date 2014-7-23 下午5:34:07
 */
@Controller
@RequestMapping("/error")
public class ErrorController
{

	/**
	 * 权限出错处理页面
	 * @return
	 */
	@RequestMapping("/right")
	public String right()
	{
		return "error/right";
	}

	/**
	 * 未知异常处理页面
	 * @return
	 */
	@RequestMapping("/unknown")
	public String unknown()
	{
		return "error/unknown";
	}

}
