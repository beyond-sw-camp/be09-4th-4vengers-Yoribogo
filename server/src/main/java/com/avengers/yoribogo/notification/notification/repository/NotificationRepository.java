package com.avengers.yoribogo.notification.notification.repository;

import com.avengers.yoribogo.notification.notification.domain.NotificationEntity;
import com.avengers.yoribogo.notification.notification.domain.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    // 특정 사용자의 DELETED 상태가 아닌 알림을 조회하는 메서드
    List<NotificationEntity> findByUserIdAndNotificationStatusNot(Long userId, NotificationStatus notificationStatus);
}
