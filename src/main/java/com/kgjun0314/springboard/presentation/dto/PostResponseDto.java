package com.kgjun0314.springboard.presentation.dto;

import com.kgjun0314.springboard.domain.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class PostResponseDto {
    private UUID id;
    private String title;
    private String content;
    private String username;
    private List<CommentResponseDto> commentDtoList;
    private int likesCount;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public PostResponseDto(Post post) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        username = post.getSiteUser().getUsername();
        commentDtoList = post.getCommentList()
                .stream()
                .distinct()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        likesCount = post.getLikes().size();
        createdDate = post.getCreatedDate();
        lastModifiedDate = post.getLastModifiedDate();
    }
}
