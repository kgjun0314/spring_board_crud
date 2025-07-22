package com.kgjun0314.springboard.presentation.dto;

import com.kgjun0314.springboard.domain.entity.SiteUser;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID id;
    private String username;
    private String email;

    public UserResponseDto(SiteUser siteUser) {
        id = siteUser.getId();
        username = siteUser.getUsername();
        email = siteUser.getEmail();
    }
}
