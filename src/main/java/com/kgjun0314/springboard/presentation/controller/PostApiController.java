package com.kgjun0314.springboard.presentation.controller;

import com.kgjun0314.springboard.application.PostService;
import com.kgjun0314.springboard.application.UserService;
import com.kgjun0314.springboard.domain.entity.Post;
import com.kgjun0314.springboard.domain.entity.SiteUser;
import com.kgjun0314.springboard.presentation.dto.PostRequestDto;
import com.kgjun0314.springboard.presentation.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostApiController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<Page<PostResponseDto>> list(int page, String keyword) {
//        System.out.println("/api/post/list called.");
        return ResponseEntity.ok(postService.getList(page, keyword));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PostResponseDto> detail(@PathVariable("id") UUID id) {
//        System.out.println("/api/post/detail called.");
        return ResponseEntity.ok(postService.getPostDto(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<PostResponseDto> create(@RequestBody PostRequestDto postRequestDto, Principal principal) {
//        System.out.println("/api/post/create/ called.");
        SiteUser siteUser = userService.getUser(principal.getName());
        PostResponseDto response = postService.create(postRequestDto.getTitle(), postRequestDto.getContent(), siteUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/modify/{id}")
    public ResponseEntity<PostResponseDto> modify(@PathVariable("id") UUID id, @RequestBody PostRequestDto postRequestDto, Principal principal) {
//        System.out.println("/api/post/modify/" + id + " called.");
        Post post = postService.getPostEntity(id);
        if(!post.getSiteUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        PostResponseDto response = postService.modify(post, postRequestDto.getTitle(), postRequestDto.getContent());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id, Principal principal) {
//        System.out.println("/api/post/delete/" + id + " called.");
        Post post = postService.getPostEntity(id);
        if(!post.getSiteUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        postService.delete(post);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like/{id}")
    public ResponseEntity<PostResponseDto> like(@PathVariable("id") UUID id, Principal principal) {
//        System.out.println("/api/post/like/" + id + " called.");
        Post post = postService.getPostEntity(id);
        SiteUser siteUser = userService.getUser(principal.getName());
        return ResponseEntity.ok(postService.like(post, siteUser));
    }
}