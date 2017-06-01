package org.riderman.selfspace;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.riderman.ExitApplication;
import org.riderman.R;
import org.riderman.exception.NetException;
import org.riderman.tools.SimpleHttpClient;
import org.riderman.tools.Validation;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Toast;

public class PhoneBind extends Activity{
	private Button backBtn;
	private EditText phonEditText;
	private EditText codeEditText;
	private Button sendCodeBtn;
	private Button submitButton;
	private int recLen = 60;
	private String sendPhone=""; //临时存储发送验证码的手机号
	
	private boolean  isSend = false;  //判断是否已经点击了发送验证码按钮
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selfspace_account_bindphone);
		
		backBtn = (Button) findViewById(R.id.backBtn);
		phonEditText = (EditText) findViewById(R.id.phone_edittext);
		codeEditText =(EditText) findViewById(R.id.code_edittext);
		sendCodeBtn = (Button) findViewById(R.id.sendCodeBtn);
		submitButton = (Button) findViewById(R.id.submitBtn);
		
		backBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent();
				setResult(1,intent);
				PhoneBind.this.finish();
			}
		});
		
		sendCodeBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				if(Validation.validatePhoneNumber(phonEditText.getText().toString())){
					Log.d("mmmm", "发送验证码");			
					if(!isSend){
						isSend = true;
					}
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							SharedPreferences appInfo = getSharedPreferences("app_info", 0);
							String sid = appInfo.getString("sid", "");
							SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN,sid);
							Map map = new HashMap();
							sendPhone = phonEditText.getText().toString();
							map.put("phone", phonEditText.getText().toString());
				    		JSONObject jsonObject = null;
							try {
								jsonObject = client.post("/selfspace/phone/newPhone/sendCode", map);
							} catch (NetException e) {
								e.printStackTrace();
							}
				    		Log.d("mmmm", "验证码发送结果："+jsonObject); //这里就不考虑发送验证码失败的情况了	
						}
					}).start();
					    		
		    		//重新发送倒计时按钮
		    		handler.postDelayed(runnable, 1000);
		    		sendCodeBtn.setClickable(false);
				}else {
					new AlertDialog.Builder(PhoneBind.this)    
		                .setTitle("警告")  
		                .setIcon(android.R.drawable.ic_dialog_alert)
		                .setMessage("请输入正确的手机号")  			
		                .setPositiveButton("确定", null)  				
		                .show(); 
				}
			}
		});
		
		submitButton.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				if(isSend){
					if(!codeEditText.getText().toString().isEmpty()){
						if(sendPhone.equals(phonEditText.getText().toString())){
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
										jsonObject = client.post("/selfspace/phone/newPhone/verification", map);
										Log.d("mmmm", "修改新手机号提交的返回值："+jsonObject);
										if(jsonObject.getBoolean("success")){
											msgCode = 0 ;
										}else {
											msgCode = 1;
										}
									} catch (NetException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										msgCode = 99;
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									verifyHandler.sendEmptyMessage(msgCode);
									
								}
							}).start();
						}else{ //点击发送验证码后，检验手机号是否已经被改变
							new AlertDialog.Builder(PhoneBind.this)    
			                .setTitle("警告")  
			                .setIcon(android.R.drawable.ic_dialog_alert)
			                .setMessage("你改变了新号码，请重新发送验证码")  			
			                .setPositiveButton("确定", null)  				
			                .show(); 
						}
						
					}else{
						new AlertDialog.Builder(PhoneBind.this)    
			                .setTitle("警告")  
			                .setIcon(android.R.drawable.ic_dialog_alert)
			                .setMessage("请输入验证码")  			
			                .setPositiveButton("确定", null)  				
			                .show(); 
					}
				}else { //没点发送验证码按钮
					new AlertDialog.Builder(PhoneBind.this)    
		                .setTitle("警告")  
		                .setIcon(android.R.drawable.ic_dialog_alert)
		                .setMessage("请先发送验证码")  			
		                .setPositiveButton("确定", null)  				
		                .show(); 
				}
			}
		});
		
		
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
	
	private Handler verifyHandler  = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 0:
				//在sp中修改密码
				SharedPreferences appInfo = getSharedPreferences("app_info", 0);
				SharedPreferences.Editor editor = appInfo.edit();
				editor.putString("username", phonEditText.getText().toString());
				editor.commit();
				
				Intent intent=new Intent();
				setResult(1,intent);
				PhoneBind.this.finish();//同时销毁两个activity
				break;
			case 1:
				new AlertDialog.Builder(PhoneBind.this)    
	                .setTitle("错误")  
	                .setIcon(android.R.drawable.ic_dialog_alert)
	                .setMessage("请输入正确验证码")  			
	                .setPositiveButton("确定", null)  				
	                .show(); 
				break;
			case 99:
				Toast.makeText(PhoneBind.this, "网络异常，请确认是否联网", Toast.LENGTH_SHORT).show() ;
			break;
			default:
				break;
			}
		}

	};
}
