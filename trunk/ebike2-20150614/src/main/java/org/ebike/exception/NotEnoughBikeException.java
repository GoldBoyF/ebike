package org.ebike.exception;

/**
 * 
 * @ClassName: NotEnoughBikeException 
 * @Description: 下单时吗，车辆不足时异常类
 * @author hzjintianfan
 * @date 2014-7-23 下午6:43:07
 */
public class NotEnoughBikeException extends Exception
{
	/**  */
	private static final long serialVersionUID = -7901388109415130485L;

	public NotEnoughBikeException()
	{

	}

	public NotEnoughBikeException(String msg)
	{
		super(msg);
	}
}
