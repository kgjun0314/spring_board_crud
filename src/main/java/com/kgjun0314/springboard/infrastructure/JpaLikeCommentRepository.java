package com.kgjun0314.springboard.infrastructure;

import com.kgjun0314.springboard.domain.entity.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaLikeCommentRepository extends JpaRepository<LikeComment, UUID> {
}
