package com.avengers.yoribogo.recipeboard.dto;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardRecommentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecipeBoardRecommentDTO {

    @JsonProperty("recomment_id")
    private Long recipeBoardRecommentId;

    @JsonProperty("recomment_content")
    private String recipeBoardRecommentContent;

    @JsonProperty("recomment_created_at")
    private LocalDateTime recipeBoardRecommentCreatedAt;

    @JsonProperty("comment_id")
    private Long recipeBoardCommentId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("recomment_status")
    private RecipeBoardRecommentStatus recipeBoardRecommentStatus;
}
