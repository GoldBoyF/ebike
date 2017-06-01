package org.ebike.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebike.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @ClassName: ProviderController 
 * @Description: 商家管理控制器
 * @author hzjintianfan
 * @date 2014-7-23 下午5:40:17
 */
@Controller
@RequestMapping(value = "/providers")
public class ProviderController extends BaseController
{
	@Autowired
	private ProviderService providerService;

	/**
	 * 查找附近的租车点
	 * @param lng 经度
	 * @param lat 纬度
	 * @return 
	 */
	@RequestMapping(value = "/recent", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Object recent(@RequestParam Double lng, @RequestParam Double lat)
	{
		Map data = new HashMap();
		List list = providerService.getRecentProviders(lng, lat);
		data.put("list", list);
		data.put("total", list.size());
		return data;
	}

	/**
	 * 租车点列表过滤器
	 * @param city 所选城市
	 * @param zone 所在区
	 * @param bikeType 自行车类型
	 * @param order 排序顺序
	 * @return
	 */
	@RequestMapping(value = "/filter", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Object filter(@RequestParam(required = false) Double lng, Double lat, String zone, String bikeType,
			Integer order)
	{
		System.out.println("bikeType:" + bikeType);
		Map data = new HashMap();
		List list = providerService.getFilteredProviders(lng, lat, zone, bikeType, order);
		data.put("list", list);
		data.put("total", list.size());
		return data;
	}

	/**
	 * 查看租车点详情
	 * @param id 租车点ID
	 * @return 
	 */
	@RequestMapping(value = "/detail/{id}", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Object detail(@PathVariable String id)
	{
		return providerService.getProvideDetail(id);
	}
}
