package org.ebike.controller;

import org.ebike.bean.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @ClassName: DemoController 
 * @Description: Restful URL风格测试类（与工程业务无关）
 * @author hzjintianfan
 * @date 2014-7-23 下午5:29:45
 */
@Deprecated
@Controller
@RequestMapping(value = "/demos")
public class DemoController extends BaseController
{

	@RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.DELETE)
	@ResponseBody
	public Object delete(@PathVariable String id)
	{
		JsonData jsonData = new JsonData();
		jsonData.msg = "delete";
		return jsonData;
	}

	@RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Object view(@PathVariable String id)
	{
		JsonData jsonData = new JsonData();
		jsonData.msg = "view";
		return jsonData;
	}

	@RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.PUT)
	@ResponseBody
	public Object modify(@PathVariable String id)
	{
		JsonData jsonData = new JsonData();
		jsonData.msg = "modify";
		return jsonData;
	}

	@RequestMapping(value = "", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object add()
	{
		JsonData jsonData = new JsonData();
		jsonData.msg = "add";
		return jsonData;
	}

	@RequestMapping(value = "", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Object list()
	{
		System.out.println("assdasdadasdad");
		JsonData jsonData = new JsonData();
		jsonData.msg = "list";
		return jsonData;
	}
}
