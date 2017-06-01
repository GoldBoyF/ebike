package org.riderman.selfspace;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.riderman.Login;
import org.riderman.R;
import org.riderman.exception.NetException;
import org.riderman.tools.SimpleHttpClient;
import org.riderman.tools.Validation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class PasswordSetting extends Activity{
	
	private Button backBtn;
	private EditText curPwdEditText;
	private EditText newPwdEditText;
	private EditText reNewPwdEditText;
	private Button submitBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selfspace_account_password);
		
		backBtn = (Button) findViewById(R.id.backBtn);
		curPwdEditText = (EditText) findViewById(R.id.current_password_edittext);
		newPwdEditText = (EditText) findViewById(R.id.new_password_edittext);
		reNewPwdEditText = (EditText) findViewById(R.id.confirm_new_password_edittext);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		
		backBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				PasswordSetting.this.finish();
			}
		});
		
		submitBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				if(newPwdEditText.getText().toString().equals(reNewPwdEditText.getText().toString())){
					if(Validation.validatePassword(newPwdEditText.getText().toString())){
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								int msgCode = -1;
								SharedPreferences appInfo = getSharedPreferences("app_info", 0);
								String sid = appInfo.getString("sid", "");
								SimpleHttpClient client = new SimpleHttpClient(
										SimpleHttpClient.DOMAIN,sid);
								Map map = new HashMap();
								map.put("oldPassword", curPwdEditText.getText().toString());
								map.put("newPassword", newPwdEditText.getText().toString());
								try {
									Log.d("mmmm", "����sid"+sid+","+map);
									JSONObject jsonObject = client.post("/selfspace/password/reset", map);
									Log.d("mmmm", "�޸�����ӿڷ������ݣ�"+jsonObject);
									if(jsonObject.getBoolean("success")){
										msgCode = 0; //�����޸ĳɹ�
									}else {
										msgCode = 1; //ԭ�������
									}
								} catch (NetException e) {
									e.printStackTrace();
									msgCode = 99;
								} catch (JSONException e) {
									e.printStackTrace();
								}
								Log.d("mmmm", "���ص�msgCode:"+msgCode);
								handler.sendEmptyMessage(msgCode);
							}
						}).start();
						
						
						/*try {
							if(jsonObject.getBoolean("success")){
								new AlertDialog.Builder(PasswordSetting.this)    
				                .setTitle("��ʾ")  
				                .setIcon(android.R.drawable.ic_dialog_alert)
				                .setMessage("�����޸ĳɹ�")  			
				                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										PasswordSetting.this.finish();
										
									}
								})  				
				                .show(); 
								//��sp���޸�����
								SharedPreferences.Editor editor = appInfo.edit();
								editor.putString("password", newPwdEditText.getText().toString());
								editor.commit();
							}else{
								new AlertDialog.Builder(PasswordSetting.this)    
					                .setTitle("����")  
					                .setIcon(android.R.drawable.ic_dialog_alert)
					                .setMessage("ԭ�������")  			
					                .setPositiveButton("ȷ��", null)  				
					                .show(); 
								curPwdEditText.setText("");
								newPwdEditText.setText("");
								reNewPwdEditText.setText("");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}*/
					}else{
						new AlertDialog.Builder(PasswordSetting.this)    
			                .setTitle("����")  
			                .setIcon(android.R.drawable.ic_dialog_alert)
			                .setMessage("���볤��Ϊ6-20λ��������ĸ������")  			
			                .setPositiveButton("ȷ��", null)  				
			                .show(); 
						curPwdEditText.setText("");
						newPwdEditText.setText("");
						reNewPwdEditText.setText("");
					}
				}else{
					new AlertDialog.Builder(PasswordSetting.this)    
		                .setTitle("����")  
		                .setIcon(android.R.drawable.ic_dialog_alert)
		                .setMessage("��ȷ�����������������һ��")  			
		                .setPositiveButton("ȷ��", null)  				
		                .show(); 
					curPwdEditText.setText("");
					newPwdEditText.setText("");
					reNewPwdEditText.setText("");
				}
			}
		});
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 0:
				new AlertDialog.Builder(PasswordSetting.this)    
	                .setTitle("��ʾ")  
	                .setIcon(android.R.drawable.ic_dialog_alert)
	                .setMessage("�����޸ĳɹ�")  			
	                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {									
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							PasswordSetting.this.finish();
							
						}
					})  				
	                .show(); 
				//��sp���޸�����
				SharedPreferences appInfo = getSharedPreferences("app_info", 0);
				SharedPreferences.Editor editor = appInfo.edit();
				editor.putString("password", newPwdEditText.getText().toString());
				editor.commit();
				break;
			case 1:
				new AlertDialog.Builder(PasswordSetting.this)    
	                .setTitle("����")  
	                .setIcon(android.R.drawable.ic_dialog_alert)
	                .setMessage("ԭ�������")  			
	                .setPositiveButton("ȷ��", null)  				
	                .show(); 
				curPwdEditText.setText("");
				newPwdEditText.setText("");
				reNewPwdEditText.setText("");
				break;
			case 99:
				Toast.makeText(PasswordSetting.this, "�����쳣����ȷ���Ƿ�����", Toast.LENGTH_SHORT).show() ;
			break;
			default:
				break;
			}
		}

	};
}
