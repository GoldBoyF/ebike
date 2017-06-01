package org.riderman.selfspace;

import java.io.File;
import java.net.URL;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.riderman.ExitApplication;
import org.riderman.R;
import org.riderman.exception.NetException;
import org.riderman.picpopupwindow.SelectPicPopupWindow;
import org.riderman.tools.CommonTool;
import org.riderman.tools.RandTool;
import org.riderman.tools.SimpleHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelfSetting extends Activity{
	private Button backBtn;
	private ImageView photoImageView;
	private TextView nicknameTextView;
	private TextView rendertTextView;
	
	private RelativeLayout photoLayout;
	private RelativeLayout nicknameLayout;
	private RelativeLayout renderLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selfspace_setting);
		ExitApplication.getInstance().addActivity(this);
		
		backBtn = (Button) findViewById(R.id.backBtn);
		photoImageView = (ImageView) findViewById(R.id.photo_imageview);
		nicknameTextView = (TextView) findViewById(R.id.nickname_textview);
		rendertTextView = (TextView) findViewById(R.id.render_textview);
		
		photoLayout = (RelativeLayout) findViewById(R.id.photo_block);
		nicknameLayout = (RelativeLayout) findViewById(R.id.nickname_block);
		renderLayout = (RelativeLayout) findViewById(R.id.render_block);
		
		backBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				setResult(2, intent);
				SelfSetting.this.finish();
			}
		});
		
		photoImageView.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				//使用startActivityForResult启动SelectPicPopupWindow当返回到此Activity的时候就会调用onActivityResult函数
				startActivityForResult(new Intent(SelfSetting.this,
						SelectPicPopupWindow.class), 1);
			}
		});
		
		nicknameLayout.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SelfSetting.this, NicknameSetting.class);
				intent.putExtra("nickname", nicknameTextView.getText().toString());
				startActivityForResult(intent, 2);
			}
		});

		renderLayout.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SelfSetting.this, RenderSetting.class);
				intent.putExtra("render", rendertTextView.getText().toString());
				startActivityForResult(intent, 3);
			}
		});
		
		//加载我的个人信息
		SharedPreferences appInfo = getSharedPreferences("app_info", 0);  		

		String photo = appInfo.getString("photo","");
		if(photo!=null && !photo.isEmpty() && !photo.equals("null")){
			String tPathString = android.os.Environment.getExternalStorageDirectory()
					 .getAbsolutePath()+"/ebike/person";
			photoImageView.setImageBitmap(CommonTool.getBitmapFromSD(tPathString+photo.substring(photo.lastIndexOf("/")))); 
		}
		
		String nickname = appInfo.getString("nickname","");
		if(nickname==null || nickname.isEmpty() || nickname.equals("null")){
			nicknameTextView.setText("请输入");
		}else {
			nicknameTextView.setText(nickname);
		}
		
		String render = appInfo.getString("render","");
		if(render==null || render.isEmpty() || render.equals("null")){
			rendertTextView.setText("请选择");
		}else {
			rendertTextView.setText(render);
		}			

	}
	
	private Bitmap getBitmap(String s)
	{
		Bitmap bitmap = null;
		try
		{
			URL url = new URL(s);
			bitmap = BitmapFactory.decodeStream(url.openStream());
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bitmap;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("mmmm", "返回的结果是:"+resultCode);
		String tPathString = android.os.Environment.getExternalStorageDirectory()
				 .getAbsolutePath()+"/ebike";
		switch (resultCode) {
		case 1:
			if (data != null) {
				//取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
				Uri mImageCaptureUri = data.getData();
				//返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
				if (mImageCaptureUri != null) {
					Bitmap image;
					try {
						//这个方法是根据Uri获取Bitmap图片的静态方法
						image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
						if (image != null) {
							//photoImageView.setImageBitmap(image);
							uploadImage(image);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Bundle extras = data.getExtras();
					if (extras != null) {
						//这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
						Bitmap image = extras.getParcelable("data");
						if (image != null) {
							//photoImageView.setImageBitmap(image);
							uploadImage(image);
						}						
					}
				}

			}
			break;
		case 2:
			String newNickname = data.getStringExtra("nickname");
			nicknameTextView.setText(newNickname);
			Log.d("mmmm", "返回啦");
			break;
		case 3:
			String render = data.getStringExtra("render");
			rendertTextView.setText(render);
			Log.d("mmmm", "返回啦"+render);
			break;
		default:
			break;

		}
	}
	
	private void uploadImage(final Bitmap image){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				SharedPreferences appInfo = getSharedPreferences("app_info", 0);
				String sid = appInfo.getString("sid", "");
				Log.d("mmmm", "上传图片时的sid："+sid);
				String tPathString = android.os.Environment.getExternalStorageDirectory()
						 .getAbsolutePath()+"/ebike";
				String filePath = CommonTool.savePicToSdcard(image, tPathString, UUID.randomUUID().toString());
				SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN,sid);
				Log.d("mmmm", "上传图片时的文件是否存在："+new File(filePath).exists());
				try {
					JSONObject jsonObject = client.postFile("/selfspace/photo", filePath);
				
					Log.d("mmmm", "上传图片结果："+jsonObject.getBoolean("success"));
					if(jsonObject.getBoolean("success")){ //上传成功，则把文件移到/ebike/person目录
						String photoName = filePath.substring(filePath.lastIndexOf("/")); // 比如 2222134.jpg
						String localDestPath = tPathString+"/person/"+photoName; //本地的照片存储路径
						SharedPreferences.Editor editor = appInfo.edit();
						editor.putString("photo", "/storage/person/"+photoName);
						editor.commit();
						CommonTool.savePhotoFromURL(SimpleHttpClient.DOMAIN+"/storage/person/"+photoName, localDestPath);
						handler.sendEmptyMessage(0);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NetException e) {
					e.printStackTrace();
				}				
			}
		}).start();
		
	}
	
	@Override
	public void onBackPressed() {		
		Intent intent = new Intent();
		setResult(2, intent);
		SelfSetting.this.finish();
	}
	
	/**
	 * 上传成功后更新图片视图
	 */
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 0:
				initPhoto();
				break;
			default:
				break;
			}
		}

	};
	
	//加载本地照片
	private void initPhoto() {
		SharedPreferences appInfo = getSharedPreferences("app_info", 0);
		String photo = appInfo.getString("photo", "");
		Log.d("mmmm", "SelfspaceSetting页面加载的照片sp数据:" + photo);
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
	}
}
