package com.deliveryman.deliverymanapp.auth.security;

public interface LegacyPasswordVerifier {
    boolean verify(String rawPassword, String encodedPassword);
    String getAlgorithmName();
}
