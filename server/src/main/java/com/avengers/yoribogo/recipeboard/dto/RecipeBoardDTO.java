package com.avengers.yoribogo.recipeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeBoardDTO {

    @JsonProperty("board_id")
    private Long recipeBoardId;

    @JsonProperty("menu_name")
    private String recipeBoardMenuName;

    @JsonProperty("ingredients")
    private String recipeBoardIngredient;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("board_image")
    private MultipartFile boardImage;

}
