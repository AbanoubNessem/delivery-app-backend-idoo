package com.deliveryman.deliverymanapp.auth.service;

import com.deliveryman.deliverymanapp.auth.dto.*;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    TokenResponse refreshToken(RefreshTokenRequest request);
    void logout(String tokenHeader);
    UserResponse getCurrentUser();
    boolean validateToken(String token);
}
