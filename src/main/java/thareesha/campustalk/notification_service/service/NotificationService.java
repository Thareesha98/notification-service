package thareesha.campustalk.notification_service.service;

import java.time.Instant;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import thareesha.campustalk.notification_service.config.RabbitMQConfig;
import thareesha.campustalk.notification_service.dto.NotificationDTO;
import thareesha.campustalk.notification_service.event.NotificationEvent;
import thareesha.campustalk.notification_service.model.Notification;
import thareesha.campustalk.notification_service.repository.NotificationRepository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static thareesha.campustalk.notification_service.config.RabbitMQConfig.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final RabbitTemplate rabbitTemplate;
    private String c;// <-- use ONLY thissss

    // 🔥 CREATE + PUBLISH EVENT
    public NotificationDTO create(
            Long userId,
            String title,
            String message,
            String type,
            Long referenceId
    ) {
        Notification n = Notification.builder()
                .userId(userId)
                .message(message)
                .type(type)
                .referenceId(referenceId)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(n);

        NotificationDTO dto = NotificationDTO.builder()
                .id(n.getId())
                .title(title)
                .message(n.getMessage())
                .type(n.getType())
                .referenceId(n.getReferenceId())
                .read(n.isRead())
                .createdAt(n.getCreatedAt())
                .build();

        // 🔥 Publish RabbitMQ event
        NotificationEvent event = NotificationEvent.builder()
                .id(n.getId())
                .userId(userId)
                .title(title)
                .message(message)
                .type(type)
                .referenceId(referenceId)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );

        return dto;
    }

    public List<Notification> listForUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public long unreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    public void markAsRead(Long id, Long userId) {
        Notification n = notificationRepository.findById(id)
                .filter(no -> no.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Notification not found / not owned"));

        n.setRead(true);
        notificationRepository.save(n);
    }
}


