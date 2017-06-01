package org.ebike.entity;

import java.util.Date;

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
 * @ClassName: Comment 
 * @Description: 评论
 * @author hzjintianfan
 * @date 2014-7-23 下午6:41:43
 */
@Entity
@Table(name = "T_COMMENT")
public class Comment
{
	@Id
	@Column(name = "C_ID", length = 40)
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String id;

	@Column(name = "C_CONTENT", length = 400)
	private String content;

	@Column(name = "C_DATE")
	private Date date;

	@ManyToOne
	@JoinColumn(name = "C_ACCOUNT_ID")
	private Account account;

	@Column(name = "C_SUBJECT_ID")
	private String subjectId;

	@Column(name = "C_TYPE")
	private Integer type;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public String getSubjectId()
	{
		return subjectId;
	}

	public void setSubjectId(String subjectId)
	{
		this.subjectId = subjectId;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}

}
