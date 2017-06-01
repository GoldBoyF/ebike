package org.riderman.selfspace;

import java.io.File;

import org.riderman.ExitApplication;
import org.riderman.Login;
import org.riderman.R;
import org.riderman.Register;
import org.riderman.service.BaseService;
import org.riderman.tools.CommonTool;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelfSpace extends Activity{
	private TextView selfTextView;
	private TextView orderTextView;
	private TextView accountTextView;

	private TextView registerLinkTextView;
	private Button loginBtn;
	private ImageView photoImageView;

	private LinearLayout loginLayout;
	private LinearLayout notLoginLayout;

	private Button logoutBtn;

	private boolean loginStatus;

	private BaseService baseService = new BaseService();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selfspace);

		loginLayout = (LinearLayout) findViewById(R.id.login_page);
		notLoginLayout = (LinearLayout) findViewById(R.id.not_login_page);
		selfTextView = (TextView) findViewById(R.id.selfspace_textview);
		orderTextView = (TextView) findViewById(R.id.selfspace_orderList);
		accountTextView = (TextView) findViewById(R.id.selfspace_account_setting);
		photoImageView = (ImageView) findViewById(R.id.photo_imageview);
		logoutBtn = (Button) findViewById(R.id.logoutBtn);

		initPage();
		ExitApplication.getInstance().addActivity(this);
	}

	private void initPage() {
		selfTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (loginStatus) {
					Intent intent = new Intent(SelfSpace.this,
							SelfSetting.class);
					startActivityForResult(intent,2);
				} else {
					Toast.makeText(getApplicationContext(), "���ȵ�¼",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		orderTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (loginStatus) {
					Intent intent = new Intent(SelfSpace.this, OrderList.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "���ȵ�¼",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		accountTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (loginStatus) {
					Intent intent = new Intent(SelfSpace.this,
							AccountSetting.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "���ȵ�¼",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		logoutBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				baseService.logout(SelfSpace.this);
				Intent intent = new Intent(SelfSpace.this, Login.class);
				intent.putExtra("isExit", true);
				startActivity(intent);
			}
		});

		SharedPreferences appInfo = getSharedPreferences("app_info", 0);
		this.loginStatus = appInfo.getBoolean("loginStatus", false);

		if (loginStatus) {
			loginLayout.setVisibility(View.VISIBLE);
			notLoginLayout.setVisibility(View.INVISIBLE);
			initLoginCmps();
			logoutBtn.setVisibility(View.VISIBLE);
		} else {
			loginLayout.setVisibility(View.INVISIBLE);
			notLoginLayout.setVisibility(View.VISIBLE);
			initNotLoginCmps();
			logoutBtn.setVisibility(View.INVISIBLE);
		}
	}

	private void initNotLoginCmps() {
		registerLinkTextView = (TextView) findViewById(R.id.register_link_textview);
		registerLinkTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		loginBtn = (Button) findViewById(R.id.loginBtn);

		registerLinkTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SelfSpace.this, Register.class);
				startActivityForResult(intent, 1);
			}
		});

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SelfSpace.this, Login.class);
				intent.putExtra("isExist", true);
				startActivityForResult(intent, 1);
			}
		});

	}

	//���ر�����Ƭ
	private void initLoginCmps() {
		SharedPreferences appInfo = getSharedPreferences("app_info", 0);
		String photo = appInfo.getString("photo", "");
		Log.d("mmmm", "Selfspaceҳ����ص���Ƭsp����:" + photo);
		if (photo != null && !photo.isEmpty() && !photo.equals("null")) {
			String tPathString = android.os.Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/ebike/person";
			Log.d("mmmm",
					"������Ƭ��·��:" + tPathString
							+ photo.substring(photo.lastIndexOf("/")));
			Log.d("mmmm",
					"������Ƭ�Ƿ����:"
							+ new File(tPathString
									+ photo.substring(photo.lastIndexOf("/")))
									.exists());
			photoImageView.setImageBitmap(CommonTool
					.getBitmapFromSD(tPathString
							+ photo.substring(photo.lastIndexOf("/"))));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1: // ��¼�ɹ�����ʱˢ��ҳ��
			Log.d("mmmm", "��¼�ɹ�����Selfspceҳ��");
			initPage();
			break;
		case 2: // SelfspaceSetting���ص���ҳ��ʱ
			Log.d("mmmm", "�������÷���Selfspceҳ��");//ˢ��ҳ�棬��Ϊ������Ƭ��Ϣ�޸���
			initPage();
			break;
		default:
			break;
		}

	}


}
