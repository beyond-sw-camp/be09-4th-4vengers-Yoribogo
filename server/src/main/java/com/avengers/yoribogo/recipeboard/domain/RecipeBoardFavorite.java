package com.avengers.yoribogo.recipeboard.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RECIPE_BOARD_FAVORITE")
@Data
public class RecipeBoardFavorite {

    @Id
    @Column(name = "RECIPE_BOARD_FAVORITE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeBoardFavoriteId;

    @Column(name = "USER_ID")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPE_BOARD_ID")
    private RecipeBoard recipeBoard;
}
