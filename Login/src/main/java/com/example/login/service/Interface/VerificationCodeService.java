package com.example.login.service.Interface;



public interface VerificationCodeService {

       void sendVerificationCodeWithEmail(String target_email, String generate_code);

   //  void sendVerificationCodeWithPhone(String target_phone, String generate_code);


       //根据email_phone从缓存取出对应的验证码，进行验证
       boolean validateVerificationCode(String email_phone, String input_code);

 //    void sendVerificationCodeByPhone();

    }
