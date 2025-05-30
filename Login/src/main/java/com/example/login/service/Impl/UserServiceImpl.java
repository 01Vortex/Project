package com.example.login.service.Impl;

import com.example.login.mapper.UserMapper;
import com.example.login.model.User;
import com.example.login.service.UserService;
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
public class UserServiceImpl implements UserService, UserDetailsService {

    // 将 userMapper 和 passwordEncoder 声明为 final，并在构造函数中初始化。这样可以确保注入后不可变，提高类的安全性和可读性。
    //实例调用函数用用final
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);


    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder,UserMapper userMapper){
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }



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
        // 判断是邮箱还是手机号
        if(email_phone.contains("@")){
            userMapper.updatePasswordByEmail(email_phone,passwordEncoder.encode(newPassword));
            logger.info("邮箱 {} 的密码已重置", email_phone);
        }
        else{
            userMapper.updatePasswordByPhone(email_phone,passwordEncoder.encode(newPassword));
            logger.info("手机号 {} 的密码已重置", email_phone);
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

    // 根据用户角色获取权限
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String role = user.getRole();
        if (role == null || role.isBlank()){
            role = "ROLE_NULL";
        }
        return AuthorityUtils.createAuthorityList(role);
    }























}
