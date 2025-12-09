// src/main/java/com/tr/rms/modules/auth/dto/RolePermissionResponse.java
package com.tr.rms.security.auth.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RolePermissionResponse(
        UUID roleId,
        String roleName,
        UUID permissionId,
        String permissionName,
        LocalDateTime grantedAt
) {}