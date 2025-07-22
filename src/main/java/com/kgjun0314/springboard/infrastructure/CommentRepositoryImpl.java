package com.kgjun0314.springboard.infrastructure;

import com.kgjun0314.springboard.domain.entity.Comment;
import com.kgjun0314.springboard.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final JpaCommentRepository jpaCommentRepository;

    @Override
    public Optional<Comment> findById(UUID id) {
        return jpaCommentRepository.findByIdWithAllRelations(id);
    }

    @Override
    public void save(Comment comment) {
        jpaCommentRepository.save(comment);
    }

    @Override
    public void delete(Comment comment) {
        jpaCommentRepository.delete(comment);
    }
}
