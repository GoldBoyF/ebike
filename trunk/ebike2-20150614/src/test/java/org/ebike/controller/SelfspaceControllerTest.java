package org.ebike.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

public class SelfspaceControllerTest extends BaseTest
{

	@Test
	public void testModify()
	{
		Map map = new HashMap();
		map.put("nickname", "15982089793");
		map.put("render", "男");
		JSONObject jsonObject = client.post("/selfspace", map);
		Assert.assertTrue(jsonObject.getBoolean("success"));
	}

	@Test
	public void testView()
	{
		Map map = new HashMap();
		JSONObject jsonObject = client.get("/selfspace");
		System.out.println(jsonObject);
	}

	@Test
	public void resetPasswordFail()
	{
		Map map = new HashMap();
		map.put("oldPassword", "111111");
		map.put("newPassword", "666666");
		JSONObject jsonObject = client.post("/selfspace/password/reset", map);
		Assert.assertFalse(jsonObject.getBoolean("success"));
	}

	@Test
	public void resetPasswordSuccess()
	{
		Map map = new HashMap();
		map.put("oldPassword", "123456");
		map.put("newPassword", "123456");
		JSONObject jsonObject = client.post("/selfspace/password/reset", map);
		Assert.assertTrue(jsonObject.getBoolean("success"));
	}

	@Test
	public void testSendOldPhoneVerificationCode()
	{
		Map map = new HashMap();
		JSONObject jsonObject = client.post("/selfspace/phone/oldPhone/sendCode", map);
		Assert.assertTrue(jsonObject.getBoolean("success"));
	}

	@Test
	public void testVerifyOldPhoneCode()
	{
		Map map = new HashMap();
		map.put("code", "000000");
		JSONObject jsonObject = client.post("/selfspace/phone/oldPhone/verification", map);
		Assert.assertTrue(jsonObject.getBoolean("success"));
	}

	@Test
	public void testGetRentOrders()
	{
		JSONObject jsonObject = client.get("/selfspace/rentOrders?status=-1&page=0&pageSize=100");
		System.out.println(jsonObject);
	}

	@Test
	public void testViewRentOrder()
	{
		JSONObject jsonObject = client.get("/selfspace/rentOrders/001");
		System.out.println(jsonObject);
	}

	@Test
	public void testEvalueOrder()
	{
		Map map = new HashMap();
		map.put("star", "4");
		map.put("comment", "李尚租车点服务好");
		JSONObject jsonObject = client.post("/selfspace/rentOrders/8a70a2114753bf7d014753bfbe4b0000/grade", map);
		Assert.assertTrue(jsonObject.getBoolean("success"));
	}
}
