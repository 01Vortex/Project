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
    public String processRegister(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registration";
        }

        userService.registerNewUserAccount(user);
        model.addAttribute("message", "You have successfully registered! Please log in.");
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