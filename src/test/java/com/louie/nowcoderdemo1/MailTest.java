package com.louie.nowcoderdemo1;

import com.louie.nowcoderdemo1.utils.MailFunction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class MailTest {

    @Autowired
    private MailFunction mailFunction;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testMail() {
        Context context = new Context();
        context.setVariable("username", "louie");
        String process = templateEngine.process("/mail/maildemo", context);
        mailFunction.sendMail("shirley.yiyi520@gmail.com", "婆婆，这是我的程序在测试发邮件", process);

    }
}
