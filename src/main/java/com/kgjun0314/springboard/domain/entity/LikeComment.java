package com.kgjun0314.springboard.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(
        name = "like_comment",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "comment_id"})
        }
)
public class LikeComment {
    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private SiteUser siteUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
}
