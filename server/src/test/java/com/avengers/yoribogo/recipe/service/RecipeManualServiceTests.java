package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.dto.RecipeManualDTO;
import com.avengers.yoribogo.recipe.dto.RequestRecipeManualDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
@Transactional
class RecipeManualServiceTests {

    @Autowired
    private RecipeManualService recipeManualService;

    @DisplayName("매뉴얼 요리 레시피 아이디로 조회 테스트")
    @Test
    void testFindRecipeManualByRecipeId() {
        // Given
        Long recipeId = 1L;

        // When
        List<RecipeManualDTO> recipeManualDTOList = recipeManualService.findRecipeManualByRecipeId(recipeId);

        // Then
        Assertions.assertNotNull(recipeManualDTOList, "매뉴얼이 null 입니다.");

        // 로그 찍기
        recipeManualDTOList.forEach(x -> log.info(x.toString()));
    }

    @DisplayName("매뉴얼 등록 테스트")
    @Test
    void testRegistRecipeManual() {
        // Given
        Long recipeId = 1L;

        List<Map<String,String>> manual = new ArrayList<>();

        Map<String,String> map = new HashMap<>();

        map.put("image","이미지");
        map.put("content","4. 매뉴얼 등록");

        manual.add(map);

        RequestRecipeManualDTO requestRecipeManualDTO = RequestRecipeManualDTO
                .builder()
                .manual(manual)
                .build();

        // When
        List<RecipeManualDTO> recipeManualDTOList =
                recipeManualService.registRecipeManual(recipeId, requestRecipeManualDTO);

        // Then
        Assertions.assertNotNull(recipeManualDTOList, "매뉴얼이 null 입니다.");

        // 로그 찍기
        recipeManualDTOList.forEach(x -> log.info(x.toString()));
    }

    @DisplayName("매뉴얼 수정 테스트")
    @Test
    void testModifyRecipeManual() {
        // Given
        Long recipeId = 1L;

        List<Map<String,String>> manual = new ArrayList<>();

        Map<String,String> map = new HashMap<>();

        map.put("image","이미지");
        map.put("content","1. 매뉴얼 등록");

        manual.add(map);

        RequestRecipeManualDTO requestRecipeManualDTO = RequestRecipeManualDTO
                .builder()
                .manual(manual)
                .build();

        // When
        List<RecipeManualDTO> recipeManualDTOList =
                recipeManualService.modifyRecipeManual(recipeId, requestRecipeManualDTO);

        // Then
        Assertions.assertNotNull(recipeManualDTOList, "매뉴얼이 null 입니다.");

        // 로그 찍기
        recipeManualDTOList.forEach(x -> log.info(x.toString()));
    }
}
