package com.kgjun0314.springboard.infrastructure;

import com.kgjun0314.springboard.domain.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaCommentRepository extends JpaRepository<Comment, UUID> {
    @EntityGraph(attributePaths = {
            "siteUser",
            "likes"
    })
    @Query("select c " +
            "from Comment c " +
            "where c.id = :id")
    Optional<Comment> findByIdWithAllRelations(@Param("id") UUID id);
}
