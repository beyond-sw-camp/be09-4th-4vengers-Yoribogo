package com.avengers.yoribogo.user.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequestUpdateUserDTO {
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("profile_image")
    private MultipartFile profileImage;  // 클라이언트가 보낸 파일
}
