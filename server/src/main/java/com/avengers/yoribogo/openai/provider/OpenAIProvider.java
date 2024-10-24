package com.avengers.yoribogo.openai.provider;

import com.avengers.yoribogo.openai.dto.ResponseChatFluxDTO;
import com.avengers.yoribogo.openai.dto.RequestChatFluxDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@NoArgsConstructor
@Component
public class OpenAIProvider {

    @Value("${openai.secret-key}")
    private String secretKey;

    private WebClient webClient;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE );

    @PostConstruct
    public void init() {
        var client = HttpClient.create().responseTimeout(Duration.ofSeconds(45));

        webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + secretKey)
                .build();
    }

    public Flux<String> ask(RequestChatFluxDTO request) throws JsonProcessingException {
        String requestValue = objectMapper.writeValueAsString(request);
        return webClient.post()
                .bodyValue(requestValue)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(ResponseChatFluxDTO.class)
                .onErrorResume(error -> {
                    if (error.getMessage().contains("JsonToken.START_ARRAY")) {
                        return Flux.empty();
                    }
                    else {
                        return Flux.error(error);
                    }
                })
                .filter(response -> {
                    var content = response.getChoices().get(0).getDelta().getContent();
                    return content != null && !content.equals("\n\n");
                })
                .map(response -> response.getChoices().get(0).getDelta().getContent());
    }
}
