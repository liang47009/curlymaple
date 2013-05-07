package com.yunfeng.game.world;

import com.yunfeng.game.world.manager.ZoneManager;

public interface IZone {

	int getId();

	void setZoneManager(ZoneManager zoneManager);

	String getName();

}
