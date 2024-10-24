package com.avengers.yoribogo.recipe.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "RECOMMENDED_MENU")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecommendedMenu {

    @Id
    @Column(name = "RECOMMENDED_MENU_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendedMenuId;

    @Enumerated(EnumType.STRING)
    @Column(name = "SATISFACTION")
    private Satisfaction satisfaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "RECOMMENDED_MENU_STATUS")
    private RecommendedMenuStatus recommendedMenuStatus;

    @Column(name = "RECOMMENDED_MENU_CREATED_AT")
    private LocalDateTime recommendedMenuCreatedAt;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "RECIPE_ID")
    private Long recipeId;

}
