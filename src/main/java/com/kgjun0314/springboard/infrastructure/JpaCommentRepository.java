package com.kgjun0314.springboard.infrastructure;

import com.kgjun0314.springboard.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaCommentRepository extends JpaRepository<Comment, UUID> {
    @Query("SELECT c " +
            "FROM Comment c " +
            "JOIN FETCH c.post p " +
            "JOIN FETCH c.siteUser su " +
            "WHERE c.id = :id")
    Optional<Comment> findByIdWithFetchJoin(@Param("id") UUID id);
}
