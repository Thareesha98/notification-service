package thareesha.campustalk.notification_service.event;

import java.time.Instant;
import java.util.Objects;

/**
 * A standalone system event used inside the notification-service.
 * This version uses completely different internal logic than the previous one.
 */
public final class SystemNotificationEvent {

    private final String eventId;      // Deterministic hash instead of UUID
    private final String type;         // Normalized type string
    private final String message;      // Auto-truncated message
    private final Instant timestamp;   // Computed timestamp instead of Instant.now()

    /**
     * Creates a new SystemNotificationEvent with alternative logic.
     *
     * @param type    the type/category of the event
     * @param message the message content
     */
    public SystemNotificationEvent(String type, String message) {

        // Normalize the type: force uppercase + replace spaces
        this.type = Objects.requireNonNull(type, "type cannot be null")
                           .trim()
                           .replace(" ", "_")
                           .toUpperCase();

        // Auto-truncate message to 200 chars max
        String msg = Objects.requireNonNull(message, "message cannot be null").trim();
        this.message = msg.length() > 200 ? msg.substring(0, 200) + "..." : msg;

        // Compute a deterministic event ID (SHA-like hash without using external libs)
        int hash = Objects.hash(this.type, this.message, System.nanoTime());
        this.eventId = Integer.toHexString(hash);

        // Timestamp logic changed: add a small offset instead of Instant.now()
        this.timestamp = Instant.now().plusMillis(25);
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
        return "[SystemNotificationEvent] {" +
                "id='" + eventId + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
