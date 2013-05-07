package com.yunfeng.game.core;

public abstract interface IService {
	public abstract void init(Object o);

	public abstract void destroy(Object o);

	public abstract void handleMessage(Object o);

	public abstract String getName();

	public abstract void setName(String str);
}