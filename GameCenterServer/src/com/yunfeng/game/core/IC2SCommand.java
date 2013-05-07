package com.yunfeng.game.core;

import org.jboss.netty.buffer.ChannelBuffer;

public interface IC2SCommand {
	void execute(ChannelBuffer channelBuffer, MemoryData memoryData);
}
