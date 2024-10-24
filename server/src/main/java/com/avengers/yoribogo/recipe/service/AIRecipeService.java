package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.dto.AIRecipeDTO;

public interface AIRecipeService {

    // AI 요리 레시피 등록
    AIRecipeDTO registAIRecipe(AIRecipeDTO aiRecipeDTO);

    // AI 요리 레시피 수정
    AIRecipeDTO modifyAIRecipe(AIRecipeDTO aiRecipeDTO);

    // AI 요리 레시피 이름으로 조회(이름이 동일한 요리)
    AIRecipeDTO findAIRecipeByMenuName(String aiAnswer);

}
