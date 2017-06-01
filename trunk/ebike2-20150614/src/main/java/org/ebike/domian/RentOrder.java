package org.ebike.domian;

/**
 * 
 * @ClassName: RentOrder 
 * @Description: 订单记录
 * @author hzjintianfan
 * @date 2014-7-23 下午6:39:32
 */
public class RentOrder
{
	private String orderId;
	private String providerId;
	private String providerName;
	private String providerLocation;
	private String providerPhone;
	private String description;
	private String providerPhoto;
	private Double price;
	private Integer status;

	public String getProviderId()
	{
		return providerId;
	}

	public void setProviderId(String providerId)
	{
		this.providerId = providerId;
	}

	public String getProviderName()
	{
		return providerName;
	}

	public void setProviderName(String providerName)
	{
		this.providerName = providerName;
	}

	public String getProviderLocation()
	{
		return providerLocation;
	}

	public void setProviderLocation(String providerLocation)
	{
		this.providerLocation = providerLocation;
	}

	public String getProviderPhone()
	{
		return providerPhone;
	}

	public void setProviderPhone(String providerPhone)
	{
		this.providerPhone = providerPhone;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getProviderPhoto()
	{
		return providerPhoto;
	}

	public void setProviderPhoto(String providerPhoto)
	{
		this.providerPhoto = providerPhoto;
	}

	public Double getPrice()
	{
		return price;
	}

	public void setPrice(Double price)
	{
		this.price = price;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

}
