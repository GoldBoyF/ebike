package org.riderman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ParcelableStoreDetail implements Parcelable { // ����ʵ�ֽӿ�Parcelable

	// ���ﶨ��������������˵������д��˳��Ҫһ��
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
		// �ȶ�ȡmId���ٶ�ȡmDate
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

	// ʵ��Parcelable�ķ���writeToParcel����ParcelableDate���л�Ϊһ��Parcel����
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

	// ʵ������̬�ڲ�����CREATORʵ�ֽӿ�Parcelable.Creator
	public static final Parcelable.Creator<ParcelableStoreDetail> CREATOR = new Creator<ParcelableStoreDetail>() {

		@Override
		public ParcelableStoreDetail[] newArray(int size) {
			return new ParcelableStoreDetail[size];
		}

		// ��Parcel�������л�ΪParcelableDate
		@Override
		public ParcelableStoreDetail createFromParcel(Parcel source) {
			return new ParcelableStoreDetail(source);
		}
	};

}
