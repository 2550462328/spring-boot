package cn.ictt.zhanghui.springboot_test.base.util.third;

import com.sun.mail.smtp.SMTPSendFailedException;
import com.sun.mail.util.MailConnectException;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.AuthenticationFailedException;

public class MailUtil {
    private static final String HOST = "mail.ictt.cn";
    private static final String ALIAS = "系统通知";
    private static final String SENDER = "zwdai@ictt.cn";
    private static final String USERNAME = "zwdai@ictt.cn";
    private static final String PASSWORD = "ys1234";

    public static void main(String[] args) {
        MailUtil.send("zwdai@ictt.cn", "您的服务器已经上线，请查看", "daidaidai");
    }

    public static boolean send(String receiver, String subject, String message) {
        HtmlEmail email = new HtmlEmail();
        try {
            email.setHostName(HOST);
            email.setCharset("UTF-8");
            email.addTo(receiver);
            email.setFrom(SENDER, ALIAS);
            email.setAuthentication(USERNAME, PASSWORD);
            email.setSubject(subject);
            email.setMsg(message);
            email.send();
            return true;
        } catch (EmailException e) {
            Class<?> clazz = e.getCause().getClass();
            if (AuthenticationFailedException.class.equals(clazz)) {
                System.out.println("AuthenticationFailedException:邮箱验证出错");
            } else if (SMTPSendFailedException.class.equals(clazz)) {
                System.out.println("SMTPSendFailedException:发件人邮箱地址不正确");
            } else if (MailConnectException.class.equals(clazz)) {
                System.out.println("MailConnectException:检查SMTP地址是否正确,或者帐号是否开启SMTP服务");
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }
}
