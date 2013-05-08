package com.yunfeng.game.module.user.message;

import java.util.List;
import com.yunfeng.game.core.IS2CCommand;
import org.jboss.netty.buffer.*;
import com.yunfeng.protocol.netty.BufferWriter;
import org.jboss.netty.channel.Channel;
import org.springframework.stereotype.Controller;

@Controller
public class Login_S2C implements IS2CCommand {
	private final short messageId = 101;
	private int result;

	public Login_S2C(int result) {
		this.result = result;
	}

	public Login_S2C() {

	}

	public void send(Channel channel) {
		ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(channel
				.getConfig().getBufferFactory());
		int length = 0;
		length += BufferWriter.writeShort(channelBuffer, messageId);
		length += BufferWriter.writeInt(channelBuffer, result);
		channelBuffer = ChannelBuffers.copiedBuffer(channelBuffer.array(), 0,
				length);
		channel.write(channelBuffer);
	}

	public void send(Channel channel, int result) {
		ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(channel
				.getConfig().getBufferFactory());
		int length = 0;
		length += BufferWriter.writeShort(channelBuffer, messageId);
		length += BufferWriter.writeInt(channelBuffer, result);
		channelBuffer = ChannelBuffers.copiedBuffer(channelBuffer.array(), 0,
				length);
		channel.write(channelBuffer);
	}
}
