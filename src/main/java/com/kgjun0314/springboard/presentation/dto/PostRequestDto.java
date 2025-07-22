package com.kgjun0314.springboard.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequestDto {
    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=200)
    private String title;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;
}
