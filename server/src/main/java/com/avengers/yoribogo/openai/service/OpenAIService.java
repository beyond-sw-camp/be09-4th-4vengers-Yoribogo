package com.avengers.yoribogo.openai.service;

import com.avengers.yoribogo.openai.dto.ResponseChatDTO;
import com.avengers.yoribogo.openai.dto.ResponseImagesDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Flux;

public interface OpenAIService {

    ResponseChatDTO getRecommend(String prompt);

    ResponseImagesDTO getImages(String prompt);

    Flux<String> getRecommendManuals(String prompt) throws JsonProcessingException;

}
