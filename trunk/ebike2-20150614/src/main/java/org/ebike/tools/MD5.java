package org.ebike.tools;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @ClassName: MD5 
 * @Description: MD5工具类
 * @author hzjintianfan
 * @date 2014-7-23 下午7:23:08
 */
public class MD5
{
	public static String getMD5(String input)
	{
		String s = null;
		char hexDigits[] =
		{ // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try
		{
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] source = new byte[input.length()];
			for (int i = 0; i < input.length(); i++)
			{
				source[i] = (byte) input.charAt(i);
			}
			md.update(source);
			byte tmp[] = md.digest();
			// MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2];
			// 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++)
			{
				// 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i];
				// 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				// 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf];
				// 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 计算文件内容DM5值
	 * @param file
	 * @return
	 */
	public static String getMD5(File file)
	{
		FileInputStream fis = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length = -1;
			while ((length = fis.read(buffer)) != -1)
			{
				md.update(buffer, 0, length);
			}
			return bytesToString(md.digest());
		}
		catch (Exception ex)
		{
			Logger.getLogger(MD5.class.getName()).log(Level.SEVERE, null, ex);
		}
		finally
		{
			try
			{
				fis.close();
			}
			catch (Exception ex)
			{
				Logger.getLogger(MD5.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return null;
	}

	public static String bytesToString(byte[] data)
	{
		char hexDigits[] =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] temp = new char[data.length * 2];
		for (int i = 0; i < data.length; i++)
		{
			byte b = data[i];
			temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
			temp[i * 2 + 1] = hexDigits[b & 0x0f];
		}
		return new String(temp);

	}

}