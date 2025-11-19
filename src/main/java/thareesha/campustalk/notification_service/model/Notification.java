package thareesha.campustalk.notification_service.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // owner of this notification (user id from Auth/Identity service)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    private String type; // POST, EVENT, LIKE, COMMENT, SYSTEM

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
