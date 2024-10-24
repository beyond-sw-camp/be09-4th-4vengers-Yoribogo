package com.avengers.yoribogo.recipeboard.recipeboardlike.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LikeRequestDTO {
    private Long userId;      // 좋아요 누르는 유저 ID
    private Long postId;      // 좋아요 대상 게시글 ID


    public LikeRequestDTO() {
    }

    public LikeRequestDTO(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }


}
