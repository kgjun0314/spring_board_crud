package com.kgjun0314.springboard.infrastructure;

import com.kgjun0314.springboard.domain.entity.LikeComment;
import com.kgjun0314.springboard.domain.repository.LikeCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LikeCommentRepositoryImpl implements LikeCommentRepository {

    private final JpaLikeCommentRepository likeCommentRepository;

    @Override
    public void save(LikeComment likeComment) {
        likeCommentRepository.save(likeComment);
    }
}
