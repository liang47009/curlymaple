package com.yunfeng.protocol.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * netty 编码类
 * 
 * @author xialiangliang
 * 
 */
public class NettyEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object object) throws Exception {
		if (!channel.isConnected()) {
			return null;
		}
		ChannelBuffer buffer = (ChannelBuffer) object;
		byte[] objSe = buffer.array();
		// objSe = XXTEA.encrypt(objSe);
		if (objSe != null && objSe.length > 0) {
			int length = objSe.length;
			ChannelBuffer newBuffer = ChannelBuffers.buffer(length + 4);
			newBuffer.writeInt(length);
			newBuffer.writeBytes(objSe);
			return newBuffer;
		}
		return null;
	}

}