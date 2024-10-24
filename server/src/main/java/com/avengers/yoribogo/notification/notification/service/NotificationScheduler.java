package com.avengers.yoribogo.notification.notification.service;

import com.avengers.yoribogo.notification.weeklypopularrecipe.service.WeeklyPopularRecipeService;
import com.avengers.yoribogo.notification.notification.repository.NotificationRepository;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationScheduler {

    private final WeeklyPopularRecipeService weeklyPopularRecipeService;
    private final RecipeBoardRepository recipeBoardRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    public NotificationScheduler(WeeklyPopularRecipeService weeklyPopularRecipeService, RecipeBoardRepository recipeBoardRepository, NotificationRepository notificationRepository, NotificationService notificationService) {
        this.weeklyPopularRecipeService = weeklyPopularRecipeService;
        this.recipeBoardRepository = recipeBoardRepository;
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
    }

    // 11시에 실행되는 스케줄러 ( 알림 저장 )
    @Scheduled(cron = "0 0 11 * * ?")
//    @Scheduled(fixedRate = 100000)
    public void saveLunchRecipeNotification() {
        notificationService.saveRecipeNotification("lunch");

    }

    // 17시에 실행되는 스케줄러 ( 알림 저장 )
    @Scheduled(cron = "0 0 17 * * ?")
    public void saveDinnerRecipeNotification() {
        notificationService.saveRecipeNotification("dinner");
    }



}
