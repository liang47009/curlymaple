package test.com.yunfeng.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import com.yunfeng.protocol.netty.BufferWriter;

public class Test_S2C {
	private final short messageId = 6;
	private String message;
	private static Test_S2C instance = new Test_S2C();

	public Test_S2C(String message) {

		this.message = message;
	}

	public static Test_S2C getInstance() {
		return instance;
	}

	private Test_S2C() {

	}

	public void send(Channel channel) {
		ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(channel
				.getConfig().getBufferFactory());
		int length = 0;
		length += BufferWriter.writeShort(channelBuffer, messageId);
		length += BufferWriter.writeString(channelBuffer, message);
		channelBuffer = ChannelBuffers.copiedBuffer(channelBuffer.array(), 0,
				length);
		channel.write(channelBuffer);

	}

	public void send(Channel channel, String message) {
		ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(channel
				.getConfig().getBufferFactory());
		int length = 0;
		length += BufferWriter.writeShort(channelBuffer, messageId);
		length += BufferWriter.writeString(channelBuffer, message);
		channelBuffer = ChannelBuffers.copiedBuffer(channelBuffer.array(), 0,
				length);
		channel.write(channelBuffer);

	}
}
