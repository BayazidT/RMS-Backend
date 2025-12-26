package com.tr.rms.rbac.service;

import com.tr.rms.rbac.dto.RoleResponse;
import com.tr.rms.rbac.entity.Role;
import com.tr.rms.rbac.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleResponse> getRoles() {
        List<Role> roles = roleRepository.findAll();

        List<RoleResponse> roleResponses = new ArrayList<>();
        for (Role role : roles) {
            RoleResponse roleResponse = new RoleResponse(role.getId(), role.getName());
            roleResponses.add(roleResponse);
        }
        return roleResponses;
    }
}
