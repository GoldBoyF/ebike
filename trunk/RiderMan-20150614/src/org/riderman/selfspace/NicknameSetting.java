package org.riderman.selfspace;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.riderman.Login;
import org.riderman.R;
import org.riderman.exception.NetException;
import org.riderman.tools.SimpleHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NicknameSetting extends Activity{
	private Button cancelBtn;
	private Button saveBtn;
	private EditText nicknameEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selfspace_nickname_setting);
		
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		nicknameEditText = (EditText) findViewById(R.id.nickname_edittext);
		
		cancelBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				NicknameSetting.this.finish();
			}
		});
		
		saveBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				if(nicknameEditText.getText().toString().isEmpty()){
					new AlertDialog.Builder(NicknameSetting.this)    
	                .setTitle("警告")  
	                .setIcon(android.R.drawable.ic_dialog_alert)
	                .setMessage("昵称不能为空")  			
	                .setPositiveButton("确定", null)  				
	                .show(); 
				}else{
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							SharedPreferences appInfo = getSharedPreferences("app_info", 0);  
							String sid = appInfo.getString("sid", "");
							SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN,sid);
							Map map = new HashMap();
							map.put("nickname", nicknameEditText.getText().toString());
							try {
								JSONObject jsonObject = client.post("/selfspace/nickname",map);
								if(jsonObject.getBoolean("success")){
									Intent intent=new Intent();
									intent.putExtra("nickname", nicknameEditText.getText().toString());
									//保存到sp中
									SharedPreferences.Editor editor = appInfo.edit();
									editor.putString("nickname", nicknameEditText.getText().toString());
									editor.commit();
									setResult(2,intent);
									finish();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (NetException e) {
								e.printStackTrace();
							}
						}
					}).start();
					
				}
			}
		});
		
		//获取intent的nickname
		Intent intent = getIntent();
		String nickname = intent.getStringExtra("nickname");
		if(nickname.equals("请输入")){
			nicknameEditText.setText("");	
		}else {
			nicknameEditText.setText(nickname);	
		}			
	}
	
	
}
