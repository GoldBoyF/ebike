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
									Log.d("mmmm", "参数sid"+sid+","+map);
									JSONObject jsonObject = client.post("/selfspace/password/reset", map);
									Log.d("mmmm", "修改密码接口返回数据："+jsonObject);
									if(jsonObject.getBoolean("success")){
										msgCode = 0; //密码修改成功
									}else {
										msgCode = 1; //原密码错误
									}
								} catch (NetException e) {
									e.printStackTrace();
									msgCode = 99;
								} catch (JSONException e) {
									e.printStackTrace();
								}
								Log.d("mmmm", "返回的msgCode:"+msgCode);
								handler.sendEmptyMessage(msgCode);
							}
						}).start();
						
						
						/*try {
							if(jsonObject.getBoolean("success")){
								new AlertDialog.Builder(PasswordSetting.this)    
				                .setTitle("提示")  
				                .setIcon(android.R.drawable.ic_dialog_alert)
				                .setMessage("密码修改成功")  			
				                .setPositiveButton("确定", new DialogInterface.OnClickListener() {									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										PasswordSetting.this.finish();
										
									}
								})  				
				                .show(); 
								//在sp中修改密码
								SharedPreferences.Editor editor = appInfo.edit();
								editor.putString("password", newPwdEditText.getText().toString());
								editor.commit();
							}else{
								new AlertDialog.Builder(PasswordSetting.this)    
					                .setTitle("警告")  
					                .setIcon(android.R.drawable.ic_dialog_alert)
					                .setMessage("原密码错误")  			
					                .setPositiveButton("确定", null)  				
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
			                .setTitle("警告")  
			                .setIcon(android.R.drawable.ic_dialog_alert)
			                .setMessage("密码长度为6-20位，允许字母和数字")  			
			                .setPositiveButton("确定", null)  				
			                .show(); 
						curPwdEditText.setText("");
						newPwdEditText.setText("");
						reNewPwdEditText.setText("");
					}
				}else{
					new AlertDialog.Builder(PasswordSetting.this)    
		                .setTitle("警告")  
		                .setIcon(android.R.drawable.ic_dialog_alert)
		                .setMessage("请确认两次输入的新密码一致")  			
		                .setPositiveButton("确定", null)  				
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
	                .setTitle("提示")  
	                .setIcon(android.R.drawable.ic_dialog_alert)
	                .setMessage("密码修改成功")  			
	                .setPositiveButton("确定", new DialogInterface.OnClickListener() {									
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							PasswordSetting.this.finish();
							
						}
					})  				
	                .show(); 
				//在sp中修改密码
				SharedPreferences appInfo = getSharedPreferences("app_info", 0);
				SharedPreferences.Editor editor = appInfo.edit();
				editor.putString("password", newPwdEditText.getText().toString());
				editor.commit();
				break;
			case 1:
				new AlertDialog.Builder(PasswordSetting.this)    
	                .setTitle("警告")  
	                .setIcon(android.R.drawable.ic_dialog_alert)
	                .setMessage("原密码错误")  			
	                .setPositiveButton("确定", null)  				
	                .show(); 
				curPwdEditText.setText("");
				newPwdEditText.setText("");
				reNewPwdEditText.setText("");
				break;
			case 99:
				Toast.makeText(PasswordSetting.this, "网络异常，请确认是否联网", Toast.LENGTH_SHORT).show() ;
			break;
			default:
				break;
			}
		}

	};
}
