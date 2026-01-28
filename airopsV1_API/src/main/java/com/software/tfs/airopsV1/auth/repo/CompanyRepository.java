package com.software.tfs.airopsV1.auth.repo;

import com.software.tfs.airopsV1.auth.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
}
