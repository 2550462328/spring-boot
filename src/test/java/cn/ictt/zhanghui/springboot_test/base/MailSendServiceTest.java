package cn.ictt.zhanghui.springboot_test.base;

import cn.ictt.zhanghui.springboot_test.SpringbootTestApplicationTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;

public class MailSendServiceTest extends SpringbootTestApplicationTests {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void sendMailTest() throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2550462328@qq.com");
        message.setTo("2063681701@qq.com");
        message.setSubject("主题：带附件的邮件");
        message.setText("测试邮件内容");
        mailSender.send(message);
    }

    @Test
    public void sendAttachmentsMail() throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("2550462328@qq.com");
        helper.setTo("2063681701@qq.com");
        helper.setSubject("主题：简单邮件");
        helper.setText("测试邮件内容");;

        FileSystemResource file = new FileSystemResource(new File("E:\\image\\1.jpg"));
        helper.addAttachment("附件-1.jpg", file);
        helper.addAttachment("附件-2.jpg", file);

        mailSender.send(mimeMessage);
    }

    @Test
    public void sendInlineMail() throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("2550462328@qq.com");
        helper.setTo("2063681701@qq.com");
        helper.setSubject("主题：嵌入静态资源");
        helper.setText("<html><body><img style=\"width:100px;height:100px\" src=\"cid:test\" ></body></html>", true);
        FileSystemResource file = new FileSystemResource(new File("E:\\image\\1.jpg"));
        helper.addInline("test", file);
        mailSender.send(mimeMessage);
    }

    @Test
    public void sendTemplateMail() throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("2550462328@qq.com");
        helper.setTo("2063681701@qq.com");
        helper.setSubject("主题：模板邮件");
        Context context = new Context();
        context.setVariable("username","Grey Wolf");
        //获取thymeleaf的html模板
        String emailContent= templateEngine.process("message_template", context);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }
}
