package com.avengers.yoribogo.recipe.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PUBLIC_DATA_RECIPE")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PublicDataRecipe {

    @Id
    @Column(name = "PUBLIC_DATA_RECIPE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publicDataRecipeId;

    @Column(name = "PUBLIC_DATA_MENU_NAME")
    private String publicDataMenuName;

    @Column(name = "PUBLIC_DATA_MENU_INGREDIENT")
    private String publicDataMenuIngredient;

    @Column(name = "PUBLIC_DATA_MENU_IMAGE")
    private String publicDataMenuImage;

    @Column(name = "RECIPE_ID")
    private Long recipeId;

}
