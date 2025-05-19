package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.Interface.VerificationCodeService;
import com.example.login.service.Interface.UserService;
import com.example.login.utility.DataValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LoginController {

    private UserService userService;
    private VerificationCodeService verificationCodeService;
    private RedisTemplate<String, String> redisTemplate; // 注入 Redis 模板

    @Autowired
    public LoginController(UserService userService, VerificationCodeService verificationCodeService, RedisTemplate<String, String> redisTemplate) {
        this.userService = userService;
        this.verificationCodeService = verificationCodeService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/index")
    public String index() {
        return "index"; // 对应 templates/index.html
    }


    @GetMapping("/login")
    public String login(Model model) {
        System.out.println("正在进入登录页面");
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

    // 注册
    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("user") User user,
            @RequestParam("code") String code,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
       if (!(DataValidationUtil.isValidUsername(user.getUsername())
                || DataValidationUtil.isValidEmail(user.getEmail())
                || DataValidationUtil.isValidPassword(user.getPassword()))) {
            result.rejectValue("username", "error.user", "请输入正确的用户名、邮箱和密码");
            return "registration";
       } else if (result.hasErrors()) {
           return "registration";
       }

       if (!verificationCodeService.validateVerificationCode(user.getEmail(), code)) {
           redirectAttributes.addFlashAttribute("error", "验证码错误或已过期");
           return "redirect:/register";
       }
           userService.createAccount(user);
           redirectAttributes.addFlashAttribute("message", "注册成功，请登录");
           return "redirect:/login";
    }


    // 重置密码 开启跨域接收不到请求
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, @RequestParam String code, @RequestParam String newPassword) {
        System.out.println(email+code+newPassword);
        //  数据验证 函数一般返回合法即true  !函数返回false
        if (!(DataValidationUtil.isValidEmail(email) || DataValidationUtil.isValidPassword(newPassword))) {
            System.out.println("数据验证失败");
            return "redirect:/forgot-password";
        }

        // 验证码校验并重置密码
        if (verificationCodeService.validateVerificationCode(email, code)) {
            userService.resetPassword(email, newPassword);
            System.out.println("密码重置成功");
        }
        return "redirect:/login";
    }















}