package com.avengers.yoribogo.user.dto;

import com.avengers.yoribogo.user.domain.enums.AcceptStatus;
import com.avengers.yoribogo.user.domain.enums.ActiveStatus;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.domain.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("user_auth_id")
    private String userAuthId;

    @JsonProperty("user_status")
    private ActiveStatus userStatus;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("withdrawn_at")
    private LocalDateTime withdrawnAt;

    @JsonProperty("profile_image")
    private String profileImage;

    @JsonProperty("accept_status")
    private AcceptStatus acceptStatus;

    @JsonProperty("signup_path")
    private SignupPath signupPath;

    @JsonProperty("user_role")
    private UserRole userRole;

    @JsonProperty("user_likes")
    private Long userLikes;

    @JsonProperty("tier_id")
    private Long tierId ;

    @JsonProperty("user_identifier")
    private String userIdentifier;
}

