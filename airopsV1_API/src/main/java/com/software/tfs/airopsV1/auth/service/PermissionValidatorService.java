package com.software.tfs.airopsV1.auth.service;

import com.software.tfs.airopsV1.auth.model.CompanyMember;
import com.software.tfs.airopsV1.auth.model.Permission;
import com.software.tfs.airopsV1.auth.model.RolePermission;
import com.software.tfs.airopsV1.auth.model.UserRole;
import com.software.tfs.airopsV1.auth.repo.PermissionRepository;
import com.software.tfs.airopsV1.auth.repo.RolePermissionRepository;
import com.software.tfs.airopsV1.auth.repo.UserRoleRepository;
import com.software.tfs.airopsV1.auth.util.PermissionKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Used to validate that users can perform what they're after
 */
@Service
public class PermissionValidatorService {

    private UserRoleRepository userRoleRepository;
    private RolePermissionRepository rolePermissionRepository;
    private PermissionRepository permissionRepository;

    @Autowired
    public PermissionValidatorService(UserRoleRepository userRoleRepository, RolePermissionRepository rolePermissionRepository, PermissionRepository permissionRepository) {
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean doesUserHavePerm(CompanyMember member, PermissionKeys permissionKey) {
        List<UserRole> userRoles = this.userRoleRepository
                .findByMemberId(member.getId())
                .stream()
                .toList();

        Permission permission = this.permissionRepository.findByKey(permissionKey.name());

        for(final UserRole role : userRoles) {
            List<Permission> permissions = this.rolePermissionRepository.findAllByRole(role.getRole())
                    .stream()
                    .map(RolePermission::getPermission)
                    .toList();

            if (permissions.contains(permission)) {
                return true;
            }
        }

        return false;
    }
}
