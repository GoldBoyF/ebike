package org.riderman.selfspace;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.riderman.R;
import org.riderman.exception.NetException;
import org.riderman.tools.SimpleHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class OrderComment extends Activity{
	
	private TextView titleTextView;
	private Button backBtn;
	private EditText commentEditText;
	private RatingBar ratingBar;
	private Button submitBtn;
	private String orderId;
	private int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_comment);
		
		titleTextView = (TextView) findViewById(R.id.provider_title);
		backBtn = (Button) findViewById(R.id.backBtn);
		commentEditText = (EditText) findViewById(R.id.comment_edittext);
		ratingBar = (RatingBar) findViewById(R.id.userratingBar);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		
		orderId = getIntent().getStringExtra("orderId");
		position = getIntent().getIntExtra("position", -1);
		Log.d("mmmm", "����ҳ�涩���ţ�"+orderId+","+position);
		backBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				
				OrderComment.this.finish();
			}
		});
		
		submitBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						SharedPreferences appInfo = getSharedPreferences("app_info", 0);
						String sid= appInfo.getString("sid", "");
						SimpleHttpClient client = new SimpleHttpClient(SimpleHttpClient.DOMAIN, sid);
						Map map = new HashMap();
						map.put("star", (int)ratingBar.getRating());
						map.put("comment", commentEditText.getText().toString());
						Log.d("mmmm", "�������"+map);
						JSONObject jsonObject = null;
						try {
							jsonObject = client.post("/selfspace/rentOrders/"+orderId+"/grade",map);
						} catch (NetException e) {
							e.printStackTrace();
						}
						Log.d("mmmm", "�ҵ����۷��ص�JSON:"+jsonObject);
					}
				}).start();
				Intent intent = new Intent();
				intent.putExtra("position", position);
				Log.d("mmmm", "����ҳ�淵�ص�pos:"+position);
				setResult(1, intent);
				OrderComment.this.finish();
			}
		});
		
	}
}
