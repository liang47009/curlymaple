package com.yunfeng.game.core;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunfeng.game.world.manager.UserManager;

@Service
public class CommandExecutor implements IService, Runnable {
	private String name;
	@Resource
	private UserManager userManager;
	private volatile boolean isActive = false;

	public void init(Object o) {
		if (this.isActive) {
			throw new IllegalArgumentException(
					"Object is already initialized. Destroy it first!");
		}
		this.isActive = true;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("CommandExecutor");
		while (this.isActive) {
			userManager.executeCommand();
		}
	}

	@Override
	public void destroy(Object paramObject) {
		this.isActive = false;
	}

	@Override
	public void handleMessage(Object paramObject) {

	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}
