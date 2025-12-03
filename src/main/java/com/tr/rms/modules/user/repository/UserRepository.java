package com.tr.rms.modules.user.repository;

import com.tr.rms.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
//    <T> ScopedValue<T> findByUsernameAndDeletedFalse(String username);
}
