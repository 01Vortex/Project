package com.example.login.service.Interface;

import com.example.login.model.User;

public interface UserService {
    public User loadUserByUsername(String username);

    public boolean verifyAndRegister(User user, String code);


    }
