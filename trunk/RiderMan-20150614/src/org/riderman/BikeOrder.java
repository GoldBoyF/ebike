package org.riderman;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.riderman.tools.CommonTool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BikeOrder extends Activity {

	private ListView mListView;

	private List<Map<String, Object>> mListItem;
	private Button payConfirmButton;
	private ImageButton backButton;
	private int count = 0;
	private ActivityStackManager activityStackManager;

	private String timeStart;
	private String timeEnd;
	private String dataMessage;
	private MyAdapter myAdapter;
	private DatePicker dataPicker;
	private String TotalCosat;
	// 设定可选
	private final String[] Hours = {   "7", "8",
			"9", "10", "11", "12", "13", "14", "15", "16", "17", "18"  };
	private final String[] Hours1 = {   "7:00", "8:00",
			"9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"  };

	private ParcelableStoreDetail parcelableDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bike_order);
		activityStackManager = ActivityStackManager.getInstance();

		Intent intent = getIntent();
		parcelableDate = (ParcelableStoreDetail) intent
				.getParcelableExtra("StoreDetail");
		// Log.v("lishang","62  "+parcelableDate.bikeDetailCount);
		mListItem = getItemList();
		mListView = (ListView) findViewById(R.id.bikestoredetail);
		myAdapter = new MyAdapter(this);
		mListView.setAdapter(myAdapter);
		mListView.setDividerHeight(0);
		mListView.setClickable(false);// 自主屏蔽点击事件
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			}
		});
		new Utility();
		Utility.setListViewHeightBasedOnChildren(mListView);
		payConfirmButton = (Button) findViewById(R.id.payconfirmbutton);
		payConfirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TextView totalcost=(TextView)findViewById(R.id.totalcost);
				double cost = 0;
				int usagetime = Integer.parseInt(timeEnd)
						- Integer.parseInt(timeStart);
				if (usagetime <= 0)
					Toast.makeText(BikeOrder.this, "亲，您选择的时间不对哦",
							Toast.LENGTH_LONG).show();
				else {
					for (Map<String, Object> map : mListItem) {

						cost += Float.parseFloat((String) map.get("bikeprice"))
								* Integer.parseInt((String) map.get("bikenumb"))
								* (Integer.parseInt(timeEnd) - Integer
										.parseInt(timeStart));

					}
					if (cost == 0)
						Toast.makeText(BikeOrder.this, "亲，请选择自行车数目",
								Toast.LENGTH_LONG).show();
					else {
						TotalCosat = "" + cost;
						Log.v("lishang", "89  " + TotalCosat);
						Intent intent = new Intent(BikeOrder.this,
								PaymentConfirm.class);
						List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
						for (Map<String, Object> map : mListItem) {
							int bikenumb = Integer.parseInt((String) map
									.get("bikenumb"));
							if (bikenumb > 0) {
								HashMap<String, String> hashMap = new HashMap<String, String>();
								hashMap.put("bikesort",
										(String) map.get("bikesort"));
								hashMap.put("bikenumb",
										(String) map.get("bikenumb"));
								hashMap.put(
										"bikeprice",
										String.format(
												"%.0f元",
												bikenumb
														* (Integer
																.parseInt(timeEnd) - Integer
																.parseInt(timeStart))
														* Float.parseFloat(""
																+ map.get("bikeprice")))
												+ "  (单价："
												+ map.get("bikeprice")
												+ "元/小时)");
								hashMap.put("id", (String) map.get("id"));
								list.add(hashMap);
								// map.put("bikesort",
								// parcelableDate.bikeDetail.get(i).get("type")
								// + ": " +
								// parcelableDate.bikeDetail.get(i).get("name"));
								// map.put("bikenumb", "0");
								// map.put("booktime", "2014-8-10");
								// map.put("usagetime", "9:00到12:00");
								// map.put("bikeprice",
								// parcelableDate.bikeDetail.get(i).get("price"));

							}
						}
						String timeMessage = timeStart + "：00点到 " + timeEnd
								+ "：00点";
						ParcelableOrderDetail parcelableOrderDetail = new ParcelableOrderDetail(
								dataMessage, TotalCosat, timeMessage, list);
						intent.putExtra("OrderDetail", parcelableOrderDetail);
						startActivity(intent);
					}
				}
			}
		});
		backButton = (ImageButton) findViewById(R.id.backbutton);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 返回
				finish();
			}
		});

		Spinner spinner = (Spinner) findViewById(R.id.SpinnerStart);
		// 将可选内容与ArrayAdapter连接起来
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, Hours1);
		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner.setAdapter(adapter);
		// 添加事件Spinner事件监听
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				timeStart = Hours[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		Spinner spinner2 = (Spinner) findViewById(R.id.spinnerEnd);
		// 将可选内容与ArrayAdapter连接起来
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, Hours1);
		// 设置下拉列表的风格
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner2.setAdapter(adapter2);
		// 添加事件Spinner事件监听
		// 设置默认值
		spinner2.setVisibility(View.VISIBLE);
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				timeEnd = Hours[position];
				Log.v("lishang", "139   " + timeEnd);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		dataPicker = (DatePicker) findViewById(R.id.datePicker1);

		OnDateChangedListener onDateChangedListener = new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				dataMessage = dataPicker.getYear() + "-"
						+ (dataPicker.getMonth() + 1) + "-"
						+ dataPicker.getDayOfMonth();
				// Log.v("lishang", "151  " + dataMessage);
			}
		};
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);// 获取年份
		int month = ca.get(Calendar.MONTH) + 1;// 获取月份
		int day = ca.get(Calendar.DATE);// 获取日
		int minute = ca.get(Calendar.MINUTE);// 分
		int hour = ca.get(Calendar.HOUR);// 小时
		int second = ca.get(Calendar.SECOND);// 秒
		int WeekOfYear = ca.get(Calendar.DAY_OF_WEEK);
		dataPicker.init(year, month, day, onDateChangedListener);
		dataMessage = year + "-" + month + "-" + day;
	}

	public final class ViewHolder {
		public TextView bikesort;
		public EditText bikenumb;
		public TextView booktime;
		public TextView usagetime;
		public TextView bikeprice;
		public Button decreaseButton;
		public Button increaseButton;
		public TextView remainTextView;
		public ImageView imgImageView;

	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private int count;
		private TextView countView;
		private int curPosition;

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
			curPosition = position;
			if (convertView == null) {
				holder = new ViewHolder();// 接受对象
				convertView = mInflater.inflate(R.layout.list_item_bike_order1,
						null);
				holder.bikesort = (TextView) convertView
						.findViewById(R.id.bikesort_detail);
				holder.bikenumb = (EditText) convertView
						.findViewById(R.id.bikenumb);
				// holder.booktime = (TextView) convertView
				// .findViewById(R.id.booktime);
				// holder.usagetime = (TextView) convertView
				// .findViewById(R.id.usagetime);
				holder.bikeprice = (TextView) convertView
						.findViewById(R.id.storedistance);
				holder.decreaseButton = (Button) convertView
						.findViewById(R.id.decrease);
				holder.increaseButton = (Button) convertView
						.findViewById(R.id.increase);
				holder.remainTextView = (TextView) convertView
						.findViewById(R.id.remainTextView);
				holder.imgImageView = (ImageView) convertView
						.findViewById(R.id.imageViewbikeimg);

				convertView.setTag(holder);// why
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.bikesort.setText((String) mListItem.get(position).get(
					"bikesort"));
			holder.bikenumb.setText((String) mListItem.get(position).get(
					"bikenumb"));
			// holder.booktime.setText((String) mListItem.get(position).get(
			// "booktime"));
			// holder.usagetime.setText((String) mListItem.get(position).get(
			// "usagetime"));
			holder.bikeprice.setText((String) mListItem.get(position).get(
					"bikeprice"));
			holder.decreaseButton
					.setOnClickListener(new listButtonOnClickListener(position,
							myAdapter, 0));
			holder.increaseButton
					.setOnClickListener(new listButtonOnClickListener(position,
							myAdapter, 1));
			holder.remainTextView.setText("可租"
					+ mListItem.get(position).get("remainNum") + "辆");
			if (parcelableDate.bikeDetail.get(position).get("photo") != null) {
				holder.imgImageView.setImageBitmap(CommonTool
						.getBikeBitmapFromSD(parcelableDate.bikeDetail.get(
								position).get("photo")));
			}

			// holder.remainTextView.
			// 自定义Adapter
			return convertView;
		}
	}

	private class listButtonOnClickListener implements OnClickListener {

		private int position;
		private MyAdapter myAdapter;
		private int flag;

		public listButtonOnClickListener(int position, MyAdapter myAdapter,
				int flag) {
			super();
			this.position = position;
			this.myAdapter = myAdapter;
			this.flag = flag;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (flag == 0) {
				Log.v("lishang",
						"hello" + (count) + "  "
								+ mListItem.get(position).get("bikenumb"));
				int i = Integer.parseInt((String) mListItem.get(position).get(
						"bikenumb"));
				i--;
				if (i >= 0) {
					i = i > 0 ? i : 0;
					mListItem.get(position).put("bikenumb", new String("" + i));
					int j = Integer.parseInt((String) mListItem.get(position)
							.get("remainNum"));
					j++;
					mListItem.get(position)
							.put("remainNum", new String("" + j));
				}
				myAdapter.notifyDataSetChanged();
			}
			if (flag == 1) {
				Log.v("lishang",
						"hello" + (count) + "  "
								+ mListItem.get(position).get("bikenumb"));

				int i = Integer.parseInt((String) mListItem.get(position).get(
						"remainNum"));
				i--;
				if (i >= 0) {
					int j = Integer.parseInt((String) mListItem.get(position)
							.get("bikenumb"));
					j++;
					mListItem.get(position).put("bikenumb", new String("" + j));

					// i = Integer.parseInt((String)
					// mListItem.get(position).get(
					// "remainNum"));
					// i = i > 0 ? i : 0;
					mListItem.get(position)
							.put("remainNum", new String("" + i));
					myAdapter.notifyDataSetChanged();
				}
			}
		}

	};

	private List<Map<String, Object>> getItemList() {
		// 打包返回数据 留给上面分析
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < parcelableDate.bikeDetail.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("bikesort", parcelableDate.bikeDetail.get(i).get("type")
					+ ": " + parcelableDate.bikeDetail.get(i).get("name"));
			map.put("bikenumb", "0");
			map.put("booktime", "2014-8-10");
			map.put("usagetime", "9:00到12:00");
			Log.v("lishang",
					"dfdf  " + parcelableDate.bikeDetail.get(i).get("id"));
			map.put("id", "" + parcelableDate.bikeDetail.get(i).get("id"));
			map.put("bikeprice", parcelableDate.bikeDetail.get(i).get("price"));
			map.put("remainNum",
					parcelableDate.bikeDetail.get(i).get("remainNum"));
			// map.put("bikeprice",
			// parcelableDate.bikeDetail.get(i).get("price"));

			list.add(map);
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

}
