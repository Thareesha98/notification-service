package thareesha.campustalk.notification_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import thareesha.campustalk.notification_service.model.Notification;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndReadFalse(Long userId);
}
