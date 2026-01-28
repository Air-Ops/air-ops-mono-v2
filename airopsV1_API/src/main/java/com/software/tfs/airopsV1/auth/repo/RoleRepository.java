package com.software.tfs.airopsV1.auth.repo;

import com.software.tfs.airopsV1.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
