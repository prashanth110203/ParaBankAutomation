package com.parabank.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TestDataGenerator {
    
    private static final Random random = new Random();
    
    public static String generateUniqueUsername(String baseUsername) {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String timestamp = sdf.format(new Date());
        int randomNum = random.nextInt(1000);
        String uniqueUsername = baseUsername.toLowerCase() + "_" + timestamp + "_" + randomNum;
        System.out.println("Generated UNIQUE Username: " + uniqueUsername);
        return uniqueUsername;
    }
}