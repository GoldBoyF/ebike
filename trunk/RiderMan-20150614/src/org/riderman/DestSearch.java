package org.riderman;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class DestSearch extends Activity implements OnClickListener {

	private ImageButton backButton;
	private Button searchButton;
	private EditText searchEditText;
	private LatLng latLng;
	private GeoCoder geoCoder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		backButton = (ImageButton) findViewById(R.id.backButton_search);
		backButton.setOnClickListener(this);
		searchButton = (Button) findViewById(R.id.search);
		searchButton.setOnClickListener(this);
		searchButton.setEnabled(false);
		searchEditText = (EditText) findViewById(R.id.inputsearch);
		latLng=new LatLng(0, 0);
		searchEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (searchEditText.getText().toString() != null) {
					searchButton.setEnabled(true);
				} else
					searchButton.setEnabled(false);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// 设定光标位置
				searchEditText.setSelection(searchEditText.length());
			}
		});
		searchEditText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_DEL) {
					if (searchEditText.getText().toString().length() < 1)
						searchButton.setEnabled(false);
				}
				return false;
			}
		});
 
		TextView textView = (TextView) findViewById(R.id.TextView1_1);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView1_2);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView1_3);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView2_1);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView2_2);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView2_3);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView3_1);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView3_2);
		textView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.TextView3_3);
		textView.setOnClickListener(this);
		
		 geoCoder=GeoCoder.newInstance();
		 geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				// TODO Auto-generated method stub
				latLng=new LatLng(result.getLocation().latitude, result.getLocation().longitude);
		Log.v("lishang", "weidu "+latLng.latitude+"jingdu "+latLng.longitude);
				search(latLng.latitude, latLng.longitude);
			}
			

		});
		
		
	}
		
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.backButton_search:
			finish();
			break;
		case R.id.search:
			// 开启 
			// 为何必须选定城市呢？还不能选择失败
			geoCoder.geocode(new GeoCodeOption().city("杭州市").address(
					("杭州市"+searchEditText.getText().toString())));
			break;
		default:
			TextView  textView = (TextView) findViewById(v.getId());
			searchEditText.setText(textView.getText());
			geoCoder.geocode(new GeoCodeOption().city("杭州市").address(
					("杭州市"+searchEditText.getText().toString())));
			break;
		}
		
	}

	private void search(double lat, double lng) {
//地列位置换算，经纬度获取		
		Log.v("lishang", "weidu "+latLng.latitude+"jingdu "+latLng.longitude);
		Intent intent = new Intent(DestSearch.this, Homemenu.class);
		intent.putExtra("lat", lat);
		intent.putExtra("lng", lng);
		intent.putExtra("CMD", ConstantTool.IS_SEARCH_CDM);
		//可以放很多
		startActivity(intent);
		finish();
	}
}