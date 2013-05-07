package test.com.yunfeng.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * amf3协议解码类
 * 
 * @author sunwei
 * @version 2010-7-21
 * @since JDK1.5
 */
public class AMF3Decoder extends FrameDecoder {
	public static final Logger logger = LoggerFactory
			.getLogger(AMF3Decoder.class);

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() < 4) {
			return null;
		}
		buffer.markReaderIndex();
		int length = buffer.readInt();
		if (buffer.readableBytes() < length) {
			buffer.resetReaderIndex();
			return null;
		}
		ChannelBuffer obj = buffer.readBytes(length);
		// int magicNum = obj.readInt();
		// int dataLength = obj.readInt();
		// System.out.println("magic num={" + magicNum + "},data length={"
		// + dataLength + "}");
		// // 读AMF3字节流的内容
		// byte[] content = new byte[obj.readableBytes()];
		// obj.readBytes(content);
		// SerializationContext serializationContext = new
		// SerializationContext();
		// Amf3Input amf3Input = new Amf3Input(serializationContext);
		// amf3Input.setInputStream(new ByteArrayInputStream(content));
		// Object message = amf3Input.readObject();
		return obj;
	}
}