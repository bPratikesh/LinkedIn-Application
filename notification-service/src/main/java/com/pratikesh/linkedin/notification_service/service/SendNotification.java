package com.pratikesh.linkedin.notification_service.service;

import com.pratikesh.linkedin.notification_service.entity.Notification;
import com.pratikesh.linkedin.notification_service.repository.NotificationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendNotification {

    private final NotificationRepo notificationRepo;
    public void send(Long userId, String message){
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUserId(userId);
        notificationRepo.save(notification);
        log.info("Notifications saved for the User: {}", userId);
    }
}
