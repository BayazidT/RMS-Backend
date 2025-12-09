// src/main/java/com/tr/rms/security/token/RefreshTokenService.java
package com.tr.rms.security.token;

import com.tr.rms.modules.user.entity.User;
import com.tr.rms.security.jwt.JwtProperties;
import com.tr.rms.rbac.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        var token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .revokedAt(Instant.now().plusMillis(jwtProperties.refreshExpiration()))
                .expiresAt(Instant.now().plusSeconds(10))
                .build();

        return refreshTokenRepository.save(token);
    }

    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(t -> {
                    t.setRevokedAt(Instant.now());
                    refreshTokenRepository.save(t);
                });
    }

    public RefreshToken verifyAndGet(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }
        return refreshToken;
    }
}