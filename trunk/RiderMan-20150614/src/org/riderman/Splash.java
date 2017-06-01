package org.riderman;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.riderman.service.BaseService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class Splash extends Activity {
	
	private BaseService baseService = new BaseService();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Log.d("mmmm", "启动了");
		new Thread() {
			// 待扩展
			public void run() {		
				//登录认证
				SharedPreferences appInfo = getSharedPreferences("app_info", 0);
				boolean autoLogin = appInfo.getBoolean("autoLogin", false);
				if(autoLogin){
					String username = appInfo.getString("username", "");
					String password = appInfo.getString("password", "");
					baseService.doLogin(Splash.this, username, password);
				}else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//初始化文件路径
				String tPathString = android.os.Environment.getExternalStorageDirectory()
						 .getAbsolutePath()+"/ebike";
				File tPath=new File(tPathString);
	            if(!tPath.exists()){
	            	tPath.mkdirs();
	            }
	            File personPath=new File(tPathString+"/person");
	            if(!personPath.exists()){
	            	personPath.mkdirs();
	            }
	            File bikePath=new File(tPathString+"/bike");
	            if(!bikePath.exists()){
	            	bikePath.mkdirs();
	            }
				Intent intent = new Intent(Splash.this, Homemenu.class);
//				intent.putExtra("key", parcelableDate);
				startActivity(intent);
				finish();
			}
		}.start();
	}


}
