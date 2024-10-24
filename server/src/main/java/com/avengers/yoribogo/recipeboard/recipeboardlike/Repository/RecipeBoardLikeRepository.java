package com.avengers.yoribogo.recipeboard.recipeboardlike.Repository;

import com.avengers.yoribogo.recipeboard.recipeboardlike.dto.RecipeBoardLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeBoardLikeRepository extends JpaRepository<RecipeBoardLikeEntity, Long> {
    // 특정 유저가 특정 게시글에 이미 좋아요를 눌렀는지 확인
    Optional<RecipeBoardLikeEntity> findByUserUserIdAndRecipeBoardRecipeBoardId(Long userId, Long recipeBoardId);
}
