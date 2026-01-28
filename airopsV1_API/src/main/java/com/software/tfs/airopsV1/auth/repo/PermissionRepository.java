package com.software.tfs.airopsV1.auth.repo;

import com.software.tfs.airopsV1.auth.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Permission findByKey(String permissionKey);
}
