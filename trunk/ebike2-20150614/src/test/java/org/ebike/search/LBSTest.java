package org.ebike.search;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.ebike.tools.SpatialIndexHelper;

public class LBSTest
{

	public static void main(String[] args) throws IOException
	{
		Date now = new Date();
		/*List<SpatialData> datas = new ArrayList<SpatialData>();
		datas.add(new SpatialData("001", "西城区", 116.372648,39.920026));
		datas.add(new SpatialData("002", "朝阳区", 116.476133,39.963396));
		datas.add(new SpatialData("003", "东城区", 116.421516,39.935519));
		datas.add(new SpatialData("004", "海定区", 116.301646,39.966161));
		datas.add(new SpatialData("005", "石景山区",116.230357,39.912168));
		datas.add(new SpatialData("006", "丰台区", 116.295897,39.865664));
		SpatialIndexHelper.addIndex(datas);*/

		List list = SpatialIndexHelper.search(116.405131, 39.915488, 100);
		SpatialIndexHelper.showResults(list);
		System.out.println("cost: " + (new Date().getTime() - now.getTime()));
	}

}
