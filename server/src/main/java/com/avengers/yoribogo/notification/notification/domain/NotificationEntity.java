package com.avengers.yoribogo.notification.notification.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "notification_content", nullable = false)
    private String notificationContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_read_status", nullable = false)
    private NotificationStatus notificationStatus = NotificationStatus.UNREAD;

    @Column(name = "notification_created_at", nullable = false)
    private LocalDateTime notificationCreatedAt;

    @Column(name = "notification_read_at")
    private LocalDateTime notificationReadAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
