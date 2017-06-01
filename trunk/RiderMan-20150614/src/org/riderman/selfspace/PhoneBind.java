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
	private String sendPhone=""; //��ʱ�洢������֤����ֻ���
	
	private boolean  isSend = false;  //�ж��Ƿ��Ѿ�����˷�����֤�밴ť
	
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
					Log.d("mmmm", "������֤��");			
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
				    		Log.d("mmmm", "��֤�뷢�ͽ����"+jsonObject); //����Ͳ����Ƿ�����֤��ʧ�ܵ������	
						}
					}).start();
					    		
		    		//���·��͵���ʱ��ť
		    		handler.postDelayed(runnable, 1000);
		    		sendCodeBtn.setClickable(false);
				}else {
					new AlertDialog.Builder(PhoneBind.this)    
		                .setTitle("����")  
		                .setIcon(android.R.drawable.ic_dialog_alert)
		                .setMessage("��������ȷ���ֻ���")  			
		                .setPositiveButton("ȷ��", null)  				
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
										Log.d("mmmm", "�޸����ֻ����ύ�ķ���ֵ��"+jsonObject);
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
						}else{ //���������֤��󣬼����ֻ����Ƿ��Ѿ����ı�
							new AlertDialog.Builder(PhoneBind.this)    
			                .setTitle("����")  
			                .setIcon(android.R.drawable.ic_dialog_alert)
			                .setMessage("��ı����º��룬�����·�����֤��")  			
			                .setPositiveButton("ȷ��", null)  				
			                .show(); 
						}
						
					}else{
						new AlertDialog.Builder(PhoneBind.this)    
			                .setTitle("����")  
			                .setIcon(android.R.drawable.ic_dialog_alert)
			                .setMessage("��������֤��")  			
			                .setPositiveButton("ȷ��", null)  				
			                .show(); 
					}
				}else { //û�㷢����֤�밴ť
					new AlertDialog.Builder(PhoneBind.this)    
		                .setTitle("����")  
		                .setIcon(android.R.drawable.ic_dialog_alert)
		                .setMessage("���ȷ�����֤��")  			
		                .setPositiveButton("ȷ��", null)  				
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
				sendCodeBtn.setText("��ȡ��֤��");
			}else {
				recLen--;
				sendCodeBtn.setText("ʣ��" + recLen+"��");
				handler.postDelayed(this, 1000);
			}
		}
	};
	
	private Handler verifyHandler  = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 0:
				//��sp���޸�����
				SharedPreferences appInfo = getSharedPreferences("app_info", 0);
				SharedPreferences.Editor editor = appInfo.edit();
				editor.putString("username", phonEditText.getText().toString());
				editor.commit();
				
				Intent intent=new Intent();
				setResult(1,intent);
				PhoneBind.this.finish();//ͬʱ��������activity
				break;
			case 1:
				new AlertDialog.Builder(PhoneBind.this)    
	                .setTitle("����")  
	                .setIcon(android.R.drawable.ic_dialog_alert)
	                .setMessage("��������ȷ��֤��")  			
	                .setPositiveButton("ȷ��", null)  				
	                .show(); 
				break;
			case 99:
				Toast.makeText(PhoneBind.this, "�����쳣����ȷ���Ƿ�����", Toast.LENGTH_SHORT).show() ;
			break;
			default:
				break;
			}
		}

	};
}
