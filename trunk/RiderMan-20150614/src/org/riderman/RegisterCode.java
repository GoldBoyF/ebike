package org.riderman;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.riderman.exception.NetException;
import org.riderman.selfspace.PhoneSecurity;
import org.riderman.tools.SimpleHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterCode extends Activity{
	private TextView infoTextView;
	private EditText codeEditText;
	private Button nextBtn;
	private Button cancelBtn;
	private Button reSendBtn;
	private String phone;
	private String rsid;
	private int recLen = 60;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_code);
		//下一步按钮事件
		infoTextView = (TextView) findViewById(R.id.info_text_view);
		codeEditText = (EditText) findViewById(R.id.code_edit_text);
		nextBtn = (Button)findViewById(R.id.nextBtn);
		reSendBtn = (Button)findViewById(R.id.resend_button);
		
		//获取传递过来的参数
		Intent intent = getIntent();
		this.phone = intent.getStringExtra("phone");
		this.rsid = intent.getStringExtra("rsid");
		infoTextView.setText("包含验证码的短信已经发送至"+phone);
		
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(1, new Intent());
				RegisterCode.this.finish();
			}
		});
		
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final String code = codeEditText.getText().toString();
				if(code!=null && !code.isEmpty()){
					
					new Thread(new Runnable() {
						public void run() {
							int msgCode = -1;
							SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN,rsid);
				    		Map map = new HashMap();
				    		map.put("code", code);
				    		try {
								JSONObject jsonObject = client.post("/user/phone/verification", map);
								if(jsonObject.getBoolean("success")){
									msgCode = 0;
								}else {
									msgCode = 1;
								}
							} catch (NetException e) {
								msgCode = 99;
								e.printStackTrace();
							} catch (JSONException e) {
								e.printStackTrace();
							}
				    		verifyHandler.sendEmptyMessage(msgCode);//最重要
						}
					}).start();
					
				}else{
					new AlertDialog.Builder(RegisterCode.this)    
	                .setTitle("警告")  
	                .setIcon(android.R.drawable.ic_dialog_alert)
	                .setMessage("验证码为空")  			
	                .setPositiveButton("确定", null)  				
	                .show();  
				}
				
			}
			
		});
		reSendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {	
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN,rsid);
			    		Map map = new HashMap();
			    		map.put("phone", phone);
			    		try {
							JSONObject jsonObject = client.post("/user/phone/sendCode", map);
						} catch (NetException e) {
							e.printStackTrace();
						}
					}
				}).start();
	    		//重新发送倒计时按钮
	    		handler.postDelayed(runnable, 1000);
	    		reSendBtn.setClickable(false);
			}
			
		});
		
		//此处验证码直接倒计时
		handler.postDelayed(runnable, 1000);
		reSendBtn.setClickable(false);
		
	}
	
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if(recLen==0){
				reSendBtn.setClickable(true);
				recLen=60;
				reSendBtn.setText("重新发送验证码");
			}else {
				recLen--;
				reSendBtn.setText("重新发送验证码（剩余" + recLen+"秒）");
				handler.postDelayed(this, 1000);
			}
		}
	};
	
	/**
	 * 返回此页面时直接销毁
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		setResult(1,new Intent());
		RegisterCode.this.finish();
	}
	
	private Handler verifyHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 0:
				Intent it = new Intent(RegisterCode.this,
						RegisterPassword.class);
				it.putExtra("rsid", rsid);
				it.putExtra("phone", phone);
				startActivityForResult(it, 1);
				break;
			case 1:
				new AlertDialog.Builder(RegisterCode.this)    
                .setTitle("警告")  
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("验证码无效")  			
                .setPositiveButton("确定", null)  				
                .show();  
				break;
			case 99:
				Toast.makeText(RegisterCode.this, "网络异常，请确认是否联网", Toast.LENGTH_SHORT).show() ;
				break;
			default:
				break;
			}
		}

	};
	
	
}
