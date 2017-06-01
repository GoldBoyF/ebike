package org.riderman.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
	public static boolean validatePhoneNumber(String phone){
		if(phone==null || phone.isEmpty())
			return false;
		return phone.matches("^(13|15|17|18)\\d{9}$");
	}
	
	public static boolean validatePassword(String password){
		Pattern p = Pattern.compile("^[A-Za-z0-9]{6,20}$");
		 Matcher m = p.matcher(password);
		 return  m.matches();
	}
}
