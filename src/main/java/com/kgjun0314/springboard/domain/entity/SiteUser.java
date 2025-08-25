package com.kgjun0314.springboard.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class SiteUser extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private UUID id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private Set<LikeComment> likesComment;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private Set<LikePost> likesPost;

    @Column(name = "refresh_token")
    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
