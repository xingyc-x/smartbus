package com.pcis.smartbus.mail.service;

import com.pcis.smartbus.db.domain.Project;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.mail.service.impl.MailServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.Format;
import java.text.Normalizer.Form;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Configuration.class)
@EnableAutoConfiguration
public class MailServiceTest {
  
	@Autowired
	MailSenderService mailSender;
	
	@Test
	public void MailTest() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String ,Object> model = new HashMap<>();
		model.put("date", dateFormat.format(new Date()));
		
		SmartbusUser user = new SmartbusUser();
		user.setRealName("王琪善");
		user.setUserName("新空间");
		model.put("user", user);
		
		Project project = new Project();
		project.setOrder("1A");
		project.setLocation("东南大学");
		model.put("project", project);
		
		mailSender.sendWithHTMLTemplate("MrQSWang@163.com", "威腾母线监测平台报警邮件", model);
	}
}