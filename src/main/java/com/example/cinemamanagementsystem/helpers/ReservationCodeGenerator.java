package com.example.cinemamanagementsystem.helpers;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class ReservationCodeGenerator {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();
    private static final Set<String> usedCodes = new HashSet<>();

    public static String generateCode(){
        String code;
        do{
            code = generateRandomCode();
        } while(usedCodes.contains(code));
        usedCodes.add(code);
        return code;
    }

    private static String generateRandomCode(){
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for(int i = 0; i < CODE_LENGTH; i++){
            int randomIndex = random.nextInt(CHARS.length());
            code.append(CHARS.charAt(randomIndex));
        }
        return code.toString();
    }
}
