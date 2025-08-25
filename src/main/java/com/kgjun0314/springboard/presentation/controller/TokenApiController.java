package com.kgjun0314.springboard.presentation.controller;

import com.kgjun0314.springboard.presentation.dto.JwtTokenDto;
import com.kgjun0314.springboard.util.CookieUtil;
import com.kgjun0314.springboard.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/token")
public class TokenApiController {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final CookieUtil cookieUtil;

    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenDto> refreshToken(
            @CookieValue("refresh_token") String refreshToken,
            HttpServletResponse response
    ) {
        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtUtil.validateToken(refreshToken, userDetails)) {
            // 1. 새로운 액세스 토큰 생성
            String newAccessToken = jwtUtil.generateAccessToken(userDetails);
            
            // 2. 새로운 리프레시 토큰 생성 (Rotation)
            String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

            // 3. 새로운 리프레시 토큰을 쿠키에 저장
            Cookie refreshTokenCookie = cookieUtil.createCookie("refresh_token", newRefreshToken, jwtUtil.getRefreshTokenValidityInSeconds());
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok(new JwtTokenDto(newAccessToken));
        }

        return ResponseEntity.badRequest().build();
    }
}
