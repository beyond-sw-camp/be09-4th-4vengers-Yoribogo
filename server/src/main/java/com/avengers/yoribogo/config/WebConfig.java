package com.avengers.yoribogo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:5173")
//                .allowedOrigins("http://localhost:30000")
                /* 설명. ingress 적용 이후 CORS 불필요로 인한 경로 제거*/
                .allowedOrigins()
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH")
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 쿠키, 인증 정보 허용
                .maxAge(3600); // Preflight 요청 캐싱 시간 설정
    }
}