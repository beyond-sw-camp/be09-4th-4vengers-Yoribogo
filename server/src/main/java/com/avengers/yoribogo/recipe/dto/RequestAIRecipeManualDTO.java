package com.avengers.yoribogo.recipe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RequestAIRecipeManualDTO {

    @JsonProperty("menu_name")
    private String menuName;

    @JsonProperty("menu_ingredient")
    private String menuIngredient;

}
