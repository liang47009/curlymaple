package com.yunfeng.game.module.system.message;

import java.util.*;
import com.yunfeng.game.core.*;
import org.jboss.netty.buffer.*;
import com.yunfeng.protocol.netty.*;
import org.jboss.netty.channel.Channel;

public class Test_S2C implements IS2CCommand{
	private final short messageId  = 7;
	private String message ;
	private static Test_S2C instance  = new Test_S2C();

	public  Test_S2C(String message){
		
		this.message = message;
	}
	public static Test_S2C getInstance(){
		return instance;
	}
	private  Test_S2C(){
		
	}
	public void send(Channel channel){
		ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
		int length = 0;
		length += BufferWriter.writeShort(channelBuffer, messageId);
		length += BufferWriter.writeString(channelBuffer,message);
		channelBuffer = ChannelBuffers.copiedBuffer(channelBuffer.array(), 0, length);
		channel.write(channelBuffer);
		
	}
	public void send(Channel channel,String message){
		ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
		int length = 0;
		length += BufferWriter.writeShort(channelBuffer, messageId);
		length += BufferWriter.writeString(channelBuffer,message);
		channelBuffer = ChannelBuffers.copiedBuffer(channelBuffer.array(), 0, length);
		channel.write(channelBuffer);
		
	}
}
