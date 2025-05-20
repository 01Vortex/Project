package com.example.login.config;



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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.config.Customizer;


import javax.sql.DataSource;
import java.security.SecureRandom;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class LoginSecurityConfig {


    private final UserDetailsService userDetailsService;

    private final DataSource dataSource;


    @Lazy
    @Autowired
    public LoginSecurityConfig(UserDetailsService userDetailsService,DataSource dataSource) {

     this.userDetailsService = userDetailsService;
     this.dataSource  = dataSource;

    }


    // 配置 remember-me 功能
    @Bean
    public PersistentTokenRepository remembermeTokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }

    // 密码加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(14, new SecureRandom());
    }

    // 配置 AuthenticationProvider，用于处理用户认证
    //Spring Security检测到手动配置了AuthenticationProvider，导致默认的UserDetailsService自动配置失效，从而发出警告。
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // 修复点：直接设置 UserDetailsService 实例
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }



    // 配置 HttpSecurity，用于配置认证和授权
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserServiceImpl userServiceImpl) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
             //   .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/forgot-password", "/send-code-email", "/send-code-phone","/reset-password").permitAll()
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
                        .rememberMeCookieName("remember-me")
                        .tokenRepository(remembermeTokenRepository()) // 使用数据库存储
                        .key(RandomCodeUtil.generateAlphaNumericKey(20)) // 可选：自定义 key，默认使用安全随机生成的 key
                        .tokenValiditySeconds(15)  // 设置 token 有效时间（秒），默认为 14 天（1209600 秒） 86400 1天
                        .userDetailsService(userServiceImpl) // 用于加载用户信息
                        .rememberMeParameter("remember-me")   // 前端 checkbox 的 name 属性值
                        .alwaysRemember(false)                // 是否始终记住（忽略前端 checkbox）
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .authenticationProvider(authenticationProvider()); // 启用自定义 provider


        return http.build();
    }


}