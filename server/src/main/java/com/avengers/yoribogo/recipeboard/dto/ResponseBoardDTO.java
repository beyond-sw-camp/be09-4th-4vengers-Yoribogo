package com.avengers.yoribogo.recipeboard.dto;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseBoardDTO {

    @JsonProperty("board_id")
    private Long recipeBoardId;

    @JsonProperty("menu_name")
    private String recipeBoardMenuName;

    @JsonProperty("ingredients")
    private String recipeBoardIngredient;

    @JsonProperty("board_image")
    private String recipeBoardImage;

    @JsonProperty("created_at")
    private LocalDateTime recipeBoardCreatedAt;

    @JsonProperty("likes")
    private int recipeBoardLikes;

    @JsonProperty("comments")
    private int recipeBoardComments;

    @JsonProperty("status")
    private RecipeBoardStatus recipeBoardStatus;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("manuals")
    private List<RecipeBoardManualDTO> manuals;
}
