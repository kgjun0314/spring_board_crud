package com.kgjun0314.springboard.presentation.controller;

import com.kgjun0314.springboard.presentation.dto.UserLoginDto;
import com.kgjun0314.springboard.presentation.dto.UserRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserApiController userApiController;

    @GetMapping("/signup")
    public String signup(UserRequestDto userRequestDto) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserRequestDto userRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!userRequestDto.getPassword1().equals(userRequestDto.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            userApiController.signup(userRequestDto);
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(UserLoginDto userLoginDto) {
        return "login_form";
    }

    @PostMapping("/login")
    public String login(@Valid UserLoginDto userLoginDto, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login_form";
        }
        try {
            userApiController.login(userLoginDto, response);
        } catch(RuntimeException e) {
            bindingResult.reject("loginFailed", e.getMessage());
            return "login_form";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // accessToken 쿠키 삭제
        ResponseCookie deleteAccessToken = ResponseCookie.from("accessToken", "")
                .path("/")
                .maxAge(0)
                .httpOnly(false)
                .secure(true)
                .sameSite("Strict")
                .build();

        // refreshToken 쿠키 삭제
        ResponseCookie deleteRefreshToken = ResponseCookie.from("refreshToken", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshToken.toString());

        // 시큐리티 컨텍스트 제거 (선택 사항)
        SecurityContextHolder.clearContext();

        return "redirect:/";
    }
}
