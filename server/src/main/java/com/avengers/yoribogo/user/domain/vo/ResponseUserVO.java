package com.avengers.yoribogo.user.domain.vo;

import com.avengers.yoribogo.user.domain.enums.AcceptStatus;
import com.avengers.yoribogo.user.domain.enums.ActiveStatus;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.domain.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseUserVO {

    @JsonProperty("user_id")
    private Long userId; // 사용자 고유 ID (DB 기본 키)

    @JsonProperty("user_auth_id")
    private String userAuthId; // 사용자 인증 ID, 일반 로그인 시 사용자가 입력한 ID 또는 소셜 로그인 시 고유 ID

    @JsonProperty("user_name")
    private String userName; // 사용자 이름

    @JsonProperty("nickname")
    private String nickname; // 사용자 닉네임

    @JsonProperty("email")
    private String email; // 이메일 (선택 사항)

    @JsonProperty("user_status")
    private ActiveStatus userStatus; // 사용자 상태 (ACTIVE, INACTIVE 등)

    @JsonProperty("created_at")
    private LocalDateTime createdAt; // 생성 날짜

    @JsonProperty("withdrawn_at")
    private LocalDateTime withdrawnAt; // 탈퇴 날짜

    @JsonProperty("profile_image")
    private String profileImage; // 프로필 이미지

    @JsonProperty("accept_status")
    private AcceptStatus acceptStatus; // 약관 동의 여부

    @JsonProperty("signup_path")
    private SignupPath signupPath; // 가입 경로 (NORMAL, KAKAO, GOOGLE 등)

    @JsonProperty("user_identifier")
    private String userIdentifier; // 가입 경로 + user_auth_id 결합된 고유 식별자

    // 추가된 필드들
    @JsonProperty("user_role")
    private UserRole userRole; // 사용자 역할 (관리자, 일반 사용자 등)

    @JsonProperty("user_likes")
    private Long userLikes; // 사용자가 받은 좋아요 수

    @JsonProperty("tier_id")
    private Long tierId; // 사용자의 등급 (1: 브론즈, 2: 실버 등)
}
