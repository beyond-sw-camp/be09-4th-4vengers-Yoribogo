package com.avengers.yoribogo.recipeboard.dto;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardCommentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class RecipeBoardCommentDTO {

    @JsonProperty("comment_id")
    private Long recipeBoardCommentId;

    @JsonProperty("comment_content")
    private String recipeBoardCommentContent;

    @JsonProperty("comment_created-at")
    private LocalDateTime recipeBoardCommentCreatedAt;

    @JsonProperty("recipe_board_id")
    private Long recipeBoardId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("comment_status")
    private RecipeBoardCommentStatus recipeBoardCommentStatus;


}
