package com.pcis.smartbus.mail.service;

import org.springframework.context.annotation.ComponentScan;

import com.pcis.smartbus.mail.service.impl.MailServiceImpl;

/**
* @author qishan
* @version 创建时间：2019年8月24日 上午8:48:59
*/
@org.springframework.context.annotation.Configuration
@ComponentScan(basePackageClasses=MailSenderService.class)
public class Configuration {

}
