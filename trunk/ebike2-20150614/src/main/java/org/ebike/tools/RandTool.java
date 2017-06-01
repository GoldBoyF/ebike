package org.ebike.tools;

/**
 * 
 * @ClassName: RandTool 
 * @Description: 随机数生成工具类
 * @author hzjintianfan
 * @date 2014-7-23 下午7:25:41
 */
public class RandTool
{

	/**
	 * 生成N位随机数字
	 * @param num
	 * @return
	 */
	public static String rand(Integer num)
	{
		String result = "";
		for (int i = 0; i < num; i++)
		{
			result += (int) (Math.random() * 10);
		}
		return result;
	}

}
