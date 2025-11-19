package thareesha.campustalk.notification_service.service;

import java.time.Instant;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import thareesha.campustalk.notification_service.model.Notification;
import thareesha.campustalk.notification_service.repository.NotificationRepository;

@Service
public class NotificationService {
	private final NotificationRepository repo;
    private final ApplicationEventPublisher eventPublisher; // lightweight internal event publishing

    public NotificationService(NotificationRepository repo, ApplicationEventPublisher eventPublisher) {
        this.repo = repo;
        this.eventPublisher = eventPublisher;
    }
    
    @Transactional 
    public Notification createNotification(Long userId, String title, String message, String type, Long refId) {
    	Notification n = Notification.builder()
                .userId(userId)
                .title(title)
                .message(message)
                .type(type)
                .referenceId(refId)
                .read(false)
                .createdAt(Instant.now())
                .build();
    	
    	Notification saved = repo.save(n);

        eventPublisher.publishEvent(new NotificationCreatedEvent(this, saved));
        return saved;
    }
    
    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    @Transactional(readOnly = true)
    public long countUnread(Long userId) {
        return repo.countByUserIdAndReadFalse(userId);
    }
    
    @Transactional
    public void markAsRead(Long id, Long userId) {
        Notification n = repo.findById(id)
                .filter(notif -> notif.getUserId().equals(userId))
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));

        if(!n.isRead()){
            n.setRead(true);
            repo.save(n);
        }
    }
}
