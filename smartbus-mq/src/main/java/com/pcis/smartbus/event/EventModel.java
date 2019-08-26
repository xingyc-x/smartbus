package com.pcis.smartbus.event;

import java.util.Map;

/**
* @author qishan
* @version 创建时间：2019年8月23日 下午3:33:49
* @info 存储事件信息
*/
public class EventModel {

	private EventType eventType;//事件的类型
	private Map<String,Object> eventMsg;//事件的额外信息
	
	public EventModel() {}

	public EventType getEventType() {
		return eventType;
	}

	public EventModel setEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}

	public Map<String, Object> getEventMsg() {
		return eventMsg;
	}

	public EventModel setEventMsg(Map<String, Object> eventMsg) {
		this.eventMsg = eventMsg;
		return this;
	}
	
	
}
