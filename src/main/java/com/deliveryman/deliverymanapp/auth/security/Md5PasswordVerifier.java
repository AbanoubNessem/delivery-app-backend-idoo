package com.deliveryman.deliverymanapp.auth.security;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Md5PasswordVerifier implements LegacyPasswordVerifier {
    @Override
    public boolean verify(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(rawPassword.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            String hash = sb.toString();
            return hash.equalsIgnoreCase(encodedPassword);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    @Override
    public String getAlgorithmName() {
        return "MD5";
    }
}
