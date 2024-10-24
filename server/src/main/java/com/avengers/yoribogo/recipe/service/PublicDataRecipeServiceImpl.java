package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.recipe.domain.PublicDataRecipe;
import com.avengers.yoribogo.recipe.dto.PublicDataRecipeDTO;
import com.avengers.yoribogo.recipe.repository.PublicDataRecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublicDataRecipeServiceImpl implements PublicDataRecipeService {

    private final ModelMapper modelMapper;
    private final PublicDataRecipeRepository publicDataRecipeRepository;

    @Autowired
    public PublicDataRecipeServiceImpl(ModelMapper modelMapper,
                                       PublicDataRecipeRepository publicDataRecipeRepository) {
        this.modelMapper = modelMapper;
        this.publicDataRecipeRepository = publicDataRecipeRepository;
    }

    // 공공데이터 요리 레시피 등록
    @Override
    @Transactional
    public PublicDataRecipeDTO registPublicDataRecipe(PublicDataRecipeDTO publicDataRecipeDTO) {
        PublicDataRecipe publicDataRecipe =
                modelMapper.map(publicDataRecipeDTO, PublicDataRecipe.class);
        publicDataRecipe.setPublicDataMenuName(publicDataRecipeDTO.getMenuName());
        publicDataRecipe.setPublicDataMenuIngredient(publicDataRecipeDTO.getMenuIngredient());
        publicDataRecipe.setPublicDataMenuImage(publicDataRecipeDTO.getMenuImage());
        return modelMapper.map(publicDataRecipeRepository.save(publicDataRecipe), PublicDataRecipeDTO.class);
    }

    // 공공데이터 요리 레시피 수정
    @Override
    @Transactional
    public PublicDataRecipeDTO modifyPublicDataRecipe(PublicDataRecipeDTO publicDataRecipeDTO) {
        // 기존 엔티티 조회
        PublicDataRecipe publicDataRecipe =
                publicDataRecipeRepository.findByRecipeId(publicDataRecipeDTO.getRecipeId());

        // 조회되지 않은 경우
        if(publicDataRecipe == null) throw new CommonException(ErrorCode.NOT_FOUND_PUBLIC_DATA_RECIPE);

        // 변경된 정보 반영
        publicDataRecipe.setPublicDataMenuName(publicDataRecipeDTO.getMenuName());
        publicDataRecipe.setPublicDataMenuIngredient(publicDataRecipeDTO.getMenuIngredient());
        publicDataRecipe.setPublicDataMenuImage(publicDataRecipeDTO.getMenuImage());

        return modelMapper.map(publicDataRecipeRepository.save(publicDataRecipe), PublicDataRecipeDTO.class);
    }

    // 공공데이터 요리 레시피 요리 이름으로 조회(이름이 동일한 요리)
    @Override
    public PublicDataRecipeDTO findPublicDataRecipeByMenuName(String aiAnswer) {
        PublicDataRecipe publicDataRecipe = publicDataRecipeRepository.findByPublicDataMenuName(aiAnswer)
                .orElse(null);

        if (publicDataRecipe == null) return null;

        return PublicDataRecipeDTO
                .builder()
                .publicDataRecipeId(publicDataRecipe.getPublicDataRecipeId())
                .menuName(publicDataRecipe.getPublicDataMenuName())
                .menuIngredient(publicDataRecipe.getPublicDataMenuIngredient())
                .menuImage(publicDataRecipe.getPublicDataMenuImage())
                .recipeId(publicDataRecipe.getRecipeId())
                .build();
    }

}
