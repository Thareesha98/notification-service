package thareesha.campustalk.notification_service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotificationRequest {
    @NotNull
    private Long userId;           // optional: allow server to override if using header
    private String title;
    @NotBlank
    private String message;
    private String type;
    private Long referenceId;
}
