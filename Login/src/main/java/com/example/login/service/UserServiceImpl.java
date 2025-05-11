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
        // 检查用户名是否存在
        User existingUser = userMapper.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new IllegalArgumentException("该用户名已被占用，请选择其他用户名");
        }
        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userMapper.insert(user);
    }
}