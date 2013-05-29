package com.yunfeng.protocol.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static String format32(String plainText)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes());
		byte b[] = md.digest();
		int num;
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < b.length; i++) {
			num = b[i];
			if (num < 0)
				num += 256;
			if (num < 16)
				buf.append("0");
			buf.append(Integer.toHexString(num));
		}
		return buf.toString();
	}

	public static String format16(String plainText)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes());
		byte b[] = md.digest();
		int num;
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < b.length; i++) {
			num = b[i];
			if (num < 0)
				num += 256;
			if (num < 16)
				buf.append("0");
			buf.append(Integer.toHexString(num));
		}
		return buf.toString().substring(8, 24);
	}

	public static int asc2(String string) {
		char[] temp = string.toCharArray();
		int build = 0;
		for (int i = 0; i < temp.length; ++i) {
			char each = temp[i];
			if (i == temp.length - 1) {
				build *= (int) each;
			} else {
				build += (int) each;
			}
		}
		return build;
	}
}
