package org.ebike.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;

public class SpotControllerTest extends BaseTest
{
	@Test
	public void testlist()
	{
		Map map = new HashMap();
		JSONObject jsonObject = client.get("/spots?start=0&limit=100");
		System.out.println(jsonObject);
	}

	@Test
	public void testdetail()
	{
		Map map = new HashMap();
		JSONObject jsonObject = client.get("/spots/001");
		System.out.println(jsonObject);
	}
}
