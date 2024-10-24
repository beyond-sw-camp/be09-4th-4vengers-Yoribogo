package com.avengers.yoribogo.recipeboard.recipeboard.dto;

import com.avengers.yoribogo.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipe_board")
@Data
public class RecipeBoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_board_id")
    private Long recipeBoardId;

    @Column(name = "recipe_board_menu_name", length = 255)
    private String recipeBoardMenuName;

    @Column(name = "recipe_board_ingredient", columnDefinition = "TEXT")
    private String recipeBoardIngredient;

    @Column(name = "recipe_board_image", columnDefinition = "TEXT", nullable = true)
    private String recipeBoardImage;

    @Column(name = "recipe_board_likes")
    private Long recipeBoardLikes;

    @Column(name = "recipe_board_comments")
    private Long recipeBoardComments;

    @Column(name = "recipe_board_created_at")
    private LocalDateTime recipeBoardCreatedAt;

    @Column(name = "recipe_board_status", length = 255)
    private String recipeBoardStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}