package thareesha.campustalk.notification_service.service;

import java.time.Instant;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import thareesha.campustalk.notification_service.dto.NotificationDTO;
import thareesha.campustalk.notification_service.model.Notification;
import thareesha.campustalk.notification_service.repository.NotificationRepository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.thareesha.campustalk.notification.config.RabbitMQConfig.NOTIFICATION_EXCHANGE;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

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

        // Prepare DTO for async push
        NotificationDTO dto = NotificationDTO.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .message(saved.getMessage())
                .type(saved.getType())
                .referenceId(saved.getReferenceId())
                .read(saved.isRead())
                .createdAt(saved.getCreatedAt())
                .build();

        try {
            String json = objectMapper.writeValueAsString(dto);
            rabbitTemplate.convertAndSend(
                    NOTIFICATION_EXCHANGE,
                    "user." + userId,      // routing key
                    json
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to send notification event", e);
        }

        return saved;
    }
}

