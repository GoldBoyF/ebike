package org.ebike.tools;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @ClassName: RequestIP 
 * @Description: 请求IP处理工具类
 * @author hzjintianfan
 * @date 2014-7-23 下午7:27:04
 */
public class RequestIP
{

	/**
	 * 获取当前请求的原始IP
	 * @param request 当前请求的request对象
	 * @return ip 当前请求的IP
	 */
	public static String getRequestIp(HttpServletRequest request)
	{
		if (request == null)
			return null;
		else
		{
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getRemoteAddr();
			}
			if (ip.equals("localhost") || ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1"))
			{
				try
				{
					InetAddress addr = InetAddress.getLocalHost();
					ip = addr.getHostAddress();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			return ip;
		}
	}

	/**
	 * 将IP中的一段转化为最大的三位数
	 * @param ipSection IP中的一段(xxx、*、x?等形式，不包含"-")
	 * @return number 可能取到的最大值
	 */
	private static int getMaxNumber(String ipSection)
	{
		if (ipSection == null)
			return 0;
		else if (ipSection.equals("*"))
			return 255;
		else
		{
			char[] tmp = ipSection.toCharArray();
			if (tmp.length == 3 && tmp[0] == '?')
			{
				tmp[0] = '2';
			}
			for (int i = 0; i < tmp.length; i++)
			{// 把其它位'?'替换为9
				if (tmp[i] == '?')
				{
					tmp[i] = '9';
				}
			}
			String number = new String(tmp);
			return Integer.parseInt(number);
		}
	}

	/**
	 * 将IP中的一段转化为最小的三位数
	 * @param ipSection IP中的一段(xxx、*、x?等形式，不包含"-")
	 * @return number 可能取到的最小值
	 */
	public static int getMinNumber(String ipSection)
	{
		if (ipSection == null)
			return 0;
		else if (ipSection.equals("*"))
			return 0;
		else
		{
			char[] tmp = ipSection.toCharArray();
			for (int i = 0; i < tmp.length; i++)
			{// 把其它位'?'替换为0
				if (tmp[i] == '?')
				{
					tmp[i] = '0';
				}
			}
			String number = new String(tmp);
			return Integer.parseInt(number);
		}
	}

	/**
	 * 判断指定的judgeIp是否满足判定条件requiredIp
	 * @param requiredIp 判断条件(127.0.0.1)，每段可以是具体数字(xxx)，数字区间(xxx-xxx)，数字可以用(*、?)表示通配
	 * @param judgeIp 待判断的IP(127.0.0.1)，每段必须是1-3位数字
	 * @return true or false(在/不在指定的IP段内)
	 */
	public static boolean checkIp(String requiredIp, String judgeIp)
	{
		boolean match = true;
		if (requiredIp == null || judgeIp == null)
			return false;
		else
		{
			/**
			 * requiredIp是从数据库中读出来的，其合法性已由输入模块校验
			 * judgeIP是从request对象中获取的，其合法性已由web标准校验
			 * 此处不再对IP的合法性进行判断，一旦由于IP抛异常，则认为IP
			 * 为手动传入的不合法IP
			 */
			try
			{
				String[] judgeIpSection = judgeIp.split("\\.");
				String[] requiredIpSection = requiredIp.split("\\.");
				/**
				 * 每段都是由"-"分隔的1到3位数字，每位数字可以
				 * 用一个"?"替代，或者3位数字用一个"*"替代。
				 * 每一段分别比较，只有当每段都在指定的范围时，
				 * 才可认为judgeIp在requiredIp段内
				 */
				for (int i = 0; i < 4; i++)
				{
					int max = 0;// 本段可能取到的最大值
					int min = 0;// 本段可能取到的最小值

					String[] tmp = requiredIpSection[i].split("-");
					if (tmp.length == 2)
					{// 如果是区段
						min = getMinNumber(tmp[0]);
						max = getMaxNumber(tmp[1]);
					}
					else
					{// 如果不是区段
						min = getMinNumber(tmp[0]);
						max = getMaxNumber(tmp[0]);
					}

					int num = Integer.parseInt(judgeIpSection[i]);// 待判定IP此段对应的值
					if (!(num >= min && num <= max))
					{// 一旦不在此范围内，则该IP不在判定的区段内
						match = false;
					}
				}
			}
			catch (Exception e)
			{
				match = false;
				e.printStackTrace();
			}
			return match;
		}
	}

	/**
	 * 判断指定的judgeIp是否满足判定条件requiredIp
	 * @param requiredIp 判断条件数组(127.0.0.1)，每段可以是具体数字(xxx)，数字区间(xxx-xxx)，数字可以用(*、?)表示通配
	 * @param judgeIp 待判断的IP(127.0.0.1)，每段必须是1-3位数字
	 * @return true or false(在/不在指定的IP段内)
	 */
	public static boolean checkIp(String[] requiredIp, String judgeIp)
	{
		boolean match = true;
		if (requiredIp == null || judgeIp == null)
			return false;
		else
		{
			for (String item : requiredIp)
			{
				match = checkIp(item, judgeIp);
				if (match)
				{// judgeIp只要满足数组中的一条，就可以了
					break;
				}
			}
			return match;
		}
	}
}