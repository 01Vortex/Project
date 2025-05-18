package com.example.login.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration  //如未使用,扫不到该类的Bean
public class SpringBeanConfig {
    @Value("${aliyun.sms.access-key-id}")
    private String smsKeyId;

    @Value("${aliyun.sms.access-secret}")
    private String smsSecret;




    // RedisTemplate
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(factory);
        return template;
    }

    // 阿里云短信客户端
    @Bean
    public IAcsClient iAcsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou", // 地域ID
                "smsKeyId", // 替换为你的 AccessKeyId
                "smsSecret" // 替换为你的 AccessKeySecret
        );

        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        return new DefaultAcsClient(profile);
    }





























    }













































