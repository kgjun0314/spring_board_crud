package com.kgjun0314.springboard.presentation.controller;

import com.kgjun0314.springboard.application.PostService;
import com.kgjun0314.springboard.presentation.dto.CommentRequestDto;
import com.kgjun0314.springboard.presentation.dto.CommentResponseDto;
import com.kgjun0314.springboard.presentation.dto.PostResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {
    private final PostService postService;
    private final CommentApiController commentApiController;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String create(Model model, @PathVariable UUID id, @Valid CommentRequestDto commentRequestDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            PostResponseDto postDto = postService.getPostDto(id);
            model.addAttribute("postDto", postDto);
            return "post_detail";
        }
        ResponseEntity<CommentResponseDto> response = commentApiController.create(id, commentRequestDto, principal);
        CommentResponseDto commentResponseDto = response.getBody();
        if (commentResponseDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return String.format("redirect:/post/detail/%s#commentDto_%s", commentResponseDto.getPostId(), commentResponseDto.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(CommentRequestDto commentRequestDto, @PathVariable("id") UUID id, Principal principal) {
        ResponseEntity<CommentResponseDto> response = commentApiController.detail(id);
        CommentResponseDto commentResponseDto = response.getBody();
        if(commentResponseDto == null || !commentResponseDto.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentRequestDto.setContent(commentResponseDto.getContent());
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid CommentRequestDto commentRequestDto, BindingResult bindingResult, @PathVariable("id") UUID id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }
        ResponseEntity<CommentResponseDto> response = commentApiController.detail(id);
        CommentResponseDto commentResponseDto = response.getBody();
        if(commentResponseDto == null || !commentResponseDto.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentApiController.modify(commentResponseDto.getId(), commentRequestDto, principal);
        return String.format("redirect:/post/detail/%s#commentDto_%s", commentResponseDto.getPostId(), commentResponseDto.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") UUID id, Principal principal) {
        ResponseEntity<CommentResponseDto> response = commentApiController.detail(id);
        CommentResponseDto commentResponseDto = response.getBody();
        if(commentResponseDto == null || !commentResponseDto.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentApiController.delete(id, principal);
        return String.format("redirect:/post/detail/%s", commentResponseDto.getPostId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like/{id}")
    public String like(@PathVariable("id") UUID id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            ResponseEntity<CommentResponseDto> response = commentApiController.like(id, principal);
            CommentResponseDto commentResponseDto = response.getBody();
            if(commentResponseDto == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            return String.format("redirect:/post/detail/%s#commentDto_%s", commentResponseDto.getPostId(), commentResponseDto.getId());
        } catch (DataIntegrityViolationException e) {
            ResponseEntity<CommentResponseDto> response = commentApiController.detail(id);
            CommentResponseDto commentResponseDto = response.getBody();
            redirectAttributes.addFlashAttribute("likeError", "이미 추천하셨습니다.");
            if(commentResponseDto == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            return String.format("redirect:/post/detail/%s#commentDto_%s", commentResponseDto.getPostId(), commentResponseDto.getId());
        }
    }
}
