package com.tr.rms.rbac.repository;

import com.tr.rms.rbac.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
}
