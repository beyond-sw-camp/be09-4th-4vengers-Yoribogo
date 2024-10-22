package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.openai.service.OpenAIService;
import com.avengers.yoribogo.recipe.domain.RecipeManual;
import com.avengers.yoribogo.recipe.dto.RecipeManualDTO;
import com.avengers.yoribogo.recipe.dto.RequestRecipeManualDTO;
import com.avengers.yoribogo.recipe.repository.RecipeManualRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class RecipeManualServiceImpl implements RecipeManualService {

    private final ModelMapper modelMapper;
    private final RecipeManualRepository recipeManualRepository;
    private final OpenAIService openAIService;

    @Autowired
    public RecipeManualServiceImpl(ModelMapper modelMapper,
                                   RecipeManualRepository recipeManualRepository,
                                   OpenAIService openAIService) {
        this.modelMapper = modelMapper;
        this.recipeManualRepository = recipeManualRepository;
        this.openAIService = openAIService;
    }

    // 요리 레시피 아이디로 매뉴얼 조회
    @Override
    public List<RecipeManualDTO> findRecipeManualByRecipeId(Long recipeId) {
        // 기존 엔티티 목록 조회
        List<RecipeManual> recipeManualList = recipeManualRepository.findByRecipeId(recipeId);

        // 조회된게 없을 경우 예외처리
        if (recipeManualList.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_MANUAL);
        }

        // RecipeManual -> RecipeManualDTO 변환 및 List 반환
        return convertEntityToDTO(recipeManualList);
    }

    // 요리 레시피 매뉴얼 등록
    @Override
    @Transactional
    public List<RecipeManualDTO> registRecipeManual(Long recipeId, RequestRecipeManualDTO requestRecipeManualDTO) {
        List<RecipeManual> recipeManualList = new ArrayList<>();

        List<Map<String,String>> manual = requestRecipeManualDTO.getManual();
        for(int i = 0; i < manual.size(); i++) {
            // 매뉴얼 생성
            RecipeManualDTO newRecipeManualDTO = RecipeManualDTO
                    .builder()
                    .recipeManualStep(i+1)
                    .manualMenuImage(manual.get(i).get("image"))
                    .manualContent(manual.get(i).get("content"))
                    .recipeId(recipeId)
                    .build();

            // 매뉴얼 등록
            recipeManualList.add(recipeManualRepository.save(modelMapper.map(newRecipeManualDTO, RecipeManual.class)));
        }

        // RecipeManual -> RecipeManualDTO 변환 및 List 반환
        return convertEntityToDTO(recipeManualList);
    }

    // 요리 레시피 매뉴얼 수정
    @Override
    @Transactional
    public List<RecipeManualDTO> modifyRecipeManual(Long recipeId, RequestRecipeManualDTO requestRecipeManualDTO) {
        // 기존 엔티티 목록 조회
        List<RecipeManual> recipeManualList =
                recipeManualRepository.findByRecipeId(recipeId);

        // 조회된게 없을 경우 예외처리
        if (recipeManualList.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_MANUAL);
        }

        // 기존 엔티티 삭제
        recipeManualRepository.deleteAll(recipeManualList);

        // 바뀐 매뉴얼 등록
        return registRecipeManual(recipeId, requestRecipeManualDTO);
    }

    // AI 생성 매뉴얼 등록
    @Async
    @Override
    public void registAIRecipeManual(Long recipeId, String recipePrompt) {
        String aiAnswerRecipe = openAIService.getRecommend(recipePrompt).getChoices().get(0).getMessage().getContent();
        log.info(aiAnswerRecipe);

        // ':'가 있는 경우, ':' 이후의 문자열만 남기기
        aiAnswerRecipe = parseString(aiAnswerRecipe);

        // AI가 생성한 요리 레시피 매뉴얼 등록
        List<Map<String, String>> manual = new ArrayList<>();

        List<String> contents = Arrays.stream(aiAnswerRecipe.split("\n")).toList();
        for (String content : contents) {
            Map<String, String> map = new HashMap<>();
            map.put("content", content);
            manual.add(map);
        }

        for(int i = 0; i < manual.size(); i++) {
            // 매뉴얼 생성
            RecipeManualDTO newRecipeManualDTO = RecipeManualDTO
                    .builder()
                    .recipeManualStep(i + 1)
                    .manualMenuImage(manual.get(i).get("image"))
                    .manualContent(manual.get(i).get("content"))
                    .recipeId(recipeId)
                    .build();

            // 매뉴얼 등록
            recipeManualRepository.save(modelMapper.map(newRecipeManualDTO, RecipeManual.class));
        }
    }

    // RecipeManual -> RecipeManualDTO 변환 및 List 반환 메소드
    private List<RecipeManualDTO> convertEntityToDTO(List<RecipeManual> recipeManualList) {
        return recipeManualList.stream()
                .map(recipeManual -> modelMapper.map(recipeManual, RecipeManualDTO.class))
                .toList();
    }

    // ':'가 있는 경우, ':' 이후의 문자열만 남기는 메소드
    private String parseString(String aiAnswer) {
        int colonIndex = aiAnswer.indexOf(":");
        if (colonIndex != -1) {
            aiAnswer = aiAnswer.substring(colonIndex + 1).trim();
        }
        return aiAnswer;
    }

}
