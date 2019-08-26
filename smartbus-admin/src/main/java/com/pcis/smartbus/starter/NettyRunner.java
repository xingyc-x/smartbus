package com.pcis.smartbus.starter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.pcis.smartbus.netty.NettyServer;


/*
 * @author 王琪善
 * @data 2019-7-8
 * spring启动完成以后启动netty服务
 */
@Component
public class NettyRunner  implements CommandLineRunner{

	@Autowired
	NettyServer netty;
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Netty开始执行");
		//绑定8888 socket端口
		netty.bind(8888);
	}

}
