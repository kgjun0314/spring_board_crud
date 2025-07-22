package com.kgjun0314.springboard.presentation.controller;

import com.kgjun0314.springboard.domain.entity.Post;
import com.kgjun0314.springboard.presentation.dto.CommentRequestDto;
import com.kgjun0314.springboard.presentation.dto.PostRequestDto;
import com.kgjun0314.springboard.presentation.dto.PostResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {
    private final PostApiController postApiController;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        ResponseEntity<Page<PostResponseDto>> response = postApiController.list(page, keyword);
        Page<PostResponseDto> paging = response.getBody();
        model.addAttribute("paging", paging);
        model.addAttribute("keyword", keyword);
        return "post_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") UUID id, CommentRequestDto commentRequestDto) {
        ResponseEntity<PostResponseDto> response = postApiController.detail(id);
        PostResponseDto postDto = response.getBody();
        model.addAttribute("postDto", postDto);
        return "post_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(PostRequestDto postRequestDto) {
        return "post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid PostRequestDto postRequestDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "post_form";
        }
        postApiController.create(postRequestDto, principal);
        return "redirect:/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(PostRequestDto postRequestDto, @PathVariable("id") UUID id, Principal principal) {
        ResponseEntity<PostResponseDto> response = postApiController.detail(id);
        PostResponseDto postResponseDto = response.getBody();

        if(postResponseDto == null || !postResponseDto.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        postRequestDto.setTitle(postResponseDto.getTitle());
        postRequestDto.setContent(postResponseDto.getContent());
        return "post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid PostRequestDto postRequestDto, BindingResult bindingResult, @PathVariable("id") UUID id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "post_form";
        }
        ResponseEntity<PostResponseDto> response = postApiController.detail(id);
        PostResponseDto postResponseDto = response.getBody();
        if(postResponseDto == null || !postResponseDto.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        postApiController.modify(postResponseDto.getId(), postRequestDto, principal);
        return String.format("redirect:/post/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") UUID id, Principal principal) {
        ResponseEntity<PostResponseDto> response = postApiController.detail(id);
        PostResponseDto postResponseDto = response.getBody();
        if(postResponseDto == null || !postResponseDto.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        postApiController.delete(id, principal);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like/{id}")
    public String like(@PathVariable("id") UUID id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            postApiController.like(id, principal);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("likeError", "이미 추천하셨습니다.");
        }
        return String.format("redirect:/post/detail/%s", id);
    }
}
