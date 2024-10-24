package com.avengers.yoribogo.user.domain.vo.login;

import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.domain.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestLoginVO {

    @JsonProperty("user_auth_id")
    private String userAuthId;

    @JsonProperty("password")
    private String password;
}