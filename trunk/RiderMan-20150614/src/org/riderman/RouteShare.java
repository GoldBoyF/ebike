package org.riderman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import org.riderman.picpopupwindow.SelectPicPopupWindow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class RouteShare extends Fragment {
	Bitmap imageBitmap;
	private View view;
	private ImageView imageView;
	private Handler handler;
	private String nameSnipshot;
	private String obsolutePathString;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_router_share, null);
		imageView = (ImageView) view.findViewById(R.id.imageViewsnipshot);
		ImageView snipshot = (ImageView) view.findViewById(R.id.snipshotcamera);
		snipshot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(getActivity(),
						SelectPicPopupWindow.class), 1);
			}
		});

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == ConstantTool.GET_PHOTO) {
					imageView.setImageBitmap(imageBitmap);
					showShare();
				}
			}

		};
		showShare();
		return view;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void showShare() {
		ShareSDK.initSDK(getActivity());
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher,
				getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		oks.setImagePath("/sdcard/test.jpg");
		oks.setImagePath(obsolutePathString);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");
		// 启动分享GUI
		oks.show(getActivity());
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v("lishang", "lishang");

		if (resultCode == 1)
			if (data != null) {
				Uri mImageCaptureUri = data.getData();
				if (mImageCaptureUri != null) {
					try {
						imageBitmap = MediaStore.Images.Media.getBitmap(
								getActivity().getContentResolver(),
								mImageCaptureUri);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Bundle extras = data.getExtras();
					if (extras != null)
						imageBitmap = extras.getParcelable("data");
				}
				if (imageBitmap != null) {
					FileOutputStream fout = null;
					nameSnipshot = DateFormat.format("yyyyMMdd_hhmmss",
							Calendar.getInstance(Locale.CHINA)) + ".jpg";
					File file = new File("/sdcard/SnipShotEbike/");
					file.mkdirs();
					obsolutePathString= file.getPath() + nameSnipshot;
//					obsolutePathString = filename;
					try {
						fout = new FileOutputStream(obsolutePathString);
						imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
								fout);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						try {
							fout.flush();
							fout.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					Message.obtain(handler, ConstantTool.GET_PHOTO)
							.sendToTarget();

				}
			}

	}

}
