package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.VerificationCodeService;
import com.example.login.service.UserService;
import com.example.login.utility.DataValidationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LoginController {

    private final UserService userService;
    private final VerificationCodeService verificationCodeService;

    private static final Logger logger = LogManager.getLogger(LoginController.class);


    @Autowired
    public LoginController(UserService userService, VerificationCodeService verificationCodeService) {
        this.userService = userService;
        this.verificationCodeService = verificationCodeService;
    }

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
        //  数据验证 函数一般返回合法即true  !函数返回false
        if (!(DataValidationUtil.isValidEmail(email) || DataValidationUtil.isValidPassword(newPassword))) {
            logger.error("数据验证失败");
            return "redirect:/forgot-password";
        }

        // 验证码校验并重置密码
        if (verificationCodeService.validateVerificationCode(email, code)) {
            userService.resetPassword(email, newPassword);
        }
        logger.info("密码重置成功");
        return "redirect:/login";
    }















}