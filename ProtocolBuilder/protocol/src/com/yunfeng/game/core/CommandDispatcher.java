package com.yunfeng.game.core;

import java.util.*;
import javax.annotation.Resource;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.stereotype.Controller;
import com.yunfeng.game.module.role.message.EnterHall_C2S;
import com.yunfeng.game.module.role.message.EnterZone_C2S;
import com.yunfeng.game.module.role.message.EnterRoom_C2S;
import com.yunfeng.game.module.role.message.EnterDesk_C2S;
import com.yunfeng.game.module.role.message.QuitZone_C2S;
import com.yunfeng.game.module.role.message.QuitRoom_C2S;
import com.yunfeng.game.module.role.message.QuitDesk_C2S;
import com.yunfeng.game.module.system.message.Test_C2S;
import com.yunfeng.game.module.user.message.Login_C2S;

@Controller
public class CommandDispatcher {
	private AbstractXmlApplicationContext axac;
	private static final short ROLE_ENTERHALL_C2S_NUMBER = 200;
	private static final short ROLE_ENTERZONE_C2S_NUMBER = 202;
	private static final short ROLE_ENTERROOM_C2S_NUMBER = 204;
	private static final short ROLE_ENTERDESK_C2S_NUMBER = 206;
	private static final short ROLE_QUITZONE_C2S_NUMBER = 208;
	private static final short ROLE_QUITROOM_C2S_NUMBER = 209;
	private static final short ROLE_QUITDESK_C2S_NUMBER = 210;
	private static final short SYSTEM_TEST_C2S_NUMBER = 6;
	private static final short USER_LOGIN_C2S_NUMBER = 100;
	private static final Map<Short, IC2SCommand> mm = new TreeMap<Short, IC2SCommand>();;

	public AbstractXmlApplicationContext getAxac() {
		return axac;
	}

	public void setAxac(AbstractXmlApplicationContext axac) {
		this.axac = axac;
	}

	public void init() {
		mm.put(ROLE_ENTERHALL_C2S_NUMBER, axac.getBean(EnterHall_C2S.class));
		mm.put(ROLE_ENTERZONE_C2S_NUMBER, axac.getBean(EnterZone_C2S.class));
		mm.put(ROLE_ENTERROOM_C2S_NUMBER, axac.getBean(EnterRoom_C2S.class));
		mm.put(ROLE_ENTERDESK_C2S_NUMBER, axac.getBean(EnterDesk_C2S.class));
		mm.put(ROLE_QUITZONE_C2S_NUMBER, axac.getBean(QuitZone_C2S.class));
		mm.put(ROLE_QUITROOM_C2S_NUMBER, axac.getBean(QuitRoom_C2S.class));
		mm.put(ROLE_QUITDESK_C2S_NUMBER, axac.getBean(QuitDesk_C2S.class));
		mm.put(SYSTEM_TEST_C2S_NUMBER, axac.getBean(Test_C2S.class));
		mm.put(USER_LOGIN_C2S_NUMBER, axac.getBean(Login_C2S.class));
	}

	public static IC2SCommand parse(short messageId) {
		return mm.get(messageId);
	}
}
