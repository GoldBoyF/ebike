package org.riderman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.riderman.tools.CommonTool;

import com.baidu.platform.comapi.map.s;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class BikeStoreDetail extends Activity {

	private boolean state = false;
	private ListView bikestoreListView;
	private ListView perOptionListView;
	private List<Map<String, Object>> mListItemComments;
	private List<Map<String, Object>> mListItemBikeType;
	private ImageButton backButton;
	private Button bookInStore;
	private ActivityStackManager activityStackManager;
	private ParcelableStoreDetail parcelableDate;
	private TextView storeowncontact;
	private List<Bitmap> bitmapsList;
	private HashMap<String, Object> mapimg;
	private Handler handler;
	private BikeTypeListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bikestoredetails);
		activityStackManager = ActivityStackManager.getInstance();

		Intent intent = getIntent();
		intent.getExtras();
		intent.getParcelableExtra("StoreDetail");
		parcelableDate = intent.getParcelableExtra("StoreDetail");
		state = false;
		// Log.v("lishang",
		// "Bikestore  " + parcelableDate.commentListCount + "len  "
		// + parcelableDate.commentList.size() + "  "
		// + parcelableDate.bikeDetail + "  "
		// + parcelableDate.bikeDetail.size());
		// 创建列表。scrow布局里面显示双列表
		mListItemComments = getStoreDetailData(parcelableDate, 1);
		// for (int i = 0; i < mListItemComments.size(); i++)
		// Log.v("lishang", "57  " + mListItemComments.get(i));

		bikestoreListView = (ListView) findViewById(R.id.bikestoredetail);
		mListItemBikeType = getStoreDetailData(parcelableDate, 2);
		adapter = new BikeTypeListAdapter(BikeStoreDetail.this);
		bikestoreListView.setAdapter(adapter);
		bikestoreListView.setDividerHeight(0);
		new Utility();
		Utility.setListViewHeightBasedOnChildren(bikestoreListView);

		perOptionListView = (ListView) findViewById(R.id.peroption);
		AdviceAdapter adapterq = new AdviceAdapter(this);
		perOptionListView.setAdapter(adapterq);
		Utility.setListViewHeightBasedOnChildren(perOptionListView);
		perOptionListView.setDividerHeight(0);
		perOptionListView.setClickable(false);
		bikestoreListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Toast.makeText(BikeStoreDetail.this, "测试", Toast.LENGTH_LONG)
				// .show();
			}
		});

		// getBitmap();

		backButton = (ImageButton) findViewById(R.id.backbutton);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 返回
				finish();
			}
		});
		bookInStore = (Button) findViewById(R.id.bookinstore);
		bookInStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BikeStoreDetail.this,
						BikeOrder.class);
				intent.putExtra("StoreDetail", parcelableDate);
				startActivity(intent);
			}
		});
		TextView textView = (TextView) findViewById(R.id.storeownername);
		textView.setText(parcelableDate.storeDetailMap.get("owner"));
		storeowncontact = (TextView) findViewById(R.id.storeowncontact);
		storeowncontact.setText(parcelableDate.storeDetailMap.get("phone"));
		Button imageButton = (Button) findViewById(R.id.dialbutton);
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL); // android.intent.action.DIAL
				intent.setData(Uri.parse("tel:"
						+ storeowncontact.getText().toString()));
				startActivity(intent);
			}
		});
		handler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				// TODO Auto-generated method stub
				super.dispatchMessage(msg);

				if (msg.what == 0x01) {
					Log.v("lishang12344567", "lishang12344567" + "  msg");

					state = true;
					adapter.notifyDataSetChanged();
					// new Utility();
					// Utility.setListViewHeightBasedOnChildren(bikestoreListView);

				}
			}

		};
		getBitmap();
	}

	public final class ViewHolderContent {
		public TextView bikesort;
		public TextView bikenumb;
		public TextView booktime;
		public TextView usagetime;
		public TextView bikeprice;
		public TextView bikefree_detail;
		public ImageView bikeImageView;

	}

	public final class ViewHolderTitle {
		public TextView storetitle;
		public RatingBar storerating;
		public TextView storeaddr;
		public TextView storedistance;

	}

	public final class ViewHolderAdvice {
		public TextView username;
		public RatingBar useradvice;
		public TextView usertime;
		public TextView usercontent;

	}

	public class BikeTypeListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public BikeTypeListAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mListItemBikeType.size() + 1;

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
			ViewHolderContent bikeStoreTypeDetail = null;
			ViewHolderTitle titleHoder = null;
			Log.v("lishang", "" + position);
			if (position == 0) {
				if (convertView == null) {
					titleHoder = new ViewHolderTitle();// 接受对象
					convertView = mInflater.inflate(
							R.layout.list_item_bike_store_title, null);
					titleHoder.storetitle = (TextView) convertView
							.findViewById(R.id.storetitle);
					titleHoder.storerating = (RatingBar) convertView
							.findViewById(R.id.storerating);
					titleHoder.storeaddr = (TextView) convertView
							.findViewById(R.id.storeaddr);
					titleHoder.storedistance = (TextView) convertView
							.findViewById(R.id.storedistance);
					convertView.setTag(titleHoder);// why
				} else {
					titleHoder = (ViewHolderTitle) convertView.getTag();
				}
				// 此处是扩展的地方 显示店铺详情
				Log.v("lishang1", "176" + parcelableDate.storeDetailMap + "");
				titleHoder.storetitle.setText(parcelableDate.storeDetailMap
						.get("name"));
				titleHoder.storerating.setNumStars(5);
				titleHoder.storerating
						.setRating(Float
								.parseFloat(parcelableDate.storeDetailMap
										.get("grade")));
				titleHoder.storerating.setScaleX(0.7f);
				titleHoder.storerating.setScaleY(0.7f);

				titleHoder.storeaddr.setText(parcelableDate.storeDetailMap
						.get("location"));

				titleHoder.storedistance.setText(""
						+ (parcelableDate.storeDetailMap.get("distance")));

				// //
			} else {
				bikeStoreTypeDetail = new ViewHolderContent();// 接受对象
				convertView = mInflater.inflate(R.layout.list_item_bike_type,
						null);
				bikeStoreTypeDetail.bikeprice = (TextView) convertView
						.findViewById(R.id.bikeprice_detail);
				bikeStoreTypeDetail.bikesort = (TextView) convertView
						.findViewById(R.id.bikesort_detail);
				bikeStoreTypeDetail.bikefree_detail = (TextView) convertView
						.findViewById(R.id.bikefree_detail);
				bikeStoreTypeDetail.bikeImageView = (ImageView) convertView
						.findViewById(R.id.imageViewbikeimg);
				if (bikeStoreTypeDetail.bikeImageView == null) {
					Log.v("lishang55", "12333");

				}
				bikeStoreTypeDetail.bikeprice.setText(""
						+ mListItemBikeType.get(position - 1).get("price")
						+ "元/小时");
				bikeStoreTypeDetail.bikesort.setText(""
						+ mListItemBikeType.get(position - 1).get("type")
						+ " :"
						+ mListItemBikeType.get(position - 1).get("name"));
				bikeStoreTypeDetail.bikefree_detail.setText("可租："
						+ mListItemBikeType.get(position - 1).get("remainNum")
						+ "辆" + " 已租："
						+ mListItemBikeType.get(position - 1).get("total")
						+ "辆");
				if (state) {
					String photo = parcelableDate.bikeDetail.get(position - 1)
							.get("photo");
					Log.v("lishang888", photo);
					if (photo != null && !photo.isEmpty()
							&& !photo.equals("null")) {

						if (CommonTool.getBikeBitmapFromSD(photo) == null) {
						} else
							bikeStoreTypeDetail.bikeImageView
									.setImageBitmap(CommonTool
											.getBikeBitmapFromSD(photo));
					}
				}
			}
			return convertView;

		}
	}

	public class AdviceAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public AdviceAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mListItemComments.size();
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

			ViewHolderAdvice adviceHoder = null;
			Log.v("lishang12345", "" + position);
			if (convertView == null) {
				adviceHoder = new ViewHolderAdvice();// 接受对象
				convertView = mInflater
						.inflate(R.layout.list_item_advice, null);
				adviceHoder.username = (TextView) convertView
						.findViewById(R.id.username);
				adviceHoder.useradvice = (RatingBar) convertView
						.findViewById(R.id.userratingBar);
				adviceHoder.usercontent = (TextView) convertView
						.findViewById(R.id.usercontent);
				adviceHoder.usertime = (TextView) convertView
						.findViewById(R.id.usertime);
				convertView.setTag(adviceHoder);// why
			} else {
				adviceHoder = (ViewHolderAdvice) convertView.getTag();
			}
			// 此处是扩展的地方
			// Log.v("lishang", "238  "+ )
			adviceHoder.username.setText(""
					+ mListItemComments.get(position).get("nickname"));
			adviceHoder.useradvice.setNumStars(5);
			adviceHoder.useradvice.setRating(Float.parseFloat(""
					+ mListItemComments.get(position).get("grade")));
			adviceHoder.useradvice.setScaleX(0.5f);
			adviceHoder.useradvice.setScaleY(0.50f);
			// adviceHoder.usertime.setText("2014-7-19");
			adviceHoder.usertime.setText(""
					+ mListItemComments.get(position).get("commentDate"));
			adviceHoder.usercontent.setText(""
					+ mListItemComments.get(position).get("comment"));
			return convertView;
		}
	}

	private List<Map<String, Object>> getStoreDetailData(
			ParcelableStoreDetail parcelableDate, int type) {
		// 打包返回数据 留给上面分析
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// parcelableDate

		if (type == 1) {
			for (int i = 0; i < parcelableDate.commentListCount; i++) {
				// list整体是个引用 要标记数目
				// 不可以加引用 要加对象 否则进入一个对象 ，面向对象的变成，不要混淆
				HashMap<String, Object> map = new HashMap<String, Object>();
				Log.v("lishang123456", "w3-"
						+ parcelableDate.commentList.get(i).get("comment")
						+ "-");
				if (parcelableDate.commentList.get(i).get("comment").equals("")) {
					map.put("comment", "该用户很懒，没有留下任何评价");
				} else {
					map.put("comment",
							parcelableDate.commentList.get(i).get("comment"));
				}
				map.put("commentDate",
						parcelableDate.commentList.get(i).get("commentDate"));

				if (parcelableDate.commentList.get(i).get("nickname")
						.equals("null")) {

					map.put("nickname", "匿名");
				} else
					map.put("nickname",
							parcelableDate.commentList.get(i).get("nickname"));
				map.put("grade", parcelableDate.commentList.get(i).get("grade"));

				list.add(map);

			}

		}
		if (type == 2) {
			// getBitmap();
			for (int i = 0; i < parcelableDate.bikeDetailCount; i++) {
				mapimg = new HashMap<String, Object>();
				mapimg.put("total",
						parcelableDate.bikeDetail.get(i).get("total"));
				mapimg.put("remainNum",
						parcelableDate.bikeDetail.get(i).get("remainNum"));
				mapimg.put("id", parcelableDate.bikeDetail.get(i).get("id"));
				mapimg.put("price",
						parcelableDate.bikeDetail.get(i).get("price"));
				mapimg.put("name", parcelableDate.bikeDetail.get(i).get("name"));
				mapimg.put("type", parcelableDate.bikeDetail.get(i).get("type"));
				mapimg.put("disatance",
						parcelableDate.bikeDetail.get(i).get("disatance"));
				list.add(mapimg);
			}
		}
		return list;
	}

	protected void onRestart() {
		// TODO Auto-generated method stub
		activityStackManager.popActivity(this);
		super.onRestart();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		activityStackManager.pushActivity(this);
		super.onStop();

	}

	private String pathString = null;

	private void getBitmap() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				bitmapsList = new ArrayList<Bitmap>();
				Log.v("lishang123456", "" + parcelableDate.bikeDetail.size());
				// TODO Auto-generated method stub
				for (int i = 0; i < parcelableDate.bikeDetail.size(); i++)

				{
					Log.v("lishang123456", ""
							+ parcelableDate.bikeDetail.get(i).get("photo"));
					pathString = parcelableDate.bikeDetail.get(i).get("photo");
					Bitmap bitmap = null;
					Log.d("mmm", "下载图片：" + pathString);
					if (pathString != null && !pathString.isEmpty()
							&& !pathString.equals("null")) {
						CommonTool.downloadBikeImage(pathString);
					}
				}
				Message.obtain(handler, 0x01).sendToTarget();
			}
		}).start();

	}
}
