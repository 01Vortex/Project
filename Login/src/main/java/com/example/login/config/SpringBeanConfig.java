package com.example.login.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
*如未使用@Configuration  ,扫不到该类的Bean
*如注册了未实现的thymeleaf bean 则可能导致前端无法被渲染
*/
@Configuration
public class SpringBeanConfig {
    @Value("${aliyun.sms.access-key-id}")
    private String smsKeyId;

    @Value("${aliyun.sms.access-secret}")
    private String smsSecret;




    /**
     * 配置RedisTemplate以支持自定义序列化方式
     * 该方法通过Spring的@Bean注解声明，用于创建并配置一个RedisTemplate实例
     * RedisTemplate是Spring Data Redis提供的用于操作Redis的数据结构库，它可以使用不同的序列化方式来处理存入Redis的数据
     * @param factory RedisConnectionFactory实例，用于创建、管理和配置Redis连接
     * 该参数由Spring框架自动注入，用于构建RedisTemplate
     * @return 返回一个配置好的RedisTemplate实例，用于执行各种Redis操作
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(factory);
        return template;
    }

    // 阿里云短信客户端






























    }













































