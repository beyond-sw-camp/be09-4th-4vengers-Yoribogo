package com.avengers.yoribogo.user.dto.profile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserProfileDTO {
    private String nickname;
    private String profileImage;  // 티어 이미지 추가
    private String tierName;
    private String tierImage;
    private String email;
    private String userRole;
    private String signupPath;
}