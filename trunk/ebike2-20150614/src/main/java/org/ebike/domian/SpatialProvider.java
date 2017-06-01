package org.ebike.domian;

/**
 * 
 * @ClassName: SpatialProvider 
 * @Description: 商家信息
 * @author hzjintianfan
 * @date 2014-7-23 下午6:39:58
 */
public class SpatialProvider
{
	private String id;
	private String name;
	private String location;
	private Double lng;
	private Double lat;
	private Double grade;
	private Double distance;
	private Double low;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public Double getLng()
	{
		return lng;
	}

	public void setLng(Double lng)
	{
		this.lng = lng;
	}

	public Double getLat()
	{
		return lat;
	}

	public void setLat(Double lat)
	{
		this.lat = lat;
	}

	public Double getGrade()
	{
		return grade;
	}

	public void setGrade(Double grade)
	{
		this.grade = grade;
	}

	public Double getDistance()
	{
		return distance;
	}

	public void setDistance(Double distance)
	{
		this.distance = distance;
	}

	public Double getLow()
	{
		return low;
	}

	public void setLow(Double low)
	{
		this.low = low;
	}

}
