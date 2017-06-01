package org.riderman.selfspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.riderman.ConstantTool;
import org.riderman.ExitApplication;
import org.riderman.Homemenu;
import org.riderman.R;
import org.riderman.exception.NetException;
import org.riderman.tools.CommonTool;
import org.riderman.tools.DownloadImage;
import org.riderman.tools.SimpleHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderList extends Activity {

	private Button backBtn;
	private ListView orderListView;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private MyAdapter adapter;
	private int status = -1;

	private Button orderListTextView;
	private LinearLayout statusFilterLayout;
	private LinearLayout listLayout;

	// 状态过滤按钮
	private TextView allTextView;
	private TextView toPayTextView;
	private TextView payedTextView;
	private LinearLayout transLayout;
	private int cmd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		cmd = intent.getIntExtra("PAY_SUCCESS", 0xff);

		setContentView(R.layout.activity_selfspace_orderlist);
		ExitApplication.getInstance().addActivity(this);

		statusFilterLayout = (LinearLayout) findViewById(R.id.statusfilter_layout);
		listLayout = (LinearLayout) findViewById(R.id.list_layout);
		orderListTextView = (Button) findViewById(R.id.orderlist_textview);
		backBtn = (Button) findViewById(R.id.backBtn);
		orderListView = (ListView) findViewById(R.id.order_listview);

		allTextView = (TextView) findViewById(R.id.all_textview);
		toPayTextView = (TextView) findViewById(R.id.topay_textview);
		payedTextView = (TextView) findViewById(R.id.payed_textview);
		transLayout = (LinearLayout) findViewById(R.id.trans_layout);
		allTextView.setOnClickListener(statusListener);
		toPayTextView.setOnClickListener(statusListener);
		payedTextView.setOnClickListener(statusListener);

		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				OrderList.this.finish();
				if (cmd == ConstantTool.PAY_SUCCESS) {
					Intent intent = new Intent(OrderList.this, Homemenu.class);
					startActivity(intent);
				}
			}
		});

		orderListTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				statusFilterLayout.setVisibility(View.VISIBLE);
				listLayout.setAlpha(0.5f);
			}
		});
		orderListView.setDividerHeight(0);// 不显示分割线

		new Thread(new Runnable() {

			@Override
			public void run() {
				int msgCode = -1;
				try {
					mData = getData(-1);
					msgCode = 0;
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msgCode = 99;
				}
				handler.sendEmptyMessage(msgCode);
			}
		}).start();

		adapter = new MyAdapter(this);
		orderListView.setAdapter(adapter);

		transLayout.getBackground().setAlpha(100);

		transLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				statusFilterLayout.setVisibility(View.INVISIBLE);
				listLayout.setAlpha(1.0f);
			}
		});
	}

	// 获取list数据
	private List<Map<String, Object>> getData(int status) throws JSONException,
			NetException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		SharedPreferences appInfo = getSharedPreferences("app_info", 0);
		String sid = appInfo.getString("sid", "");
		SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN,
				sid);
		JSONObject jsonObject = client.get("/selfspace/rentOrders?status="
				+ status + "&page=0&pageSize=100");
		Log.d("mmmm", "我的订单列表的JSON:" + jsonObject);
		Integer total = jsonObject.getInt("total");
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			Map map = new HashMap();
			map = new HashMap<String, Object>();
			map.put("img", json.getString("providerPhoto"));
			map.put("orderId", json.getString("orderId"));
			map.put("provider", json.getString("providerName"));
			map.put("bikeType", json.getString("description"));
			map.put("price", json.get("price"));
			map.put("status", json.getInt("status"));
			list.add(map);
			String img = json.getString("providerPhoto");
			//下载图片
			if (img != null && !img.isEmpty() && !img.equals("null")) {
				CommonTool.downloadBikeImage(img);
			}	
		}

		return list;
	}

	// 列表模块实现
	public final class ViewHolder {
		public ImageView bikeImageView;
		public TextView providerTextView;
		public TextView bikeTypeTextView;
		public TextView priceTextView;
		public Button payBtn;
		public TextView statusTextView;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();

				convertView = mInflater.inflate(
						R.layout.list_item_selfspace_order, null);
				holder.bikeImageView = (ImageView) convertView
						.findViewById(R.id.bike_imageview);
				holder.providerTextView = (TextView) convertView
						.findViewById(R.id.provider_textview);
				holder.bikeTypeTextView = (TextView) convertView
						.findViewById(R.id.biketype_textview);
				holder.priceTextView = (TextView) convertView
						.findViewById(R.id.price_textview);
				holder.payBtn = (Button) convertView.findViewById(R.id.payBtn);
				holder.statusTextView = (TextView) convertView
						.findViewById(R.id.status_textview);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String img = (String) mData.get(position).get("img");
			if (img != null && !img.isEmpty() && !img.equals("null")) {
				holder.bikeImageView.setImageBitmap(CommonTool.getBikeBitmapFromSD(img));
			}
			holder.providerTextView.setText((String) mData.get(position).get(
					"provider"));
			String bikeType = (String) mData.get(position).get("bikeType");
			bikeType = bikeType.replaceAll(";", "\n");
			holder.bikeTypeTextView.setText(bikeType);
			holder.priceTextView
					.setText(mData.get(position).get("price") + "元");
			int status = (Integer) mData.get(position).get("status");
			if (status == 1) {
				holder.payBtn.setVisibility(View.VISIBLE);
				holder.payBtn.setText("付款");
				holder.payBtn.setOnClickListener(new PayClickListener(position,
						adapter));
				holder.statusTextView.setText("待付款");
			} else if (status == 2) {
				holder.payBtn.setVisibility(View.VISIBLE);
				holder.payBtn.setText("评价");
				holder.payBtn.setOnClickListener(new CommentClickListener(
						position, adapter));
				holder.statusTextView.setText("交易成功");
			} else if (status == 3) {
				holder.payBtn.setVisibility(View.INVISIBLE);
				holder.statusTextView.setText("已评价");
			}
			return convertView;
		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			listLayout.setAlpha(1.0f);
			Log.d("mmmm", "list数据返回" + mData);
			switch (msg.what) {
			case 0:
				statusFilterLayout.setVisibility(View.INVISIBLE);
				adapter.notifyDataSetChanged();
				break;
			case 99:
				Toast.makeText(OrderList.this, "网络异常，请确认是否联网",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	};

	private OnClickListener statusListener = new OnClickListener() { // 坚挺

		@Override
		public void onClick(View v) {
			status = -1;
			switch (v.getId()) {
			case R.id.all_textview: // 待付款
				status = -1;
				break;
			case R.id.topay_textview: // 待付款
				status = 1;
				break;
			case R.id.payed_textview: // 已付款
				status = 2;
				break;
			default:
				break;
			}
			// 发送请求
			new Thread(new Runnable() {

				@Override
				public void run() {
					int msgCode = -1;
					try {
						mData = getData(status);
						msgCode = 0;
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (NetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msgCode = 99;
					}
					handler.sendEmptyMessage(msgCode);
				}
			}).start();
		}
	};

	private class PayClickListener implements OnClickListener {

		private int position;
		private MyAdapter myAdapter;

		public PayClickListener(int position, MyAdapter myAdapter) {
			super();
			this.position = position;
			this.myAdapter = myAdapter;
		}

		@Override
		public void onClick(View v) {

			myAdapter.notifyDataSetChanged();
		}

	};

	private class CommentClickListener implements OnClickListener {

		private int position;
		private MyAdapter myAdapter;

		public CommentClickListener(int position, MyAdapter myAdapter) {
			super();
			this.position = position;
			this.myAdapter = myAdapter;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(OrderList.this, OrderComment.class);
			intent.putExtra("position", position);
			intent.putExtra("orderId", mData.get(position).get("orderId")
					.toString());
			startActivityForResult(intent, 1);
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1: // 评价界面返回
			int position = data.getIntExtra("position", -1);
			Log.d("mmmm", "评价界面返回" + position);// 刷新页面
			mData.get(position).put("status", 3);
			adapter.notifyDataSetChanged();
			break;
		case 2: // 付款界面返回
			Log.d("mmmm", "付款界面返回");// 刷新页面
			break;
		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (cmd == ConstantTool.PAY_SUCCESS) {
			Intent intent = new Intent(OrderList.this, Homemenu.class);
			startActivity(intent);
		}
	}

}
