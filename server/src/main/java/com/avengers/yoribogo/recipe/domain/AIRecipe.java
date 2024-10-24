package com.avengers.yoribogo.recipe.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AI_RECIPE")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AIRecipe {

    @Id
    @Column(name = "AI_RECIPE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aiRecipeId;

    @Column(name = "AI_MENU_NAME")
    private String aiMenuName;

    @Column(name = "AI_MENU_INGREDIENT")
    private String aiMenuIngredient;

    @Column(name = "AI_MENU_IMAGE")
    private String aiMenuImage;

    @Column(name = "RECIPE_ID")
    private Long recipeId;

}
