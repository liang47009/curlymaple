package com.yunfeng.game.world;

import java.util.concurrent.atomic.AtomicInteger;

public class BloodWarRoom implements IRoom {
	private static AtomicInteger autoID = new AtomicInteger(0);
	private int id;
	private String name;// 房间名称
	private String password;// 房间密码
	private int maxUsers;// 最大游戏人数
	private int maxPlayers;// 最大游戏人数
	private int maxObservers;// 最大旁观人数
	// private User owner;
	private IZone zone;// 房间所属区

	private static int getNewID() {
		return autoID.getAndIncrement();
	}

	public BloodWarRoom() {
		this.id = getNewID();
		this.name = "第" + this.id + "桌";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public int getMaxObservers() {
		return maxObservers;
	}

	public void setMaxObservers(int maxObservers) {
		this.maxObservers = maxObservers;
	}

	public IZone getZone() {
		return zone;
	}

	public void setZone(IZone zone) {
		this.zone = zone;
	}

}