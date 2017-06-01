package org.ebike.tools;

/**
 * 
 * @ClassName: SpatialData 
 * @Description: 空间数据工具类
 * @author hzjintianfan
 * @date 2014-7-23 下午7:28:25
 */
public class SpatialData
{
	private String id;
	private String address;
	private double x;
	private double y;

	public SpatialData()
	{

	}

	public SpatialData(String id, String address, double x, double y)
	{
		super();
		this.id = id;
		this.address = address;
		this.x = x;
		this.y = y;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public double getX()
	{
		return x;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y)
	{
		this.y = y;
	}

}
