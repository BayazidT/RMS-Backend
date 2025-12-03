// src/main/java/com/tr/rms/modules/auth/dto/RoleCreateRequest.java
package com.tr.rms.security.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleCreateRequest(
        @NotBlank @Size(max = 50) String name,
        @Size(max = 255) String description
) {}