package test.com.yunfeng.protocol;

import static org.jboss.netty.channel.Channels.*;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.logging.LoggingHandler;

import com.yunfeng.protocol.netty.NettyDecoder;
import com.yunfeng.protocol.netty.NettyEncoder;

/**
 * 
 * @author sunwei
 * @version 2010-7-22
 * @since JDK1.5
 */
public class PushServerPipelineFactory implements ChannelPipelineFactory {
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		// 处理日志
		pipeline.addLast("logger", new LoggingHandler());
		// 处理coder
		pipeline.addLast("decoder", new NettyDecoder());
		pipeline.addLast("encoder", new NettyEncoder());
		//
		pipeline.addLast("handler", new PushProtocolHandler());
		//
		return pipeline;
	}
}