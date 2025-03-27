package com.pratikesh.linkedin.notification_service.service;

import com.pratikesh.linkedin.notification_service.entity.Notification;
import com.pratikesh.linkedin.notification_service.repository.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendNotification {

    private final NotificationRepo notificationRepo;
    public void send(Long userId, String message){
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUserId(userId);
        notificationRepo.save(notification);
    }
}
