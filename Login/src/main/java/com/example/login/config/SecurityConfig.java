package com.example.login.config;


import com.example.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userService.loadUserByUsername(username);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/forgot-password").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/index", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")               // 默认就是 /logout，可选
                        .logoutSuccessUrl("/login")         // 登出后跳转的页面
                        .invalidateHttpSession(true)        // 使 session 失效
                        .deleteCookies("JSESSIONID")        // 删除 cookie
                        .permitAll()
                )
               .sessionManagement(session -> session
                .maximumSessions(1) // 最大并发会话数为 1
                .maxSessionsPreventsLogin(true) // 达到上限后阻止新登录
                );


        return http.build();
    }

}