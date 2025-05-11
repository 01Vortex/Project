package com.example.login.service;

import com.example.login.mapper.UserMapper;
import com.example.login.model.User;
import com.example.login.utility.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder bcryptPasswordEncoder;

    public User loadUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public void registerNewUserAccount(User user) {
        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userMapper.insert(user);
    }
}