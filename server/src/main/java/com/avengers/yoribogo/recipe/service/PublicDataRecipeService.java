package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.dto.PublicDataRecipeDTO;

public interface PublicDataRecipeService {

    // 공공데이터 요리 레시피 등록
    PublicDataRecipeDTO registPublicDataRecipe(PublicDataRecipeDTO publicDataRecipeDTO);

    // 공공데이터 요리 레시피 수정
    PublicDataRecipeDTO modifyPublicDataRecipe(PublicDataRecipeDTO publicDataRecipeDTO);

    // 공공데이터 요리 레시피 요리 이름으로 조회(이름이 동일한 요리)
    PublicDataRecipeDTO findPublicDataRecipeByMenuName(String aiAnswer);

}
