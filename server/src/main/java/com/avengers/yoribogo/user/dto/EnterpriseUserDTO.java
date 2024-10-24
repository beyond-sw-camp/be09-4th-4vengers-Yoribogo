package com.avengers.yoribogo.user.dto;
import com.avengers.yoribogo.user.domain.enums.AcceptStatus;
import com.avengers.yoribogo.user.domain.enums.ActiveStatus;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.domain.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnterpriseUserDTO {

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
    private UserRole userRole = UserRole.ENTERPRISE; // 기본값은 일반 회원

    @JsonProperty("user_likes")
    private Long userLikes = 0L; // 기본값 0

    @JsonProperty("tier_id")
    private Long tierId = 1L; // 기본값 1

    @JsonProperty("user_identifier")
    private String userIdentifier;
}
