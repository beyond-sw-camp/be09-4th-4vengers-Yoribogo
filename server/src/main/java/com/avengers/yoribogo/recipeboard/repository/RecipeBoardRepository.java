package com.avengers.yoribogo.recipeboard.repository;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeBoardRepository extends JpaRepository<RecipeBoard, Long> {

    Page<RecipeBoard> findByRecipeBoardMenuNameContaining(String recipeBoardMenuName, Pageable pageable);

    Page<RecipeBoard> findByUserId(Long userId, Pageable pageable);
}
