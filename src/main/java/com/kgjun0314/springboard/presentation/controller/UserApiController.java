package com.kgjun0314.springboard.presentation.controller;

import com.kgjun0314.springboard.application.UserService;
import com.kgjun0314.springboard.presentation.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserApiController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UserRequestDto userRequestDto) {
        userService.create(userRequestDto.getUsername(), userRequestDto.getEmail(), userRequestDto.getPassword1());
        return ResponseEntity.ok().build();
    }
}
