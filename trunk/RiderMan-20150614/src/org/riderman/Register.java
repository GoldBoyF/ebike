package org.riderman;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.riderman.exception.NetException;
import org.riderman.picpopupwindow.SelectPicPopupWindow;
import org.riderman.selfspace.PhoneSecurity;
import org.riderman.selfspace.SelfSetting;
import org.riderman.selfspace.SelfSpace;
import org.riderman.tools.SimpleHttpClient;
import org.riderman.tools.Validation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity{
	private Button nextBtn;
	private Button cancelBtn;
	private EditText editText;
	private String rsid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//��һ����ť�¼�
		nextBtn = (Button) findViewById(R.id.nextBtn);
		editText = (EditText) findViewById(R.id.phone_edittext);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Register.this.finish();
			}
		});
		
		nextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(Validation.validatePhoneNumber(editText.getText().toString())){
					new AlertDialog.Builder(Register.this).setTitle("eBike���ᷢ�Ͷ�����֤����") 
				    .setIcon(android.R.drawable.ic_dialog_info)
				    .setMessage(editText.getText().toString())
				    .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
				 
				        @Override 
				        public void onClick(DialogInterface dialog, int which) { 
				        	new Thread(new Runnable() {
								
								@Override
								public void run() {
									// �����ȷ�ϡ���Ĳ��� 
									int msgCode = -1;
						        	SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN);
						    		Map map = new HashMap();
						    		map.put("phone", editText.getText().toString());
						    		JSONObject jsonObject;
									try {
										jsonObject = client.post("/user/phone/sendCode", map);
										if(jsonObject.getBoolean("success")){
											rsid = jsonObject.getString("sid");
											msgCode = 100;
										}else {
											msgCode = jsonObject.getInt("msg");	
										}
									} catch (NetException e) {
										e.printStackTrace();
										msgCode = 99;
									} catch (JSONException e) { //�����쳣���ᷢ��
										e.printStackTrace();
									}
									handler.sendEmptyMessage(msgCode);
									
								}
							}).start();
				        	
				        } 
				    }) 
				    .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() { 
				 
				        @Override 
				        public void onClick(DialogInterface dialog, int which) { 
				        	// ��������ء���Ĳ���,���ﲻ����û���κβ��� 
				        } 
				    }).show(); 
					
				}else{
					new AlertDialog.Builder(Register.this)    
					                .setTitle("����")  
					                .setIcon(android.R.drawable.ic_dialog_info)
					                .setMessage("��������ȷ�ֻ���")  			
					                .setPositiveButton("ȷ��", null)  				
					                .show();  
				}
			}
		});
		
	}
	
	/**
	 * ���ش�ҳ��ʱֱ������
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Register.this.finish();
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what==100){
				Intent intent = new Intent(Register.this,
						RegisterCode.class);
				intent.putExtra("rsid", rsid);
				intent.putExtra("phone", editText.getText().toString());
				startActivityForResult(intent, 1);
			}else if (msg.what==99) {
				Toast.makeText(Register.this, "�����쳣����ȷ���Ƿ�����", Toast.LENGTH_SHORT).show() ;
			}else {
				Integer errorCode = msg.what;	
				String errorMsg = "";
				if(errorCode.equals(0)){
					errorMsg = "�����ѱ�ע��";
				}else if (errorCode.equals(1)) {
					errorMsg = "�����ʹ���̫�ࣩ���Ʒ���";
				}else if (errorCode.equals(2)) {
					errorMsg = "�����ֻ���";
				}else {
					errorMsg = "δ֪ԭ��";
				}
				new AlertDialog.Builder(Register.this)    
	                .setTitle("����")  
	                .setIcon(android.R.drawable.ic_dialog_alert)
	                .setMessage(errorMsg)  			
	                .setPositiveButton("ȷ��", null)  				
	                .show();  
			}
		}

	};
	
	/*private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 0:

				break;
			case 1:
				
				break;
			case 99:
				Toast.makeText(Register.this, "�����쳣����ȷ���Ƿ�����", Toast.LENGTH_SHORT).show() ;
			break;
			default:
				break;
			}
		}

	};*/
}
