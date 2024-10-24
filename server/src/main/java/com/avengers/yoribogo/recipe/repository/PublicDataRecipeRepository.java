package com.avengers.yoribogo.recipe.repository;

import com.avengers.yoribogo.recipe.domain.PublicDataRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicDataRecipeRepository extends JpaRepository<PublicDataRecipe, Long> {

    // 공공데이터 요리 레시피 요리 이름으로 조회(이름이 동일한 요리)
    Optional<PublicDataRecipe> findByPublicDataMenuName(String menuName);

    // 요리 레시피 아이디로 조회
    PublicDataRecipe findByRecipeId(Long recipeId);

}
