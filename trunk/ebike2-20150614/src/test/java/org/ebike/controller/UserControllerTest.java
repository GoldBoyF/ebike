package org.ebike.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.ebike.tools.MyHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserControllerTest
{
	public static String DOMAIN = "http://localhost:8080/ebike";

	protected MyHttpClient client;

	@Before
	public void setUp()
	{
		client = new MyHttpClient(DOMAIN);
	}

	@Test
	public void testSendCode()
	{
		Map map = new HashMap();
		map.put("phone", "15926307076");
		JSONObject jsonObject = client.post("/user/phone/sendCode", map);
		Assert.assertTrue(jsonObject.getBoolean("success"));
	}

	@Test
	public void testVerifyPhoneCode()
	{
		Map map = new HashMap();
		map.put("code", "000000"); //根据短信获取
		JSONObject jsonObject = client.post("/user/phone/verification;", map);
		Assert.assertTrue(jsonObject.getBoolean("success"));
	}

	@Test
	public void testRegister()
	{
		Map map = new HashMap();
		map.put("password", "123456");
		JSONObject jsonObject = client.post("/user/register", map);
		Assert.assertTrue(jsonObject.getBoolean("success"));
	}

	@Test
	public void testSuit()
	{
		testSendCode();
		testVerifyPhoneCode();
		testRegister();
	}
}
