package com.tr.rms.security.auth;

import com.tr.rms.security.auth.dto.*;
import com.tr.rms.security.auth.dto.RefreshRequest;
import com.tr.rms.security.jwt.JwtService;
import com.tr.rms.security.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.tr.rms.modules.user.entity.User;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final com.tr.rms.modules.user.repository.UserRepository userRepository;
    private final com.tr.rms.rbac.repository.RoleRepository roleRepository;
    private final com.tr.rms.rbac.repository.PermissionRepository permissionRepository;
    private final com.tr.rms.security.jwt.JwtProperties jwtProperties;

    public LoginResponse login(LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String accessToken = jwtService.generateToken(userDetails);
        var dbUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var refreshToken = refreshTokenService.createRefreshToken(dbUser);
        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                jwtProperties.accessExpiration() / 1000
        );
    }

    public LoginResponse refresh(RefreshRequest request) {
        var rt = refreshTokenService.verifyAndGet(request.refreshToken());
        refreshTokenService.revokeToken(request.refreshToken());
        Set<GrantedAuthority> authorities = roleRepository.findActiveByUserId(rt.getUser().getId())
                .stream()
                .flatMap(role -> permissionRepository.findByRoleId(role.getId()).stream())
                .map(perm -> new SimpleGrantedAuthority(perm.getName()))
                .collect(Collectors.toSet());

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(rt.getUser().getUsername())
                .password(rt.getUser().getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!rt.getUser().isActive() || rt.getUser().isDeleted())
                .build();

        String newAccess = jwtService.generateToken(userDetails);
        var newRefresh = refreshTokenService.createRefreshToken(rt.getUser());

        return new LoginResponse(newAccess, newRefresh.getToken(),
                jwtProperties.accessExpiration() / 1000);
    }

    public UserProfileResponse getProfile(Authentication auth) {

        User dbUser = userRepository
                .findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<String> roleNames = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring(5))
                .collect(Collectors.toSet());

        Set<String> permissionNames = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> !a.startsWith("ROLE_"))
                .collect(Collectors.toSet());

        return new UserProfileResponse(
                dbUser.getId(),
                dbUser.getUsername(),
                dbUser.getEmail(),
                dbUser.getName(),
                roleNames,
                permissionNames
        );
    }
}