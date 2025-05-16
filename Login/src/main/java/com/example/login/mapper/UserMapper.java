package com.example.login.mapper;

import com.example.login.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users(username, password,email,role) VALUES(#{username}, #{password},#{email},#{role})")
    void createNewAccount(User user);

    @Update("UPDATE users SET password = #{password} WHERE email = #{email}")
    void updatePasswordByEmail(String email, String password);

    @Update("UPDATE users SET password = #{password} WHERE phone_number = #{phone}")
    void updatePasswordByPhone(String phone, String password);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);


}