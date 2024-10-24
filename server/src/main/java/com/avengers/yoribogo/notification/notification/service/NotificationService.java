package com.avengers.yoribogo.notification.notification.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.notification.notification.dto.NotificationDTO;
import com.avengers.yoribogo.notification.notification.domain.NotificationEntity;
import com.avengers.yoribogo.notification.notification.domain.NotificationStatus;
import com.avengers.yoribogo.notification.notification.repository.NotificationRepository;
import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.WeeklyPopularRecipeEntity;
import com.avengers.yoribogo.notification.weeklypopularrecipe.repository.WeeklyPopularRecipeMongoRepository;
import com.avengers.yoribogo.notification.weeklypopularrecipe.service.WeeklyPopularRecipeService;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoard;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardRepository;
import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RecipeBoardRepository recipeBoardRepository;
    private final WeeklyPopularRecipeService weeklyPopularRecipeService;
    private final WeeklyPopularRecipeMongoRepository weeklyPopularRecipeMongoRepository;

    public NotificationService(WeeklyPopularRecipeService weeklyPopularRecipeService, WeeklyPopularRecipeMongoRepository weeklyPopularRecipeMongoRepository, RecipeBoardRepository recipeBoardRepository, UserRepository userRepository, NotificationRepository notificationRepository) {
        this.weeklyPopularRecipeService = weeklyPopularRecipeService;
        this.weeklyPopularRecipeMongoRepository = weeklyPopularRecipeMongoRepository;
        this.recipeBoardRepository = recipeBoardRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    // SSE 연결 로직
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((ex) -> emitters.remove(emitter));

        // 연결 시에 최초 메시지 전송
        try {
            emitter.send(SseEmitter.event().name("connect").data("Connected!"));
            log.info("(Service)SSE 최초 연결 및 메세지 전송 완료!!");
        } catch (IOException e) {
            /*
            Web 새로고침 시 SSE 연결된 객체는 죽고 다시 재연결 되기 때문에
            연결된 객체에게 한 번 메세지를 보내려고 하여
            "현재 연결은 사용자의 호스트 시스템의 소프트웨어의 의해 중단되었습니다"
            문구가 콘솔에 뜹니다.
            */
            emitter.completeWithError(e);

        }
        return emitter;
    }

    // 요리 위키 이미지 업데이트 알림을 전송
    public void sendImageUpdateNotification(String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("image-update").data(message));
            } catch (IOException e) {
                deadEmitters.add(emitter);  // 전송 실패한 emitter를 제거 목록에 추가
            }
        }
        // 실패한 emitter 제거
        emitters.removeAll(deadEmitters);
    }

    // 알림 저장 테스트용 API
    public NotificationEntity createNotification() {
        NotificationEntity notification = new NotificationEntity();

        // 여기에서 필요한 데이터를 직접 설정
        notification.setUserId(2L);  // 예시로 userId 1L 설정
        notification.setNotificationContent("테스트 알림 내용");
        notification.setNotificationCreatedAt(LocalDateTime.now());
        notification.setNotificationStatus(NotificationStatus.UNREAD);  // 기본 상태로 설정

        return notificationRepository.save(notification);
    }


    public List<NotificationDTO> sendNotificationsToUser(Long userId) {
        // 1. 해당 사용자의 알림 목록 조회 (DELETED 되지 않은 것들만)
        List<NotificationEntity> userNotifications = notificationRepository.findByUserIdAndNotificationStatusNot(userId, NotificationStatus.DELETED);

        // Entity를 DTO로 변환
        List<NotificationDTO> notificationDTOs = userNotifications
                .stream()
                .map(NotificationDTO::fromEntity)  // DTO로 변환
                .collect(Collectors.toList());

        // 2. 알림 데이터를 SSE로 전송
        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                for (NotificationDTO notification : notificationDTOs) {
                    // 알림 객체 전체를 JSON 형식으로 전송
                    emitter.send(SseEmitter.event().name("notification").data(notification));
                }
            } catch (IOException e) {
                deadEmitters.add(emitter);  // 실패한 emitter 목록에 추가
            }
        }

        // 실패한 emitters 제거
        emitters.removeAll(deadEmitters);

        // 전송된 알림 DTO 목록 반환
        return notificationDTOs;
    }

    // 알림 상태 업데이트 메서드
    public void updateNotificationStatus(Long notificationId, NotificationStatus status) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_NOTIFICATION));

        // 이미 Enum 타입으로 전달된 상태를 바로 설정
        notification.setNotificationStatus(status);

        if (status == NotificationStatus.READ) {
            notification.setNotificationReadAt(LocalDateTime.now());
        }

        notificationRepository.save(notification);
    }

    // 레시피 알림 저장 로직
    public void saveRecipeNotification(String mealType) {
        // 상위 3개의 레시피 가져오기
        List<WeeklyPopularRecipeEntity> top3Recipes = weeklyPopularRecipeService.getTop3LikedRecipes();

        if (top3Recipes.isEmpty()) {
            log.info("(Service) Top3 레시피 조회에 실패하였습니다");
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE);
        }

        // 랜덤으로 하나 선택
        WeeklyPopularRecipeEntity selectedRecipe = top3Recipes.get(new Random().nextInt(top3Recipes.size()));

        // 선택된 레시피의 객체 조회
        RecipeBoard recipe = recipeBoardRepository.findById(Long.parseLong(selectedRecipe.getMyRecipeId()))
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE));

        // 모든 회원 조회
        List<UserEntity> users = userRepository.findAll();

        if (users.isEmpty()) {
            log.info("(Service) 알림 생성에 필요한 회원 조회에 실패하였습니다.");
            new CommonException(ErrorCode.NOT_FOUND_USER);
            return;
        }

        // 각 회원별로 알림 생성 및 저장
        for (UserEntity user : users) {
            // 알림 메시지 설정 (점심/저녁에 따른 메시지 변경)
            String notificationContent;
            if ("lunch".equals(mealType)) {
                notificationContent = " [점심 추천 레시피] 오늘의 추천 레시피: " + recipe.getRecipeBoardMenuName();
            } else {
                notificationContent = " [저녁 추천 레시피] 오늘의 추천 레시피: " + recipe.getRecipeBoardMenuName();
            }

            // 알림 저장 (DB에 저장)
            NotificationEntity notification = new NotificationEntity();
            notification.setUserId(user.getUserId()); // 회원 ID 설정
            notification.setNotificationContent(notificationContent);
            notification.setNotificationCreatedAt(LocalDateTime.now());
            notification.setNotificationStatus(NotificationStatus.UNREAD); // 기본값 Unread 설정
            notificationRepository.save(notification);

            log.info("회원 {}에게 {} 알림이 저장되었습니다: {}", user.getUserName(), mealType, notificationContent);
        }
    }

    }
