package com.software.tfs.airopsV1.auth.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userid) {
        super(String.format("User with id %s not found", userid));
    }
}
