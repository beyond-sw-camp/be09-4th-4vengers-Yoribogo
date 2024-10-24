package com.avengers.yoribogo.user.domain.vo.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseEmailMessageVO {

    @JsonProperty("message")
    private String message;
}
