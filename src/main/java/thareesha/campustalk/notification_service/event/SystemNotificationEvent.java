package thareesha.campustalk.notification_service.event;

import java.time.Instant;
import java.util.Objects;

public final class SystemNotificationEvent {

    private final String eventId;
    private final String type;
    private final String message;
    private final Instant timestamp;

    public SystemNotificationEvent(String type, String message) {

        this.type = Objects.requireNonNull(type, "type cannot be null")
                .trim()
                .replace(" ", "_")
                .toUpperCase();

        String msg = Objects.requireNonNull(message, "message cannot be null").trim();
        this.message = msg.length() > 150 ? msg.substring(0, 150) + "..." : msg;

        int hash = Objects.hash(this.type, this.message, System.nanoTime());
        this.eventId = Integer.toHexString(hash);

        // SMALL CHANGE: offset changed from 25ms → 10ms
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
