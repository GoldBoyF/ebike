package org.riderman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.riderman.routers.Router1;
import org.riderman.routers.Router2;
import org.riderman.routers.Router3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class RoutePush extends Fragment {

	private View view;
	private ListView listView;
	private List<Map<String, Object>> mListItem;
	private List<Bitmap> bitmaps=new ArrayList<Bitmap>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_router, null);
		listView = (ListView) view.findViewById(R.id.RouterlistView);
		mListItem = getItemList(null);
		MyAdapter myAdapter = new MyAdapter(getActivity());
		listView.setAdapter(myAdapter);
		listView.setDividerHeight(0);


		int count = 0;
		for (Map<String, Object> map : mListItem) {
			bitmaps.add(BitmapFactory.decodeResource(getActivity()
					.getResources(), Integer.parseInt(""
					+ map.get("PicimageView"))));
		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				Toast.makeText(getActivity(), "ceshi", Toast.LENGTH_LONG)
//						.show();
				switch (position) {
				case 0:
					startActivity(new Intent(getActivity(),Router1.class));
					
					break;
				case 1:
					startActivity(new Intent(getActivity(),Router2.class));
					
				break;
				case 2:
					startActivity(new Intent(getActivity(),Router3.class));
					
					break;
				default:
					break;
				}
			
			
			
			}
		});
//		showShare();
		return view;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	class ViewHolder {

		public ImageView PicimageView;
		public TextView title;
		public TextView location;
		public TextView distance;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mListItem.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();// ���ܶ���
				convertView = mInflater
						.inflate(R.layout.list_item_router, null);
				holder.PicimageView = (ImageView) convertView
						.findViewById(R.id.PicimageView);
				holder.title = (TextView) convertView
						.findViewById(R.id.routertitle);
				holder.location = (TextView) convertView
						.findViewById(R.id.routerlocation);
				holder.distance = (TextView) convertView
						.findViewById(R.id.routerdistance);
				convertView.setTag(holder);// why
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// �˴�����չ�ĵط�
			if (mListItem.size() > 0) {

				// Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				// R.drawable.img_xihu);
				// Log.v("lishang", "--  --" + view);
				// holder.PicimageView.setImageBitmap(bitmap);
				holder.PicimageView.setImageBitmap(bitmaps.get(position));
				holder.title.setText("" + mListItem.get(position).get("title"));
				holder.location.setText(""
						+ mListItem.get(position).get("location"));
				holder.distance.setText("���룺"
						+ mListItem.get(position).get("distance") + "Km");
			}
			return convertView;
		}
	}

	private List<Map<String, Object>> getItemList(JSONArray jsonArray) {
		// ����������� �����������
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// for (int i = 0; jsonArray != null && i < jsonArray.length(); i++) {
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("storetitle", jsonArray.getJSONObject(i).get("name"));
		// map.put("storerating", jsonArray.getJSONObject(i).get("grade") + "");
		// map.put("storeaddr", jsonArray.getJSONObject(i).get("location"));
		// String distance = String.format("%.2fǧ��", jsonArray
		// .getJSONObject(i).get("distance"));
		// map.put("storedistance", distance);
		// map.put("id", "" + jsonArray.getJSONObject(i).get("id"));
		// map.put("lat", jsonArray.getJSONObject(i).get("lat"));
		// map.put("lng", jsonArray.getJSONObject(i).get("lng"));
		//
		// list.add(map);
		// }

		// public ImageView PicimageView;
		// public TextView title;
		// public TextView location;
		// public TextView distance;
		//

		int[] img_ID = { R.drawable.img_xihu, R.drawable.img_lj1,
				R.drawable.img_9x };
		String[] strings={ "�������������죺����·��","�����һ�壺����","��Ϫʮ�˽�"};
		String [] location={"��������������ɽ·98��","������������������","��������������Ϫʮ�˽�"};
		String [] distance={"70","60","30"};		
		for (int i = 0; i < 3; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("PicimageView", "" + img_ID[i]);
			map.put("title", strings[i]);
			map.put("location", location[i]);
			// String distance = String.format("%.2fǧ��", jsonArray
			// .getJSONObject(i).get("distance"));
			map.put("distance", distance[i]);
			list.add(map);
		}
		return list;
	}

	
//	  private void showShare() {
//	        ShareSDK.initSDK(getActivity());
//	        OnekeyShare oks = new OnekeyShare();
//	        //�ر�sso��Ȩ
//	        oks.disableSSOWhenAuthorize();
//	        
//	        // ����ʱNotification��ͼ�������
//	        oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//	        // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
//	        oks.setTitle(getString(R.string.share));
//	        // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
//	        oks.setTitleUrl("http://sharesdk.cn");
//	        // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
//	        oks.setText("���Ƿ����ı�");
//	        // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
//	        oks.setImagePath("/sdcard/test.jpg");
//	        // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
//	        oks.setUrl("http://sharesdk.cn");
//	        // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
//	        oks.setComment("���ǲ��������ı�");
//	        // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
//	        oks.setSite(getString(R.string.app_name));
//	        // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
//	        oks.setSiteUrl("http://sharesdk.cn");
//
//	        // ��������GUI
//	        oks.show(getActivity());
//	   }
}
