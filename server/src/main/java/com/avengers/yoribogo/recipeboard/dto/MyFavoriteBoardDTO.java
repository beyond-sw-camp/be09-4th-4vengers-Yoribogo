package com.avengers.yoribogo.recipeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MyFavoriteBoardDTO { // 마이페이지 조회용 게시글 dto

    @JsonProperty("board_id")
    private Long recipeBoardId;

    @JsonProperty("menu_name")
    private String recipeBoardMenuName;

    @JsonProperty("board_image")
    private String recipeBoardImage;
}
