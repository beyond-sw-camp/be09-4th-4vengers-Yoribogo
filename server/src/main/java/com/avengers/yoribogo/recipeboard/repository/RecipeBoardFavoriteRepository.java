package com.avengers.yoribogo.recipeboard.repository;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeBoardFavoriteRepository extends JpaRepository<RecipeBoardFavorite, Long> {
    Page<RecipeBoardFavorite> findAllByUserId(Long userId, Pageable pageable);

    Optional<RecipeBoardFavorite> findByUserIdAndRecipeBoard_RecipeBoardId(Long userId, Long recipeBoardId);

    boolean existsByUserIdAndRecipeBoard_RecipeBoardId(Long userId, Long boardId);
}
