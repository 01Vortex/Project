package com.example.login.service.Interface;



public interface VerificationCodeService {

       void sendVerificationCodeWithEmail(String target_email, String generate_code);

   //  void sendVerificationCodeWithPhone(String target_phone, String generate_code);

       boolean validateVerificationCode(String email_phone, String cached_code);

 //    void sendVerificationCodeByPhone();

    }
