package com.kgjun0314.springboard.application;

import com.kgjun0314.springboard.domain.entity.LikePost;
import com.kgjun0314.springboard.domain.entity.SiteUser;
import com.kgjun0314.springboard.domain.repository.LikePostRepository;
import com.kgjun0314.springboard.domain.repository.UserRepository;
import com.kgjun0314.springboard.exception.DataNotFoundException;
import com.kgjun0314.springboard.domain.entity.Post;
import com.kgjun0314.springboard.domain.repository.PostRepository;
import com.kgjun0314.springboard.presentation.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;

    public Page<PostResponseDto> getList(int page, String keyword) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<Post> paging = postRepository.findAllByKeyword(keyword, pageable);
        return paging.map(PostResponseDto::new);
    }

    public PostResponseDto getPostDto(UUID id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return new PostResponseDto(post.get());
        }
        else {
            throw new DataNotFoundException("post not found");
        }
    }

    public Post getPostEntity(UUID id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        }
        else {
            throw new DataNotFoundException("post not found");
        }
    }

    public PostResponseDto create(String title, String content, SiteUser siteUser) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setCreatedDate(LocalDateTime.now());
        post.setSiteUser(siteUser);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    public PostResponseDto modify(Post post, String title, String content) {
        post.setTitle(title);
        post.setContent(content);
        post.setLastModifiedDate(LocalDateTime.now());
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public PostResponseDto like(Post post, SiteUser siteUser) {
        LikePost likePost = new LikePost();
        likePost.setPost(post);
        likePost.setSiteUser(siteUser);
        likePostRepository.save(likePost);
        return new PostResponseDto(post);
    }
}
