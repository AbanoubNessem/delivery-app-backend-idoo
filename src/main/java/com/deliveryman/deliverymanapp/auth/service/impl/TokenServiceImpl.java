package com.deliveryman.deliverymanapp.auth.service.impl;

import com.deliveryman.deliverymanapp.auth.entity.LoginUser;
import com.deliveryman.deliverymanapp.auth.entity.RefreshToken;
import com.deliveryman.deliverymanapp.auth.exception.TokenException;
import com.deliveryman.deliverymanapp.auth.repository.RefreshTokenRepository;
import com.deliveryman.deliverymanapp.auth.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public RefreshToken createRefreshToken(LoginUser user) {
        // Revoke all previous tokens for the user to implement single concurrent session or revoke-on-login policy
        refreshTokenRepository.revokeAllUserTokens(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isRevoked()) {
            throw new TokenException(token.getToken(), "Refresh token has been revoked");
        }
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenException(token.getToken(), "Refresh token has expired");
        }
        return token;
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(LoginUser user) {
        refreshTokenRepository.revokeAllUserTokens(user);
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
