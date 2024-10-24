package com.avengers.yoribogo.notification.weeklypopularrecipe.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Data
// Event 발생을 위한 객체
public class InsertLikeToMongoEvent {
    private String userId;      // 좋아요 누르는 유저 ID
    private String postId;      // 좋아요 대상 게시글 ID

    public InsertLikeToMongoEvent(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
