package org.riderman.selfspace;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.riderman.ExitApplication;
import org.riderman.Login;
import org.riderman.R;
import org.riderman.Register;
import org.riderman.Splash;
import org.riderman.exception.NetException;
import org.riderman.tools.SimpleHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhoneSecurity extends Activity{
	
	private Button backBtn;
	private EditText phonEditText;
	private EditText codeEditText;
	private Button sendCodeBtn;
	private Button submitButton;
	private int recLen = 60;
	
	private boolean  isSend = false;  //判断是否已经点击了发送验证码按钮
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selfspace_account_security);
		ExitApplication.getInstance().addActivity(this);
		
		backBtn = (Button) findViewById(R.id.backBtn);
		phonEditText = (EditText) findViewById(R.id.phone_edittext);
		codeEditText =(EditText) findViewById(R.id.code_edittext);
		sendCodeBtn = (Button) findViewById(R.id.sendCodeBtn);
		submitButton = (Button) findViewById(R.id.submitBtn);
		
		backBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				PhoneSecurity.this.finish();
			}
		});
		
		sendCodeBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				if(!isSend){
					isSend = true;
				}
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Log.d("mmmm", "发送验证码");
						SharedPreferences appInfo = getSharedPreferences("app_info", 0);
						String sid = appInfo.getString("sid", "");
						SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN,sid);
			    		JSONObject jsonObject;
						try {
							jsonObject = client.post("/selfspace/phone/oldPhone/sendCode", new HashMap());
							Log.d("mmmm", "验证码发送结果："+jsonObject);
						} catch (NetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}			    		
					}
				}).start();				
	    		
	    		//重新发送倒计时按钮
	    		handler.postDelayed(runnable, 1000);
	    		sendCodeBtn.setClickable(false);
			}
		});
		
		submitButton.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				if(isSend){
					if(!codeEditText.getText().toString().isEmpty()){
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								int msgCode = -1;
								SharedPreferences appInfo = getSharedPreferences("app_info", 0);
								String sid = appInfo.getString("sid", "");
								SimpleHttpClient client = new SimpleHttpClient(
										SimpleHttpClient.DOMAIN,sid);
								Map map = new HashMap();
								map.put("code", codeEditText.getText().toString());
								JSONObject jsonObject;
								try {
									jsonObject = client.post("/selfspace/phone/oldPhone/verification", map);
									Log.d("mmmm", "验证旧手机号的返回值："+jsonObject);
									if(jsonObject.getBoolean("success")){
										msgCode = 0;
									}else {
										msgCode = 1;
									}
								} catch (NetException e) {
									msgCode = 99;
									e.printStackTrace();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								verifyHandler.sendEmptyMessage(msgCode);
								
								
							}
						}).start();
					}else{
						new AlertDialog.Builder(PhoneSecurity.this)    
			                .setTitle("警告")  
			                .setIcon(android.R.drawable.ic_dialog_alert)
			                .setMessage("请输入验证码")  			
			                .setPositiveButton("确定", null)  				
			                .show(); 
					}
				}else {//没点发送验证码按钮
					new AlertDialog.Builder(PhoneSecurity.this)    
		                .setTitle("警告")  
		                .setIcon(android.R.drawable.ic_dialog_alert)
		                .setMessage("请先发送验证码")  			
		                .setPositiveButton("确定", null)  				
		                .show(); 
				}
			}
		});
		
		//初始化我的手机号
		SharedPreferences appInfo = getSharedPreferences("app_info", 0);
		String phone = appInfo.getString("username", "");
		phonEditText.setText(phone);
		
		
	}
	
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if(recLen==0){
				sendCodeBtn.setClickable(true);
				recLen=60;
				sendCodeBtn.setText("获取验证码");
			}else {
				recLen--;
				sendCodeBtn.setText("剩余" + recLen+"秒");
				handler.postDelayed(this, 1000);
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("mmmm", "PhoneBind页面返回到PhoneSecurity的值："+resultCode);
		switch (resultCode) {
		case 1:
			Intent intent=new Intent();
			setResult(1,intent);
			PhoneSecurity.this.finish();
			break;
		default:
			break;

		}
	}
	
	private Handler verifyHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 0:
				Intent intent = new Intent(PhoneSecurity.this, PhoneBind.class);
				startActivityForResult(intent, 1);
				break;
			case 1:
				new AlertDialog.Builder(PhoneSecurity.this)    
	                .setTitle("错误")  
	                .setIcon(android.R.drawable.ic_dialog_alert)
	                .setMessage("请输入正确验证码")  			
	                .setPositiveButton("确定", null)  				
	                .show(); 
				break;
			case 99:
				Toast.makeText(PhoneSecurity.this, "网络异常，请确认是否联网", Toast.LENGTH_SHORT).show() ;
			break;
			default:
				break;
			}
		}

	};

}
