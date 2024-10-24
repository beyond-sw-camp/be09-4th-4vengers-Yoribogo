package com.avengers.yoribogo.user.domain.vo.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailVerificationSignupVO {
    @NotBlank
    @Email
    private String email;

}
