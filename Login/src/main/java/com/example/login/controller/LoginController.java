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


    // 显示重置密码页面
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam(required = false) String email,
                                        @RequestParam(required = false) String phone,
                                        Model model) {
        if (email != null && !email.isEmpty()) {
            model.addAttribute("email", email);
        } else if (phone != null && !phone.isEmpty()) {
            model.addAttribute("phone", phone);
        } else {
            return "redirect:/forgot-password";
        }
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



    // 重置密码step1: 验证验证码
    @PostMapping("/reset-password-step1")
    @ResponseBody
    public Map<String, Object> verifyCode(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String email = payload.get("email");
        String phone = payload.get("phone");
        String code = payload.get("code");
        boolean isValid = false;
        if (email != null && !email.isEmpty()) {
            isValid = verificationCodeService.validateVerificationCode(email, code);
        } else if (phone != null && !phone.isEmpty()) {
            isValid = verificationCodeService.validateVerificationCode(phone, code);
        }
        response.put("success", isValid);
         return response;
    }

    // 重置密码step2: 密码重置
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam(required = false) String email,
                                @RequestParam(required = false) String phone,
                                @RequestParam("newPassword") String newPassword,
                                RedirectAttributes redirectAttributes) {
        try {
            if (email != null && !email.isEmpty()) {
                userService.resetPassword(email, newPassword);
            } else if (phone != null && !phone.isEmpty()) {
                userService.resetPassword(phone, newPassword);
            } else {
                throw new IllegalArgumentException("无效请求，缺少邮箱或手机号");
            }
            redirectAttributes.addFlashAttribute("message", "密码重置成功，请登录");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "密码重置失败：" + e.getMessage());
            return "redirect:/reset-password?" + (email != null ? "email=" + email : "phone=" + phone);
        }
    }





}