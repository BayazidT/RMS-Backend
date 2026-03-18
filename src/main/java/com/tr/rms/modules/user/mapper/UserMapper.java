package com.tr.rms.modules.user.mapper;

import com.tr.rms.modules.user.dto.UserRequest;
import com.tr.rms.modules.user.dto.UserResponse;
import com.tr.rms.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    public User toEntity(UserRequest request) {
        return User.builder()
                .name(request.name())
                .username(request.username())
                .email(request.email())
                .phone(request.phone())
                .active(true)
                .password(passwordEncoder.encode(request.password()!=null?request.password():"123456"))
                .build();
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }

    public void updateEntity(User user, UserRequest request) {
        user.setName(request.name());
        user.setEmail(request.email());
    }
}
