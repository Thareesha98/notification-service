package thareesha.campustalk.notification_service.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A lightweight, fully standalone event object used inside the
 * notification-service. This class has ZERO dependencies on any other
 * project files and can safely be used for logging or basic event passing.
 *
 * It does not depend on Spring, Lombok, or any domain model classes.
 */
public final class NotificationEvent {

    private final String eventId;
    private final String type;
    private final String message;
    private final Instant timestamp;

    /**
     * Creates a new NotificationEvent.
     *
     * @param type    - A short label for the event (e.g., "USER_REGISTERED")
     * @param message - Human readable message body
     */
    public NotificationEvent(String type, String message) {
        this.eventId = UUID.randomUUID().toString();
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.message = Objects.requireNonNull(message, "message cannot be null");
        this.timestamp = Instant.now();
    }

    public String getEventId() {
        return eventId;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "NotificationEvent{" +
                "eventId='" + eventId + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

