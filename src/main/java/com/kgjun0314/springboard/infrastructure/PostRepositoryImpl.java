package com.kgjun0314.springboard.infrastructure;

import com.kgjun0314.springboard.domain.entity.Post;
import com.kgjun0314.springboard.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepository {
    private final JpaPostRepository postRepository;

    @Override
    public Optional<Post> findById(UUID id) {
        return postRepository.findByIdWithAllRelations(id);
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Override
    public Page<Post> findAllByKeyword(String keyword, Pageable pageable) {
        return postRepository.findAllByKeyword(keyword, pageable);
    }
}
