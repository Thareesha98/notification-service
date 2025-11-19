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


import lombok.RequiredArgsConstructor;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Gateway must pass X-User-Id
    private Long getUserIdFromGateway(@RequestHeader("X-User-Id") Long userId) {
        return userId;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<NotificationDTO> create(
            @RequestHeader("X-User-Id") Long callerUserId, // from gateway
            @RequestBody Map<String, Object> body
    ) {
        Long userId = ((Number) body.get("userId")).longValue();
        String title = (String) body.get("title");
        String message = (String) body.get("message");
        String type = (String) body.get("type");
        Long referenceId = body.get("referenceId") == null ? null : ((Number) body.get("referenceId")).longValue();

        NotificationDTO dto = notificationService.create(
                userId, title, message, type, referenceId
        );

        return ResponseEntity.ok(dto);
    }

    // LIST
    @GetMapping
    public ResponseEntity<List<Notification>> list(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.ok(notificationService.listForUser(userId));
    }

    // UNREAD COUNT
    @GetMapping("/unread-count")
    public ResponseEntity<?> unreadCount(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.ok(Map.of("count", notificationService.unreadCount(userId)));
    }

    // MARK READ
    @PostMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id
    ) {
        notificationService.markAsRead(id, userId);
        return ResponseEntity.ok(Map.of("message", "Notification marked as read"));
    }
}

