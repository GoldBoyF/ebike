package org.riderman.selfspace;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.riderman.ExitApplication;
import org.riderman.R;
import org.riderman.exception.NetException;
import org.riderman.tools.SimpleHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class RenderSetting extends Activity{
	private LinearLayout maleLayout;
	private LinearLayout femaleLayout;
	
	private ImageView maleImageView;
	private ImageView femaleImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selfspace_render_setting);
		ExitApplication.getInstance().addActivity(this);
		
		maleLayout = (LinearLayout) findViewById(R.id.male_layout);
		femaleLayout = (LinearLayout) findViewById(R.id.female_layout);
		maleImageView = (ImageView) findViewById(R.id.male_imageview);
		femaleImageView = (ImageView) findViewById(R.id.female_imageview);
		
		//获取intent的render
		Intent intent = getIntent();
		String render = intent.getStringExtra("render");
		if(render.equals("男")){
			maleImageView.setVisibility(View.VISIBLE);
			femaleImageView.setVisibility(View.INVISIBLE);
		}else if(render.equals("女")) {
			maleImageView.setVisibility(View.INVISIBLE);
			femaleImageView.setVisibility(View.VISIBLE);
		}else {
			maleImageView.setVisibility(View.INVISIBLE);
			femaleImageView.setVisibility(View.INVISIBLE);
		}
		
		maleLayout.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				maleImageView.setVisibility(View.VISIBLE);
				femaleImageView.setVisibility(View.INVISIBLE);
				sendPost("男");
			}
		});
		
		femaleLayout.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				maleImageView.setVisibility(View.INVISIBLE);
				femaleImageView.setVisibility(View.VISIBLE);
				sendPost("女");
			}
		});


	}
	
	private void sendPost(final String render){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				SharedPreferences appInfo = getSharedPreferences("app_info", 0);  
				String sid = appInfo.getString("sid", "");
				SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN,sid);
				Map map = new HashMap();
				map.put("render", render);
				try {
					JSONObject jsonObject = client.post("/selfspace/render",map);
					if(jsonObject.getBoolean("success")){
						Intent intent=new Intent();
						intent.putExtra("render", render);
						//保存到sp中
						SharedPreferences.Editor editor = appInfo.edit();
						editor.putString("render", render);
						editor.commit();
						setResult(3,intent);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}
}
