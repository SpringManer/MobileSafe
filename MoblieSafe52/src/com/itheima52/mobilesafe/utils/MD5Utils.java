package com.itheima52.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	public static  String encode(String str) {
		StringBuffer stringBuffer = new StringBuffer();
		// 获取加密
		try {
			MessageDigest instance = MessageDigest.getInstance("MD5");
			// 加密后返回的数据
			byte[] digest = instance.digest(str.getBytes());
			

			// 遍历一下数组
			for (byte b : digest) {

				int i = b & 0xff;// 获取最低八位有效值

				String hexString = Integer.toHexString(i);// 将整型转化成十六进制

				// 判断如果是以为，就转换成两位
				if (hexString.length() < 2) {

					hexString = "0" + hexString;
				}
				stringBuffer.append(hexString);
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return stringBuffer.toString();

	}

}
