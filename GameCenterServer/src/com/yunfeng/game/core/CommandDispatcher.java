package com.yunfeng.game.core;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

@Controller
public class CommandDispatcher {
	@Resource
	private GameCenterApplicationContext gcac;

	// private static final short SYSTEM_TEST_C2S_NUMBER = 6;
	// private static final short USER_LOGIN_C2S_NUMBER = 100;
	private static final Map<Short, IC2SCommand> mm = new TreeMap<Short, IC2SCommand>();;

	public void init() {
		// mm.put(SYSTEM_TEST_C2S_NUMBER, bwac.getBean(Test_C2S.class));
		// mm.put(USER_LOGIN_C2S_NUMBER, bwac.getBean(Login_C2S.class));
	}

	public static IC2SCommand parse(short messageId) {
		return mm.get(messageId);
	}

	public GameCenterApplicationContext getGcac() {
		return gcac;
	}

	public void setGcac(GameCenterApplicationContext gcac) {
		this.gcac = gcac;
	}
}
