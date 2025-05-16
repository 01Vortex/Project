package com.example.login.utility;


import java.util.Random;
import java.security.SecureRandom;

public class RandomCodeUtil {

    // 使用 SecureRandom 实例，确保使用强熵源
    private static final SecureRandom secureRandom = new SecureRandom();

    // 生成六位数的随机验证码
    public static String generateCode() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }



    //生成固定长度的字母数字字符串（不推荐用于加密，可用于用户可见 token）
    public static String generateAlphaNumericKey(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }














}
