package org.riderman;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class RiderManApplication extends Application {

	public void onCreate() {
		super.onCreate();
		// ��ʹ�� SDK �����֮ǰ��ʼ�� context ��Ϣ������ ApplicationContext
		// ʹ�ðٶȵ�ͼ֮ǰ���ȳ�ʼ��SDK why��
		SDKInitializer.initialize(this);
	}

}
