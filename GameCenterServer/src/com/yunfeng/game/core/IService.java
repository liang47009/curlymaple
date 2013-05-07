package com.yunfeng.game.core;

public abstract interface IService {
	public abstract void init(Object paramObject);

	public abstract void destroy(Object paramObject);

	public abstract void handleMessage(Object paramObject);

	public abstract String getName();

	public abstract void setName(String paramString);
}

/*
 * Location: D:\SmartFoxServer2X\SFS2X\lib\sfs2x-core.jar Qualified Name:
 * com.smartfoxserver.bitswarm.service.IService JD-Core Version: 0.6.2
 */