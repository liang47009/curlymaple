package com.yunfeng.game.world;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.yunfeng.game.world.manager.RoomManager;
import com.yunfeng.game.world.manager.ZoneManager;

/**
 * 游戏区
 * 
 * @author xialiangliang
 * 
 */
public class BloodWarZone implements IZone {
	private static AtomicInteger autoID = new AtomicInteger(0);
	private int id;
	private String name;
	private ZoneManager zoneManager;
	private final RoomManager roomManager;
	private final Map<Integer, IRoom> roomsById;
	private final Map<String, IRoom> roomsByName;
	private volatile int maxAllowedRooms = 5;
	private volatile int maxAllowedUsers = 600;

	public BloodWarZone() {
		this.id = getNewID();
		this.name = "第" + this.id + "区";
		this.roomsById = new ConcurrentHashMap<>();
		this.roomsByName = new ConcurrentHashMap<>();
		this.roomManager = new RoomManager();
		this.roomManager.setOwnerZone(this);
		for (int i = 0; i < maxAllowedRooms; i++) {
			roomManager.createRoom();
		}

	}

	private static int getNewID() {
		return autoID.getAndIncrement();
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

	public ZoneManager getZoneManager() {
		return zoneManager;
	}

	public void setZoneManager(ZoneManager zoneManager) {
		this.zoneManager = zoneManager;
	}

	public int getMaxAllowedRooms() {
		return maxAllowedRooms;
	}

	public void setMaxAllowedRooms(int maxAllowedRooms) {
		this.maxAllowedRooms = maxAllowedRooms;
	}

	public int getMaxAllowedUsers() {
		return maxAllowedUsers;
	}

	public void setMaxAllowedUsers(int maxAllowedUsers) {
		this.maxAllowedUsers = maxAllowedUsers;
	}

	public Map<Integer, IRoom> getRoomsById() {
		return roomsById;
	}

	public Map<String, IRoom> getRoomsByName() {
		return roomsByName;
	}

}