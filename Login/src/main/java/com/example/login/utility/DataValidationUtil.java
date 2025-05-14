package com.example.login.utility;

import java.util.regex.Pattern;

public class DataValidationUtil {

        // 校验邮箱格式
        public static boolean isValidEmail(String email) {
            if (email == null || email.isEmpty()) {
                return false;
            }
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(emailRegex);
            return pattern.matcher(email).matches();
        }

        // 校验是否为空或仅包含空白字符
        public static boolean isNotEmpty(String input) {
            return input != null && !input.trim().isEmpty();
        }

        // 校验手机号格式（这里以中国大陆手机号为例）
        public static boolean isValidPhoneNumber(String phoneNumber) {
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                return false;
            }
            String phoneRegex = "^1[3-9]\\d{9}$"; // 中国大陆手机号正则表达式
            Pattern pattern = Pattern.compile(phoneRegex);
            return pattern.matcher(phoneNumber).matches();
        }
        public static boolean isValidPassword(String password) {
            if (password == null || password.isEmpty()) {
                return false;
            }
            String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";
            Pattern pattern = Pattern.compile(passwordRegex);
            return pattern.matcher(password).matches();
        }
        public static boolean isValidUsername(String username) {
            if (username == null || username.isEmpty()) {
                return false;
            }
            String usernameRegex = "^[a-zA-Z0-9_-]{3,16}$";
            Pattern pattern = Pattern.compile(usernameRegex);
            return pattern.matcher(username).matches();
        }

    }

