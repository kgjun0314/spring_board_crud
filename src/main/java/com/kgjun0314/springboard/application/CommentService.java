package com.kgjun0314.springboard.application;

import com.kgjun0314.springboard.domain.entity.*;
import com.kgjun0314.springboard.domain.repository.CommentRepository;
import com.kgjun0314.springboard.domain.repository.LikeCommentRepository;
import com.kgjun0314.springboard.exception.DataNotFoundException;
import com.kgjun0314.springboard.presentation.dto.CommentResponseDto;
import com.kgjun0314.springboard.presentation.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;

    public CommentResponseDto getCommentDto(UUID id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            return new CommentResponseDto(comment.get());
        }
        else {
            throw new DataNotFoundException("comment not found");
        }
    }

    public Comment getCommentEntity(UUID id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        }
        else {
            throw new DataNotFoundException("comment not found");
        }
    }

    public CommentResponseDto create(Post post, String content, SiteUser siteUser) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setSiteUser(siteUser);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public CommentResponseDto modify(Comment comment, String content) {
        comment.setContent(content);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public CommentResponseDto like(Comment comment, SiteUser siteUser) {
        LikeComment likeComment = new LikeComment();
        likeComment.setComment(comment);
        likeComment.setSiteUser(siteUser);
        likeCommentRepository.save(likeComment);
        return new CommentResponseDto(comment);
    }
}
