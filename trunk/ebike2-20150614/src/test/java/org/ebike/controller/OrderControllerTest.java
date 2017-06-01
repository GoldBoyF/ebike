package org.ebike.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;

public class OrderControllerTest extends BaseTest
{
	@Test
	public void testBook()
	{
		Map map = new HashMap();
		map.put("endDate", "2014-7-20 16:01:01");
		map.put("startDate", "2014-7-20 14:01:01");
		map.put("details", "84cc5e2e-121c-11e4-a95e-fa163e676a26:1");
		map.put("price", "180");
		JSONObject jsonObject = client.post("/orders/book", map);
		System.out.println(jsonObject);
	}
}
