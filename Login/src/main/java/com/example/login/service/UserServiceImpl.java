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

        // 检查密码是否符合复杂度要求
        if (!isPasswordStrong(user.getPassword())) {
            throw new IllegalArgumentException("Password does not meet complexity requirements.");
        }

        // 密码加密并保存用户
        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userMapper.insert(user);
    }





    







    private boolean isPasswordStrong(String password) {
    // 检查密码是否包含大小写字母、数字和特殊字符，且长度不少于8位
    return password != null && password.length() >= 8
            && password.matches(".*[a-z].*")
            && password.matches(".*[A-Z].*")
            && password.matches(".*\\d.*")
            && password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
}


}