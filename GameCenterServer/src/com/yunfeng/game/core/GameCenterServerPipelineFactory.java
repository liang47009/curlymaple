package com.yunfeng.game.core;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.logging.LoggingHandler;

import com.yunfeng.protocol.netty.NettyDecoder;
import com.yunfeng.protocol.netty.NettyEncoder;

public class GameCenterServerPipelineFactory implements ChannelPipelineFactory {
	private NettyDecoder decoder = new NettyDecoder();
	private NettyEncoder encoder = new NettyEncoder();
	private LoggingHandler logger = new LoggingHandler();
	private GameCenterServerHandler handler = new GameCenterServerHandler();

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		// 处理日志
		pipeline.addLast("logger", logger);
		// 处理coder
		pipeline.addLast("decoder", decoder);
		pipeline.addLast("encoder", encoder);
		pipeline.addLast("handler", handler);
		return pipeline;
	}
}