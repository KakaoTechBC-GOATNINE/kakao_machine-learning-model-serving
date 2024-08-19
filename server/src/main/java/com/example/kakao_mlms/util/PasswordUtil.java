package com.example.kakao_mlms.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordUtil {
    public static String generateRandomPassword() {
        int passwordLength = 8; // 비밀번호 길이 지정
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+"; // 사용할 문자열

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(passwordLength);

        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }
}
