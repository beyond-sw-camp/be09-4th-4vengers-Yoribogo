package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.dto.RecipeManualDTO;
import com.avengers.yoribogo.recipe.dto.RequestAIRecipeManualDTO;
import com.avengers.yoribogo.recipe.dto.RequestRecipeManualDTO;
import reactor.core.publisher.Flux;

import java.util.List;

public interface RecipeManualService {

    // 요리 레시피 아이디로 매뉴얼 조회
    List<RecipeManualDTO> findRecipeManualByRecipeId(Long recipeId);

    // 요리 레시피 매뉴얼 등록
    List<RecipeManualDTO> registRecipeManual(Long recipeId, RequestRecipeManualDTO requestRecipeManualDTO);

    // 요리 레시피 매뉴얼 수정
    List<RecipeManualDTO> modifyRecipeManual(Long recipeId, RequestRecipeManualDTO requestRecipeManualDTO);

    // AI 생성 매뉴얼 등록
    Flux<String> registAIRecipeManual(Long recipeId, RequestAIRecipeManualDTO requestAIRecipeManualDTO);

}
