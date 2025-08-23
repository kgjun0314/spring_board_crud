package com.kgjun0314.springboard.infrastructure;

import com.kgjun0314.springboard.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaPostRepository extends JpaRepository<Post, UUID> {
    @Query("select distinct p " +
            "from Post p " +
            "left join fetch p.siteUser u1 " +
            "left join p.commentList c " +
            "left join c.siteUser u2 " +
            "where p.title like %:keyword% " +
            "or p.content like %:keyword% " +
            "or u1.username like %:keyword% " +
            "or c.content like %:keyword% " +
            "or u2.username like %:keyword% ")
    Page<Post> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "JOIN FETCH p.siteUser su " +
            "WHERE p.id = :id")
    Optional<Post> findByIdWithFetchJoin(@Param("id") UUID id);
}
