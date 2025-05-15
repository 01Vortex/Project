package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.Interface.VerificationCodeService;
import com.example.login.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate; // 注入 Redis 模板

    @GetMapping("/index")
    public String index() {
        return "index"; // 对应 templates/index.html
    }


    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        return "forgot-password";
    }


    @GetMapping("/reset-password")
    public String resetPassword(Model model) {
        return "reset-password";
    }


    /*PostMapping*/

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


    // 重置密码
@PostMapping("/reset-password")
public String resetPassword(@RequestParam String email, @RequestParam String code, @RequestParam String newPassword) {
    // 验证码校验
   if (verificationCodeService.validateVerificationCode(email, code)) {
       userService.resetPassword(email, newPassword);
    }
    return "redirect:/login";
}





}