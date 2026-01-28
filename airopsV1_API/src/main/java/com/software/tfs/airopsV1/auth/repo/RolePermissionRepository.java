package com.software.tfs.airopsV1.auth.repo;

import com.software.tfs.airopsV1.auth.model.Role;
import com.software.tfs.airopsV1.auth.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {
    List<RolePermission> findAllByRole(Role role);
}
