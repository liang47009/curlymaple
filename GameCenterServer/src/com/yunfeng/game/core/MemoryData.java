package com.yunfeng.game.core;

import org.jboss.netty.channel.Channel;

public class MemoryData {
	private Channel channel;

	public MemoryData(Channel channel) {
		this.setChannel(channel);
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}
