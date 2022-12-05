package com.ding.niuke;

import com.ding.niuke.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = NiukeApplication.class)
public class MailTest {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void testMail(){
        mailClient.sendMail("1037755137@qq.com","Test","test1");
    }
    @Test
    public void testForMail(){
        Context context = new Context();
        context.setVariable("username","sunday");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("1037755137@qq.com","html",content);
    }
}
