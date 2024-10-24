package com.avengers.yoribogo.recipe.dto;

import com.avengers.yoribogo.recipe.domain.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GoodMenuDTO {

    @JsonProperty("recommended_menu_id")
    private Long recommendedMenuId;

    @JsonProperty("menu_name")
    private String menuName;

    @JsonProperty("menu_ingredient")
    private String menuIngredient;

    @JsonProperty("menu_image")
    private String menuImage;

    @JsonProperty("menu_type")
    private MenuType menuType;

    @JsonProperty("satisfaction")
    private Satisfaction satisfaction;

    @JsonProperty("recommended_menu_status")
    private RecommendedMenuStatus recommendedMenuStatus;

    @JsonProperty("recipe_id")
    private Long recipeId;

    public GoodMenuDTO(RecommendedMenu recommendedMenu, Recipe recipe) {
        this.recommendedMenuId = recommendedMenu.getRecommendedMenuId();
        this.menuName = recipe.getMenuName();
        this.menuIngredient = recipe.getMenuIngredient();
        this.menuImage = recipe.getMenuImage();
        this.menuType = recipe.getMenuType();
        this.satisfaction = recommendedMenu.getSatisfaction();
        this.recommendedMenuStatus = recommendedMenu.getRecommendedMenuStatus();
        this.recipeId = recommendedMenu.getRecipeId();
    }

}
