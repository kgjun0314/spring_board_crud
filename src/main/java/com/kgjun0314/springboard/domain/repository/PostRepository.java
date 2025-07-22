package com.kgjun0314.springboard.domain.repository;

import com.kgjun0314.springboard.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository {
    Optional<Post> findById(UUID id);
    void save(Post post);
    void delete(Post post);
    Page<Post> findAllByKeyword(String keyword, Pageable pageable);
}
