package com.avengers.yoribogo.recipeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFavoriteDTO {

    @JsonProperty("id")
    private Long recipeBoardFavoriteId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("board_id")
    private Long recipeBoardId;
}
