package com.yunfeng.game.world.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.yunfeng.game.world.BloodWarZone;
import com.yunfeng.game.world.IZone;

public class ZoneManager {
	private ConcurrentMap<String, IZone> zones;

	public ZoneManager() {
		this.zones = new ConcurrentHashMap<>();
	}

	public IZone getZoneByName(String name) {
		return (IZone) this.zones.get(name);
	}

	public IZone getZoneById(int id) {
		IZone theZone = null;

		for (IZone zone : this.zones.values()) {
			if (zone.getId() == id) {
				theZone = zone;
				break;
			}
		}
		return theZone;
	}

	public IZone createZone() {
		IZone zone = new BloodWarZone();
		zone.setZoneManager(this);
		this.zones.put(zone.getName(), zone);
		return zone;
	}
}