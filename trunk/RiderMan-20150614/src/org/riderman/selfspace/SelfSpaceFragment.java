package org.riderman.selfspace;

import java.io.File;

import org.riderman.ConstantTool;
import org.riderman.Homemenu;
import org.riderman.Login;
import org.riderman.R;
import org.riderman.Register;
import org.riderman.service.BaseService;
import org.riderman.tools.CommonTool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelfSpaceFragment extends Fragment implements
		View.OnClickListener {

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
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		view = inflater.inflate(R.layout.activity_selfspace, null);

		loginLayout = (LinearLayout) view.findViewById(R.id.login_page);
		notLoginLayout = (LinearLayout) view.findViewById(R.id.not_login_page);

		selfTextView = (TextView) view.findViewById(R.id.selfspace_textview);
		orderTextView = (TextView) view.findViewById(R.id.selfspace_orderList);
		accountTextView = (TextView) view
				.findViewById(R.id.selfspace_account_setting);
		photoImageView = (ImageView) view.findViewById(R.id.photo_imageview);
		logoutBtn = (Button) view.findViewById(R.id.logoutBtn);
		selfTextView.setOnClickListener(this);
		orderTextView.setOnClickListener(this);
		accountTextView.setOnClickListener(this);
		photoImageView.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);

		selfTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (loginStatus) {
					Intent intent = new Intent(getActivity(), SelfSetting.class);
					startActivityForResult(intent, 2);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		orderTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (loginStatus) {
					Intent intent = new Intent(getActivity(), OrderList.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		accountTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (loginStatus) {
					Intent intent = new Intent(getActivity(),
							AccountSetting.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		logoutBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				baseService.logout(getActivity());
				// initNotLoginCmps();
				// Intent intent = new Intent(getActivity(), Homemenu.class);
				// intent.putExtra("isExit", true);
				// startActivity(intent);

				// 可以在Fragmen里面自己替换自己，实现更新
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				// ft.setCustomAnimations(R.anim.slide_left_in,
				// R.anim.slide_left_out);
				ft.addToBackStack("first");
				ft.replace(R.id.fragmentLayoutLike,
						new SelfSpaceFragment());
				ft.commit();
			}
		});

		SharedPreferences appInfo = getActivity().getSharedPreferences(
				"app_info", 0);
		this.loginStatus = appInfo.getBoolean("loginStatus", false);

		if (loginStatus) {
			loginLayout.setVisibility(View.VISIBLE);
			notLoginLayout.setVisibility(View.INVISIBLE);

			SharedPreferences appInfo1 = getActivity().getSharedPreferences(
					"app_info", 0);
			String photo = appInfo1.getString("photo", "");
			Log.d("mmmm", "Selfspace页面加载的照片sp数据:" + photo);
			if (photo != null && !photo.isEmpty() && !photo.equals("null")) {
				String tPathString = android.os.Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/ebike/person";
				Log.d("mmmm",
						"本地照片的路径:" + tPathString
								+ photo.substring(photo.lastIndexOf("/")));
				Log.d("mmmm",
						"本地照片是否存在:"
								+ new File(tPathString
										+ photo.substring(photo
												.lastIndexOf("/"))).exists());
				photoImageView.setImageBitmap(CommonTool
						.getBitmapFromSD(tPathString
								+ photo.substring(photo.lastIndexOf("/"))));
			}

			logoutBtn.setVisibility(View.VISIBLE);
		} else {
			loginLayout.setVisibility(View.INVISIBLE);
			notLoginLayout.setVisibility(View.VISIBLE);

			registerLinkTextView = (TextView) view
					.findViewById(R.id.register_link_textview);
			registerLinkTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			loginBtn = (Button) view.findViewById(R.id.loginBtn);

			registerLinkTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), Register.class);
					startActivityForResult(intent, 1);
				}
			});

			loginBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), Login.class);
					intent.putExtra("isExist", true);
					startActivityForResult(intent, 1);
				}
			});

			logoutBtn.setVisibility(View.INVISIBLE);
		}
		return view;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.v("lishang", "198   onResume()");

		SharedPreferences appInfo1 = getActivity().getSharedPreferences(
				"app_info", 0);
		String photo = appInfo1.getString("photo", "");
		Log.d("mmmm", "Selfspace页面加载的照片sp数据:" + photo);
		if (photo != null && !photo.isEmpty() && !photo.equals("null")) {
			String tPathString = android.os.Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/ebike/person";
			Log.d("mmmm",
					"本地照片的路径:" + tPathString
							+ photo.substring(photo.lastIndexOf("/")));
			Log.d("mmmm",
					"本地照片是否存在:"
							+ new File(tPathString
									+ photo.substring(photo.lastIndexOf("/")))
									.exists());
			photoImageView.setImageBitmap(CommonTool
					.getBitmapFromSD(tPathString
							+ photo.substring(photo.lastIndexOf("/"))));
		}

		super.onResume();

		// getFragmentManager().getFragment(arg0, arg1)
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.v("lishang", "198   onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.v("lishang", "198   onPause()");
		super.onPause();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v("lishang", "198   onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.v("lishang", "198   onStop()");
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private void initNotLoginCmps() {
		registerLinkTextView = (TextView) view
				.findViewById(R.id.register_link_textview);
		registerLinkTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		loginBtn = (Button) view.findViewById(R.id.loginBtn);

		registerLinkTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), Register.class);
				startActivityForResult(intent, 1);
			}
		});

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), Login.class);
				intent.putExtra("Login", ConstantTool.LOG_IN);
				startActivityForResult(intent, 1);
			}
		});

	}
}
