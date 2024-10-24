package com.avengers.yoribogo.recipe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class BaseRecipeDTO {

    @JsonProperty("recipe_id")
    private Long recipeId;

    @JsonProperty("menu_name")
    private String menuName;

    @JsonProperty("menu_ingredient")
    private String menuIngredient;

    @JsonProperty("menu_image")
    private String menuImage;

}
