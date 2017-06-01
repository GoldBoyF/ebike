package org.riderman;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.riderman.selfspace.OrderList;
import org.riderman.tools.SimpleHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class TaobaoConfirm extends Activity {

	private Button payConfirmButton;
	private EditText password;
	private ImageButton backButton;
	private ProgressDialog progressDialogTrade;
	private Handler myHandler;
	private ActivityStackManager activityStackManager;
	private TextView totalCost;
	private ParcelableOrderDetail parcelableOrderDetail;
	private String dataMessage;
	private String startData;
	private String endDate;
	private String idNum = "";
	private String startDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taobaoconfirm);
		activityStackManager = ActivityStackManager.getInstance();
		Intent intent = getIntent();
		parcelableOrderDetail = (ParcelableOrderDetail) intent
				.getParcelableExtra("TotalCost");
		totalCost = (TextView) findViewById(R.id.totalCost);
		totalCost.setText(parcelableOrderDetail.timeMessage);

		payConfirmButton = (Button) findViewById(R.id.payconfirmbutton);
		payConfirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// http通信//阻塞等待 正在传输数据，正在接受数据 跳转订单详情
				{
					EditText passwordTextView = (EditText) findViewById(R.id.password);
					if (passwordTextView.getText().toString().length() < 1)
						Toast.makeText(TaobaoConfirm.this, "密码不能为空",
								Toast.LENGTH_LONG).show();
					else {

						// http通信//阻塞等待 正在传输数据，正在接受数据 跳转支付成功
						new Thread() {
							@Override
							public void run() {
								try {

									// 由线程来控制进度
									Message.obtain(myHandler,
											ConstantTool.PAY_Info_SEND)
											.sendToTarget();
									Thread.sleep(800);
									Message.obtain(myHandler,
											ConstantTool.PAY_INFO_GET)
											.sendToTarget();
									Thread.sleep(800);
									Message.obtain(myHandler,
											ConstantTool.PAY_INFO_SUCCESS)
											.sendToTarget();
									Thread.sleep(800);
									Message.obtain(myHandler,
											ConstantTool.PAY_INFO_CLOSE)
											.sendToTarget();
								} catch (Exception e) {
									progressDialogTrade.cancel();
								}
							}
						}.start();
					}

				}
				progressDialogTrade = new ProgressDialog(TaobaoConfirm.this);
				progressDialogTrade
						.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialogTrade.setCanceledOnTouchOutside(false);
				progressDialogTrade.setCancelable(false);
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

		myHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case ConstantTool.PAY_Info_SEND:
					progressDialogTrade.show();
					progressDialogTrade.setMessage("正在发送数据...");
					break;
				case ConstantTool.PAY_INFO_GET:
					progressDialogTrade.setMessage("正在接受数据...");
					break;
				case ConstantTool.PAY_INFO_SUCCESS:
					progressDialogTrade.setMessage("支付成功 正在跳转...");
					break;
				case ConstantTool.PAY_INFO_CLOSE:

					progressDialogTrade.cancel();
					// startActivity(new Intent(TaobaoConfirm.this,
					// Homemenu.class));
					pay();
					Intent intent = new Intent(TaobaoConfirm.this,
							OrderList.class);
					intent.putExtra("PAY_SUCCESS", ConstantTool.PAY_SUCCESS);
					startActivity(intent);
					// activityStackManager.finishTran();
					finish();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}

		};
	}

	public boolean pay() {
		dataMessage = parcelableOrderDetail.dataMessage;

		List<HashMap<String, String>> orderList = parcelableOrderDetail
				.getList();
		Log.v("lishang",
				"184 44" + parcelableOrderDetail.list.get(0).get("bikenumb")
						+ "size" + parcelableOrderDetail.getList().size());
		;
		for (int i = 0; i < parcelableOrderDetail.getList().size() - 1; i++) {

			idNum += orderList.get(i).get("id") + ":"
					+ orderList.get(i).get("bikenumb") + ";";
			Log.v("lishang",
					"184 44" + parcelableOrderDetail.list.get(0).get("id")
							+ "  " + idNum);
			;

		}
		if (parcelableOrderDetail.getList().size() >= 1)
			idNum += orderList.get(parcelableOrderDetail.getList().size() - 1)
					.get("id")
					+ ":"
					+ orderList.get(parcelableOrderDetail.getList().size() - 1)
							.get("bikenumb");
//		Log.v("lishang", msg)
		startDate = parcelableOrderDetail.getDataMessage();
		endDate = parcelableOrderDetail.getDataMessage();
		new Thread(new Runnable() {
			private JSONObject jsonObject;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String string = new String("/orders/book?details=" + idNum
							+ startDate + endDate); // 需要添加错误处理
					Log.v("lishang", "184 " + string);
					SharedPreferences appInfo = getSharedPreferences(
							"app_info", 0);
					String sid = appInfo.getString("sid", "");
					SimpleHttpClient client = new SimpleHttpClient(
							SimpleHttpClient.DOMAIN, sid);
					Map<String, String> map = new HashMap<String, String>();
					map.put("details", idNum);
					Date currentTime = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String dateString = formatter.format(currentTime);
					map.put("startDate", dateString);
					map.put("endDate", dateString);
					map.put("price", totalCost.getText().toString());
					
					jsonObject = client.post("/orders/book", map);
//					while(jsonObject!=null){
//						
//					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.v("lishang", "jsonObject=null " + (jsonObject == null));
				}
				Log.d("mmmm", "订购成功的JSON"+totalCost.getText().toString()+","+jsonObject);

			}

		}).start();
		return true;
	}

}
