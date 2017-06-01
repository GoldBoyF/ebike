package org.riderman;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;
import org.riderman.exception.NetException;
import org.riderman.selfspace.SelfSetting;
import org.riderman.selfspace.SelfSpace;
import org.riderman.service.BaseService;
import org.riderman.tools.CommonTool;
import org.riderman.tools.SimpleHttpClient;
import org.riderman.tools.Validation;

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

public class RegisterPassword extends Activity{
	
	private EditText pwdEditText;
	private EditText pwdConfirmEditText;
	private Button finishBtn;
	private String rsid;
	private Button cancelBtn;
	
	private String phone;
	
	private BaseService baseService = new BaseService();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_password);
		
		pwdEditText = (EditText)findViewById(R.id.password_edittext);
		pwdConfirmEditText = (EditText)findViewById(R.id.password_comfirm_edittext);
		finishBtn = (Button)findViewById(R.id.finishBtn);
		
		Intent intent = getIntent();
		this.rsid = intent.getStringExtra("rsid");
		this.phone = intent.getStringExtra("phone");
		Log.d("mmmm", "RegisterPassword接收参数："+rsid+","+phone);
		
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(1, new Intent());
				RegisterPassword.this.finish();
			}
		});
		
		finishBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {	
				if(pwdEditText.getText().toString().length()==0){
					new AlertDialog.Builder(RegisterPassword.this)    
	                .setTitle("警告")  
	                .setIcon(android.R.drawable.ic_dialog_alert)
	                .setMessage("密码不能为空")  			
	                .setPositiveButton("确定", null)  				
	                .show();  
				}else{			
					if(pwdConfirmEditText.getText().toString().equals(pwdEditText.getText().toString())){
						if(Validation.validatePassword(pwdEditText.getText().toString())){
							finishBtn.setClickable(false);
							new Thread(new Runnable() {
								
								@Override
								public void run() {									
									SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN,rsid);
									Map map = new HashMap();
									map.put("password", pwdConfirmEditText.getText().toString());
									JSONObject jsonObject;
									try {
										jsonObject = client.post("/user/register", map);
										if(jsonObject.getBoolean("success")){									
											int result = baseService.doLogin(RegisterPassword.this, phone, pwdConfirmEditText.getText().toString());
											handler.sendEmptyMessage(result);
										}
									} catch (NetException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}
							}).start();
							
						}else {
							new AlertDialog.Builder(RegisterPassword.this)    
			                .setTitle("警告")  
			                .setIcon(android.R.drawable.ic_dialog_alert)
			                .setMessage("输入6-20位的密码，数字和字母大小写")  			
			                .setPositiveButton("确定", null)  				
			                .show();  
						}
					}else {
						new AlertDialog.Builder(RegisterPassword.this)    
		                .setTitle("警告")  
		                .setIcon(android.R.drawable.ic_dialog_alert)
		                .setMessage("两次密码不一致")  			
		                .setPositiveButton("确定", null)  				
		                .show();  
					}
				}
			}
			
		});
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what==1){ //通常自动登录会成功，此时会跳转到个人中心页面
				Intent intent = new Intent(RegisterPassword.this, Homemenu.class);
			intent.putExtra("CMD", ConstantTool.PAY_TOGO);	 
				startActivity(intent);
			}else { //极少数跳回登录
				Intent intent = new Intent(RegisterPassword.this, Login.class);
				startActivity(intent);
			}
			finish();
			finishBtn.setClickable(true);
		}

	};
	
}
