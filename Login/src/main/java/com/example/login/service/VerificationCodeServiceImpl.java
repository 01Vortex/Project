package com.example.login.service;



import com.example.login.service.Interface.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;


@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    private static final String VERIFICATION_CODE_PREFIX = "VerificationCodeWithEmail:";

    private static final Logger logger = LogManager.getLogger(VerificationCodeServiceImpl.class);


    public void sendVerificationCodeWithEmail(String target_email, String generate_code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2901449366@qq.com");
        message.setTo(target_email);
        message.setSubject("您的注册验证码");
        message.setText("验证码：" + generate_code + "，请勿泄露给他人。");
        javaMailSender.send(message);
    }


    public void storeVerificationCodeToRedis(String email_phone, String random_code){
        redisTemplate.opsForValue().set(
                "VerificationCodeWithEmail:" + email_phone,
                random_code,
                180, TimeUnit.SECONDS
        );

    }

    public boolean validateVerificationCode(String email_phone, String input_code) {
    // 缓存里的验证码
    Object storedCodeObject = redisTemplate.opsForValue().get(VERIFICATION_CODE_PREFIX + email_phone);

    //将对象storedCodeObject转换为字符串
    String storedCodeString = storedCodeObject.toString();


    // 检查验证码是否正确
    if (!storedCodeString.equals(input_code)) {
        logger.warn("验证码错误");
        return false;
    }

    // 验证码验证成功并清除缓存
    redisTemplate.delete(VERIFICATION_CODE_PREFIX + email_phone);
    logger.info("账号:{}验证成功",  email_phone);

    return true;
}




































}
