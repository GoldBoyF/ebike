package org.ebike.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.ebike.domian.RentOrder;
import org.ebike.entity.Account;
import org.ebike.entity.Bike;
import org.ebike.entity.Order;
import org.ebike.entity.OrderBike;
import org.ebike.entity.Provider;
import org.ebike.exception.NotEnoughBikeException;
import org.ebike.tools.RandTool;
import org.hibernate.LockMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @ClassName: OrderService 
 * @Description: 订单管理业务类
 * @author hzjintianfan
 * @date 2014-7-23 下午6:59:00
 */
@Service
@Transactional
public class OrderService extends BaseService
{

	/**
	 * 根据订单ID获取订单详情
	 * @param id 订单ID
	 * @return
	 */
	public RentOrder getRentOrder(String id)
	{
		RentOrder r = new RentOrder();
		Order order = baseDao.query(Order.class, id);
		r.setProviderId(order.getProvider().getId());
		r.setProviderName(order.getProvider().getName());
		r.setProviderLocation(order.getProvider().getLocation());
		r.setProviderPhone(order.getProvider().getPhone());
		Set<OrderBike> orderBikes = order.getOrderBikes();
		String photoPath = "";
		r.setDescription(r.getDescription());
		r.setProviderPhoto(order.getProvider().getPhoto());
		r.setPrice(Double.valueOf(order.getPrice()));
		r.setStatus(Integer.valueOf(order.getStatus()));
		return r;
	}

	/**
	 * 评价店铺
	 * @param orderId 订单ID
	 * @param star 星级
	 * @param comment 评论
	 */
	public void evaluteOrder(String orderId, Integer star, String comment)
	{
		Order order = baseDao.query(Order.class, orderId);
		order.setStatus(3);
		order.setGrade(star);
		order.setCommentDate(new Date());
		order.setComment(comment);
		baseDao.modify(order);
		if (star != null)
		{
			Provider p = order.getProvider();
			Integer factor = p.getOrders().size();
			Double grade = p.getGrade() * (factor - 1) / (factor * 1.0f) + star / (factor * 1.0f);
			p.setGrade(grade);
			baseDao.modify(p);
		}
	}

	/**
	 * 订车
	 * @param accounId 
	 * @param details 订单细节 格式如下：bikeId:num;bikeId;num;bikeId:num
	 * @param startDate 订车开始时间
	 * @param endDate 订车结束时间
	 * @return
	 */
	//@Transactional(isolation=Isolation.SERIALIZABLE)
	public Order bookBike(String accounId, String details, Double myPrice, Date startDate, Date endDate)
			throws NotEnoughBikeException
	{
		Order order = new Order();
		Set<OrderBike> obs = new HashSet<OrderBike>();
		Double price = 0.0;
		;
		String[] items = details.split(";");
		String description = "";
		for (String item : items)
		{
			String[] o = item.split(":");
			OrderBike ob = new OrderBike();
			Bike bike = baseDao.query(Bike.class, o[0],LockMode.PESSIMISTIC_WRITE);
			if (bike.getRemainNum() < Integer.valueOf(o[1]))
				throw new NotEnoughBikeException();
			ob.setBike(bike);
			ob.setOrder(order);
			ob.setNum(Integer.valueOf(o[1]));
			obs.add(ob);
			bike.setRemainNum(bike.getRemainNum() - Integer.valueOf(o[1]));
			baseDao.modify(bike);
			price = price + Integer.valueOf(o[1]) * bike.getPrice();
			description = description + bike.getName() + ":" + Integer.valueOf(o[1]) + "辆;";
		}
		int hour = endDate.getHours() - startDate.getHours();
		order.setDescription(description.substring(0, description.length() - 1));
		order.setPrice(myPrice);
		order.setOrderBikes(obs);
		order.setOrderDate(new Date());
		order.setStatus(2);
		order.setStartDate(startDate);
		order.setEndDate(endDate);
		order.setAccount(baseDao.query(Account.class, accounId));
		order.setProvider(obs.iterator().next().getBike().getProvider());
		order.setOrderNumber(RandTool.rand(11));
		String orderId = baseDao.add(order);
		return baseDao.query(Order.class, orderId);
	}

	/**
	 * 付款
	 * @param orderId 订单ID
	 * @param payId 
	 * @return 
	 */
	public boolean pay(String orderId, String payId)
	{
		Order order = baseDao.query(Order.class, orderId);
		order.setStatus(2);
		baseDao.modify(order);
		return true;
	}

}
