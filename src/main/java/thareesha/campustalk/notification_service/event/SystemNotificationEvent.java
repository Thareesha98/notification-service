package thareesha.campustalk.notification_service.event;

import java.time.Instant;
import java.util.Objects;

public final class SystemNotificationEvent {

    private final String eventId;
    private final String type;
    private final String message;
    private final Instant timestamp;

    public SystemNotificationEvent(String type, String message) {

        String normalizedType = Objects.requireNonNull(type, "type cannot be null")
                .trim()
                .replace(" ", "_")
                .toUpperCase();

        this.type = normalizedType;

        String msg = Objects.requireNonNull(message, "message cannot be null").trim();
        this.message = msg.lenth() > 150 ? msg.substring(0, 150) + "..." : msg;

        // SMALL CHANGE: ensure positive hash before converting to hex
        int hash = Math.abs(Objects.hash(this.type, this.message, System.nanoTime()));
        this.eventId = Integer.toHexString(hash);

        this.timestamp = Instant.now().plusMillis(10);
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
