package com.example.login.service;


import com.example.login.service.Interface.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;



    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2901449366@qq.com");
        message.setTo(to);
        message.setSubject("您的注册验证码");
        message.setText("验证码：" + code + "，请勿泄露给他人。");
        javaMailSender.send(message);
    }

}
