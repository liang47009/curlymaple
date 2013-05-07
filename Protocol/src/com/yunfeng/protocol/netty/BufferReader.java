package com.yunfeng.protocol.netty;

import org.jboss.netty.buffer.ChannelBuffer;

public class BufferReader {

	public static int readInt(ChannelBuffer channelBuffer) {
		return channelBuffer.readInt();
	}

	public static double readDouble(ChannelBuffer channelBuffer) {
		return channelBuffer.readDouble();
	}

	public static String readString(ChannelBuffer channelBuffer) {
		short length = channelBuffer.readShort();
		if (length < 0) {
			return null;
		}
		String s = new String(channelBuffer.readBytes(length).array());
		return s;
	}

	public static short readShort(ChannelBuffer channelBuffer) {
		return channelBuffer.readShort();
	}

	public static boolean readBoolean(ChannelBuffer channelBuffer) {
		return channelBuffer.readByte() == 0 ? false : true;
	}

	public static long readLong(ChannelBuffer channelBuffer) {
		return channelBuffer.readLong();
	}

	public static float readFloat(ChannelBuffer channelBuffer) {
		return channelBuffer.readFloat();
	}
}