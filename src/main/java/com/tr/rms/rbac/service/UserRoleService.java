package com.tr.rms.rbac.service;

import com.tr.rms.modules.user.entity.User;
import com.tr.rms.rbac.entity.Role;
import com.tr.rms.rbac.entity.UserRole;
import com.tr.rms.rbac.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public void setUserRole(UUID userId, UUID roleId) {
        User user = new User();
        user.setId(userId);
        Role role = new Role();
        role.setId(roleId);
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRoleRepository.save(userRole);
    }
}
