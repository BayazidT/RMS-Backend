// src/main/java/com/tr/rms/rbac/entity/UserRole.java
package com.tr.rms.rbac.entity;

import com.tr.rms.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_roles", schema = "eis_cafe")
@IdClass(UserRoleId.class)
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class UserRole {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Id
    @Column(name = "role_id")
    private UUID roleId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt = LocalDateTime.now();
}