package com.example.login.utility;

import java.util.Random;

public class RandomCodeUtil {

    public static String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
