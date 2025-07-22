package com.kgjun0314.springboard.jwt;

import com.kgjun0314.springboard.application.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = parseJwt(request);
        if (token != null && jwtUtil.validateToken(token)) {
            setAuthentication(token, request);
        } else {
            // accessToken이 없거나 만료된 경우 → refreshToken 시도
            String refreshToken = parseRefreshToken(request);

            if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
                String username = jwtUtil.getUsernameFromToken(refreshToken);
                String newAccessToken = jwtUtil.generateAccessToken(username);

                // 새 accessToken을 쿠키에 저장
                ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                        .httpOnly(true)
                        .secure(true)
                        .sameSite("Strict")
                        .path("/")
                        .maxAge(15 * 60) // 15분
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

                // 인증 설정
                setAuthentication(newAccessToken, request);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token, HttpServletRequest request) {
        String username = jwtUtil.getUsernameFromToken(token);
        var userDetails = userService.loadUserByUsername(username);

        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    private String parseRefreshToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}