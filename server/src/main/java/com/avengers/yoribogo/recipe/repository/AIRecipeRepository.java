package com.avengers.yoribogo.recipe.repository;

import com.avengers.yoribogo.recipe.domain.AIRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AIRecipeRepository extends JpaRepository<AIRecipe, Long> {

    // 요리 이름으로 조회하는 메소드(이름이 동일한 요리)
    Optional<AIRecipe> findByAiMenuName(String menuName);

    // 요리 레시피 아이디로 조회
    AIRecipe findByRecipeId(Long recipeId);

}
