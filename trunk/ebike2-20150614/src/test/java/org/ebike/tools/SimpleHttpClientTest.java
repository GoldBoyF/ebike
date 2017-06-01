package org.ebike.tools;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SimpleHttpClientTest
{

	@Test
	public void testUnLoginPort()
	{
		String domain = "http://localhost:8080/ebike";
		SimpleHttpClient client = new SimpleHttpClient(domain);
		System.out.println(client.get("/providers/detail/007"));
	}

	@Test
	public void testLogin()
	{
		String domain = "http://etotech.net:5000/ebike";
		SimpleHttpClient client = new SimpleHttpClient(domain);
		Map map = new HashMap();
		map.put("j_username", "15900000000");
		map.put("j_password", "123456");
		System.out.println(client.post("/user/login", map)); //9807FD77BB10F945B04D911373D0CBD9
	}

	@Test
	public void testSelfspace()
	{
		String domain = "http://localhost:8080/ebike";
		String sid = "C876AE6780E19D0F30CE3CB195310E46";
		SimpleHttpClient client = new SimpleHttpClient(domain, sid);
		System.out.println(client.get("/selfspace"));
	}

	@Test
	public void testUpload()
	{
		String domain = "http://etotech.net:5000/ebike";
		String sid = "9807FD77BB10F945B04D911373D0CBD9";
		SimpleHttpClient client = new SimpleHttpClient(domain, sid);
		//System.out.println(client.postFile("/selfspace/photo","D:\\3.png"));
	}
}
