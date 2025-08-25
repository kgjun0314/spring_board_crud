package com.kgjun0314.springboard.presentation.controller;

import com.kgjun0314.springboard.application.UserService;
import com.kgjun0314.springboard.domain.entity.SiteUser;
import com.kgjun0314.springboard.presentation.dto.JwtTokenDto;
import com.kgjun0314.springboard.util.CookieUtil;
import com.kgjun0314.springboard.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/token")
public class TokenApiController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final CookieUtil cookieUtil;

    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenDto> refreshToken(
            @CookieValue("refresh_token") String refreshToken,
            HttpServletResponse response
    ) {
        // 1. Validate Refresh Token
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        // 2. Extract username and find user
        String username = jwtUtil.extractUsername(refreshToken);
        SiteUser user = userService.getUser(username);

        // 3. Check if the token in DB matches the one provided
        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token mismatch");
        }

        // 4. Generate new tokens
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

        // 5. Save new refresh token to DB (rotation)
        userService.saveRefreshToken(username, newRefreshToken);

        // 6. Set new refresh token in cookie
        Cookie refreshTokenCookie = cookieUtil.createCookie("refresh_token", newRefreshToken, jwtUtil.getRefreshTokenValidityInSeconds());
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new JwtTokenDto(newAccessToken));
    }
}
