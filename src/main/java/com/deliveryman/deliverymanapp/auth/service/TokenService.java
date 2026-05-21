package com.deliveryman.deliverymanapp.auth.service;

import com.deliveryman.deliverymanapp.auth.entity.LoginUser;
import com.deliveryman.deliverymanapp.auth.entity.RefreshToken;
import java.util.Optional;

public interface TokenService {
    RefreshToken createRefreshToken(LoginUser user);
    RefreshToken verifyExpiration(RefreshToken token);
    void revokeAllUserTokens(LoginUser user);
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
}
