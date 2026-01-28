package com.software.tfs.airopsV1.auth.dto.response;

import java.util.UUID;

public class RoleResponse {

    private UUID roleId;
    private String roleName;

    public RoleResponse(UUID roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
