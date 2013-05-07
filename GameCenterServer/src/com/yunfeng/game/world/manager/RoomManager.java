package com.yunfeng.game.world.manager;

import com.yunfeng.game.world.BloodWarRoom;
import com.yunfeng.game.world.IRoom;
import com.yunfeng.game.world.IZone;

public class RoomManager {
	private IZone ownerZone;

	public IRoom createRoom() {
		IRoom newRoom = new BloodWarRoom();
		newRoom.setZone(this.ownerZone);
		return newRoom;
	}

	public IZone getOwnerZone() {
		return ownerZone;
	}

	public void setOwnerZone(IZone ownerZone) {
		this.ownerZone = ownerZone;
	}
}