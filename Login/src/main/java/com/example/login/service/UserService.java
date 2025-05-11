package com.example.login.service;

import com.example.login.model.User;

public interface UserService {
    public User loadUserByUsername(String username);

    public void registerNewUserAccount(User user);

    }
