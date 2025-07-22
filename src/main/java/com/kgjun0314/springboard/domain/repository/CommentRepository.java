package com.kgjun0314.springboard.domain.repository;

import com.kgjun0314.springboard.domain.entity.Comment;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository {
    Optional<Comment> findById(UUID id);
    void save(Comment comment);
    void delete(Comment comment);
}
