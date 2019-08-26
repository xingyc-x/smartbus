package com.pcis.smartbus.event;
/**
* @author qishan
* @version 创建时间：2019年8月23日 下午3:25:47
*/
public enum EventType {

	EMAIL(1);//发送邮件事件
	private int value;
	EventType(int value)
	{
		this.value = value;
	}
	public EventType getType() {
		for(EventType e : EventType.values())
		{
			if(e.value == value)
				return e;
		}
		return null;
	}

	
}
