package test.com.yunfeng.protocol;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class Main {

	public static void main(String[] args) {
		// server();
		new Main().client();
		new Main().client();
		new Main().client();
		new Main().client();
		new Main().client();
		new Main().client();
		new Main().client();
	}

	private static List<Channel> channels = new ArrayList<>(32);

	private void client() {
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
		ChannelFuture cf = bootstrap.connect(new InetSocketAddress(
				"172.0.17.253", 8888));
//		ChannelFuture cf = bootstrap.connect(new InetSocketAddress(
//				"172.0.17.153", 8888));
		channels.add(cf.getChannel());
	}

	// private static void server() {
	// // 开始NIO线程
	// ChannelFactory factory = new NioServerSocketChannelFactory(
	// Executors.newCachedThreadPool(),
	// Executors.newCachedThreadPool());
	// // 服务启始点
	// ServerBootstrap bootstrap = new ServerBootstrap(factory);
	// // 处理过滤器
	// bootstrap.setPipelineFactory(new PushServerPipelineFactory());
	// // 设置相关参数
	// bootstrap.setOption("child.tcpNoDelay", true);
	// // 设置相关参数
	// bootstrap.setOption("child.keepAlive", true);
	// // 绑定相关端口
	// bootstrap.bind(new InetSocketAddress("127.0.0.1", 8888));
	// }
}