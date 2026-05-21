package com.deliveryman.deliverymanapp.auth.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ErpPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final List<LegacyPasswordVerifier> legacyVerifiers;

    public ErpPasswordEncoder(List<LegacyPasswordVerifier> legacyVerifiers) {
        this.legacyVerifiers = legacyVerifiers;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        // Encode using BCrypt for any new or updated passwords
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            return false;
        }

        // 1. Try standard BCrypt verification if it looks like a BCrypt hash
        try {
            if (encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$") || encodedPassword.startsWith("$2y$")) {
                if (bCryptPasswordEncoder.matches(rawPassword, encodedPassword)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // Ignore exception and fall through to legacy check
        }

        // 2. Iterate through custom legacy verifiers (e.g. plain text, MD5, custom hash)
        for (LegacyPasswordVerifier verifier : legacyVerifiers) {
            try {
                if (verifier.verify(rawPassword.toString(), encodedPassword)) {
                    return true;
                }
            } catch (Exception e) {
                // Log and continue to check other verifiers
            }
        }

        return false;
    }
}
