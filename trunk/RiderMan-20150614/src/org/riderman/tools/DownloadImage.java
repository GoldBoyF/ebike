package org.riderman.tools;

import java.io.File;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
	ImageView imageView;

	public DownloadImage(ImageView imageView) {
		// TODO Auto-generated constructor stub
		this.imageView = imageView;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		// TODO Auto-generated method stub
		String photo = urls[0];
		String url = SimpleHttpClient.DOMAIN+photo;
		String localPhotoPath = android.os.Environment.getExternalStorageDirectory()
				 .getAbsolutePath()+"/ebike/bike"+photo.substring(photo.lastIndexOf("/"));
		Bitmap tmpBitmap = null;
		if(!new File(localPhotoPath).exists()){
			try {
				InputStream is = new java.net.URL(url).openStream();
				tmpBitmap = BitmapFactory.decodeStream(is);
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("test", e.getMessage());
			}
			CommonTool.savePhotoFromURL(url, localPhotoPath);
		}else{
			tmpBitmap = CommonTool.getBitmapFromSD(localPhotoPath);
		}
				
		return tmpBitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		imageView.setImageBitmap(result);
	}
}