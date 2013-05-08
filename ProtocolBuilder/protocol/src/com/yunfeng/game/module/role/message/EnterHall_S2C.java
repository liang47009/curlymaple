package com.yunfeng.game.module.role.message;

import java.util.List;
import com.yunfeng.game.core.IS2CCommand;
import org.jboss.netty.buffer.*;
import com.yunfeng.protocol.netty.*;
import org.jboss.netty.channel.Channel;
import org.springframework.stereotype.Controller;

@Controller
public class EnterHall_S2C implements IS2CCommand{
	private final short messageId  = 201;
	private List<ISendable> zones ;

	public  EnterHall_S2C(List<ISendable> zones){
		this.zones = zones;
	}
	public  EnterHall_S2C(){
		
	}
	public void send(Channel channel){
		ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
		int length = 0;
		length += BufferWriter.writeShort(channelBuffer, messageId);
		length += BufferWriter.writeList(channelBuffer,zones);
		channelBuffer = ChannelBuffers.copiedBuffer(channelBuffer.array(), 0, length);
		channel.write(channelBuffer);
	}
	public void send(Channel channel,List<ISendable> zones){
		ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
		int length = 0;
		length += BufferWriter.writeShort(channelBuffer, messageId);
		length += BufferWriter.writeList(channelBuffer,zones);
		channelBuffer = ChannelBuffers.copiedBuffer(channelBuffer.array(), 0, length);
		channel.write(channelBuffer);
	}
}
