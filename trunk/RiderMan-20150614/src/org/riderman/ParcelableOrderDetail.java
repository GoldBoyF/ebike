package org.riderman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableOrderDetail implements Parcelable {

	String dataMessage;
	String totalCost;
	String timeMessage;
	int listLen;
	public String getTimeMessage() {
		return timeMessage;
	}
	List<HashMap<String, String>> list;
	
	public ParcelableOrderDetail(String dataMessage, String totalCost,String timeMessage,
			List<HashMap<String, String>> list ) {
		super();
		this.dataMessage = dataMessage;
		this.totalCost = totalCost;
		this.list =list;
		this.listLen=list.size();
		this.timeMessage=timeMessage;
	}

	public String getTotalCost() {
		return totalCost;
	}

	public int getListLen() {
		return listLen;
	}

	public List<HashMap<String, String>> getList() {
		return list;
	}

	public ParcelableOrderDetail(Parcel source) {
		// TODO Auto-generated constructor stub
		dataMessage=source.readString();
		totalCost=source.readString();
		timeMessage=source.readString();
		listLen =source.readInt();
		list=new ArrayList<HashMap<String,String>>();
		for(int i=0;i<listLen;i++)
			list.add(source.readHashMap(HashMap.class.getClassLoader()));
	}

	public String getDataMessage() {
		return dataMessage;
	}

	public void setDataMessage(String dataMessage) {
		this.dataMessage = dataMessage;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(dataMessage);
		dest.writeString(totalCost);
		dest.writeString(timeMessage);
		dest.writeInt(listLen);
		for(HashMap<String,String > hashMap: list)
			dest.writeMap(hashMap);
	} // 声明实现接口Parcelable
	public static final Parcelable.Creator<ParcelableOrderDetail> CREATOR = new Creator<ParcelableOrderDetail>() {

		@Override
		public ParcelableOrderDetail[] newArray(int size) {
			return new ParcelableOrderDetail[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public ParcelableOrderDetail createFromParcel(Parcel source) {
			return new ParcelableOrderDetail(source);
		}
	};
}
