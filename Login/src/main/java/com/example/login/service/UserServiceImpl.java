package com.example.login.service;

import com.example.login.mapper.UserMapper;
import com.example.login.model.User;
import com.example.login.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    private static final String VERIFICATION_CODE_PREFIX = "verification_code:";
    private static final String PENDING_USER_PREFIX = "verifyAndRegister";

    public boolean verifyAndRegister(User user, String input_code) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("该用户名已被占用，请选择其他用户名");
        }
        // 检查邮箱是否已被注册
        if (userMapper.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("该邮箱已被注册，请输入其他邮箱地址");
        }
        Object storedCodeObject =redisTemplate.opsForValue().get(VERIFICATION_CODE_PREFIX + user.getEmail());
        String storedCodeString = storedCodeObject.toString();

        // 检查验证码是否正确
        if (storedCodeString == null) return false;
        if (!storedCodeString.equals(input_code)) return false;
        // 插入数据库
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userMapper.insert(user);
        // 清除缓存
        redisTemplate.delete(VERIFICATION_CODE_PREFIX + user.getEmail());
        redisTemplate.delete(PENDING_USER_PREFIX + user.getEmail());
        return true;
    }

    public User loadUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public void resetPassword(String email_phone, String newPassword){
        if(email_phone.contains("@")){
            String email = email_phone;
            userMapper.updatePasswordByEmail(email,passwordEncoder.encode(newPassword));
        }
        else{
            String phone = email_phone;
            userMapper.updatePasswordByPhone(phone,passwordEncoder.encode(newPassword));
        }
    }


}
