package com.kgjun0314.springboard.domain.repository;

import com.kgjun0314.springboard.domain.entity.SiteUser;

import java.util.Optional;

public interface UserRepository {
    void save(SiteUser user);
    Optional<SiteUser> findByUsername(String username);
}
