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
public class PublicDataRecipeDTO extends BaseRecipeDTO {

    @JsonProperty("public_data_recipe_id")
    private Long publicDataRecipeId;

}
