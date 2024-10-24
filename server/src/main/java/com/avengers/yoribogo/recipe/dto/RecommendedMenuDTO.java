package com.avengers.yoribogo.recipe.dto;

import com.avengers.yoribogo.recipe.domain.RecommendedMenuStatus;
import com.avengers.yoribogo.recipe.domain.Satisfaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RecommendedMenuDTO {

    @JsonProperty("recommended_menu_id")
    private Long recommendedMenuId;

    @JsonProperty("satisfaction")
    private Satisfaction satisfaction;

    @JsonProperty("recommended_menu_status")
    private RecommendedMenuStatus recommendedMenuStatus;

    @JsonProperty("created_at")
    private LocalDateTime recommendedMenuCreatedAt;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("recipe_id")
    private Long recipeId;

}
