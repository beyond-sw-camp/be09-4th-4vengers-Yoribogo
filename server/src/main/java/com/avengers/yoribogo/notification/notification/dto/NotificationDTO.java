package com.avengers.yoribogo.notification.notification.dto;

import com.avengers.yoribogo.notification.notification.domain.NotificationEntity;
import com.avengers.yoribogo.notification.notification.domain.NotificationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long notificationId;
    private String notificationContent;
    private NotificationStatus notificationStatus;
    private LocalDateTime notificationCreatedAt;
    private LocalDateTime notificationReadAt;

    // Entity To DTO 메소드
    public static NotificationDTO fromEntity(NotificationEntity entity) {
        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationId(entity.getNotificationId());
        dto.setNotificationContent(entity.getNotificationContent());
        dto.setNotificationStatus(entity.getNotificationStatus());
        dto.setNotificationCreatedAt(entity.getNotificationCreatedAt());
        dto.setNotificationReadAt(entity.getNotificationReadAt());
        return dto;
    }
}