package org.riderman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ParcelableStoreDetail implements Parcelable { // 声明实现接口Parcelable

	// 这里定义了两个变量来说明读和写的顺序要一致
	public int commentListCount = 9;
	public int bikeDetailCount;
	public HashMap<String, String> storeDetailMap;
	public List<HashMap<String, String>> commentList;
	public List<HashMap<String, String>> bikeDetail;

	public ParcelableStoreDetail(List<HashMap<String, String>> bikeDetail,
			List<HashMap<String, String>> commentList,
			HashMap<String, String> storeDetail) {
		super();
		this.commentList = commentList;
//		Log.v("lishang", commentList+"");
		this.storeDetailMap = storeDetail;
		this.bikeDetail = bikeDetail;
		commentListCount = commentList.size();
		bikeDetailCount = bikeDetail.size();
	}

	// @SuppressWarnings("unchecked")
	public ParcelableStoreDetail(Parcel source) {
		// 先读取mId，再读取mDate
		commentListCount = source.readInt();
		bikeDetailCount = source.readInt();
		commentList = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < commentListCount; i++)
			commentList.add(source.readHashMap(HashMap.class.getClassLoader()));		
		bikeDetail = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < bikeDetailCount; i++)
			bikeDetail.add(source.readHashMap(HashMap.class.getClassLoader()));
		storeDetailMap=new HashMap<String, String>(source.readHashMap(HashMap.class.getClassLoader()));
//		storeDetailMap = source.readHashMap(HashMap.class.getClassLoader());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// 实现Parcelable的方法writeToParcel，将ParcelableDate序列化为一个Parcel对象
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(commentListCount);
		dest.writeInt(bikeDetailCount);
		for (HashMap<String, String> map : commentList)
			dest.writeMap(map);
		for (HashMap<String, String> map : bikeDetail)
			dest.writeMap(map);
		dest.writeMap(storeDetailMap);
	}

	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Parcelable.Creator<ParcelableStoreDetail> CREATOR = new Creator<ParcelableStoreDetail>() {

		@Override
		public ParcelableStoreDetail[] newArray(int size) {
			return new ParcelableStoreDetail[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public ParcelableStoreDetail createFromParcel(Parcel source) {
			return new ParcelableStoreDetail(source);
		}
	};

}
