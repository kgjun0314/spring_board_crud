package com.kgjun0314.springboard.presentation.controller;

import com.kgjun0314.springboard.application.UserService;
import com.kgjun0314.springboard.jwt.JwtAccessToken;
import com.kgjun0314.springboard.jwt.JwtRefreshToken;
import com.kgjun0314.springboard.jwt.JwtUtil;
import com.kgjun0314.springboard.presentation.dto.UserLoginDto;
import com.kgjun0314.springboard.presentation.dto.UserRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserApiController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UserRequestDto userRequestDto) {
        userService.create(userRequestDto.getUsername(), userRequestDto.getEmail(), userRequestDto.getPassword1());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto,  HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword())
            );
            String name = authentication.getName();
            String accessToken = jwtUtil.generateAccessToken(name);
            String refreshToken = jwtUtil.generateRefreshToken(name);

            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(180 * 24 * 60 * 60) // 180일
                    .build();

            ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(15 * 60) // 15분
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

            JwtAccessToken jwtAccesstoken = new JwtAccessToken();
            jwtAccesstoken.setToken(accessToken);

            return ResponseEntity.ok(jwtAccesstoken);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("아이디 또는 비빌먼호가 일치하지 않습니다.");
        }
    }

    @PostMapping("/refresh")
    public JwtAccessToken refresh(@RequestBody JwtRefreshToken jwtRefreshToken) {
        String refreshToken = jwtRefreshToken.getToken();
        if(jwtUtil.validateToken(refreshToken)) {
            String name = jwtUtil.getUsernameFromToken(refreshToken);
            String newAccessToken = jwtUtil.generateAccessToken(name);
            JwtAccessToken newJwtAccessToken = new JwtAccessToken();
            newJwtAccessToken.setToken(newAccessToken);
            return newJwtAccessToken;
        } else {
            throw new RuntimeException("Invalid Refresh Token");
        }
    }
}
