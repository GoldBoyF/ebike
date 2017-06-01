package org.ebike.controller;

import java.util.HashMap;

import org.junit.Test;

public class MainControllerTest extends BaseTest
{

	@Test
	public void testLogin()
	{
		System.out.println(client.post("/demos", new HashMap()));
	}

	@Test
	public void testBeat()
	{
		System.out.println(client.post("/beat", new HashMap()));
	}

}
