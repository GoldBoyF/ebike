package org.ebike.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: JsonData 
 * @Description: 标准请求JSON返回通用格式
 * @author hzjintianfan
 * @date 2014-7-23 下午5:21:07
 */
public class JsonData
{
	public boolean success = true; //业务请求是否成功
	public Map data = new HashMap();
	public Map errors = new HashMap();
	public String msg;

	public JsonData()
	{

	}

	public JsonData(boolean success)
	{
		this.success = success;
	}
}
