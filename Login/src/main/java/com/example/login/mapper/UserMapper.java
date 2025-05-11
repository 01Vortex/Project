package com.example.login.mapper;

import com.example.login.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO users(username, password) VALUES(#{username}, #{password})")
    void insert(User user);
}