package com.pcis.smartbus.netty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.pcis.smartbus.utils.RedisUtil;
@Component("serh")
@Scope("prototype")
public class ServerHandler extends ChannelInboundHandlerAdapter{
	private final String machinePrefix = "machine:";
	@Autowired
	RedisUtil redisUtil;
	//通道激活
	 @Override
	    public void channelActive(ChannelHandlerContext ctx) {
	        //System.out.println("active");
	    }
	 //通道数据可读
		@Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) {
	        ByteBuf b = (ByteBuf)msg;
	        byte[] a = new byte[b.readableBytes()];
	        b.readBytes(a);
	        String[] str = new String(a).split(";");
	        
	        int len = str.length;
	        String key = machinePrefix+str[0];
	        
	        //设备首次上线，还未绑定项目，暂时不接受数据
	        if(!redisUtil.Has(key))
	        {
	        	for(int i=1;i<len;i++)
	        	{
	        		redisUtil.ListSet(key, "p"+Integer.toString(i));
	        	}
	        }
	        else
	        {
	        	String proNum = (String) redisUtil.ListGetOne(key, 0);
	        	if(proNum==null )
	        		//proNum==null表示正在绑定，
	        		;
	        	else if(proNum.equals("p1"))
	        		//proNum==p1表示还没绑定过
	        			;
	        	else
	        	{//mac已经绑定了项目，将数据插入到相应的项目下面
	        		List<Object> ls  = redisUtil.ListGet(key, 1, -1);
	        		Map<String, Object> map2 = new HashMap<>();	        		
	        		int listLen = ls.size();
	        		for(int i=0;i<listLen;i++)
	        		{
	        			map2.put((String)ls.get(i), str[i+1]);
	        		}
	        		redisUtil.HashSetAll(proNum, map2);
	        	}
	        }
	       // System.out.println(Thread.currentThread().getName()+"处理了");
	        b.release();
	    }
		@Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
	        // Close the connection when an exception is raised.
	        cause.printStackTrace();
	        ctx.close();
	    }
	
}
