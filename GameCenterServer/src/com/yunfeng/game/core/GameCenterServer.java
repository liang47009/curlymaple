package com.yunfeng.game.core;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class GameCenterServer {
	public static void main(String[] args) {
		GameCenterServer ns = new GameCenterServer();
		ns.init();
	}

	private void init() {
		GameCenterApplicationContext bwac = new GameCenterApplicationContext(
				"applicationContext.xml");
		CommandDispatcher cd = bwac.getBean(CommandDispatcher.class);
		cd.init();
		ShutDownHook shutDownHook = bwac.getBean(ShutDownHook.class);
		Runtime.getRuntime().addShutdownHook(new Thread(shutDownHook));
		this.start("127.0.0.1", 8888);
	}

	private Logger logger = Logger.getLogger(Module.SERVER);

	public void start(String ip, int port) {
		// 开始NIO线程
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		// 服务启始点
		ServerBootstrap b = new ServerBootstrap(factory);
		// 处理过滤器
		b.setPipelineFactory(new GameCenterServerPipelineFactory());
		// 设置相关参数
		b.setOption("child.tcpNoDelay", true);
		// 设置相关参数
		b.setOption("child.keepAlive", true);
		// 绑定相关端口
		b.bind(new InetSocketAddress(ip, port));
		logger.info("Server start:" + ip + ", " + port);
	}

}
