package com.kgjun0314.springboard.infrastructure;

import com.kgjun0314.springboard.domain.entity.SiteUser;
import com.kgjun0314.springboard.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository userRepository;

    @Override
    public void save(SiteUser user) {
        userRepository.save(user);
    }

    @Override
    public Optional<SiteUser> findByUsername(String username) {
//        System.out.println("UserRepositoryImpl.findByUsername");
        return userRepository.findByUsername(username);
    }
}
