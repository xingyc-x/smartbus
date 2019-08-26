package com.pcis.smartbus.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcis.smartbus.utils.RedisUtil;

/**
* @author qishan
* @version 创建时间：2019年8月23日 下午3:44:36
*/
@Service
public class EventProducer {

	@Autowired
	RedisUtil redisUtil;
	
	public EventProducer() {}
	
	public void produce(EventModel em) {
		redisUtil.PushEvent(em);
	}
}
