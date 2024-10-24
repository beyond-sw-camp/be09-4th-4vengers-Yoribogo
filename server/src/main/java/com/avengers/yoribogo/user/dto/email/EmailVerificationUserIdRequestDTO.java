package com.avengers.yoribogo.user.dto.email;

import com.avengers.yoribogo.user.domain.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


// 설명. 아이디 찾기시 이용되는 이메일 DTO
@Data
public class EmailVerificationUserIdRequestDTO {

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @JsonProperty("nickname")
    private String nickname;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식을 입력해 주세요.")
    @JsonProperty("email")
    private String email;
}

