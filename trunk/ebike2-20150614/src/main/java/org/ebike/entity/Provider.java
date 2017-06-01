package org.ebike.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @ClassName: Provider 
 * @Description: 商家
 * @author hzjintianfan
 * @date 2014-7-23 下午6:42:23
 */
@Entity
@Table(name = "T_PROVIDER")
@JsonIgnoreProperties(
{ "bikes", "orders" })
public class Provider
{
	@Id
	@Column(name = "C_ID", length = 40)
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String id;

	@Column(name = "C_NAME")
	private String name;

	@Column(name = "C_LOCATION", length = 255)
	private String location;

	@Column(name = "C_LNG")
	private Double lng;

	@Column(name = "C_LAT")
	private Double lat;

	@Column(name = "C_OWNER")
	private String owner;

	@Column(name = "C_PHONE")
	private String phone;

	@Column(name = "C_GRADE")
	private Double grade;

	@Column(name = "C_CITY")
	private String city;

	@Column(name = "C_ZONE")
	private String zone;

	@Column(name = "C_LOW_PRICE")
	private Double lowPrice;

	@Column(name = "C_RANK")
	private Integer rank;

	@Column(name = "C_PHOTO")
	private String photo;

	@OneToMany(cascade =
	{ CascadeType.ALL }, mappedBy = "provider")
	private Set<Bike> bikes;

	@OneToMany(cascade =
	{ CascadeType.ALL }, mappedBy = "provider")
	private Set<Order> orders;

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

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public Double getGrade()
	{
		return grade;
	}

	public void setGrade(Double grade)
	{
		this.grade = grade;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getZone()
	{
		return zone;
	}

	public void setZone(String zone)
	{
		this.zone = zone;
	}

	public Double getLowPrice()
	{
		return lowPrice;
	}

	public void setLowPrice(Double lowPrice)
	{
		this.lowPrice = lowPrice;
	}

	public Integer getRank()
	{
		return rank;
	}

	public void setRank(Integer rank)
	{
		this.rank = rank;
	}

	public Set<Bike> getBikes()
	{
		return bikes;
	}

	public void setBikes(Set<Bike> bikes)
	{
		this.bikes = bikes;
	}

	public Set<Order> getOrders()
	{
		return orders;
	}

	public void setOrders(Set<Order> orders)
	{
		this.orders = orders;
	}

	public String getPhoto()
	{
		return photo;
	}

	public void setPhoto(String photo)
	{
		this.photo = photo;
	}

}
