package com.pcis.smartbus.netty;



import javax.annotation.PreDestroy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import com.pcis.smartbus.netty.ServerHandler;
@Component
public class NettyServer implements BeanFactoryAware{
	private BeanFactory bf;
	@Autowired
	ServerHandler serh;
	//在linux下支持epllEventLoopGroup,效率更高
	private EventLoopGroup accept = new NioEventLoopGroup(2);
	private EventLoopGroup process = new NioEventLoopGroup(4);
	private ServerBootstrap boot = new ServerBootstrap();
	
	@PreDestroy
    public void close() {
		accept.shutdownGracefully();
		process.shutdownGracefully();
		System.out.println("Netty线程池关闭");
    }
	public void bind(int port)
	{
		System.out.println("开始绑定");
		
	try {
		boot.group(accept, process)
        .channel(NioServerSocketChannel.class) 
        .option(ChannelOption.SO_BACKLOG, 1024) 
        .childHandler(new ChannelInitializer<SocketChannel>() { 
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast((ServerHandler)bf.getBean("serh"));
            }
        });
		
			ChannelFuture f = boot.bind(port).sync();
			System.out.println("绑定端口");
			f.channel().closeFuture().sync();
			System.out.println("关闭Netty服务器");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			// TODO Auto-generated catch block
			accept.shutdownGracefully();
			process.shutdownGracefully();
		}
		
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		bf=beanFactory;
	}
}
