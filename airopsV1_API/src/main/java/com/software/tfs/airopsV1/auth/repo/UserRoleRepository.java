package com.software.tfs.airopsV1.auth.repo;

import com.software.tfs.airopsV1.auth.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    Optional<UserRole> findByMemberId(UUID id);
}
