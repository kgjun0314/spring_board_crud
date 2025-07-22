package com.kgjun0314.springboard.infrastructure;

import com.kgjun0314.springboard.domain.entity.LikePost;
import com.kgjun0314.springboard.domain.repository.LikePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LikePostRepositoryImpl implements LikePostRepository {

    private final JpaLikePostRepository likePostRepository;

    @Override
    public void save(LikePost likePost) {
        likePostRepository.save(likePost);
    }
}
