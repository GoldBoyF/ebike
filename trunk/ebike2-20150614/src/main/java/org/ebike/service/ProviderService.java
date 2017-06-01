package org.ebike.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebike.domian.SpatialProvider;
import org.ebike.entity.Provider;
import org.ebike.tools.SpatialIndexHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @ClassName: ProviderService 
 * @Description: 商家（租车点）管理业务类
 * @author hzjintianfan
 * @date 2014-7-23 下午7:03:09
 */
@Service
@Transactional
public class ProviderService extends BaseService
{

	/**
	 * 获取附近租车点
	 * @param lng
	 * @param lat
	 * @return
	 */
	public List<SpatialProvider> getRecentProviders(Double lng, Double lat)
	{
		List<SpatialProvider> spList = new ArrayList<SpatialProvider>();
		Map params = new HashMap();
		params.put("lng", lng);
		params.put("lat", lat);
		List list = baseDao
				.query("SELECT distinct p,(0.747*POWER(MOD(ABS(p.lng - :lng),360),2) + POWER(ABS(p.lat - :lat),2)) AS distance from Provider p order by distance,p.id",
						params, 0, 100);
		for (Object item : list)
		{
			Object[] o = (Object[]) item;
			SpatialProvider sp = new SpatialProvider();
			Provider p = (Provider) o[0];
			sp.setId(p.getId());
			sp.setName(p.getName());
			sp.setGrade(p.getGrade());
			sp.setLng(p.getLng());
			sp.setLat(p.getLat());
			sp.setLocation(p.getLocation());
			sp.setLow(p.getLowPrice());
			Double distance = SpatialIndexHelper.calDistance(lng, lat, p.getLng(), p.getLat());
			sp.setDistance(distance);

			if (distance > 5)
			{
				break;
			}
			spList.add(sp);
		}
		return spList;
	}

	/**
	 * 租车点过滤器
	 * @param lng 当前经度
	 * @param lat 当前纬度
	 * @param city 城市
	 * @param zone 地区  （为null时表示不限制）
	 * @param bikeType 自行车类型
	 * @param order 排序规则
	 * @return
	 */
	public List<SpatialProvider> getFilteredProviders(Double lng, Double lat, String zone, String bikeType,
			Integer order)
	{
		StringBuffer hql = new StringBuffer(
				"select  distinct p,(0.747*POWER(MOD(ABS(p.lng - :lng),360),2) + POWER(ABS(p.lat - :lat),2)) AS distance from Provider p left join p.bikes b where 1=1 and p.city='杭州市' ");
		if (zone != null && !zone.isEmpty())
		{
			hql.append("and p.zone=:zone ");
		}
		if (bikeType != null && !bikeType.isEmpty())
		{
			hql.append("and b.type=:bikeType ");
		}

		if (order.equals(1))
		{ //按距离
			hql.append("order by distance,p.id ");
		}
		else if (order.equals(2))
		{ //按评价
			hql.append("order by p.grade desc,p.id");
		}
		else if (order.equals(3))
		{ //按价格
			hql.append("order by p.lowPrice ,p.id");
		}
		else if (order.equals(4))
		{ //按热门度
			hql.append("order by p.rank desc,p.id");
		}
		else
		{
			hql.append(" order by distance,p.id");
		}
		List<SpatialProvider> spList = new ArrayList<SpatialProvider>();
		Map params = new HashMap();
		params.put("lng", lng);
		params.put("lat", lat);
		params.put("zone", zone);
		params.put("bikeType", bikeType);
		List list = baseDao.query(hql.toString(), params, 0, 100);
		for (Object item : list)
		{
			Object[] o = (Object[]) item;
			SpatialProvider sp = new SpatialProvider();
			Provider p = (Provider) o[0];
			sp.setId(p.getId());
			sp.setName(p.getName());
			sp.setGrade(p.getGrade());
			sp.setLng(p.getLng());
			sp.setLat(p.getLat());
			sp.setLocation(p.getLocation());
			sp.setLow(p.getLowPrice());
			Double distance = SpatialIndexHelper.calDistance(lng, lat, p.getLng(), p.getLat());
			sp.setDistance(distance);
			spList.add(sp);
			System.out.println(o[1] + "  " + distance);
		}
		return spList;
	}

	/**
	 * 获取租车点详情
	 * @param providerId 租车点ID
	 * @return
	 */
	public Map getProvideDetail(String providerId)
	{
		Map data = new HashMap();
		Provider provider = baseDao.query(Provider.class, providerId);
		data.put("provider", provider);
		List bikes = baseDao.query("select b from Bike b where b.remainNum >0 and b.provider.id=?", providerId);
		data.put("bikes", bikes);
		List<Map> comments = new ArrayList<Map>();
		Map paraMap = new HashMap();
		paraMap.put("providerId", providerId);
		List result = baseDao
				.query("select o.comment,o.grade,o.commentDate,o.account.nickname from Order o where o.commentDate!=null and o.provider.id=:providerId order by  o.commentDate desc ",
						paraMap);
		for (Object o : result)
		{
			Object[] item = (Object[]) o;
			Map map = new HashMap();
			map.put("comment", item[0]);
			map.put("grade", item[1]);
			map.put("commentDate", item[2]);
			map.put("nickname", item[3]);
			comments.add(map);
		}
		data.put("comments", comments);
		return data;
	}

}
