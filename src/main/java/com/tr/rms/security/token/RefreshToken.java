// src/main/java/com/tr/rms/security/token/RefreshToken.java
package com.tr.rms.security.token;

import com.tr.rms.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens", schema = "eis_cafe")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // ← ADD THIS LINE
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiresAt;

    private Instant revokedAt;

    @Column(length = 45)
    private String createdFromIp;

//    private String createdFromUserAgent;

    public boolean isRevoked()   { return revokedAt != null; }
    public boolean isExpired()   { return Instant.now().isAfter(expiresAt); }
}