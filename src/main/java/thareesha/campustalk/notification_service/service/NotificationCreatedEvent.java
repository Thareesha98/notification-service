package thareesha.campustalk.notification_service.service;


import org.springframework.context.ApplicationEvent;

import thareesha.campustalk.notification_service.model.Notification;

public class NotificationCreatedEvent extends ApplicationEvent {
    private final Notification notification;

    public NotificationCreatedEvent(Object source, Notification notification) {
        super(source);
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }
}
