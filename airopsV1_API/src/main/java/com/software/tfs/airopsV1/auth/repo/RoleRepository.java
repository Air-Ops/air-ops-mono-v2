package com.software.teamfive.jcc_product_inventory_management.repo.biz;

import com.software.teamfive.jcc_product_inventory_management.model.biz.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
