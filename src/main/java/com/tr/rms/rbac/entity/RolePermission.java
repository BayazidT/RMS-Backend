// RolePermission.java
package com.tr.rms.rbac.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "role_permissions", schema = "eis_cafe")
@IdClass(RolePermissionId.class)
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RolePermission {

    @Id
    @Column(name = "role_id")
    private UUID roleId;

    @Id
    @Column(name = "permission_id")
    private UUID permissionId;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    private Permission permission;

//    @CreationTimestamp
//    @Column(name = "granted_at")
//    private LocalDateTime grantedAt;
}