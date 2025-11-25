package thareesha.campustalk.notification_service.controller;

import jakarta.validation.Valid;
import thareesha.campustalk.notification_service.dto.NotificationDTO;
import thareesha.campustalk.notification_service.model.Notification;
import thareesha.campustalk.notification_service.service.NotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(
            @RequestHeader(value = "X-User-Id", required = false) Long callerUserId, 
            @RequestBody Map<String, Object> body
    ) {
        if (callerUserId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Missing userId header"));
        }

        Long userId = ((Number) body.get("userId")).longValue();
        String title = (String) body.get("title");
        String message = (String) body.get("message");
        String type = (String) body.get("type");
        Long referenceId = body.get("referenceId") == null ? null :
                ((Number) body.get("referenceId")).longValue();

        NotificationDTO dto = notificationService.create(
                userId, title, message, type, referenceId
        );

        return ResponseEntity.ok(dto);
    }

    // LIST
    @GetMapping
    public ResponseEntity<?> list(
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Missing userId header"));
        }

        return ResponseEntity.ok(notificationService.listForUser(userId));
    }

    // UNREAD COUNT
    @GetMapping("/unread-count")
    public ResponseEntity<?> unreadCount(
        @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Missing userId header"));
        }

        return ResponseEntity.ok(
            Map.of("count", notificationService.unreadCount(userId))
        );
    }

    // MARK READ
    @PostMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @PathVariable Long id
    ) {
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Missing userId header"));
        }

        notificationService.markAsRead(id, userId);
        return ResponseEntity.ok(Map.of("message", "Notification marked as read"));
    }
}
