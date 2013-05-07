package com.yunfeng.protocol;

/**
 * 
 * @author xialiangliang
 * 
 */
public abstract interface ICompressor {
	/**
	 * 压缩
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] compress(byte[] param) throws Exception;

	/**
	 * 解压缩
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] uncompress(byte[] param) throws Exception;
}