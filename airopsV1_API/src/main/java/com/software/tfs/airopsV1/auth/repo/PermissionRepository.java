package com.software.teamfive.jcc_product_inventory_management.repo.biz;

import com.software.teamfive.jcc_product_inventory_management.model.biz.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Permission findByKey(String permissionKey);
}
