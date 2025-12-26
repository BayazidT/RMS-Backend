package com.tr.rms.rbac.controller;

import com.tr.rms.rbac.dto.RoleResponse;
import com.tr.rms.rbac.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/private/")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("roles")
    public List<RoleResponse> getRole() {
        return roleService.getRoles();
    }

}
