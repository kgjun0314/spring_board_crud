package com.kgjun0314.springboard.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private UUID id;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SiteUser siteUser;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<LikePost> likes = new HashSet<>();
}
