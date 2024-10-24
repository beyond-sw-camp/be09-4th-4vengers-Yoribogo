package com.avengers.yoribogo.recipeboard.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RECIPE_BOARD_MANUAL")
@Data
public class RecipeBoardManual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECIPE_BOARD_MANUAL_ID")
    private long recipeBoardManualId;

    @Column(name = "RECIPE_BOARD_MANUAL_STEP", nullable = false)
    private int recipeBoardManualStep;

    @Column(name = "RECIPE_BOARD_MANUAL_IMAGE")
    private String recipeBoardManualImage;

    @Column(name = "RECIPE_BOARD_MANUAL_CONTENT", nullable = false)
    private String recipeBoardManualContent;

    // ManyToOne 관계 설정 (보드와 매뉴얼 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPE_BOARD_ID", nullable = false)
    private RecipeBoard recipeBoard;
}
