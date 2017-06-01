package org.ebike.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.ebike.dao.HibernateBaseDao;
import org.ebike.entity.Spot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @ClassName: SpotController 
 * @Description: 景点控制器
 * @author hzjintianfan
 * @date 2014-7-23 下午6:34:13
 */
@Controller
@RequestMapping(value = "/spots")
public class SpotController
{
	@Autowired
	private HibernateBaseDao baseDao;

	/**
	 * 获取景点列表
	 * @param start 起始记录
	 * @param limit 返回数据集大小
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Object list(@RequestParam Integer start, Integer limit, HttpSession session)
	{
		Map data = new HashMap();
		data.put("list", baseDao.query("select s from Spot s order by s.date desc ", start, limit));
		data.put("total", baseDao.count("select s from Spot s order by s.date desc "));
		return data;
	}

	/**
	 * 根据景点ID获取景点详情
	 * @param id 景点ID
	 * @return
	 */
	@RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Object detail(@PathVariable String id)
	{
		Map data = new HashMap();
		Spot spot = baseDao.query(Spot.class, id);
		data.put("data", spot);
		return data;
	}
}
