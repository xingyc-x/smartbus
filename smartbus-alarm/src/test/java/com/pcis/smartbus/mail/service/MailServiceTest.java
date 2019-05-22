package com.pcis.smartbus.mail.service;

import com.pcis.smartbus.mail.service.impl.MailServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void sendSimpleMail() {
        mailService.sendSimpleMail("monkdmj@163.com", "test simple mail", " hello this is simple mail");
//        mailService.sendSimpleMail("220161393@seu.edu.cn", "test simple mail", " hello this is simple mail");
    }

    @Test
    public void sendHtmlMail() {
        String content = "<html>\n" +
                "<body>\n" +
                "    <h3>hello world ! 这是一封html邮件!</h3>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendHtmlMail("monkdmj@163.com", "test simple mail", content);
//        mailService.sendHtmlMail("220161393@seu.edu.cn", "test simple mail", content);

    }

    @Test
    public void sendAttachmentsMail() {
        String filePath = this.getClass().getClassLoader().getResource("application.properties").getPath();
        mailService.sendAttachmentsMail("monkdmj@163.com", "主题：带附件的邮件", "有附件，请查收！", filePath);
    }

    @Test
    public void sendInlineResourceMail() {
//        String rscId = "neo006";
//        String content = "<html><body>这是有图片的邮件：<img src=\'cid:" + rscId + "\' ></body></html>";
//        String imgPath = this.getClass().getClassLoader().getResource("application.properties").getPath();
//
//        mailService.sendInlineResourceMail("monkdmj@163.com", "主题：这是有图片的邮件", content, imgPath, rscId);
    }

    @Test
    public void sendTemplateMail() {
        //创建邮件正文
        Context context = new Context();
        context.setVariable("id", "006");
        String emailContent = templateEngine.process("mail", context);

        mailService.sendHtmlMail("monkdmj@163.com", "主题：这是模板邮件", emailContent);
    }
}