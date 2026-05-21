package com.deliveryman.deliverymanapp.auth.security;

import org.springframework.stereotype.Component;

@Component
public class PlainTextPasswordVerifier implements LegacyPasswordVerifier {
    @Override
    public boolean verify(String rawPassword, String encodedPassword) {
        return rawPassword != null && rawPassword.equals(encodedPassword);
    }

    @Override
    public String getAlgorithmName() {
        return "PLAINTEXT";
    }
}
