package com.akriti.akriti.modules.user.repo;

import com.akriti.akriti.modules.user.entity.UserEntity;
import com.akriti.akriti.modules.user.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByUsername(String username);

    boolean existsByRole(UserRole role);

    // Get user by username
    UserEntity findByUsername(String username);

    // Fetch all users with pagination
    Page<UserEntity> findAll(Pageable pageable);

    // Fetch users by role with pagination
    Page<UserEntity> findByRole(UserRole role, Pageable pageable);

    // Fetch users by type with pagination
    Page<UserEntity> findByUserType(String userType, Pageable pageable);
}
