package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }



    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid User user,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "registration";
        }

        try {
            userService.registerNewUserAccount(user);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("usernameError", e.getMessage());
            return "redirect:/register";
        }

        // 添加 flash attribute，确保跳转后还能看到提示
        redirectAttributes.addFlashAttribute("message", "You have successfully registered! Please log in.");
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