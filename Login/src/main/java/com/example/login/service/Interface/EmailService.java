package com.example.login.service.Interface;

import com.example.login.model.User;

public interface EmailService {

    public void sendVerificationCode(String to, String code);

    }
