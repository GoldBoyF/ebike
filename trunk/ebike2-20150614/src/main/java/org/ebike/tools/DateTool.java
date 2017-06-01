package org.ebike.tools;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @ClassName: DateTool 
 * @Description: 日期时间工具类
 * @author hzjintianfan
 * @date 2014-7-23 下午7:06:33
 */
public class DateTool
{

	/**
	 * 获取今日凌晨时间
	 * @return
	 */
	public static Date getTodayDate()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

}
