package com.tr.rms.modules.user.dto;

import java.util.UUID;

public record UserRequest(
        String name,
        String username,
        String email,
        String phone,
        String password,
        UUID roleId
) {}
