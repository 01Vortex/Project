package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.Interface.EmailService;
import com.example.login.service.Interface.UserService;
import com.example.login.utility.VerificationCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate; // 注入 Redis 模板

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }



    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }


    @GetMapping("/send-code")
    @ResponseBody
    public String sendVerificationCode(@RequestParam String email) {
        String code = VerificationCodeUtil.generateCode();
        try {
            emailService.sendVerificationCode(email, code);
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


    // 提交注册表单
    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("user") User user,
            @RequestParam("code") String code,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "registration";
        }

        try {
            boolean isVerified = userService.verifyAndRegister(user, code);
            if (!isVerified) {
                redirectAttributes.addFlashAttribute("error", "验证码错误或已过期");
                return "redirect:/register";
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("usernameError", e.getMessage());
            return "redirect:/register";
        }

        redirectAttributes.addFlashAttribute("message", "注册成功，请登录");
        return "redirect:/login";
    }









    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        return "forgot_password";
    }

    @GetMapping("/index")
    public String index() {
        return "index"; // 对应 templates/index.html
    }



    // Add more methods for captcha, third-party login etc.
}