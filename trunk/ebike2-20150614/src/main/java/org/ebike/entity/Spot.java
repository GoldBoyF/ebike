package org.ebike.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @ClassName: Spot 
 * @Description: 景点
 * @author hzjintianfan
 * @date 2014-7-23 下午6:42:46
 */
@Entity
@Table(name = "T_SPOT")
public class Spot
{
	@Id
	@Column(name = "C_ID", length = 40)
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String id;

	@Column(name = "C_NAME")
	private String name;

	@Column(name = "C_ADDRESS")
	private String address;

	@Column(name = "C_PHOTO")
	private String photo;

	@Column(name = "C_MAP")
	private String map;

	@Column(name = "C_LNG")
	private Double lng;

	@Column(name = "C_LAT")
	private Double lat;

	@Column(name = "C_INTRODUCTION", length = 4000)
	private String introduction;

	@Column(name = "C_DATE")
	private Date date;

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

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getPhoto()
	{
		return photo;
	}

	public void setPhoto(String photo)
	{
		this.photo = photo;
	}

	public String getMap()
	{
		return map;
	}

	public void setMap(String map)
	{
		this.map = map;
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

	public String getIntroduction()
	{
		return introduction;
	}

	public void setIntroduction(String introduction)
	{
		this.introduction = introduction;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

}
