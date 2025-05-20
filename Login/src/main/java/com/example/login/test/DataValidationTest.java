package com.example.login.test;

import com.example.login.utility.DataValidationUtil;

public class DataValidationTest {
    public static void main(String[] args) {
        if(DataValidationUtil.isValidEmail("8989898989@qq.com")) System.out.println("邮箱格式正确");
        if(DataValidationUtil.isValidPassword("Qwfoieouf.")) System.out.println("密码格式正确");
        if(DataValidationUtil.isValidUsername("admin")) System.out.println("用户名格式正确");
        if(DataValidationUtil.isValidPhoneNumber("18888888888")) System.out.println("手机号格式正确");
        if(DataValidationUtil.isNotEmpty("999")) System.out.println("非空");
    }
}
