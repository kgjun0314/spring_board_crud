package com.kgjun0314.springboard.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentRequestDto {
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
