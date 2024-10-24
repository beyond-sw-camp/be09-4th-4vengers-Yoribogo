package com.avengers.yoribogo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 최소 유지 스레드 수
        executor.setMaxPoolSize(50);  // 최대 스레드 수
        executor.setQueueCapacity(100); // 큐 크기
        executor.setThreadNamePrefix("AsyncExecutor-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;  // 이 부분에서 AsyncTaskExecutor 타입으로 반환
    }
}
