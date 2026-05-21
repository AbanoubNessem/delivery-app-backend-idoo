package com.deliveryman.deliverymanapp.auth.service.impl;

import com.deliveryman.deliverymanapp.auth.dto.*;
import com.deliveryman.deliverymanapp.auth.entity.LoginUser;
import com.deliveryman.deliverymanapp.auth.entity.RefreshToken;
import com.deliveryman.deliverymanapp.auth.exception.InvalidCredentialsException;
import com.deliveryman.deliverymanapp.auth.exception.TokenException;
import com.deliveryman.deliverymanapp.auth.exception.UserDisabledException;
import com.deliveryman.deliverymanapp.auth.mapper.UserMapper;
import com.deliveryman.deliverymanapp.auth.repository.LoginUserRepository;
import com.deliveryman.deliverymanapp.auth.security.CustomUserDetails;
import com.deliveryman.deliverymanapp.auth.security.JwtService;
import com.deliveryman.deliverymanapp.auth.service.AuthService;
import com.deliveryman.deliverymanapp.auth.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final LoginUserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            TokenService tokenService,
            LoginUserRepository userRepository,
            UserDetailsService userDetailsService,
            UserMapper userMapper
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLoginName(),
                            request.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            LoginUser user = userDetails.getLoginUser();

            String accessToken = jwtService.generateToken(userDetails);
            RefreshToken refreshToken = tokenService.createRefreshToken(user);
            UserResponse userResponse = userMapper.toResponse(user);

            Set<String> roles = new HashSet<>();
            userDetails.getAuthorities().forEach(authority -> roles.add(authority.getAuthority()));

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .user(userResponse)
                    .roles(roles)
                    .expiresIn(jwtService.getExpirationTime())
                    .build();

        } catch (DisabledException | LockedException e) {
            throw new UserDisabledException("Your account is disabled, inactive, or outside the valid date range.");
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid login name or password.");
        }
    }

    @Override
    @Transactional
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return tokenService.findByToken(requestRefreshToken)
                .map(tokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    CustomUserDetails userDetails = new CustomUserDetails(user);
                    String accessToken = jwtService.generateToken(userDetails);
                    
                    // Secure Refresh Token Rotation: create a new one, old one is revoked
                    RefreshToken newRefreshToken = tokenService.createRefreshToken(user);
                    
                    return TokenResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(newRefreshToken.getToken())
                            .expiresIn(jwtService.getExpirationTime())
                            .build();
                })
                .orElseThrow(() -> new TokenException(requestRefreshToken, "Refresh token is not in the database"));
    }

    @Override
    @Transactional
    public void logout(String tokenHeader) {
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String jwt = tokenHeader.substring(7);
            try {
                String username = jwtService.extractUsername(jwt);
                if (username != null) {
                    LoginUser user = userRepository.findByLoginName(username).orElse(null);
                    if (user != null) {
                        tokenService.revokeAllUserTokens(user);
                    }
                }
            } catch (Exception e) {
                // Ignore parsing errors on logout, proceed with clearing authentication context
            }
        }
        SecurityContextHolder.clearContext();
    }

    @Override
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || 
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new InvalidCredentialsException("User is not authenticated");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userMapper.toResponse(userDetails.getLoginUser());
    }

    @Override
    public boolean validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            if (username == null) return false;
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtService.isTokenValid(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }
}
