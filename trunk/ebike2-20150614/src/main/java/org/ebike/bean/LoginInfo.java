package org.ebike.bean;


/**
 * 
 * @ClassName: LoginInfo 
 * @Description: Session中保存的登录信息
 * @author hzjintianfan
 * @date 2014-7-23 下午5:23:24
 */
public class LoginInfo implements java.io.Serializable
{

	/**  */
	private static final long serialVersionUID = -8389187759727251436L;

	private Integer loginType; //登陆类型：0：为普通，1：后台

	/** 账号ID */
	private String acccountId;

	public Integer getLoginType()
	{
		return loginType;
	}

	public void setLoginType(Integer loginType)
	{
		this.loginType = loginType;
	}

	public String getAcccountId()
	{
		return acccountId;
	}

	public void setAcccountId(String acccountId)
	{
		this.acccountId = acccountId;
	}

}