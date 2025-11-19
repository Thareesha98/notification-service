package thareesha.campustalk.notification_service.dto;


import lombok.*;
import java.time.Instant;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

    private Long id;
    private String title;
    private String message;
    private String type;
    private Long referenceId;
    private boolean read;
    private LocalDateTime createdAt;
}
