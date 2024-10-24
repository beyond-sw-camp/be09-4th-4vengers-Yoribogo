package com.avengers.yoribogo.recipeboard.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "recipe_board_comment")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class RecipeBoardComment {

    @Id
    @Column(name = "recipe_board_comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeBoardCommentId;

    @Column(name = "recipe_board_comment_content")
    private String recipeBoardCommentContent;

    @Column(name = "recipe_board_comment_created_at")
    private LocalDateTime recipeBoardCommentCreatedAt;

    @Column(name = "recipe_board_id")
    private Long recipeBoardId;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipe_board_comment_status", nullable = false)
    private RecipeBoardCommentStatus recipeBoardCommentStatus = RecipeBoardCommentStatus.ACTIVE; // 기본값 설정

}

