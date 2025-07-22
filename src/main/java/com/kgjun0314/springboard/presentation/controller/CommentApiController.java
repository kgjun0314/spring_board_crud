package com.kgjun0314.springboard.presentation.controller;

import com.kgjun0314.springboard.application.CommentService;
import com.kgjun0314.springboard.application.PostService;
import com.kgjun0314.springboard.application.UserService;
import com.kgjun0314.springboard.domain.entity.Comment;
import com.kgjun0314.springboard.domain.entity.Post;
import com.kgjun0314.springboard.domain.entity.SiteUser;
import com.kgjun0314.springboard.presentation.dto.CommentRequestDto;
import com.kgjun0314.springboard.presentation.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentApiController {
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/detail/{id}")
    public ResponseEntity<CommentResponseDto> detail(@PathVariable("id") UUID id) {
        System.out.println("api/comment/detail/" + id + " called.");
        return ResponseEntity.ok(commentService.getCommentDto(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public ResponseEntity<CommentResponseDto> create(@PathVariable UUID id, @RequestBody CommentRequestDto commentRequestDto, Principal principal) {
        System.out.println("api/comment/create/" + id + " called.");
        Post post = postService.getPostEntity(id);
        SiteUser siteUser = userService.getUser(principal.getName());
        CommentResponseDto response = commentService.create(post, commentRequestDto.getContent(), siteUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/modify/{id}")
    public ResponseEntity<CommentResponseDto> modify(@PathVariable("id") UUID id, @RequestBody CommentRequestDto commentRequestDto, Principal principal) {
        System.out.println("api/comment/modify/" + id + " called.");
        Comment comment = commentService.getCommentEntity(id);
        if(!comment.getSiteUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        return ResponseEntity.ok(commentService.modify(comment, commentRequestDto.getContent()));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id, Principal principal) {
        System.out.println("api/comment/delete/" + id + " called.");
        Comment comment = commentService.getCommentEntity(id);
        if(!comment.getSiteUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        commentService.delete(comment);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like/{id}")
    public ResponseEntity<CommentResponseDto> like(@PathVariable("id") UUID id, Principal principal) {
        System.out.println("api/comment/like/" + id + " called.");
        Comment comment = commentService.getCommentEntity(id);
        SiteUser siteUser = userService.getUser(principal.getName());
        return ResponseEntity.ok(commentService.like(comment, siteUser));
    }
}
