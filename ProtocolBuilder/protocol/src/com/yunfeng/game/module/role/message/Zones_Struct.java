package com.yunfeng.game.module.role.message;

import com.yunfeng.game.core.IS2CCommand;
import com.yunfeng.protocol.netty.*;
import org.jboss.netty.channel.Channel;
import org.springframework.stereotype.Controller;
import org.jboss.netty.buffer.ChannelBuffer;
import com.yunfeng.protocol.util.CompactByteArray;
import java.util.*;

@Controller
public class Zones_Struct implements ISendable {
	private int id;
	private String name;

	public Zones_Struct(ChannelBuffer channelBuffer) {
		id = BufferReader.readInt(channelBuffer);
		name = BufferReader.readString(channelBuffer);
	}

	public Zones_Struct(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Zones_Struct() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int send(ChannelBuffer channelBuffer) {
		int length = 0;
		length += BufferWriter.writeString(channelBuffer, name);
		byte[] bytes = CompactByteArray.writeInts(id);
		length += BufferWriter.writeBytes(channelBuffer, bytes);
		return length;
	}
}
