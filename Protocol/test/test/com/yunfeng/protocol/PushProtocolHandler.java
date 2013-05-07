package test.com.yunfeng.protocol;

import java.util.ArrayList;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunfeng.protocol.netty.BufferWriter;
import com.yunfeng.protocol.netty.ISendable;
import com.yunfeng.protocol.util.CompactByteArray;

public class PushProtocolHandler extends SimpleChannelHandler {

	public static Logger log = LoggerFactory
			.getLogger(PushProtocolHandler.class);

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		Channel channel = ctx.getChannel();
		Test_S2C.getInstance().send(channel, "test");
		// Defender df = new Defender();
		// ArrayList<ISendable> list = new ArrayList<>();
		// list.add(df);
		// ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(channel
		// .getConfig().getBufferFactory());
		// int length = 0;
		// length += BufferWriter.writeShort(channelBuffer, (short) 903);
		// length += BufferWriter.writeBoolean(channelBuffer, true);
		// length += BufferWriter.writeList(channelBuffer, list);
		// byte[] bytes = CompactByteArray.writeInts(111, 11, 2, 45, 2);
		// length += BufferWriter.writeBytes(channelBuffer, bytes);
		// channelBuffer = ChannelBuffers.copiedBuffer(channelBuffer.array(), 0,
		// length);
		// channel.write(channelBuffer);
	}

	/**
      * 
      */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		System.out.println(e.getMessage());
		// if (e.getMessage() != null) {
		// log.warn("unkown message {}", e.getMessage());
		// ChannelManager channelManager = PushServerContext
		// .getBean(ChannelManager.class);
		// if (e.getMessage() instanceof CommandMessage) {
		// channelManager.handleMsg((CommandMessage) e.getMessage(),
		// e.getChannel());
		// } else if (e.getMessage() instanceof PushMessage) {
		// channelManager.handleMsg((PushMessage) e.getMessage(),
		// e.getChannel());
		// } else {
		// log.warn("unkown message {}", e);
		// }
		// }
	}
}