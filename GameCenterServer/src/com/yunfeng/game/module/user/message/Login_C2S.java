package com.yunfeng.game.module.user.message;

import javax.annotation.Resource;

import org.jboss.netty.buffer.ChannelBuffer;
import org.springframework.stereotype.Controller;

import com.yunfeng.game.core.IC2SCommand;
import com.yunfeng.game.core.MemoryData;
import com.yunfeng.protocol.netty.BufferReader;

@Controller
public class Login_C2S implements IC2SCommand {
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	@Resource
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void execute(ChannelBuffer channelBuffer, MemoryData memoryData) {
		int id = BufferReader.readInt(channelBuffer);
		userService.login(memoryData, id);
	}
}