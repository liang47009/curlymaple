package com.yunfeng.protocol;

public abstract interface IEncrypter {
	/**
	 * 加密
	 * @param param
	 * @return
	 */
	public abstract byte[] encrypt(byte[] param);
	/**
	 * 解密
	 * @param param
	 * @return
	 */
	public abstract byte[] decrypt(byte[] param);
}