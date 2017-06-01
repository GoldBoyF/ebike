package org.riderman.tools;

import android.R.integer;


public class RandTool {
	
	public static String rand(Integer num){
		String result = "";
		for (int i = 0; i < num; i++) {
			result += (int)(Math.random()*10);
		}
		return result;
	}
	
}
