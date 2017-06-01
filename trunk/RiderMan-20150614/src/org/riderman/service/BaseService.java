package org.riderman.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.riderman.ActivityStackManager;
import org.riderman.Login;
import org.riderman.exception.NetException;
import org.riderman.selfspace.SelfSpace;
import org.riderman.tools.CommonTool;
import org.riderman.tools.SimpleHttpClient;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BaseService {
	/**
	 * 用户登录接口(登录，获取用户参数，下载用户头像)
	 * @param context
	 * @param username
	 * @param password
	 * @return  0:用户名或密码错误；1：登陆成功；-1：网络错误
	 * @throws NetException 
	 */
	public int doLogin(final Context context,String username,String password)  {
		if(username == null || username.isEmpty() || password == null || password.isEmpty())
			return 0;
		String photo = null;
		final SharedPreferences appInfo = context.getSharedPreferences("app_info", 0);
		SharedPreferences.Editor editor = appInfo.edit();
		//发送登录请求
		SimpleHttpClient client = new SimpleHttpClient(
				SimpleHttpClient.DOMAIN);
		Map map = new HashMap();
		map.put("j_username", username);
		map.put("j_password", password);
		JSONObject jsonObject;
		try {
			jsonObject = client.post("/user/login", map);
			if (jsonObject.getBoolean("success")) {//登录成功
				editor.putBoolean("autoLogin", true);
				editor.putString("username", username);
				editor.putString("password", password);
				editor.putBoolean("loginStatus", true);
				editor.putString("nickname", jsonObject.getString("nickname"));
				editor.putString("render", jsonObject.getString("render"));
				editor.putString("photo", jsonObject.getString("photo"));
				editor.putString("sid", jsonObject.getString("sid"));	
				photo = jsonObject.getString("photo");
				Log.d("mmmm", "登录成功"+jsonObject.getString("sid"));
				
				//下载图片到本地
				if(photo!=null && !photo.isEmpty() && !photo.equals("null") ){ //当图片不存在时加载
					Log.d("mmmm", "从sp中读取的照片："+photo);
					String localPhotoPath = android.os.Environment.getExternalStorageDirectory()
							 .getAbsolutePath()+"/ebike/person"+photo.substring(photo.lastIndexOf("/"));
					if(!new File(localPhotoPath).exists()){
						String photoUrl = SimpleHttpClient.DOMAIN+photo;
						Log.d("mmmm", "加载照片："+photoUrl);
						CommonTool.savePhotoFromURL(photoUrl, localPhotoPath);
						Log.d("mmmm", "保存照片到本地路径："+localPhotoPath);
					}
				}
				editor.commit();
				//开启登录维持线程
				ActivityStackManager.getInstance().setThread(new Thread(new Runnable() {
						
						@Override
						public void run() {
							while (true) {
								try {
									Thread.sleep(20*60000);
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
								
								Log.d("mmmm", "心跳维持");
								try {
									SharedPreferences appInfo = context.getSharedPreferences("app_info", 0);
									String sid= appInfo.getString("sid", "");
									SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN, sid);
									JSONObject jsonObject = client.get("/beat");
									Log.d("mmmm", "心跳维持JSON:"+jsonObject);
								} catch (NetException e) {
									e.printStackTrace();
								}
							}			
						}
					}));
				ActivityStackManager.getInstance().getThread().start();
				return 1;
			} else {// 如果错误则清空错误登录信息
				editor.putBoolean("autoLogin", false);//下次直接进入未登录状态
				editor.putString("username", "");
				editor.putString("password", "");
				editor.putBoolean("loginStatus", false);
			}
			editor.commit();
			return 0;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		
		return 0;
	}
	
	/**
	 * 退出登录，把sp的autoLogin设为false
	 * @param context
	 */
	public void logout(Context context){
		SharedPreferences appInfo = context.getSharedPreferences("app_info", 0);
		SharedPreferences.Editor editor = appInfo.edit();
		editor.putBoolean("autoLogin", false);
		editor.putBoolean("loginStatus", false);
		editor.putString("nickname", "");
		editor.putString("render", "");
		editor.putString("photo", "");
		editor.commit();
	}

}
