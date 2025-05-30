package com.example.login.service;



public interface VerificationCodeService {

       //  根据邮箱发送验证码
       void sendVerificationCodeWithEmail(String target_email, String random_code);

       // 根据手机号发送验证码
       void sendVerificationCodeWithPhone(String target_phone, String random_code);

       // 验证验证码
       boolean validateVerificationCode(String email_phone, String input_code);

       // 存储验证码到 Redis
       void storeVerificationCodeToRedis(String email_phone, String random_code);


    }
