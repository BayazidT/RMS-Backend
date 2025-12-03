// src/main/java/com/tr/rms/security/auth/AuthController.java
package com.tr.rms.security.auth;

import com.tr.rms.security.auth.dto.LoginRequest;
import com.tr.rms.security.auth.dto.LoginResponse;
import com.tr.rms.security.auth.dto.RefreshRequest;
import com.tr.rms.security.auth.dto.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> me(Authentication auth) {
        return ResponseEntity.ok(authenticationService.getProfile(auth));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authenticationService.refresh(request));
    }
}
