package org.ebike.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.ebike.tools.MyHttpClient;
import org.junit.Assert;
import org.junit.Before;

public class BaseTest
{
	public static String DOMAIN = "http://localhost:8080/ebike";

	protected MyHttpClient client;

	@Before
	public void setUp()
	{
		client = new MyHttpClient(DOMAIN);
		Map params = new HashMap();
		params.put("j_username", "15926307076");
		params.put("j_password", "123456");
		JSONObject jsonObject = client.post("/user/login", params);
		Assert.assertTrue(jsonObject.getBoolean("success"));
	}
}
