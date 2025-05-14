package com.example.login.utility;

import java.util.Random;

public class VerificationCodeUtil {

    public static String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public static boolean isValid(String input, String expected) {
        return input != null && input.equals(expected);
    }
}
