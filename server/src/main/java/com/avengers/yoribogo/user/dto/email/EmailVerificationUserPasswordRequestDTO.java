package com.avengers.yoribogo.user.dto.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// 설명. 비밀번호 찾기시 이용되는 이메일 DTO
@Data
public class EmailVerificationUserPasswordRequestDTO {
    @NotBlank(message = "아이디는 필수 항목입니다.")
    @JsonProperty("user_auth_id")
    private String userAuthId;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식을 입력해 주세요.")
    @JsonProperty("email")
    private String email;

}
