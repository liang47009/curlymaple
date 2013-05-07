package test.com.yunfeng.protocol;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class Main {

	public static void main(String[] args) {
//		server();
		client();
	}
	
	private static void client() {
		// 开始NIO线程
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		// 服务启始点
		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		// 处理过滤器
		bootstrap.setPipelineFactory(new PushServerPipelineFactory());
		// 设置相关参数
		bootstrap.setOption("child.tcpNoDelay", true);
		// 设置相关参数
		bootstrap.setOption("child.keepAlive", true);
		// 绑定相关端口
		bootstrap.connect(new InetSocketAddress("127.0.0.1", 8888));
	}

	private static void server() {
		// 开始NIO线程
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		// 服务启始点
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		// 处理过滤器
		bootstrap.setPipelineFactory(new PushServerPipelineFactory());
		// 设置相关参数
		bootstrap.setOption("child.tcpNoDelay", true);
		// 设置相关参数
		bootstrap.setOption("child.keepAlive", true);
		// 绑定相关端口
		bootstrap.bind(new InetSocketAddress("127.0.0.1", 8888));
	}
}