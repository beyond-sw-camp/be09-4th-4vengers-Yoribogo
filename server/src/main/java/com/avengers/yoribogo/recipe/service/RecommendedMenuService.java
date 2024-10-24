package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.dto.GoodMenuDTO;
import com.avengers.yoribogo.recipe.dto.RecommendedMenuDTO;
import org.springframework.data.domain.Page;

public interface RecommendedMenuService {

    // 추천 요리 회원별 조회
    Page<GoodMenuDTO> findRecommendedMenuByUserId(Long userId, Integer pageNo);

    // 추천 요리 등록
    RecommendedMenuDTO registRecommendedMenu(RecommendedMenuDTO registRecommendedMenuDTO);

    // 추천 요리 삭제
    void removeRecommendedMenu(Long recommendedMenuId);

}
