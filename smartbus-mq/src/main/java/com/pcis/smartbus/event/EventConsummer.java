package com.pcis.smartbus.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.pcis.smartbus.utils.RedisUtil;

/**
* @author qishan
* @version 创建时间：2019年8月23日 下午4:04:43
*/
@Component
public class EventConsummer implements InitializingBean{

	private Map<String,EventHandler> handlers;

	private volatile boolean run = true;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	RedisUtil redisUtil;
	
	private Map<EventType,List<EventHandler>> handlersMap;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		handlers = applicationContext.getBeansOfType(EventHandler.class);
		handlersMap =  new HashMap<EventType,List<EventHandler>>();
		//首先填充handlersMap
		handlers.forEach((k,v)->{
			List<EventType> handableType = v.getHandableEvent();
			for(EventType type : handableType) {
				if(!handlersMap.containsKey(type)) {
					handlersMap.put(type, new ArrayList<EventHandler>());
				}
				handlersMap.get(type).add(v);
			}
		});
		//创建一个单独的线程，进行事件处理
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(run) {
					Object obj = getEvent();
					if(obj==null)
						continue;
					EventModel model = (EventModel) obj;
					EventType eventType = model.getEventType();
					for(EventHandler handler : handlersMap.get(eventType)) {
						handler.doHandler(model);
					}
					//处理完成，删除冗余事件
					DeletEvent();
				}
				
			}
		}).start();
	}
	
	@PreDestroy
	public void destroyThread() {
		run = false;
	}
	
	//调用BRPLP，从右边弹出并添加到左边成为冗余事件
	public Object getEvent() {
		Object obj = redisUtil.PopEvent();
		return obj;
	}
	
	//删除冗余事件
	public void DeletEvent() {
		redisUtil.DeleteEvent();
	}

}
