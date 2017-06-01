package org.ebike.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @ClassName: Bike 
 * @Description: 自行车
 * @author hzjintianfan
 * @date 2014-7-23 下午6:41:33
 */
@Entity
@Table(name = "T_BIKE")
@JsonIgnoreProperties(
{ "orders", "provider", "orderBikes" })
public class Bike
{
	@Id
	@Column(name = "C_ID", length = 40)
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String id;

	@Column(name = "C_NAME")
	private String name;

	@Column(name = "C_TYPE")
	private String type;

	@Column(name = "C_PHOTO")
	private String photo;

	@Column(name = "C_REMAIN_NUM")
	private Integer remainNum;

	@Column(name = "C_TOTAL")
	private Integer total;

	@Column(name = "C_PRICE")
	private Double price;

	@ManyToOne
	@JoinColumn(name = "C_PROVIDER_ID")
	private Provider provider;

	@OneToMany(cascade =
	{ CascadeType.ALL }, mappedBy = "bike")
	private Set<OrderBike> orderBikes;

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

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getPhoto()
	{
		return photo;
	}

	public void setPhoto(String photo)
	{
		this.photo = photo;
	}

	public Integer getRemainNum()
	{
		return remainNum;
	}

	public void setRemainNum(Integer remainNum)
	{
		this.remainNum = remainNum;
	}

	public Double getPrice()
	{
		return price;
	}

	public void setPrice(Double price)
	{
		this.price = price;
	}

	public Provider getProvider()
	{
		return provider;
	}

	public void setProvider(Provider provider)
	{
		this.provider = provider;
	}

	public Integer getTotal()
	{
		return total;
	}

	public void setTotal(Integer total)
	{
		this.total = total;
	}

	public Set<OrderBike> getOrderBikes()
	{
		return orderBikes;
	}

	public void setOrderBikes(Set<OrderBike> orderBikes)
	{
		this.orderBikes = orderBikes;
	}

}
