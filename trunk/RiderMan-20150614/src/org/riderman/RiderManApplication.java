package org.riderman;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class RiderManApplication extends Application {

	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		// 使用百度地图之前首先初始化SDK why？
		SDKInitializer.initialize(this);
	}

}
