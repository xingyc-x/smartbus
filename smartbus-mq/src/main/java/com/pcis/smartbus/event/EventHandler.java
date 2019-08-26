package com.pcis.smartbus.event;

import java.util.List;

/**
* @author qishan
* @version 创建时间：2019年8月23日 下午3:24:25
*/
public interface EventHandler {

	void doHandler(EventModel model);
	List<EventType> getHandableEvent();
}
