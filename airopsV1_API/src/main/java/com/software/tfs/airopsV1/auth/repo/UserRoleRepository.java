package com.software.teamfive.jcc_product_inventory_management.repo.join;

import com.software.teamfive.jcc_product_inventory_management.model.join.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    Optional<UserRole> findByMemberId(UUID id);
}
