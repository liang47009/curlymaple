package com.yunfeng.protocol.netty;

import java.nio.ByteBuffer;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.util.CharsetUtil;


public class BufferWriter {

	public static int writeInt(ChannelBuffer channelBuffer, int i) {
		channelBuffer.writeInt(i);
		return 4;
	}

	public static int writeDouble(ChannelBuffer channelBuffer, double d) {
		channelBuffer.writeDouble(d);
		return 8;
	}

	public static int writeString(ChannelBuffer channelBuffer, String s) {
		if (s == null) {
			channelBuffer.writeShort(0);
			return 2;
		} else {
			int length = s.getBytes(CharsetUtil.UTF_8).length;
			channelBuffer.writeShort(length);
			channelBuffer.writeBytes(s.getBytes(CharsetUtil.UTF_8));
			return 2 + length;
		}
	}

	public static int writeShort(ChannelBuffer channelBuffer, short s) {
		channelBuffer.writeShort(s);
		return 2;
	}

	public static int writeList(ChannelBuffer channelBuffer,
			List<ISendable> list) {
		int length = 2;
		writeShort(channelBuffer, (short) list.size());
		for (ISendable s : (List<ISendable>) list) {
			length += s.send(channelBuffer);
		}
		return length;
	}

	public static int writeBoolean(ChannelBuffer channelBuffer, boolean b) {
		channelBuffer.writeByte(b ? 1 : 0);
		return 1;
	}

	public static int writeFloat(ChannelBuffer channelBuffer, float f) {
		channelBuffer.writeFloat(f);
		return 4;
	}

	public static int writeLong(ChannelBuffer channelBuffer, long l) {
		channelBuffer.writeLong(l);
		return 4;
	}

	public static int writeBytes(ChannelBuffer channelBuffer, byte... bytes) {
		if (bytes == null || bytes.length == 0) {
			channelBuffer.writeShort(0);
			return 2;
		} else {
			channelBuffer.writeShort(bytes.length);
			channelBuffer.writeBytes(bytes);
			return 2 + bytes.length;
		}
	}

	public static int writeInt(ByteBuffer byteBuffer, int i) {
		byteBuffer.putInt(i);
		return 4;
	}

	public static int writeDouble(ByteBuffer byteBuffer, double d) {
		byteBuffer.putDouble(d);
		return 8;
	}

	public static int writeString(ByteBuffer byteBuffer, String s) {
		if (s == null) {
			byteBuffer.putShort((short) 0);
			return 2;
		} else {
			byteBuffer.putShort((short) s.getBytes(CharsetUtil.UTF_8).length);
			byteBuffer.put(s.getBytes(CharsetUtil.UTF_8));
			return 2 + s.getBytes().length;
		}

	}

	public static int writeShort(ByteBuffer byteBuffer, short s) {
		byteBuffer.putShort(s);
		return 2;
	}

	// public static int writeList(ByteBuffer byteBuffer, List list) {
	// int length = 2;
	// writeShort(byteBuffer, (short) list.size());
	// for (ISendable s : (List<ISendable>) list) {
	// length += s.send(byteBuffer);
	// }
	// return length;
	// }

	public static int writeBoolean(ByteBuffer byteBuffer, boolean b) {
		byteBuffer.put(b ? (byte) 1 : (byte) 0);
		return 1;
	}

	public static int writeFloat(ByteBuffer byteBuffer, float f) {
		byteBuffer.putFloat(f);
		return 4;
	}

	public static int writeLong(ByteBuffer byteBuffer, long l) {
		byteBuffer.putLong(l);
		return 4;
	}

}
