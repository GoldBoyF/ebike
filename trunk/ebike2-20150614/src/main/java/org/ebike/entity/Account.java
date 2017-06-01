package org.ebike.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @ClassName: Account 
 * @Description: 账号
 * @author hzjintianfan
 * @date 2014-7-23 下午6:41:19
 */
@Entity
@Table(name = "T_ACCOUNT")
public class Account
{
	@Id
	@Column(name = "C_ID", length = 40)
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String id;

	@Column(name = "C_USERNAME")
	private String username;

	@Column(name = "C_PASSWORD")
	private String password;

	@Column(name = "C_TYPE")
	private Integer type; //0普通；1管理员

	@Column(name = "C_RENDER")
	private String render;

	@Column(name = "C_NICKNAME")
	private String nickname;

	@Column(name = "C_DATE")
	private Date date;

	@Column(name = "C_PHOTO")
	private String photo;

	@OneToMany(cascade =
	{ CascadeType.ALL }, mappedBy = "account")
	private Set<Order> orders;

	@OneToMany(cascade =
	{ CascadeType.ALL }, mappedBy = "account")
	private Set<Comment> comments;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getRender()
	{
		return render;
	}

	public void setRender(String render)
	{
		this.render = render;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getPhoto()
	{
		return photo;
	}

	public void setPhoto(String photo)
	{
		this.photo = photo;
	}

	public Set<Order> getOrders()
	{
		return orders;
	}

	public void setOrders(Set<Order> orders)
	{
		this.orders = orders;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public Set<Comment> getComments()
	{
		return comments;
	}

	public void setComments(Set<Comment> comments)
	{
		this.comments = comments;
	}

}
