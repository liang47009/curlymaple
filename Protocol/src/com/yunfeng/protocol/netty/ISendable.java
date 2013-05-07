package com.yunfeng.protocol.netty;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 所有可发送的实体类从这里实现
 * 
 * @author xialiangliang
 * 
 */
public interface ISendable {
	int send(ChannelBuffer channelBuffer);
}
