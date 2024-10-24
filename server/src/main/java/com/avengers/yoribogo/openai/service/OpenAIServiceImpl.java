package com.avengers.yoribogo.openai.service;

import com.avengers.yoribogo.openai.dto.*;
import com.avengers.yoribogo.openai.provider.OpenAIProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    @Value("${openai.api.images-url}")
    private String imagesUrl;

    private final RestTemplate restTemplate;
    private final OpenAIProvider openAIProvider;

    // 설명. OpenAI와 통신을 위한 RestTemplate의 빈 이름으로 설정
    //  (시큐리티 RestTemplate은 AppConfiguration에 설정되어있음)
    @Autowired
    public OpenAIServiceImpl(@Qualifier("openAIRestTemplate") RestTemplate restTemplate,
                             OpenAIProvider openAIProvider) {
        this.restTemplate = restTemplate;
        this.openAIProvider = openAIProvider;
    }

    @Override
    public ResponseChatDTO getRecommend(String prompt) {
        RequestChatDTO req = new RequestChatDTO(model, prompt);
        return restTemplate.postForObject(url, req, ResponseChatDTO.class);
    }

    @Override
    public ResponseImagesDTO getImages(String prompt) {
        Integer numberOfImages = 1;
        String size = "512x512";
        RequestImagesDTO req = new RequestImagesDTO(prompt, size, numberOfImages);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");    // Content-Type 지정

        // 요청 본문과 헤더를 포함한 HttpEntity 생성
        HttpEntity<RequestImagesDTO> entity = new HttpEntity<>(req, headers);

        // API 호출
        ResponseEntity<ResponseImagesDTO> response = restTemplate.postForEntity(imagesUrl, entity, ResponseImagesDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();  // 응답 본문 반환
        } else {
            throw new RuntimeException("Failed to generate image: " + response.getStatusCode());
        }
    }

    @Override
    public Flux<String> getRecommendManuals(String prompt) throws JsonProcessingException {
        RequestChatFluxDTO req = new RequestChatFluxDTO(model, prompt, true);
        return openAIProvider.ask(req);
    }

}
