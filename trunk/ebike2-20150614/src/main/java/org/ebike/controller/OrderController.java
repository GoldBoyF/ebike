package org.ebike.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.ebike.exception.NotEnoughBikeException;
import org.ebike.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @ClassName: OrderController 
 * @Description: 订单管理控制器
 * @author hzjintianfan
 * @date 2014-7-23 下午5:39:05
 */
@Controller
@RequestMapping(value = "/orders")
public class OrderController extends BaseController
{

	@Autowired
	private OrderService orderService;

	/**
	 * 订车
	 * @param id 自行车ID
	 * @param num 订购数量
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	@RequestMapping(value = "/book", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Object book(@RequestParam(required = false) String details, Double price, Date startDate, Date endDate,
			HttpSession session)
	{
		Map data = new HashMap();
		String accounId = session.getAttribute("accountId").toString();
		if (startDate.getTime() > endDate.getTime())
		{
			data.put("success", false);
			data.put("msg", "开始时间不能小于结束时间");
			return data;
		}
		try
		{
			orderService.bookBike(accounId, details, price, startDate, endDate);
			data.put("success", true);
		}
		catch (NotEnoughBikeException e)
		{
			data.put("success", false);
			data.put("msg", "车辆不足");
			e.printStackTrace();
		}
		catch (Exception e)
		{
			data.put("success", false);
			data.put("msg", "订车失败");
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * SpringMVC接收时间参数格式化处理
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		dateFormat.setLenient(true); //为false时无法解析到时分秒
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
