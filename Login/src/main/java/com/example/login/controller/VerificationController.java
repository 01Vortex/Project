package com.example.login.controller;


import com.example.login.service.Interface.VerificationCodeService;
import com.example.login.utility.DataValidationUtil;
import com.example.login.utility.RandomCodeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class VerificationController {

    private final VerificationCodeService verificationCodeService;

    @Autowired
    public VerificationController(VerificationCodeService verificationCodeService){
        this.verificationCodeService = verificationCodeService;
    }

    private static final Logger logger = LogManager.getLogger(VerificationController.class);


    // 发送验证码到邮箱然后存在Redis   更改了URL 要去安全类放行
    @GetMapping("/send-code-email")
    @ResponseBody
    public String sendVerificationCodeWithEmail(@RequestParam String email) {
        if (!DataValidationUtil.isValidEmail(email)) {
            return "邮箱格式错误";
        }
        String random_code = RandomCodeUtil.generateCode();
        try {
            // 发送验证码到输入的email
            verificationCodeService.sendVerificationCodeWithEmail(email, random_code);
            // 存储验证码到 Redis
            verificationCodeService.storeVerificationCodeToRedis(email,random_code);
            return "验证码已发送";
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("Connection refused")) {
                logger.error("验证码到未存储到Redis:未连接到 Redis,请确保 Redis 服务已启动");
            }
            return "发送失败：" + e.getMessage();
        }
    }

    @GetMapping("/send-code-phone")
    @ResponseBody
    public String sendVerificationCodeWithPhone(@RequestParam String phone) {
        if (!DataValidationUtil.isValidPhoneNumber(phone)) {
            return "手机号码格式错误";
        }
        String random_code = RandomCodeUtil.generateCode();
        try {
            // 发送验证码到输入的手机
            verificationCodeService.sendVerificationCodeWithPhone(phone, random_code);
            // 存储验证码到 Redis
            verificationCodeService.storeVerificationCodeToRedis(phone,random_code);
            return "验证码已发送";
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("Connection refused")) {
                logger.error("验证码到未存储到Redis:未连接到 Redis,请确保Redis服务已启动");
            }
            return "发送失败：" + e.getMessage();
        }
    }

    
















}
