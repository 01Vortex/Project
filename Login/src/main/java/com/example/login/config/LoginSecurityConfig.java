package com.example.login.config;


import com.example.login.service.Interface.UserService;
import com.example.login.service.UserServiceImpl;
import com.example.login.utility.RandomCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.security.SecureRandom;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class LoginSecurityConfig {

    private final UserService userService;

    private final UserDetailsService userDetailsService;


    @Lazy
    @Autowired
    public LoginSecurityConfig(UserService userService, UserDetailsService userDetailsService) {
     this.userService = userService;
     this.userDetailsService = userDetailsService;

    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(14, new SecureRandom());
    }

    //Spring Security 检测到手动配置了 AuthenticationProvider，导致默认的 UserDetailsService 自动配置失效，从而发出警告。
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService::loadUserByUsername); // 或继续使用 userService
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserServiceImpl userServiceImpl) throws Exception {
        http

                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/forgot-password", "/send-code","/reset-password").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        /*.defaultSuccessUrl("/index", true)
                        .failureUrl("/login?error=true")*/
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            if (isAdmin) {
                                response.sendRedirect("/admin-index");
                            } else {
                                response.sendRedirect("/index");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key(RandomCodeUtil.generateAlphaNumericKey(32)) // 可以自定义 key，默认使用安全随机生成的 key
                        .tokenValiditySeconds(86400) // 设置 token 过期时间（秒），默认为 14 天
                        .userDetailsService(userDetailsService)
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .authenticationProvider(authenticationProvider()); // 启用自定义 provider


        return http.build();
    }


}