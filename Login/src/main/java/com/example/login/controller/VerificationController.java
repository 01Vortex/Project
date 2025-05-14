package com.example.login.controller;


import com.example.login.service.Interface.VerificationCodeService;
import com.example.login.utility.RandomCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

@Controller
public class VerificationController {
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate; // 注入 Redis 模板


    @GetMapping("/send-code")
    @ResponseBody
    public String sendVerificationCode(@RequestParam String email) {
        String code = RandomCodeUtil.generateCode();
        try {
            verificationCodeService.sendVerificationCodeWithEmail(email, code);
            // 存储验证码到 Redis，设置有效期为 3 分钟
            redisTemplate.opsForValue().set(
                    "verification_code:" + email,
                    code,
                    180, TimeUnit.SECONDS
            );
            return "验证码已发送";
        } catch (Exception e) {
            return "发送失败：" + e.getMessage();
        }
    }















}
