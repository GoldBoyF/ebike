package org.riderman;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.riderman.selfspace.OrderList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentConfirm extends Activity {

	private ListView mListView;
	private List<Map<String, Object>> mListItem;
	private Button payConfirmButton;
	private ImageButton backButton;
	private ActivityStackManager activityStackManager;
	private ParcelableOrderDetail parcelableOrderDetail;
	private Handler myHandler;
	protected ProgressDialog progressDialogTrade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paymentconfirm);
		activityStackManager = ActivityStackManager.getInstance();

		Intent intent = getIntent();
		parcelableOrderDetail = (ParcelableOrderDetail) intent
				.getParcelableExtra("OrderDetail");

		// Log.v("lishang", "45 " + parcelableOrderDetail.getDataMessage()
		// + "list len " + parcelableOrderDetail.getList().size() + " ss "
		// + parcelableOrderDetail.getList().get(0).toString());

		mListItem = getBookDetailList();
		mListView = (ListView) findViewById(R.id.bikeorderlist);
		MyAdapter adapter = new MyAdapter(this);
		mListView.setAdapter(adapter);
		mListView.setClickable(false);
 
		new Utility();
		Utility.setListViewHeightBasedOnChildren(mListView);
		mListView.setDividerHeight(0);
		payConfirmButton = (Button) findViewById(R.id.payconfirmbutton);

		myHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case ConstantTool.PAY_Info_SEND:
					progressDialogTrade.show();
					progressDialogTrade.setMessage("你还未登录，请登录后使用...");
					break;
				case ConstantTool.PAY_INFO_CLOSE:
					progressDialogTrade.cancel();
					Intent intent = new Intent(PaymentConfirm.this, Login.class);
					intent.putExtra("OrderDetail", parcelableOrderDetail);
					intent.putExtra("PAY_TOGO", ConstantTool.PAY_TOGO);
					startActivity(intent);
					finish();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}

		};

		payConfirmButton.setOnClickListener(new OnClickListener() {
			private TextView totalcost;

			@Override
			public void onClick(View v) {

				if (!loginCkeck()) {
					progressDialogTrade = new ProgressDialog(
							PaymentConfirm.this);
					progressDialogTrade
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialogTrade.setCanceledOnTouchOutside(false);
					progressDialogTrade.setCancelable(false);
					totalcost = (TextView) findViewById(R.id.totalcost);					
					parcelableOrderDetail.timeMessage = totalcost.getText().toString();
							
					new Thread() {
						@Override
						public void run() {
							try {
								// 由线程来控制进度
								Message.obtain(myHandler,
										ConstantTool.PAY_Info_SEND)
										.sendToTarget();
								Thread.sleep(1000);
								Message.obtain(myHandler,
										ConstantTool.PAY_INFO_CLOSE)
										.sendToTarget();
							} catch (Exception e) {
								progressDialogTrade.cancel();
							}
						}
					}.start();

				} else {
					// TODO Auto-generated method stub
					totalcost = (TextView) findViewById(R.id.totalcost);
					Intent intent = new Intent(PaymentConfirm.this,
							TaobaoConfirm.class);

					for (Map<String, Object> map : mListItem) {

						// parcelableOrderDetail.list
						List<HashMap<String, String>> orderList = parcelableOrderDetail
								.getList();

						// bundle.putBundle(key, value)

					}

					parcelableOrderDetail.timeMessage = totalcost.getText()
							.toString();
					intent.putExtra("TotalCost", parcelableOrderDetail);

					startActivity(intent);
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
		TextView totalCost = (TextView) findViewById(R.id.totalcost);
		totalCost.setText(parcelableOrderDetail.getTotalCost());

		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);// 获取年份
		int month = ca.get(Calendar.MONTH) + 1;// 获取月份
		int day = ca.get(Calendar.DATE);// 获取日
		String dataMessage = year + "年" + month + "月" + day + "日";

		TextView book_date_confirm = (TextView) findViewById(R.id.book_date_confirm);
		book_date_confirm.setText(dataMessage);
	}

	public final class ViewHolder {
		public TextView bikesort;
		public TextView bikenumb;
		public TextView booktime;
		public TextView usagetime;
		public TextView bikeprice;
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
				convertView = mInflater.inflate(R.layout.list_item_payment,
						null);
				holder.bikesort = (TextView) convertView
						.findViewById(R.id.bikesort_detail);
				holder.bikenumb = (TextView) convertView
						.findViewById(R.id.bikenumb);
				holder.booktime = (TextView) convertView
						.findViewById(R.id.booktime);
				holder.usagetime = (TextView) convertView
						.findViewById(R.id.usagetime);
				holder.bikeprice = (TextView) convertView
						.findViewById(R.id.storedistance);
				convertView.setTag(holder);// why
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// Log.v("lishang", "142 "+mListItem);
			holder.bikesort.setText((String) mListItem.get(position).get(
					"bikesort"));
			holder.bikenumb.setText((String) mListItem.get(position).get(
					"bikenumb"));
			holder.booktime.setText((String) mListItem.get(position).get(
					"booktime"));
			holder.usagetime.setText((String) mListItem.get(position).get(
					"usagetime"));
			holder.bikeprice.setText((String) mListItem.get(position).get(
					"bikeprice"));
			return convertView;
		}
	}

	private List<Map<String, Object>> getBookDetailList() {
		// 打包返回数据 留给上面分析
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List<HashMap<String, String>> orderList = parcelableOrderDetail
				.getList();
		for (int i = 0; i < parcelableOrderDetail.getList().size(); i++) {

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("bikenumb", orderList.get(i).get("bikenumb"));
			map.put("bikesort", orderList.get(i).get("bikesort"));
			map.put("bikeprice", orderList.get(i).get("bikeprice"));
			map.put("booktime", parcelableOrderDetail.getDataMessage());
			map.put("usagetime", parcelableOrderDetail.getTimeMessage());
			map.put("id", orderList.get(i).get("id"));
			Log.v("lishang12", "id  ");
			Log.v("lishang12", "id  " + orderList.get(i).get("id"));
			list.add(map);
		}
		// parcelableOrderDetail
		return list;
	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
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

	public boolean loginCkeck() {

		SharedPreferences appInfo = getSharedPreferences("app_info", 0);
		boolean autoLogin = appInfo.getBoolean("autoLogin", false);

		if (autoLogin) {
			String username = appInfo.getString("username", "");
			String password = appInfo.getString("password", "");
			Log.v("lishang", "已登录");
			return true;
		}
		Log.v("lishang", "未登录");

		return false;
	}
}
