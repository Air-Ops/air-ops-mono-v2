package com.software.tfs.airopsV1.auth.exception;

import com.software.tfs.airopsV1.auth.util.PermissionKeys;

import java.util.UUID;

public class InsufficientPermissionsException extends RuntimeException {
    public InsufficientPermissionsException(UUID userId, PermissionKeys permissionKey) {
        super(String.format("User does not have the required permission for operation(s):%nUser: %s%nPermission: %s", userId, permissionKey));
    }
}
