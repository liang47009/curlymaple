package com.yunfeng.game.module.system.message;

import java.util.*;
import com.yunfeng.game.core.*;
import org.jboss.netty.buffer.*;
import com.yunfeng.protocol.netty.*;
import org.jboss.netty.channel.Channel;

public class Sysmsg_S2C implements IS2CCommand{
	private final short messageId  = 1;
	private int type ;
	private String message ;
	private static Sysmsg_S2C instance  = new Sysmsg_S2C();

	public  Sysmsg_S2C(int type,String message){
		
		this.type = type;
		this.message = message;
	}
	public static Sysmsg_S2C getInstance(){
		return instance;
	}
	private  Sysmsg_S2C(){
		
	}
	public void send(Channel channel){
		ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
		int length = 0;
		length += BufferWriter.writeShort(channelBuffer, messageId);
		length += BufferWriter.writeInt(channelBuffer,type);
		length += BufferWriter.writeString(channelBuffer,message);
		channelBuffer = ChannelBuffers.copiedBuffer(channelBuffer.array(), 0, length);
		channel.write(channelBuffer);
		
	}
	public void send(Channel channel,int type,String message){
		ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
		int length = 0;
		length += BufferWriter.writeShort(channelBuffer, messageId);
		length += BufferWriter.writeInt(channelBuffer,type);
		length += BufferWriter.writeString(channelBuffer,message);
		channelBuffer = ChannelBuffers.copiedBuffer(channelBuffer.array(), 0, length);
		channel.write(channelBuffer);
		
	}
}
