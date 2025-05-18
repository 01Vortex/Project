package com.example.login.service;



import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
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

    @Autowired
    private IAcsClient iAcsClient;


    private static final String VERIFICATION_CODE_PREFIX = "VerificationCodeWithEmail:";

    private static final Logger logger = LogManager.getLogger(VerificationCodeServiceImpl.class);


    public void sendVerificationCodeWithEmail(String target_email, String random_code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2901449366@qq.com");
        message.setTo(target_email);
        message.setSubject("您的注册验证码");
        message.setText("验证码：" + random_code + "，请勿泄露给他人。");
        javaMailSender.send(message);
    }

    public void sendVerificationCodeWithPhone(String targetPhone, String random_code) {
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(targetPhone);
        request.setSignName("你的短信签名"); // 如：阿里云测试
        request.setTemplateCode("你的模板CODE"); // 如：SMS_123456789
        request.setTemplateParam("{\"code\":\"" + random_code + "\"}");

        try {
            SendSmsResponse response = iAcsClient.getAcsResponse(request);
            logger.info("短信发送成功: {}", response.getMessage());
        } catch (ClientException e) {
            logger.error("短信发送失败，错误码: {}, 错误信息: {}", e.getErrCode(), e.getMessage());
            throw new RuntimeException("短信验证码发送失败，请稍后再试。", e);
        }
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
