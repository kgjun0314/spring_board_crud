package com.kgjun0314.springboard.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SiteUser siteUser;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private Set<LikeComment> likes = new HashSet<>();
}
