package com.kgjun0314.springboard.infrastructure;

import com.kgjun0314.springboard.domain.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<SiteUser, UUID> {
    Optional<SiteUser> findByUsername(String username);
}
