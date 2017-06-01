package org.ebike.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @ClassName: OrderBike 
 * @Description: 订单自行车关联表
 * @author hzjintianfan
 * @date 2014-7-23 下午6:42:05
 */
@Entity
@Table(name = "T_ORDER_BIKE")
public class OrderBike
{
	@Id
	@Column(name = "C_ID", length = 40)
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name = "C_ORDER_ID")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "C_BIKE_ID")
	private Bike bike;

	@Column(name = "C_NUM")
	private Integer num;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Order getOrder()
	{
		return order;
	}

	public void setOrder(Order order)
	{
		this.order = order;
	}

	public Bike getBike()
	{
		return bike;
	}

	public void setBike(Bike bike)
	{
		this.bike = bike;
	}

	public Integer getNum()
	{
		return num;
	}

	public void setNum(Integer num)
	{
		this.num = num;
	}

}
