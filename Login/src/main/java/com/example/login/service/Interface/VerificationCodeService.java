package com.example.login.service.Interface;



public interface VerificationCodeService {

       void sendVerificationCodeWithEmail(String target_email, String random_code);

       void sendVerificationCodeWithPhone(String target_phone, String random_code);


       // 根据email_phone从缓存取出对应的验证码,进行验证
       boolean validateVerificationCode(String email_phone, String input_code);

       // 存储验证码到 Redis,设置有效期为3分钟,key的前缀一定要和验证时的前缀一样
       void storeVerificationCodeToRedis(String email_phone, String random_code);


    }
