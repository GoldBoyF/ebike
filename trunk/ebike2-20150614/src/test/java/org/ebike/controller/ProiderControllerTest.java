package org.ebike.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.ebike.tools.MyHttpClient;
import org.junit.Before;
import org.junit.Test;

public class ProiderControllerTest
{

	protected MyHttpClient client;
	public static String DOMAIN = "http://localhost:8080/ebike";

	@Before
	public void setUp()
	{
		client = new MyHttpClient(DOMAIN);
	}

	@Test
	public void testRecent()
	{
		Map map = new HashMap();
		JSONObject jsonObject = client.get("/providers/recent?lng=120.198859&lat=30.194096");
		System.out.println(jsonObject);
	}

	@Test
	public void testFilterZone()
	{
		Map map = new HashMap();
		JSONObject jsonObject = client.get("/providers/filter?lng=120.198859&lat=30.194096&order=1");
		System.out.println(jsonObject);
	}

	@Test
	public void testDetail()
	{
		Map map = new HashMap();
		JSONObject jsonObject = client.get("/providers/detail/aade7ca1-11ab-11e4-a45c-fa163e676a26");
		System.out.println(jsonObject);
	}
}
