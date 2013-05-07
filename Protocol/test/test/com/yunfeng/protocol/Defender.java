package test.com.yunfeng.protocol;

import org.jboss.netty.buffer.ChannelBuffer;

import com.yunfeng.protocol.netty.BufferWriter;
import com.yunfeng.protocol.netty.ISendable;
import com.yunfeng.protocol.util.CompactByteArray;

public class Defender implements ISendable {

	@Override
	public int send(ChannelBuffer channelBuffer) {
		int length = 0;
		length += BufferWriter.writeBoolean(channelBuffer, false);
		byte[] bytes = CompactByteArray.writeInts(111, 22, 123, 123321, 12, 12,
				12, 12, 12, 1);
		length += BufferWriter.writeBytes(channelBuffer, bytes);
		return length;
	}

}
