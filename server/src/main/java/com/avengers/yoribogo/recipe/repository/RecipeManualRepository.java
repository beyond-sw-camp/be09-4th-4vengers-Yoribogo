package com.avengers.yoribogo.recipe.repository;

import com.avengers.yoribogo.recipe.domain.RecipeManual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeManualRepository extends JpaRepository<RecipeManual, Long> {

    // 요리 레시피 아이디로 매뉴얼 조회
    List<RecipeManual> findByRecipeId(Long recipeId);

}
