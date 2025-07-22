package com.kgjun0314.springboard.infrastructure;

import com.kgjun0314.springboard.domain.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaLikePostRepository extends JpaRepository<LikePost, UUID> {
}
