package com.kgjun0314.springboard.presentation.controller;

import com.kgjun0314.springboard.application.UserService;
import com.kgjun0314.springboard.presentation.dto.JwtTokenDto;
import com.kgjun0314.springboard.presentation.dto.UserLoginDto;
import com.kgjun0314.springboard.presentation.dto.UserRequestDto;
import com.kgjun0314.springboard.util.CookieUtil;
import com.kgjun0314.springboard.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserApiController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UserRequestDto userRequestDto) {
        userService.create(userRequestDto.getUsername(), userRequestDto.getEmail(), userRequestDto.getPassword1());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> login(@RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDto.getUsername());
        final String accessToken = jwtUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        userService.saveRefreshToken(userDetails.getUsername(), refreshToken);

        Cookie refreshTokenCookie = cookieUtil.createCookie("refresh_token", refreshToken, jwtUtil.getRefreshTokenValidityInSeconds());
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new JwtTokenDto(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.invalidateRefreshToken(username);

        Cookie refreshTokenCookie = cookieUtil.deleteCookie("refresh_token");
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok().build();
    }
}
