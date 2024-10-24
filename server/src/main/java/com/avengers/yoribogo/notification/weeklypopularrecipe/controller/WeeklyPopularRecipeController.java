package com.avengers.yoribogo.notification.weeklypopularrecipe.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.WeeklyPopularRecipeEntity;
import com.avengers.yoribogo.notification.weeklypopularrecipe.service.WeeklyPopularRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weeklypopularrecipes")
public class WeeklyPopularRecipeController {

    @Autowired
    private final WeeklyPopularRecipeService weeklyPopularRecipeService;

    public WeeklyPopularRecipeController(WeeklyPopularRecipeService weeklyPopularRecipeService) {
        this.weeklyPopularRecipeService = weeklyPopularRecipeService;
    }

    // MongoDB 연결 테스트용 API
    @GetMapping("/findall")
    public ResponseEntity<ResponseDTO<List<WeeklyPopularRecipeEntity>>> getAllWeeklyPopularRecipes() {
        List<WeeklyPopularRecipeEntity> recipes = weeklyPopularRecipeService.getAllWeeklyPopularRecipes();
        return ResponseEntity.ok(ResponseDTO.ok(recipes));
    }

    // MongoDB에서 가장 많은 좋아요를 가진 레시피 추출하는 API (추후에 일주일 타임 걸어두는 로직 추가 필요)
    @GetMapping("/mostliked")
    public ResponseEntity<ResponseDTO<WeeklyPopularRecipeEntity>> getMostLikedRecipe() {
        WeeklyPopularRecipeEntity recipe = weeklyPopularRecipeService.getRandomTopLikedRecipe();
        return ResponseEntity.ok(ResponseDTO.ok(recipe));
    }

    // 상위 3개의 좋아요 레시피 가져오기 (일주일 내)
    @GetMapping("/top3liked")
    public ResponseEntity<ResponseDTO<List<WeeklyPopularRecipeEntity>>> getTop3LikedRecipes() {
        List<WeeklyPopularRecipeEntity> top3Recipes = weeklyPopularRecipeService.getTop3LikedRecipes();
        return ResponseEntity.ok(ResponseDTO.ok(top3Recipes));
    }
}