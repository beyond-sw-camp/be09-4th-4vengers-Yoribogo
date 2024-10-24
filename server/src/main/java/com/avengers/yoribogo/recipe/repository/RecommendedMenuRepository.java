package com.avengers.yoribogo.recipe.repository;

import com.avengers.yoribogo.recipe.domain.RecommendedMenu;
import com.avengers.yoribogo.recipe.domain.RecommendedMenuStatus;
import com.avengers.yoribogo.recipe.domain.Satisfaction;
import com.avengers.yoribogo.recipe.dto.GoodMenuDTO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecommendedMenuRepository extends JpaRepository<RecommendedMenu, Long> {

    // 추천 요리 회원별 조회
    @Query("SELECT new com.avengers.yoribogo.recipe.dto.GoodMenuDTO(rm, r) " +
            "FROM RecommendedMenu rm " +
            "JOIN Recipe r ON rm.recipeId = r.recipeId " +
            "WHERE rm.userId = :userId " +
            "AND rm.satisfaction = :satisfaction " +
            "AND rm.recommendedMenuStatus = :status")
    Page<GoodMenuDTO> findRecommendedMenuWithRecipeByUserId(
            @Param("userId") Long userId,
            @Param("satisfaction") Satisfaction satisfaction,
            @Param("status") RecommendedMenuStatus status,
            Pageable pageable);

    // 기존 추천된 요리 조회
    RecommendedMenu findByRecipeIdAndUserId(Long recipeId, Long userId);

}
