package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.recipe.domain.AIRecipe;
import com.avengers.yoribogo.recipe.dto.AIRecipeDTO;
import com.avengers.yoribogo.recipe.repository.AIRecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AIRecipeServiceImpl implements AIRecipeService {

    private final ModelMapper modelMapper;
    private final AIRecipeRepository aiRecipeRepository;

    @Autowired
    public AIRecipeServiceImpl(ModelMapper modelMapper,
                               AIRecipeRepository aiRecipeRepository) {
        this.modelMapper = modelMapper;
        this.aiRecipeRepository = aiRecipeRepository;
    }

    // AI 요리 레시피 등록
    @Override
    @Transactional
    public AIRecipeDTO registAIRecipe(AIRecipeDTO aiRecipeDTO) {
        AIRecipe aiRecipe = modelMapper.map(aiRecipeDTO, AIRecipe.class);
        aiRecipe.setAiMenuName(aiRecipeDTO.getMenuName());
        aiRecipe.setAiMenuIngredient(aiRecipeDTO.getMenuIngredient());
        aiRecipe.setAiMenuImage(aiRecipeDTO.getMenuImage());
        return modelMapper.map(aiRecipeRepository.save(aiRecipe), AIRecipeDTO.class);
    }

    // AI 요리 레시피 수정
    @Override
    @Transactional
    public AIRecipeDTO modifyAIRecipe(AIRecipeDTO aiRecipeDTO) {
        // 기존 엔티티 조회
        AIRecipe aiRecipe = aiRecipeRepository.findByRecipeId(aiRecipeDTO.getRecipeId());

        // 조회되지 않은 경우
        if(aiRecipe == null) throw new CommonException(ErrorCode.NOT_FOUND_AI_RECIPE);

        // 변경된 정보 반영
        aiRecipe.setAiMenuName(aiRecipeDTO.getMenuName());
        aiRecipe.setAiMenuIngredient(aiRecipeDTO.getMenuIngredient());
        aiRecipe.setAiMenuImage(aiRecipeDTO.getMenuImage());

        return modelMapper.map(aiRecipeRepository.save(aiRecipe), AIRecipeDTO.class);
    }

    // AI 요리 레시피 요리 이름으로 조회(이름이 동일한 요리)
    @Override
    public AIRecipeDTO findAIRecipeByMenuName(String aiAnswer) {
        AIRecipe aiRecipe = aiRecipeRepository.findByAiMenuName(aiAnswer).orElse(null);

        if (aiRecipe == null) return null;

        return AIRecipeDTO
                .builder()
                .aiRecipeId(aiRecipe.getAiRecipeId())
                .menuName(aiRecipe.getAiMenuName())
                .menuIngredient(aiRecipe.getAiMenuIngredient())
                .menuImage(aiRecipe.getAiMenuImage())
                .recipeId(aiRecipe.getRecipeId())
                .build();
    }

}
