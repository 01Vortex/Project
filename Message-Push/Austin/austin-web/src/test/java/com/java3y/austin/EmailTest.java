package com.java3y.austin;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailTest {
    public static void main(String[] args) {
        final String host = "smtp.qq.com";
        final int port = 465;
        final String user = "2901449366@qq.com";
        final String pass = "qrhhdwlrmuledffa"; // 注意这是授权码

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("2055314849@qq.com"));
            message.setSubject("测试邮件");
            message.setText("这是一封测试邮件，请勿回复。");

            Transport.send(message);
            System.out.println("邮件发送成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

