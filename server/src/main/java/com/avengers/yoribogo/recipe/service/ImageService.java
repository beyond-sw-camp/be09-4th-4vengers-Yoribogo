package com.avengers.yoribogo.recipe.service;

public interface ImageService {

    // AI 이미지 생성
    void generateImageAsync(String trimmedDescription, Long recipeId);

}
