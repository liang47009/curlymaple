package com.yunfeng.game.core;

import java.util.*;
import javax.annotation.Resource;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.stereotype.Controller;
import com.yunfeng.game.module.system.message.Test_C2S;
import com.yunfeng.game.module.user.message.Login_C2S;

@Controller
public class CommandDispatcher {
	private AbstractXmlApplicationContext axac ;
	private static final short SYSTEM_TEST_C2S_NUMBER  = 6;
	private static final short USER_LOGIN_C2S_NUMBER  = 100;
	private static final Map<Short, IC2SCommand> mm  = new TreeMap<Short, IC2SCommand>();;

	public AbstractXmlApplicationContext getAxac(){
		return axac;
	}
	@Resource public void setAxac(AbstractXmlApplicationContext axac){
		this.axac = axac;
	}
	public void init(){
		mm.put(SYSTEM_TEST_C2S_NUMBER, axac.getBean(Test_C2S.class));
		mm.put(USER_LOGIN_C2S_NUMBER, axac.getBean(Login_C2S.class));
	}
	public static IC2SCommand parse(short messageId){
		return mm.get(messageId);
	}
}
