package org.riderman;

import org.riderman.service.BaseService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 用户登录界面
 * 
 * @author jintf
 * 
 */
public class Login extends Activity {

	private Button okBtn;
	private Button cancelBtn;

	private EditText phoneEditText;
	private EditText passwordEditText;

	private TextView registerLinkTextView;

	private boolean isExit; // 当为true时，表示用户点击了“退出登录”按钮，此时按back直接退出。

	private BaseService baseService = new BaseService();
	private ParcelableOrderDetail parcelableOrderDetail;
	protected ProgressDialog progressDialogTrade;
	private int cmd = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Intent intent = getIntent();
		if (intent != null) {
			if (intent.getIntExtra("PAY_TOGO", 0xff) == ConstantTool.PAY_TOGO) {
				parcelableOrderDetail = (ParcelableOrderDetail) intent
						.getParcelableExtra("OrderDetail");

				Log.v("lishang", parcelableOrderDetail.timeMessage);
				cmd = intent.getIntExtra("PAY_TOGO", 0xff);
				Log.v("lishang", "cmd " + cmd);
			}
			if(intent.getIntExtra("Login", 0xff)==ConstantTool.LOG_IN)
			{
				
			}
		}

		registerLinkTextView = (TextView) findViewById(R.id.register_link_textview);
		registerLinkTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		registerLinkTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Login.this, Register.class);
				startActivity(intent);
			}
		});

		okBtn = (Button) findViewById(R.id.okBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		phoneEditText = (EditText) findViewById(R.id.phone_edittext);
		passwordEditText = (EditText) findViewById(R.id.password_edittext);

		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final String username = phoneEditText.getText().toString();
				final String password = passwordEditText.getText().toString();
				if (!username.isEmpty() && !password.isEmpty()) {
					// 发送网络请求
					new Thread(new Runnable() {
						@Override
						public void run() {
							okBtn.setClickable(false);
							int result = baseService.doLogin(Login.this,
									username, password);
							handler.sendEmptyMessage(result);
						}
					}).start();

				} else {
					new AlertDialog.Builder(Login.this).setTitle("警告")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setMessage("用户名和密码不能为空")
							.setPositiveButton("确定", null).show();
				}
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isExit) {
					ExitApplication.getInstance().exit();
				} else {
					Login.this.finish();
				}
			}
		});

		isExit = getIntent().getBooleanExtra("isExit", false);
		if (isExit) {
			cancelBtn.setText("退出");
		}
	}

	@Override
	public void onBackPressed() {
		// do something what you want
		Log.d("mmmm", "在登陆界面点击了Back:" + isExit);
		if (isExit) {
			ExitApplication.getInstance().exit();
		} else {
			super.onBackPressed();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Log.d("mmmm", "在登录界面执行登录"+username+","+password+"结果为"+result);
			switch (msg.what) {
			case 1:
				Intent intent = new Intent();
				setResult(1, intent);
				if (cmd != ConstantTool.PAY_TOGO)
					Login.this.finish();
				else if (cmd == ConstantTool.PAY_TOGO) {
					progressDialogTrade = new ProgressDialog(Login.this);
					progressDialogTrade
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialogTrade.setCanceledOnTouchOutside(false);
					progressDialogTrade.setCancelable(false);
					new Thread() {
						@Override
						public void run() {
							try {
								// 由线程来控制进度
								Message.obtain(handler,
										ConstantTool.PROGRESS_BAR_SHOW)
										.sendToTarget();
								Thread.sleep(1500);
								Message.obtain(handler,
										ConstantTool.PAY_INFO_CLOSE)
										.sendToTarget();
							} catch (Exception e) {
								progressDialogTrade.cancel();
							}
						}
					}.start();
				}
				// parcelableOrderDetail
				break;
			case 0:
				new AlertDialog.Builder(Login.this).setTitle("错误")
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setMessage("用户名或密码错误").setPositiveButton("确定", null)
						.show();
				passwordEditText.setText("");
				break;
			case -1:
				Toast.makeText(Login.this, "网络异常，请确认是否联网", Toast.LENGTH_SHORT)
						.show();
				break;

			// added by li
			case ConstantTool.PROGRESS_BAR_SHOW:
				progressDialogTrade.show();
				progressDialogTrade.setMessage("登陆成功，正跳转支付界面...");
				break;
			case ConstantTool.PAY_INFO_CLOSE:
				progressDialogTrade.cancel();
				Intent intent2 = new Intent(Login.this, TaobaoConfirm.class);
				intent2.putExtra("TotalCost", parcelableOrderDetail);
				
				startActivity(intent2);
				finish();
				break;
			// added by li
			default:
				break;
			}
			okBtn.setClickable(true);
		}

	};

}
