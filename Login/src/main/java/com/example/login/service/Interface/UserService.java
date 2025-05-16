package com.example.login.service.Interface;

import com.example.login.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public interface UserService {
    User findUserByUsername(String username);

    void resetPassword(String email_phone, String newPassword);

    void createAccount(User user);

    Collection<? extends GrantedAuthority> getAuthorities(User user);


    }
