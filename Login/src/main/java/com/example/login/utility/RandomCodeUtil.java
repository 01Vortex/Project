package com.example.login.utility;

import java.util.Random;

public class RandomCodeUtil {

public static String generateCode() {
    return String.valueOf(100000 + new Random().nextInt(900000));
}
















}
