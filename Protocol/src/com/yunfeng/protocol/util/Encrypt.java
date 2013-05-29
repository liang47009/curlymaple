package com.yunfeng.protocol.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * 
 * @author Administrator
 * 
 */
public class Encrypt {

	public static String MD5(String text) throws NoSuchAlgorithmException {
		String md5 = "";
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] md5hash = new byte[32];
		md.update(text.getBytes(), 0, text.length());
		md5hash = md.digest();
		md5 = convertToHex(md5hash);
		return md5;
	}

	public static String SHA1(String text) throws NoSuchAlgorithmException {
		String sha1 = "";
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update(text.getBytes());
		sha1 = convertToHex(crypt.digest());
		return sha1;
	}

	private static String convertToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String hexString = formatter.toString();
		formatter.close();
		return hexString;
	}
}