package com.software.tfs.airopsV1.auth.exception;

import java.util.UUID;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(UUID companyId) {
        super(String.format("Company %s not found", companyId));
    }
}
