package org.riderman.selfspace;

import org.riderman.ExitApplication;
import org.riderman.R;
import org.riderman.Splash;
import org.riderman.picpopupwindow.SelectPicPopupWindow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountSetting extends Activity{
	
	private Button backBtn;
	private TextView phoneTextView;
	private RelativeLayout passwordLayout;
	private RelativeLayout phoneLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selfspace_acccount_setting);
		ExitApplication.getInstance().addActivity(this);
		
		backBtn = (Button) findViewById(R.id.backBtn);
		phoneTextView = (TextView) findViewById(R.id.phone_textview);
		passwordLayout = (RelativeLayout) findViewById(R.id.password_layout);
		phoneLayout = (RelativeLayout) findViewById(R.id.phone_layout);
		
		backBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				AccountSetting.this.finish();
			}
		});
		
		passwordLayout.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AccountSetting.this, PasswordSetting.class);
				startActivity(intent);
			}
		});
		
		phoneLayout.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(AccountSetting.this,
						PhoneSecurity.class), 1);
			}
		});
		
		//设置
		SharedPreferences appInfo = getSharedPreferences("app_info", 0);
		String phone = appInfo.getString("username", "");
		String maskPhone = phone.substring(0,3)+"*****"+phone.substring(7);
		phoneTextView.setText("已绑定手机号 "+maskPhone);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("mmmm", "PhoneSecurity页面返回到AccountSetting的值："+resultCode);
		switch (resultCode) {
		case 1:
			//设置accountsetting页面中的电话号码
			SharedPreferences appInfo = getSharedPreferences("app_info", 0);
			String phone = appInfo.getString("username", "");
			String maskPhone = phone.substring(0,3)+"*****"+phone.substring(7);
			phoneTextView.setText(maskPhone);
			break;
		default:
			break;

		}
	}

}
