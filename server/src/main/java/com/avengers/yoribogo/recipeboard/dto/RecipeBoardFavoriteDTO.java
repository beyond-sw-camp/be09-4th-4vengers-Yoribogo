package com.avengers.yoribogo.recipeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeBoardFavoriteDTO {

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("board_id")
    private long recipeBoardId;
}
