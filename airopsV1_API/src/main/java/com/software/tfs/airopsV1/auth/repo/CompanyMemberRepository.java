package com.software.tfs.airopsV1.auth.repo;

import com.software.tfs.airopsV1.auth.model.CompanyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyMemberRepository extends JpaRepository<CompanyMember, UUID> {

    Optional<CompanyMember> findByUserId(UUID id);

    Optional<CompanyMember> findByUserIdAndCompanyId(UUID id, UUID id1);
}
