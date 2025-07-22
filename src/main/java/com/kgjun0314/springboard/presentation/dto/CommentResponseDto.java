package com.kgjun0314.springboard.presentation.dto;
import com.kgjun0314.springboard.domain.entity.Comment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentResponseDto {
    private UUID id;
    private UUID postId;
    private String content;
    private String username;
    private int likesCount;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.postId = comment.getPost().getId();
        this.content = comment.getContent();
        this.username = comment.getSiteUser().getUsername();
        this.likesCount = comment.getLikes().size();
        this.createdDate = comment.getCreatedDate();
        this.lastModifiedDate = comment.getLastModifiedDate();
    }
}
