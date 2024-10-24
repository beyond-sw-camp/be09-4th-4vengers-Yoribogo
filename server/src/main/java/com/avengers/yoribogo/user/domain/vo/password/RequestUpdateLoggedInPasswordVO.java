package com.avengers.yoribogo.user.domain.vo.password;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestUpdateLoggedInPasswordVO {
    @JsonProperty("password")
    private String password;
}