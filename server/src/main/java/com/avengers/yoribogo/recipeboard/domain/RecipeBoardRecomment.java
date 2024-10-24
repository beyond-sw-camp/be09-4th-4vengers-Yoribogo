package com.avengers.yoribogo.recipeboard.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "recipe_board_recomment")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeBoardRecomment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_board_recomment_id")
    private Long recipeBoardRecommentId;

    @Column(name = "recipe_board_recomment_content")
    private String recipeBoardRecommentContent;

    @Enumerated(EnumType.STRING) // ENUM 타입 매핑
    @Column(name = "recipe_board_recomment_status")
    private RecipeBoardRecommentStatus recipeBoardRecommentStatus;

    @Column(name = "recipe_board_recomment_created_at")
    private LocalDateTime recipeBoardRecommentCreatedAt;

    // 댓글-대댓글 관계:일대다 -> RecipeBoardComment을 참조
    @ManyToOne // ManyToOne 관계
    @JoinColumn(name = "recipe_board_comment_id") // 외래키 설정
    private RecipeBoardComment recipeBoardComment; // 댓글 객체를 직접 참조

    @Column(name = "user_id")
    private Long userId;
}
