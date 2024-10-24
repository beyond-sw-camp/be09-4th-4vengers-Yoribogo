package com.avengers.yoribogo.recipeboard.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RECIPE_BOARD")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeBoard {

    @Id
    @Column(name = "RECIPE_BOARD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recipeBoardId;

    @Column(name = "RECIPE_BOARD_MENU_NAME")
    private String recipeBoardMenuName;

    @Column(name = "RECIPE_BOARD_INGREDIENT")
    private String recipeBoardIngredient;

    @Column(name = "RECIPE_BOARD_IMAGE")
    private String recipeBoardImage;

    @Column(name = "RECIPE_BOARD_LIKES")
    private int recipeBoardLikes;

    @Column(name = "RECIPE_BOARD_COMMENTS")
    private int recipeBoardComments;

    @Column(name = "RECIPE_BOARD_CREATED_AT")
    private LocalDateTime recipeBoardCreatedAt;

    @Column(name = "RECIPE_BOARD_STATUS")
    @Enumerated(EnumType.STRING)
    private RecipeBoardStatus recipeBoardStatus;

    @Column(name = "USER_ID")
    private long userId;

    // OneToMany 관계 설정 (보드가 여러 매뉴얼을 가질 수 있음)
    @OneToMany(mappedBy = "recipeBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeBoardManual> manuals = new ArrayList<>();

    @OneToMany(mappedBy = "recipeBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeBoardFavorite> favorites = new ArrayList<>();
}
