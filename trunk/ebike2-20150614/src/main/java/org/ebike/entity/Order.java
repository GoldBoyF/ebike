package org.ebike.entity;

import java.util.Date;
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

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @ClassName: Order 
 * @Description: 订单
 * @author hzjintianfan
 * @date 2014-7-23 下午6:41:55
 */
@Entity
@Table(name = "T_ORDER")
public class Order
{
	@Id
	@Column(name = "C_ID", length = 40)
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name = "C_PROVIDER_ID")
	private Provider provider;

	@ManyToOne
	@JoinColumn(name = "C_ACCOUNT_ID")
	private Account account;

	@Column(name = "C_STATUS")
	private Integer status;

	@Column(name = "C_DESCRIPTION")
	private String description;

	@Column(name = "C_GRADE")
	private Integer grade;

	@Column(name = "C_ORDER_NUMBER")
	private String orderNumber;

	@Column(name = "C_ORDER_DATE")
	private Date orderDate;

	@Column(name = "C_START_DATE")
	private Date startDate;

	@Column(name = "C_END_DATE")
	private Date endDate;

	@Column(name = "C_PRICE")
	private Double price;

	@Column(name = "C_COMMENT", length = 400)
	private String comment;

	@Column(name = "C_COMMENT_DATE")
	private Date commentDate;

	@OneToMany(cascade =
	{ CascadeType.ALL }, mappedBy = "order")
	private Set<OrderBike> orderBikes;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Provider getProvider()
	{
		return provider;
	}

	public void setProvider(Provider provider)
	{
		this.provider = provider;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Integer getGrade()
	{
		return grade;
	}

	public void setGrade(Integer grade)
	{
		this.grade = grade;
	}

	public Date getOrderDate()
	{
		return orderDate;
	}

	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public Double getPrice()
	{
		return price;
	}

	public void setPrice(Double price)
	{
		this.price = price;
	}

	public Set<OrderBike> getOrderBikes()
	{
		return orderBikes;
	}

	public void setOrderBikes(Set<OrderBike> orderBikes)
	{
		this.orderBikes = orderBikes;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	public Date getCommentDate()
	{
		return commentDate;
	}

	public void setCommentDate(Date commentDate)
	{
		this.commentDate = commentDate;
	}

}
