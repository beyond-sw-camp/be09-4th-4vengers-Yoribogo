package com.avengers.yoribogo.recipeboard.recipeboardlike.dto;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoard;
import com.avengers.yoribogo.recipeboard.recipeboard.dto.RecipeBoardEntity;
import com.avengers.yoribogo.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipe_board_like")
@Data
public class RecipeBoardLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_board_like_id")
    private Long recipeBoardLikeId;

    @Column(name = "like_created_at")
    private LocalDateTime likeCreatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "recipe_board_id")
    private RecipeBoard recipeBoard;

    public Long getRecipeBoardLikeId() {
        return recipeBoardLikeId;
    }

    public LocalDateTime getLikeCreatedAt() {
        return likeCreatedAt;
    }

    public UserEntity getUser() {
        return user;
    }

    public RecipeBoard  getRecipeBoard() {
        return recipeBoard;
    }

    public void setRecipeBoardLikeId(Long recipeBoardLikeId) {
        this.recipeBoardLikeId = recipeBoardLikeId;
    }

    public void setLikeCreatedAt(LocalDateTime likeCreatedAt) {
        this.likeCreatedAt = likeCreatedAt;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setRecipeBoard(RecipeBoard  recipeBoard) {
        this.recipeBoard = recipeBoard;
    }
}