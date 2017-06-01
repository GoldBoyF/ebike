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
	
	private boolean  isSend = false;  //�ж��Ƿ��Ѿ�����˷�����֤�밴ť
	
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
						Log.d("mmmm", "������֤��");
						SharedPreferences appInfo = getSharedPreferences("app_info", 0);
						String sid = appInfo.getString("sid", "");
						SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN,sid);
			    		JSONObject jsonObject;
						try {
							jsonObject = client.post("/selfspace/phone/oldPhone/sendCode", new HashMap());
							Log.d("mmmm", "��֤�뷢�ͽ����"+jsonObject);
						} catch (NetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}			    		
					}
				}).start();				
	    		
	    		//���·��͵���ʱ��ť
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
									Log.d("mmmm", "��֤���ֻ��ŵķ���ֵ��"+jsonObject);
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
			                .setTitle("����")  
			                .setIcon(android.R.drawable.ic_dialog_alert)
			                .setMessage("��������֤��")  			
			                .setPositiveButton("ȷ��", null)  				
			                .show(); 
					}
				}else {//û�㷢����֤�밴ť
					new AlertDialog.Builder(PhoneSecurity.this)    
		                .setTitle("����")  
		                .setIcon(android.R.drawable.ic_dialog_alert)
		                .setMessage("���ȷ�����֤��")  			
		                .setPositiveButton("ȷ��", null)  				
		                .show(); 
				}
			}
		});
		
		//��ʼ���ҵ��ֻ���
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
				sendCodeBtn.setText("��ȡ��֤��");
			}else {
				recLen--;
				sendCodeBtn.setText("ʣ��" + recLen+"��");
				handler.postDelayed(this, 1000);
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("mmmm", "PhoneBindҳ�淵�ص�PhoneSecurity��ֵ��"+resultCode);
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
	                .setTitle("����")  
	                .setIcon(android.R.drawable.ic_dialog_alert)
	                .setMessage("��������ȷ��֤��")  			
	                .setPositiveButton("ȷ��", null)  				
	                .show(); 
				break;
			case 99:
				Toast.makeText(PhoneSecurity.this, "�����쳣����ȷ���Ƿ�����", Toast.LENGTH_SHORT).show() ;
			break;
			default:
				break;
			}
		}

	};

}
