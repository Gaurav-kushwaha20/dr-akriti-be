package com.akriti.akriti.modules.user.repo;

import com.akriti.akriti.modules.user.entity.UserEntity;
import com.akriti.akriti.modules.user.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUsername(String username);
    boolean existsByRole(UserRole role);
}
