package com.pcis.smartbus.event.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pcis.smartbus.event.EventHandler;
import com.pcis.smartbus.event.EventModel;
import com.pcis.smartbus.event.EventType;
import com.pcis.smartbus.mail.service.MailSenderService;

/**
* @author qishan
* @version 创建时间：2019年8月26日 上午8:31:05
*/
@Component
public class EmailHandler implements EventHandler{

	@Autowired
	MailSenderService mailService;
	
	@Override
	public void doHandler(EventModel model) {
		// TODO Auto-generated method stub
		Map<String,Object> message = model.getEventMsg();
		String emailTo = (String) message.get("email");
		mailService.sendWithHTMLTemplate(emailTo, "威腾母线监测平台报警邮件", message);
	}

	@Override
	public List<EventType> getHandableEvent() {
		// TODO Auto-generated method stub
		return Arrays.asList(EventType.EMAIL);
	}


}
