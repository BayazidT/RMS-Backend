package com.tr.rms.modules.user.controller;

import com.tr.rms.modules.user.dto.UserListResponse;
import com.tr.rms.modules.user.dto.UserRequest;
import com.tr.rms.modules.user.dto.UserResponse;
import com.tr.rms.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/private/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse create(@RequestBody UserRequest request) {
        return userService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public UserListResponse getAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return userService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable UUID id, @RequestBody UserRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }
}
