package thareesha.campustalk.notification_service.repository;


import thareesha.campustalk.notification_service.model.Notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndReadFalse(Long userId);
}
