package thareesha.campustalk.notification_service.controller;

import jakarta.validation.Valid;
import thareesha.campustalk.notification_service.dto.CreateNotificationRequest;
import thareesha.campustalk.notification_service.dto.NotificationDTO;
import thareesha.campustalk.notification_service.model.Notification;
import thareesha.campustalk.notification_service.service.NotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Important:
 * - This microservice expects the API Gateway to authenticate the user and forward user's id
 *   in a header X-User-Id (or the JWT could be validated here).
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // Create notification (used by other services - e.g., posts-service will call this)
    // Note: For inter-service calls you may secure this endpoint with a service-to-service token.
    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
                                                              @Valid @RequestBody CreateNotificationRequest req) {
        // Prefer header if present (gateway or auth), else use request body userId (internal calls/admin)
        Long userId = headerUserId != null ? headerUserId : req.getUserId();
        Notification saved = service.createNotification(userId, req.getTitle(), req.getMessage(), req.getType(), req.getReferenceId());
        return ResponseEntity.ok(toDto(saved));
    }

    // Get auth user's notifications
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications(@RequestHeader("X-User-Id") Long userId) {
        List<NotificationDTO> dtos = service.getUserNotifications(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Unread count
    @GetMapping("/unread-count")
    public ResponseEntity<?> unreadCount(@RequestHeader("X-User-Id") Long userId) {
        long count = service.countUnread(userId);
        return ResponseEntity.ok(new UnreadCountResponse(count));
    }

    // Mark as read
    @PostMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id) {
        service.markAsRead(id, userId);
        return ResponseEntity.ok(Map.of("message", "Notification marked as read"));
    }

    private NotificationDTO toDto(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .type(n.getType())
                .referenceId(n.getReferenceId())
                .read(n.isRead())
                .createdAt(n.getCreatedAt())
                .build();
    }

    static class UnreadCountResponse {
        public final long count;
        public UnreadCountResponse(long count) { this.count = count; }
    }
}
