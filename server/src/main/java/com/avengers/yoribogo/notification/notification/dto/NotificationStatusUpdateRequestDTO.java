package com.avengers.yoribogo.notification.notification.dto;

import com.avengers.yoribogo.notification.notification.domain.NotificationStatus;
import lombok.Data;

@Data
public class NotificationStatusUpdateRequestDTO {
    private NotificationStatus notificationStatus;
}