package com.yunfeng.game.module.role.message;

import java.util.List;
import com.yunfeng.game.core.IC2SCommand;
import com.yunfeng.game.core.MemoryData;
import org.jboss.netty.buffer.ChannelBuffer;
import com.yunfeng.protocol.netty.BufferReader;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

@Controller
public class EnterHall_C2S implements IC2SCommand{
	private RoleService roleService ;

	public RoleService getRoleService(){
		return roleService;
	}
	@Resource public void setRoleService(RoleService roleService){
		this.roleService = roleService;
	}
	public void execute(ChannelBuffer channelBuffer,MemoryData memoryData){
		roleService.enterHall(memoryData);
	}
}
