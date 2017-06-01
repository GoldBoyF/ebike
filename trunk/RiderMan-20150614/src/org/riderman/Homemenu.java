package org.riderman;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.riderman.selfspace.SelfSpaceFragment;
import org.riderman.tools.CommonTool;
import org.riderman.tools.SimpleHttpClient;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfigeration;
import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * 
 * @author Li shang 1：如果Button事件太多，可以考虑将监听事件集成或者Activity继承OnClickListener接口
 *         2：对于一些并发性低的控件可以使用局部引用的复用来减少成员变量 3：
 */
public class Homemenu extends FragmentActivity implements View.OnClickListener {

	ActivityStackManager activityStackManager;
	private RelativeLayout fragmentLayoutLike;
	private int isSearchCDM = 1;
	private TextView mapMode;
	private TextView listMode;
	private boolean modeSelection;
	private ImageButton searchDesInfo;
	// 列表模式
	private ListView bikestoreListView;
	private List<Map<String, Object>> mListItem;
	private List<Map<String, Object>> mListItemTmp;
	private int itemposion;
	private TextView bikeSortFilter;
	// 标签切换
	private LinearLayout listmodecheck;
	private LinearLayout setmapmode;
	private int STATE_HOMEMENU = 0XFF;
	SimpleHttpClient client;
	JSONObject jsonObject;
	Handler internetComHander;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener;
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	MapView mMapView;
	BaiduMap mBaiduMap;
	private LatLng staticLatLnglatLng;
	// 起泡
	BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_curposition);
	BitmapDescriptor bitmapDescriptorbike = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_locate_blue);
	// BitmapDescriptor bdA = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_marka);
	// BitmapDescriptor bdB = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_markb);
	// BitmapDescriptor bdC = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_markc);
	// BitmapDescriptor bdD = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_markd);
	// BitmapDescriptor bdE = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_marke);
	// BitmapDescriptor bdF = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_markf);
	// BitmapDescriptor bdG = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_markg);
	// BitmapDescriptor bdH = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_markh);
	// BitmapDescriptor bdI = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_marki);
	// BitmapDescriptor bdJ = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_marke);
	BitmapDescriptor bd = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_mark);
	BitmapDescriptor bdGround = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_mark);
	//
	// private final BitmapDescriptor[] bitmapDescriptorArray = { bdA, bdB, bdC,
	// bdD, bdE, bdF, bdG, bdH, bdI, bdJ };

	private InfoWindow mInfoWindow;
	ImageButton requestLocButton;
	private TextView mapState;
	boolean isFirstLoc = true;
	protected JSONArray list;
	private ScrollView FilterscrollView;
	private ScrollView Filterbikesort;
	private ScrollView FilterOrder;

	private MyAdapter madapter;

	protected ParcelableStoreDetail parcelableDate;

	protected boolean exitPressed = false;
	protected boolean firstPressed = false;

	public LatLng currentlatLng;

	private ProgressDialog progressDialogTrade;

	private GeoCoder geoCoder;

	private TextView curpositon;
	private FragmentManager fm;
	private FragmentTransaction ft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homemenu);
		STATE_HOMEMENU = ConstantTool.PAGE_RENT;
		fragmentLayoutLike = (RelativeLayout) findViewById(R.id.fragmentLayoutLike);
		activityStackManager = ActivityStackManager.getInstance();
		Intent intent = getIntent();
		// 获取经纬度
		staticLatLnglatLng = new LatLng(30.193714, 120.198418);
		myListener = new MyLocationListenner();
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mapState = (TextView) findViewById(R.id.mapstate);
		mCurrentMode = LocationMode.NORMAL;
		mapState.setText("普通");
		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		// 定位相关信息
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);

		// 设置定位位置
		// staticLatLnglatLng=??????是否来自搜索传递的数据??

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				if (!(marker.getTitle().toString().equals("目标位置")))
					showInfo(marker);
				return true;
			}
		});

		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng locationginfo) {
				// TODO Auto-generated method stub
				mBaiduMap.hideInfoWindow();
			}
		});

		// 地图显示模式普通/罗盘/跟随
		requestLocButton = (ImageButton) findViewById(R.id.mapStateButton);// 基本定位
		requestLocButton.setOnClickListener(this);
		// 列表模式下ListView显示附近的车点

		client = new SimpleHttpClient(SimpleHttpClient.DOMAIN);

		if (intent.getIntExtra("CMD", 0xFF) == ConstantTool.IS_SEARCH_CDM) {
			isSearchCDM = ConstantTool.IS_SEARCH_CDM;
			double lat = (double) intent.getDoubleExtra("lat", 0.0);
			double lng = (double) intent.getDoubleExtra("lng", 0.0);
			staticLatLnglatLng = new LatLng(lat, lng);
			LatLng latlng = new LatLng(staticLatLnglatLng.latitude,
					staticLatLnglatLng.longitude);
			// 定位初始化
			msu = MapStatusUpdateFactory.newLatLng(latlng);
			mBaiduMap.setMapStatus(msu);
			mBaiduMap.animateMapStatus(msu);
			new InternetComThread(staticLatLnglatLng,
					ConstantTool.GET_NEARBY_POINT).start();// 为何显示到C
			// requestLocButton.setVisibility(View.INVISIBLE);
		}
		bikestoreListView = (ListView) findViewById(R.id.bikestorelist);
		madapter = new MyAdapter(Homemenu.this);
		try {
			mListItem = getItemList(null);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bikestoreListView.setAdapter(madapter);
		bikestoreListView.setDividerHeight(0);
		// new Utility().setListViewHeightBasedOnChildren(bikestoreListView);
		bikestoreListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mListItem.get(position).get("id");
				mListItemTmp = new ArrayList<Map<String, Object>>();
				itemposion = position;
				for (Map<String, Object> map : mListItem) {
					mListItemTmp.add(map);

				}
				// Log.v("lishang", "id ---" +
				// mListItem.get(position).get("id"));
				new InternetComThread((String) mListItem.get(position)
						.get("id"), ConstantTool.GET_STORE_DETAIL).start();
				// Log.v("lishang", "262 " + id);
			}
		});

		// 底层一级TAB分支,选择一级功能
		LinearLayout baseFuncTab = (LinearLayout) findViewById(R.id.linearlayoutrent);
		baseFuncTab.setOnClickListener(this);
		TextView rentbike = (TextView) findViewById(R.id.bikerent);
		rentbike.setTextColor(getResources().getColor(R.color.green));
		baseFuncTab = (LinearLayout) findViewById(R.id.linearlayoutrouting);
		baseFuncTab.setOnClickListener(this);
		baseFuncTab = (LinearLayout) findViewById(R.id.linearlayoutshare);
		baseFuncTab.setOnClickListener(this);
		baseFuncTab = (LinearLayout) findViewById(R.id.linearlayoutperinfo);
		baseFuncTab.setOnClickListener(this);
		// 附近的点Tab切换模式
		listmodecheck = (LinearLayout) findViewById(R.id.listmodecheck);
		listmodecheck.setVisibility(View.INVISIBLE);
		setmapmode = (LinearLayout) findViewById(R.id.setluopanmode);
		modeSelection = false;

		mapMode = (TextView) findViewById(R.id.mapmode);
		mapMode.setOnClickListener(this);
		listMode = (TextView) findViewById(R.id.listmode);
		// listMode.setOnClickListener(this);
		listMode.setOnClickListener(this);
		// 模糊搜索
		searchDesInfo = (ImageButton) findViewById(R.id.searchDesButton);
		searchDesInfo.setOnClickListener(this);
		// 列表模式下的筛选控件
		bikeSortFilter = (TextView) findViewById(R.id.defaultsort);
		bikeSortFilter.setOnClickListener(this);
		bikeSortFilter = (TextView) findViewById(R.id.biketype);
		bikeSortFilter.setOnClickListener(this);
		bikeSortFilter = (TextView) findViewById(R.id.allhangzhou);
		bikeSortFilter.setOnClickListener(this);

		// 杂鱼选项
		FilterscrollView = (ScrollView) findViewById(R.id.FilterscrollAllHZ);
		Filterbikesort = (ScrollView) findViewById(R.id.Filterbikesort);
		FilterOrder = (ScrollView) findViewById(R.id.filterOrder);

		// 筛选列表
		TextView textView = (TextView) findViewById(R.id.TextViewNoLimit);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextViewSingle);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextViewDouble);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextViewMore);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView1_1);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView1_2);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView2_1);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView2_2);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView3_1);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView3_2);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView4_1);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView4_2);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView5_1);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView5_2);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView6_1);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView6_2);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView7_1);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView7_2);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.xihu);
		textView.setOnClickListener(this);
		//设置排序条件按钮的时间响应
		textView = (TextView) findViewById(R.id.TextViewDistance);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextViewGrade);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextViewPrice);
		textView.setOnClickListener(this);

		// 当前地理位置刷新
		ImageButton imageButton = (ImageButton) findViewById(R.id.refresh);
		currentlatLng = staticLatLnglatLng;
		imageButton.setOnClickListener(this);
		curpositon = (TextView) findViewById(R.id.curpositon);
		geoCoder = GeoCoder.newInstance();
		geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				// TODO Auto-generated method stub
				// 获取当前位置
				// Log.v("lishang", "648");

				curpositon.setText("当前位置：" + result.getAddress());
			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				// TODO Auto-generated method stub

			}
		});
		geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
				.location(currentlatLng));

		ActivityStackManager.getInstance().homeMenuClear();

		internetComHander = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub

				switch (msg.what) {
				case ConstantTool.GET_NEARBY_POINT:
					// 主界面只有一个联网操作，可以不用Handler，用一个全局变量做标记位
					try {
						list = jsonObject.getJSONArray("list");
						mListItem = getItemList(list);
						madapter.notifyDataSetChanged();
						popListOnMap(list);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case ConstantTool.GET_STORE_DETAIL:
					// 主界面只有一个联网操作，可以不用Handler，用一个全局变量做标记位
					parcelableDate = getStoreDetail(jsonObject);
					Intent intent = new Intent(Homemenu.this,
							BikeStoreDetail.class);
					intent.putExtra("StoreDetail", parcelableDate);
					startActivity(intent);
					break;
				case ConstantTool.GET_STORE_FILTER:
					// 主界面只有一个联网操作，可以不用Handler，用一个全局变量做标记位
					try {

						// Log.v("lishang", "390  " + "--" + jsonObject);
						list = jsonObject.getJSONArray("list");
						mListItem = getItemList(list);
						popListOnMap(list);
						madapter.notifyDataSetChanged();
						// popListOnMap(list);
						progressDialogTrade.cancel();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case ConstantTool.PROGRESS_BAR_SHOW:
					progressDialogTrade.setMessage("正在刷新");
					// 要注意网络错误的处理
					progressDialogTrade.show();
					break;
				case ConstantTool.PRESS_INFO_EXIT:
					Toast.makeText(Homemenu.this, "再次点击退出程序", Toast.LENGTH_LONG)
							.show();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		if (intent.getIntExtra("CMD", 0xFF) == ConstantTool.PAY_TOGO) {

			STATE_HOMEMENU = ConstantTool.PAGE_PERINFO;
			setTabBackgroud(3, imgViewIDArray, textViewIDArray, layoutIDArray,
					imgViewIDArrayA, imgViewIDArrayB);
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.addToBackStack("first");
			ft.replace(R.id.fragmentLayoutLike, new SelfSpaceFragment());
			ft.commit();
		}
	}

	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}

	public void resetOverlay(View view) {
		clearOverlay(null);
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// new InternetComThread(staticLatLnglatLng).start();
			// Toast.makeText(Homemenu.this, "地图重绘", Toast.LENGTH_SHORT).show();
			if (location == null || mMapView == null)
				return;
			currentlatLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			if (isFirstLoc) {
				if (ConstantTool.IS_SEARCH_CDM != isSearchCDM) {
					// 定位中心位置
					staticLatLnglatLng = new LatLng(location.getLatitude(),
							location.getLongitude());
					MyLocationData locData = new MyLocationData.Builder()
							.accuracy(location.getRadius()).direction(100)
							.latitude(location.getLatitude())
							.longitude(location.getLongitude()).build();
					mBaiduMap.setMyLocationData(locData);
					// 紧紧是设置位置，不显示中心
					// mCurrentMode = LocationMode.FOLLOWING;
					// 空间预留给服务器与客户端通信
					LatLng ll = new LatLng(location.getLatitude(),
							location.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);

					new InternetComThread(ll, ConstantTool.GET_NEARBY_POINT)
							.start();
					isFirstLoc = false;
				} else {
					if (ConstantTool.IS_SEARCH_CDM == isSearchCDM) {
						// 为什么不在主线程中呢？
						// isSearchCDM = 0xFF;
						isFirstLoc = false;
						MyLocationData locData = new MyLocationData.Builder()
								.direction(100)
								.latitude(staticLatLnglatLng.latitude)
								.longitude(staticLatLnglatLng.longitude)
								.build();
						mBaiduMap.setMyLocationData(locData);
						// Log.v("lishang", "467 search");
					}
				}
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		activityStackManager.pushActivity(this);
		// 主界面不添加到站里去
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
		switch (STATE_HOMEMENU) {
		case R.id.linearlayoutrent:

			STATE_HOMEMENU = ConstantTool.PAGE_RENT;
			ft = fm.beginTransaction();
			// ft.setCustomAnimations(R.anim.slide_right_in,
			// R.anim.slide_right_out, R.anim.slide_right_in,
			// R.anim.slide_right_out);
			// ft.commit();
			fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			// 成功解决
			// fm.popBackStack();
			break;
		case R.id.linearlayoutrouting:
			// activity 是不是需要紧密配合则？？？配合Fragment可行
			//
			// STATE_HOMEMENU = ConstantTool.PAGE_ADVICE_ROUTE;
			// fm = getSupportFragmentManager();
			// ft = fm.beginTransaction();
			// // ft.setCustomAnimations(R.anim.slide_right_in,
			// // R.anim.slide_right_out);
			// ft.addToBackStack("first");
			// ft.replace(R.id.fragmentLayoutLike, new TabAFm());
			// ft.commit();
			break;
		case R.id.linearlayoutshare:

			// STATE_HOMEMENU = ConstantTool.PAGE_TIPS;
			// fm = getSupportFragmentManager();
			// ft = fm.beginTransaction();
			// ft.addToBackStack("first");
			// // ft.setCustomAnimations(R.anim.slide_left_in,
			// // R.anim.slide_left_out);
			// // ft.set
			// ft.replace(R.id.fragmentLayoutLike, new TabBFm());
			// ft.commit();
			break;
		case ConstantTool.PAGE_PERINFO:
			STATE_HOMEMENU = ConstantTool.PAGE_PERINFO;
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			// ft.setCustomAnimations(R.anim.slide_left_in,
			// R.anim.slide_left_out);
			ft.addToBackStack("first");
			ft.replace(R.id.fragmentLayoutLike, new SelfSpaceFragment());
			ft.commit();
			break;
		default:
			break;

		}
		// ActivityStackManager.getInstance().homeMenuClear();
		// super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		bdGround.recycle();
		super.onDestroy();
	}

	public void popListOnMap(JSONArray list) {
		int count = list.length();
		LatLng latLng = null;
		mBaiduMap.clear();
		Toast.makeText(Homemenu.this, "附近所有的自行车租赁点",
				Toast.LENGTH_LONG).show();
		// 显示我的位置或者搜索的位置staticLatLnglatLng//
		MarkerOptions markerOptions = new MarkerOptions();
		OverlayOptions overlayOptions = markerOptions
				.position(staticLatLnglatLng).icon(bitmapDescriptor).zIndex(9);
		// overlayOptions.
		markerOptions.title("目标位置");
		// markerOptions.icon(bitmapDescriptor);
		// mBaiduMap.addOverlay(overlayOptions);
		// Log.v("lishang", markerOptions.getTitle());
		// showTitle(markerOptions.getPosition());
		for (int i = 0; i < count; i++) {
			try {
				JSONObject jsonObject = list.getJSONObject(i);
				latLng = new LatLng(jsonObject.getDouble("lat"),
						jsonObject.getDouble("lng"));
				MarkerOptions markerOptions1 = new MarkerOptions();
				markerOptions1.title(jsonObject.getString("id"));
				Bundle bundle = new Bundle();
				bundle.putString("title", jsonObject.getString("name"));
				bundle.putString("location", jsonObject.getString("location"));
				bundle.putString("distance", jsonObject.getString("distance"));
				markerOptions1.extraInfo(bundle);
				overlayOptions = markerOptions1.position(latLng)
						.icon(bitmapDescriptorbike).zIndex(9);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mBaiduMap.addOverlay(overlayOptions);
		}
	}

	private boolean popClickSelection = false;
	private String distance_Store;

	public void showInfo(final Marker marker) {

		final LatLng location = marker.getPosition();
		Point p = mBaiduMap.getProjection().toScreenLocation(location);
		p.y -= 47;
		LatLng locationInfo = mBaiduMap.getProjection().fromScreenLocation(p);
		OnInfoWindowClickListener listener = null;
		listener = new OnInfoWindowClickListener() {
			public void onInfoWindowClick() {
				mBaiduMap.hideInfoWindow();
				popClickSelection = true;
				distance_Store = String.format(
						"%.2f千米",
						Float.parseFloat(""
								+ marker.getExtraInfo().get("distance")));
				// if (!(marker.getTitle().toString().equals("目标位置")))
				// Log.v("lishang", "484 marker.getTitle().toString() "
				// + marker.getTitle().toString());

				new InternetComThread(marker.getTitle().toString(),
						ConstantTool.GET_STORE_DETAIL).start();
			}
		};
		LinearLayout linearlayout = new LinearLayout(getApplicationContext());
		linearlayout.setOrientation(LinearLayout.VERTICAL);
		TextView text = new TextView(getApplicationContext());
		Bundle bundle = marker.getExtraInfo();
		text.setText(bundle.getString("title"));
		text.setGravity(Gravity.LEFT);
		text.setTextColor(getResources().getColor(R.color.popTextTitlecolor));
		TextView text1 = new TextView(getApplicationContext());
		text.setPadding(0, 0, 0, 2);
		text.setTextSize(getResources().getDimension(R.dimen.floatpop));
		text1.setText(bundle.getString("location"));
		text1.setGravity(Gravity.LEFT);
		text1.setPadding(0, 0, 50, 2);

		text1.setTextColor(getResources().getColor(R.color.popTextcolor));
		linearlayout.addView(text);
		linearlayout.addView(text1);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		linearlayout.setLayoutParams(lp1);

		// 圆角背景
		linearlayout.setBackgroundColor(getResources().getColor(R.color.white));
		linearlayout.setPadding(10, 5, 20, 5);
		mInfoWindow = new InfoWindow(linearlayout, locationInfo, listener);
		mBaiduMap.showInfoWindow(mInfoWindow);

	}

	// 列表模式的实现
	public final class ViewHolder {
		public TextView storetitle;
		public RatingBar storerating;
		public TextView storeaddr;
		public TextView storedistance;

	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mListItem.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();// 接受对象
				convertView = mInflater.inflate(
						R.layout.list_item_bike_store_list, null);
				holder.storetitle = (TextView) convertView
						.findViewById(R.id.storetitle);
				holder.storerating = (RatingBar) convertView
						.findViewById(R.id.storerating);
				holder.storeaddr = (TextView) convertView
						.findViewById(R.id.storeaddr);
				holder.storedistance = (TextView) convertView
						.findViewById(R.id.storedistance);
				convertView.setTag(holder);// why
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 此处是扩展的地方
			if (mListItem.size() > 0) {
				holder.storetitle.setText((String) mListItem.get(position).get(
						"storetitle"));
				holder.storerating.setNumStars(5);
				holder.storerating.setRating(Float
						.parseFloat((String) mListItem.get(position).get(
								"storerating")));
 				holder.storerating.setScaleX(0.7f);
				holder.storerating.setScaleY(0.7f);
				holder.storeaddr.setText((String) mListItem.get(position).get(
						"storeaddr"));
				holder.storedistance.setText((String) mListItem.get(position)
						.get("storedistance"));
			}

			return convertView;
		}
	}

	private List<Map<String, Object>> getItemList(JSONArray jsonArray)
			throws JSONException {
		// 打包返回数据 留给上面分析
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; jsonArray != null && i < jsonArray.length(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("storetitle", jsonArray.getJSONObject(i).get("name"));
			map.put("storerating", jsonArray.getJSONObject(i).get("grade") + "");
			map.put("storeaddr", jsonArray.getJSONObject(i).get("location"));
			String distance = String.format("%.2f千米", jsonArray
					.getJSONObject(i).get("distance"));
			map.put("storedistance", distance);
			map.put("id", "" + jsonArray.getJSONObject(i).get("id"));
			map.put("lat", jsonArray.getJSONObject(i).get("lat"));
			map.put("lng", jsonArray.getJSONObject(i).get("lng"));

			list.add(map);
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ImageView img;

		switch (v.getId()) {
		case R.id.refresh:
			geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
					.location(currentlatLng));
			break;
		case R.id.linearlayoutrent:
			if (STATE_HOMEMENU == ConstantTool.PAGE_RENT)
				return;
			STATE_HOMEMENU = ConstantTool.PAGE_RENT;
			ft = fm.beginTransaction();
			// ft.setCustomAnimations(R.anim.slide_right_in,
			// R.anim.slide_right_out, R.anim.slide_right_in,
			// R.anim.slide_right_out);
			// ft.commit();
			fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			setTabBackgroud(0, imgViewIDArray, textViewIDArray, layoutIDArray,
					imgViewIDArrayA, imgViewIDArrayB);
			// 成功解决
			// fm.popBackStack();

			// private int[] imgViewIDArray = { R.id.ImageView_rent,
			// R.id.ImageView_advice, R.id.ImageView_share,
			// R.id.ImageView_personal };
			// private int[] textViewIDArray = { R.id.bikerent,
			// R.id.line_advice,
			// R.id.routershare, R.id.personalinfo };
			// private int[] layoutIDArray = { R.id.linearlayoutrent,
			// R.id.linearlayoutrouting, R.id.linearlayoutshare,
			// R.id.linearlayoutperinfo };
			// private int[] imgViewIDArrayA = {
			// R.drawable.icon_zuche_unpressed,
			// R.drawable.icon_tuijian_unpressed,
			// R.drawable.icon_fenxiang_unpressed,
			// R.drawable.icon_user_unpressed };
			// private int[] imgViewIDArrayB = { R.drawable.icon_zuche_pressed,
			// R.drawable.icon_tuijian_pressed,
			// R.drawable.icon_fenxiang_pressed,
			// R.drawable.icon_user_pressed };

			break;
		case R.id.linearlayoutrouting:
			// activity 是不是需要紧密配合则？？？配合Fragment可行
			if (STATE_HOMEMENU == ConstantTool.PAGE_ADVICE_ROUTE)
				return;

			setTabBackgroud(1, imgViewIDArray, textViewIDArray, layoutIDArray,
					imgViewIDArrayA, imgViewIDArrayB);

			STATE_HOMEMENU = ConstantTool.PAGE_ADVICE_ROUTE;
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

			// ft.setCustomAnimations(R.anim.slide_right_in,
			// R.anim.slide_right_out);
			ft.addToBackStack("first");
			ft.replace(R.id.fragmentLayoutLike, new RoutePush());
			ft.commit();
			break;
		case R.id.linearlayoutshare:
			if (STATE_HOMEMENU == ConstantTool.PAGE_TIPS)
				return;
			STATE_HOMEMENU = ConstantTool.PAGE_TIPS;
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.addToBackStack("first");
			// ft.setCustomAnimations(R.anim.slide_left_in,
			// R.anim.slide_left_out);

			ft.replace(R.id.fragmentLayoutLike, new RouteShare());
			ft.commit();
			setTabBackgroud(2, imgViewIDArray, textViewIDArray, layoutIDArray,
					imgViewIDArrayA, imgViewIDArrayB);

			break;
		case R.id.linearlayoutperinfo:
			if (STATE_HOMEMENU == ConstantTool.PAGE_PERINFO)
				return;
			// Log.v("lishang", "730");
			STATE_HOMEMENU = ConstantTool.PAGE_PERINFO;
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.addToBackStack("first");
			ft.replace(R.id.fragmentLayoutLike, new SelfSpaceFragment());
			ft.commit();
			setTabBackgroud(3, imgViewIDArray, textViewIDArray, layoutIDArray,
					imgViewIDArrayA, imgViewIDArrayB);

			break;

		// startActivity(new Intent(Homemenu.this, SelfSpace.class));
		case R.id.listmode:
			if (!modeSelection) {
				mapMode.setBackgroundColor(Homemenu.this.getResources()
						.getColor(R.color.ebike_mode_unselect));

				// bug获取图像资源必须用getresource函数 否则出错
				// mapMode.setBackgroundResource(Homemenu.this.getResources().getColor(
				// R.color.ebike_mode_unselect));
				mapMode.setTextColor(Homemenu.this.getResources().getColor(
						R.color.transwhitesmoke));
				// listMode.setBackgroundResource(Homemenu.this.getResources()
				// .getColor(R.color.transwhitesmoke));
				listMode.setBackgroundColor(Homemenu.this.getResources()
						.getColor(R.color.transwhitesmoke));
				listMode.setTextColor(Homemenu.this.getResources().getColor(
						R.color.darkcyan));
				modeSelection = true;

				listmodecheck.setVisibility(View.VISIBLE);
				// listmodecheck.setBackgroundColor(Homemenu.this.getResources()
				// .getColor(R.color.whitesmoke));
				mMapView.setVisibility(View.INVISIBLE);
				setmapmode.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.mapmode:
			if (modeSelection) {
				listMode.setBackgroundColor(Homemenu.this.getResources()
						.getColor(R.color.ebike_mode_unselect));
				listMode.setTextColor(Homemenu.this.getResources().getColor(
						R.color.transwhitesmoke));
				mapMode.setBackgroundColor(Homemenu.this.getResources()
						.getColor(R.color.transwhitesmoke));
				mapMode.setTextColor(Homemenu.this.getResources().getColor(
						R.color.darkcyan));

				modeSelection = false;
				listmodecheck.setVisibility(View.INVISIBLE);

				mMapView.setVisibility(View.VISIBLE);
				setmapmode.setVisibility(View.VISIBLE);
				FilterscrollView.setVisibility(View.INVISIBLE);
				Filterbikesort.setVisibility(View.INVISIBLE);
				FilterOrder.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.searchDesButton:
			startActivity(new Intent(Homemenu.this, DestSearch.class));
			break;
		case R.id.defaultsort:
			img = (ImageView) findViewById(R.id.underline_left);
			img.setVisibility(View.INVISIBLE);
			img = (ImageView) findViewById(R.id.underline_middle);
			img.setVisibility(View.INVISIBLE);
			img = (ImageView) findViewById(R.id.underline_right);
			img.setVisibility(View.VISIBLE);
			FilterscrollView.setVisibility(View.INVISIBLE);
			Filterbikesort.setVisibility(View.INVISIBLE);
			FilterOrder.setVisibility(View.VISIBLE);
			break;
		case R.id.biketype:
			img = (ImageView) findViewById(R.id.underline_left);
			img.setVisibility(View.INVISIBLE);
			img = (ImageView) findViewById(R.id.underline_middle);
			img.setVisibility(View.VISIBLE);
			img = (ImageView) findViewById(R.id.underline_right);
			img.setVisibility(View.INVISIBLE);
			FilterscrollView.setVisibility(View.INVISIBLE);
			Filterbikesort.setVisibility(View.VISIBLE);
			FilterOrder.setVisibility(View.INVISIBLE);
			break;
		case R.id.allhangzhou:
			img = (ImageView) findViewById(R.id.underline_left);
			img.setVisibility(View.VISIBLE);
			img = (ImageView) findViewById(R.id.underline_middle);
			img.setVisibility(View.INVISIBLE);
			img = (ImageView) findViewById(R.id.underline_right);
			img.setVisibility(View.INVISIBLE);
			FilterscrollView.setVisibility(View.VISIBLE);
			Filterbikesort.setVisibility(View.INVISIBLE);
			FilterOrder.setVisibility(View.INVISIBLE);
			break;
		case R.id.mapStateButton:
			switch (mCurrentMode) {
			case NORMAL:
				mapState.setText("跟随");
				mCurrentMode = LocationMode.FOLLOWING;

				MyLocationData locData = new MyLocationData.Builder()
						.direction(100).latitude(currentlatLng.latitude)
						.longitude(currentlatLng.longitude).build();
				mBaiduMap.setMyLocationData(locData);
				MapStatusUpdate u = MapStatusUpdateFactory
						.newLatLng(currentlatLng);
				mBaiduMap.setMapStatus(u);// 这儿是设定位置的关键
				mBaiduMap.animateMapStatus(u);// 这是个跳转动画显示
				// Log.v("lishang", "773 search");
				mBaiduMap
						.setMyLocationConfigeration(new MyLocationConfigeration(
								mCurrentMode, true, mCurrentMarker));

				break;
			case COMPASS:
				mapState.setText("普通");
				mCurrentMode = LocationMode.NORMAL;
				mBaiduMap
						.setMyLocationConfigeration(new MyLocationConfigeration(
								mCurrentMode, true, mCurrentMarker));
				break;
			case FOLLOWING:
				mapState.setText("罗盘");
				mCurrentMode = LocationMode.COMPASS;
				mBaiduMap
						.setMyLocationConfigeration(new MyLocationConfigeration(
								mCurrentMode, true, mCurrentMarker));
				break;
			}
			break;

		default: //处理所有下拉菜单项的事件
			listmodecheck.setVisibility(View.VISIBLE);
			FilterscrollView.setVisibility(View.INVISIBLE);
			Filterbikesort.setVisibility(View.INVISIBLE);
			FilterOrder.setVisibility(View.INVISIBLE);
			if (v.getId() != R.id.allhangzhou && v.getId() != R.id.biketype
					&& v.getId() != R.id.defaultsort) {
				TextView textView = (TextView) findViewById(v.getId());
				String limit = textView.getText().toString();
				if (limit != "默认排序") {
					if (limit.equals( "不限" )|| limit.equals ("单人车" )|| limit.equals("双人车") 
							|| limit.equals("多人车")) {
						TextView textView2 = (TextView) findViewById(R.id.biketype);
						textView2.setText(limit);
						textView2 = (TextView) findViewById(R.id.defaultsort);
						textView2.setText("默认排序");
						textView2 = (TextView) findViewById(R.id.allhangzhou);
						textView2.setText("全杭州");
					}else if(limit.equals("按距离")||limit.equals("按评价")||limit.equals("按价格")){
						TextView textView3 = (TextView) findViewById(R.id.defaultsort);
						textView3.setText(limit);
						textView3 = (TextView) findViewById(R.id.allhangzhou);
						textView3.setText("全杭州");
						textView3 = (TextView) findViewById(R.id.biketype);
						textView3.setText("车型");
					}else 
					 {
						TextView textView1 = (TextView) findViewById(R.id.allhangzhou);
						textView1.setText(limit);
						textView1 = (TextView) findViewById(R.id.biketype);
						textView1.setText("车型");
						textView1 = (TextView) findViewById(R.id.defaultsort);
						textView1.setText("默认排序");
					}
				}
				new InternetComThread(limit, ConstantTool.GET_STORE_FILTER)
						.start();
				progressDialogTrade = new ProgressDialog(Homemenu.this);
				progressDialogTrade
						.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialogTrade.setCanceledOnTouchOutside(false);
				progressDialogTrade.setCancelable(true);// 注意此处bug
				Message.obtain(internetComHander,
						ConstantTool.PROGRESS_BAR_SHOW).sendToTarget();
			}
			// mListItem重新获取数据 刷新ListView
			// 发送新请求 Handler里刷新
			// new InternetComThread("", GET_STORE_FILTER);

			break;
		}
	}

	class InternetComThread extends Thread {

		private LatLng latLng;
		private String params;
		private int CMD;
		private String string;

		public InternetComThread(LatLng latLng, int cMD) {
			super();
			this.latLng = latLng;
			this.CMD = cMD;
		}

		public InternetComThread(String params, int cMD) {
			super();
			this.params = params;
			CMD = cMD;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				switch (CMD) {
				case ConstantTool.GET_NEARBY_POINT:
					string = new String("/providers/recent?lng="
							+ String.valueOf(latLng.longitude) + "&lat="
							+ String.valueOf(latLng.latitude));
					// 需要添加错误处理
					jsonObject = client.get(string);
					Message.obtain(internetComHander,
							ConstantTool.GET_NEARBY_POINT).sendToTarget();
					// Log.v("lishang", "dfsfs" + jsonObject + "");
					// Log.v("lishang", "jsonObject  --" + jsonObject);
					break;
				case ConstantTool.GET_STORE_DETAIL:
					string = new String("/providers/detail/" + params);
					// 需要添加错误处理
					jsonObject = client.get(string);
					JSONArray jsonArray = jsonObject.getJSONArray("bikes");
					Message.obtain(internetComHander,
							ConstantTool.GET_STORE_DETAIL).sendToTarget();
					Log.v("lishang12", jsonObject + "");

					break;
				case ConstantTool.GET_STORE_FILTER:
					string = new String(
							"/providers/filter?bikeType=单人车&order=1&lng=116&lat=31");
					// pare
					// 需要添加错误处理
					// 网络连接出错的处理没有写好
					String zone = "";
					String bikeType = "";
					Integer order = 4;
					if(params.equals("按距离")||params.equals("按评价")||params.equals("按价格")){
						if(params.equals("按距离")){
							order=1;
						}else if (params.equals("按评价")) {
							order=2;
						}else if (params.equals("按价格")) {
							order=3;
						}
					}else if (params.equals("不限") || params.equals("全杭州")) {
						zone = "";
						bikeType = "";
					} else {
						if (params.equals("单人车") || params.equals("双人车")
								|| params.equals("多人车"))
							bikeType = params;
						else {
							zone = params;
						}
					}
					string = new String("/providers/filter?bikeType="
							+ bikeType + "&zone=" + zone + "&order="+order+"&lng="
							+ staticLatLnglatLng.longitude + "&lat="
							+ staticLatLnglatLng.latitude);

					// Log.v("lishang", "790  string  " + string + "--"
					// + staticLatLnglatLng.latitude + "  "
					// + staticLatLnglatLng.longitude + " cur "
					// + currentlatLng.latitude + "---"
					// + currentlatLng.longitude);
					jsonObject = client.get(string);
					// Log.v("lishang", "790  string" + string + "--" +
					// jsonObject);
					Message.obtain(internetComHander,
							ConstantTool.GET_STORE_FILTER).sendToTarget();
					break;
				default:
					break;
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	public void getLocalMarkObjects(LatLng latLng) {
		// new InternetComThread(latLng).start();
	}

	public void showTitle(LatLng locationInfo) {

		LinearLayout linearlayout = new LinearLayout(getApplicationContext());
		linearlayout.setOrientation(LinearLayout.VERTICAL);
		TextView text = new TextView(getApplicationContext());
		text.setText("目标位置");
		text.setGravity(Gravity.CENTER);
		text.setTextColor(getResources().getColor(R.color.black));
		linearlayout.addView(text);
		TextView text1 = new TextView(getApplicationContext());
		text1.setText("    ");
		text1.setGravity(Gravity.CENTER);
		linearlayout.addView(text1);
		TextView text2 = new TextView(getApplicationContext());
		text2.setText("    ");
		text2.setGravity(Gravity.CENTER);
		linearlayout.addView(text2);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		linearlayout.setLayoutParams(lp1);
		// 圆角背景
		linearlayout.setBackgroundResource(R.color.transparent);
		InfoWindow title = new InfoWindow(linearlayout, locationInfo, null);
		mBaiduMap.showInfoWindow(title);
	}

	private ParcelableStoreDetail getStoreDetail(JSONObject jsonObject) {
		if (jsonObject == null)
			return null;
		ParcelableStoreDetail parcelableDate = null;
		try {

			List<HashMap<String, String>> commentsList = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < jsonObject.getJSONArray("comments").length(); i++) {
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("comment", ""
						+ jsonObject.getJSONArray("comments").getJSONObject(i)
								.get("comment"));
				Log.v("lishang", "dddd");
				hashMap.put("grade", ""
						+ jsonObject.getJSONArray("comments").getJSONObject(i)
								.get("grade"));
				hashMap.put("nickname", ""
						+ jsonObject.getJSONArray("comments").getJSONObject(i)
								.get("nickname"));
				// Calendar calendar = Calendar.getInstance();
				// System.out.println(calendar.getTimeInMillis());
				// SimpleDateFormat dateFormat = new SimpleDateFormat("hhmmss");
				// System.out.println(dateFormat.format(calendar.getTime()));

				Date date = new Date(Long.parseLong(""
						+ jsonObject.getJSONArray("comments").getJSONObject(i)
								.get("commentDate")));
//				Log.v("lishang123", date + "");
//				commentsList.add(hashMap);
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd");
				String dateString = formatter.format(date);

				// hashMap.put("commentDate",
				// ""
				// + ""
				// + jsonObject.getJSONArray("comments")
				// .getJSONObject(i).get("commentDate"));
				//
				//
				hashMap.put("commentDate", dateString);
				Log.v("lishang123", dateString);
				commentsList.add(hashMap);
			}
			List<HashMap<String, String>> bikeDetail = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < jsonObject.getJSONArray("bikes").length(); i++) {
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("total", ""
						+ jsonObject.getJSONArray("bikes").getJSONObject(i)
								.get("total"));
				hashMap.put("remainNum", ""
						+ jsonObject.getJSONArray("bikes").getJSONObject(i)
								.get("remainNum"));
				hashMap.put("id", (String) jsonObject.getJSONArray("bikes")
						.getJSONObject(i).get("id"));
				hashMap.put("price", ""
						+ jsonObject.getJSONArray("bikes").getJSONObject(i)
								.get("price"));
				hashMap.put("name", (String) jsonObject.getJSONArray("bikes")
						.getJSONObject(i).get("name"));
				hashMap.put("type", (String) jsonObject.getJSONArray("bikes")
						.getJSONObject(i).get("type"));
				hashMap.put("photo", ""+jsonObject.getJSONArray("bikes")
						.getJSONObject(i).get("photo"));					
				Log.v("lishang1234567", "bike12345" + jsonObject.getJSONArray("bikes")
						.getJSONObject(i).get("photo"));				
				bikeDetail.add(hashMap);
				Log.v("lishang1234567", "bike" + bikeDetail);
			}

			// HashMap<String, String>storeDetail = new HashMap<String,
			// String>();

			HashMap<String, String> storeDetail = new HashMap<String, String>();
			storeDetail.put("id", (String) jsonObject.getJSONObject("provider")
					.get("id"));
			storeDetail.put("rank", ""
					+ jsonObject.getJSONObject("provider").get("rank"));
			storeDetail.put("phone",
					(String) jsonObject.getJSONObject("provider").get("phone"));
			storeDetail.put(
					"location",
					(String) jsonObject.getJSONObject("provider").get(
							"location"));
			storeDetail.put("name",
					(String) jsonObject.getJSONObject("provider").get("name"));
			storeDetail.put("owner",
					(String) jsonObject.getJSONObject("provider").get("owner"));
			storeDetail.put("zone",
					(String) jsonObject.getJSONObject("provider").get("zone"));
			storeDetail.put("city",
					(String) jsonObject.getJSONObject("provider").get("city"));
			storeDetail.put("grade", ""
					+ jsonObject.getJSONObject("provider").get("grade"));
			if (popClickSelection)
				storeDetail.put("distance", distance_Store);
			else
				storeDetail.put("distance", ""
						+ mListItemTmp.get(itemposion).get("storedistance"));

			// Log.v("lishang1", "1093"+
			// mListItemTmp.get(itemposion).get("storedistance"));

			// itemposion
			parcelableDate = new ParcelableStoreDetail(bikeDetail,
					commentsList, storeDetail);
			// Log.v("lishang", 877+""+parcelableDate.storeDetailMap);
			// 不构造 只能是空指针
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parcelableDate;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v("lishang", 929 + "");

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!firstPressed) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							firstPressed = true;
							Log.v("lishang", 937 + "");
							Message.obtain(internetComHander,
									ConstantTool.PRESS_INFO_EXIT)
									.sendToTarget();
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						firstPressed = false;
					}
				}).start();
			} else {
				// 程序彻底退出
				ActivityStackManager.getInstance().exitApplication();
				finish();
			}
		}
		return true;
	}

	private int[] imgViewIDArray = { R.id.ImageView_rent,
			R.id.ImageView_advice, R.id.ImageView_share,
			R.id.ImageView_personal };
	private int[] textViewIDArray = { R.id.bikerent, R.id.line_advice,
			R.id.routershare, R.id.personalinfo };
	private int[] layoutIDArray = { R.id.linearlayoutrent,
			R.id.linearlayoutrouting, R.id.linearlayoutshare,
			R.id.linearlayoutperinfo };
	private int[] imgViewIDArrayA = { R.drawable.icon_zuche_unpressed,
			R.drawable.icon_tuijian_unpressed,
			R.drawable.icon_fenxiang_unpressed, R.drawable.icon_user_unpressed };
	private int[] imgViewIDArrayB = { R.drawable.icon_zuche_pressed,
			R.drawable.icon_tuijian_pressed, R.drawable.icon_fenxiang_pressed,
			R.drawable.icon_user_pressed };

	void setTabBackgroud(int tabpositon, int[] imgViewIDArray,
			int[] textViewIDArray, int[] layoutIDArray, int[] imgViewIDArrayA,
			int[] imgViewIDArrayB) {
		TextView textView;
		LinearLayout linearLayout;
		ImageView imageView;
		for (int i = 0; i < imgViewIDArrayA.length; i++) {
			if (i == tabpositon) {
				textView = (TextView) findViewById(textViewIDArray[i]);
				textView.setTextColor(getResources().getColor(
						R.color.ebike_green_light));
				linearLayout = (LinearLayout) findViewById(layoutIDArray[i]);
				linearLayout.setBackgroundColor(getResources().getColor(
						R.color.linearselect));
				imageView = (ImageView) findViewById(imgViewIDArray[i]);
				imageView.setImageResource(imgViewIDArrayB[i]);
			} else {
				textView = (TextView) findViewById(textViewIDArray[i]);
				textView.setTextColor(getResources().getColor(
						R.color.darkgray999));
				linearLayout = (LinearLayout) findViewById(layoutIDArray[i]);
				linearLayout.setBackgroundColor(getResources().getColor(
						R.color.linearunselect));
				imageView = (ImageView) findViewById(imgViewIDArray[i]);
				imageView.setImageResource(imgViewIDArrayA[i]);

			}
		}
	}

}
