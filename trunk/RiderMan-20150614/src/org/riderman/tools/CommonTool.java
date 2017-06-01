package org.riderman.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class CommonTool {
	public static String savePicToSdcard(Bitmap bitmap, String path,
			String fileName) {		
		String filePath = "";
		if (bitmap == null) {
			return filePath;
		} else {

			filePath=path+ "/"+fileName+".jpg";
			File destFile = new File(filePath);
			OutputStream os = null;
			try {
				os = new FileOutputStream(destFile);
				bitmap.compress(CompressFormat.JPEG, 100, os);
				os.flush();
				os.close();
			} catch (IOException e) {
				filePath = "";
			}
		}
		return filePath;
	}
	

	public static void savePhotoFromURL(String imageURL,String filePath) {
		try {
			InputStream inputStream = getStreamFromURL(imageURL);
			FileOutputStream  outputStream = new FileOutputStream(filePath);
			int c;
            while((c=inputStream.read())!=-1)
            {
            	outputStream.write(c);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static InputStream getStreamFromURL(String imageURL) {
		InputStream in=null;
		try {
			URL url=new URL(imageURL);
			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
			in=connection.getInputStream();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return in;
		
	}
	
	public static Bitmap getBitmapFromUrl(String s)
	{
		Bitmap bitmap = null;
		try
		{
			URL url = new URL(s);
			bitmap = BitmapFactory.decodeStream(url.openStream());
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bitmap;
	}
	
	public static Bitmap getBitmapFromSD(String filePath)
	{
		InputStream inputStream=getBitmapInputStreamFromSDCard(filePath); 
		return BitmapFactory.decodeStream(inputStream); 
	}
	
	
	
	//读取SD卡下的图片 
	private static InputStream getBitmapInputStreamFromSDCard(String fileName){ 
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { 
			File file=new File(fileName); 
			try { 
				FileInputStream fileInputStream=new FileInputStream(file); 
			return fileInputStream; 
			} catch (Exception e) { 
			e.printStackTrace(); 
			} 
		} 
		return null; 
	} 

	/**
	 * 下载自行车图片
	 * @param photo 比如"/storage/bike/001.jpg"
	 */
	public static void downloadBikeImage(String photo){
		String url = SimpleHttpClient.DOMAIN+photo;
		String localPhotoPath = android.os.Environment.getExternalStorageDirectory()
				 .getAbsolutePath()+"/ebike/bike"+photo.substring(photo.lastIndexOf("/"));
		Bitmap tmpBitmap = null;
		if(!new File(localPhotoPath).exists()){
			CommonTool.savePhotoFromURL(url, localPhotoPath);
		}
	}
	
	/**
	 * 读取自行车图片，从sd卡中
	 * @param photo
	 * @return
	 */
	public static Bitmap getBikeBitmapFromSD(String photo)
	{
		String localPhotoPath = android.os.Environment.getExternalStorageDirectory()
				 .getAbsolutePath()+"/ebike/bike"+photo.substring(photo.lastIndexOf("/"));
		InputStream inputStream=getBitmapInputStreamFromSDCard(localPhotoPath); 
		return BitmapFactory.decodeStream(inputStream); 
	}


}
