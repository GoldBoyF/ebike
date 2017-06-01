package org.riderman;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;
import org.riderman.exception.NetException;
import org.riderman.tools.SimpleHttpClient;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class ActivityStackManager extends Application {
	// list
	private List<Activity> mList = new LinkedList<Activity>();
	private static ActivityStackManager instance;

	private ActivityStackManager() {
	}

	public synchronized static ActivityStackManager getInstance() {
		if (null == instance) {
			instance = new ActivityStackManager();
		}
		return instance;
	}

	// add Activity
	public void pushActivity(Activity activity) {
		mList.add(activity);
	}

	public void popActivity(Activity activity) {
		mList.remove(activity);

	}

	public void exitApplication() {
		Log.d("mmmm", "程序退出");
		thread.interrupt();
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public void goToHome() {
		try {

			for (int i = mList.size() - 1; i >= 0; i--) {
				if (mList.get(i) != null) {
					if (i != 0) {
						mList.get(i).finish();
						popActivity(mList.get(i));
					} else {
						mList.get(i).startActivity(
								new Intent(ActivityStackManager.this,
										Homemenu.class));
						mList.get(i).finish();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
// 清除所有无效Activtiy
		}
	}

	
//	public void finishTran() {
//		try {
//
//			for (int i = 0; i <mList.size(); i--) {
//				if (mList.get(i) != null) {
//					{
////						popActivity(mList.get(i));
//						mList.get(i).finish();
//					
//					}  
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//// 清除所有无效Activtiy
//		}
//	}
	
	public void homeMenuClear() {
		try {

			for (int i = mList.size() - 1; i >= 0; i--) {
				if (mList.get(i) != null) {

					mList.get(i).finish();
					popActivity(mList.get(i));
			
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	public Thread thread;

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}
	
	

}