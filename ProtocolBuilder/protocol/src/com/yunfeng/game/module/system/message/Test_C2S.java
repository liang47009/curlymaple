package com.yunfeng.game.module.system.message;

import java.util.List;
import com.yunfeng.game.core.IC2SCommand;
import com.yunfeng.game.core.MemoryData;
import org.jboss.netty.buffer.ChannelBuffer;
import com.yunfeng.protocol.netty.BufferReader;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

@Controller
public class Test_C2S implements IC2SCommand {
	private SystemService systemService;

	public SystemService getSystemService() {
		return systemService;
	}

	@Resource
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	public void execute(ChannelBuffer channelBuffer, MemoryData memoryData) {
		String message = BufferReader.readString(channelBuffer);
		systemService.test(memoryData, message);
	}
}