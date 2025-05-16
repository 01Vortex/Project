package com.example.login.service.Interface;

import com.example.login.model.User;

public interface UserService {
    User loadUserByUsername(String username);

    void resetPassword(String email_phone, String newPassword);

    void createAccount(User user);


    }
