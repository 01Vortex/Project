package com.example.login.service;

import com.example.login.mapper.UserMapper;
import com.example.login.model.User;
import com.example.login.service.Interface.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class UserServiceImpl implements UserService , UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);


    // 创建账号
    @Override
    public void createAccount(User user) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("该用户名已被占用，请选择其他用户名");
        }
        // 检查邮箱是否已被注册
        if (userMapper.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("该邮箱已被注册，请输入其他邮箱地址");
        }
        // 插入数据库
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        userMapper.createNewAccount(user);
        logger.info("用户 {} 创建成功", user.getUsername());
    }

    // 重置密码
    @Override
    public void resetPassword(String email_phone, String newPassword){
        if(email_phone.contains("@")){
            String email = email_phone;
            userMapper.updatePasswordByEmail(email,passwordEncoder.encode(newPassword));
            logger.info("邮箱 {} 的密码已重置", email);
        }
        else{
            String phone = email_phone;
            userMapper.updatePasswordByPhone(phone,passwordEncoder.encode(newPassword));
            logger.info("手机号 {} 的密码已重置", phone);
        }
    }


    // 实现SpringSecurity的查找用户方法
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                getAuthorities(user)
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String role = user.getRole();
        if (role == null || role.isBlank()){
            role = "ROLE_NULL";
        }
        return AuthorityUtils.createAuthorityList(role);
    }























}
