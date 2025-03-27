package com.pratikesh.linkedin.notification_service.consumer;

import com.pratikesh.linkedin.notification_service.clients.ConnectionsClient;
import com.pratikesh.linkedin.notification_service.dto.PersonDto;
import com.pratikesh.linkedin.notification_service.entity.Notification;
import com.pratikesh.linkedin.notification_service.repository.NotificationRepo;
import com.pratikesh.linkedin.notification_service.service.SendNotification;
import com.pratikesh.linkedin.posts_service.event.PostCreatedEvent;
import com.pratikesh.linkedin.posts_service.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {

    private final ConnectionsClient connectionsClient;
    private final SendNotification sendNotifications;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent){
        log.info("Sending notifications for the handlePostCreated:{}",postCreatedEvent);
        List<PersonDto> connections = connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());

        for (PersonDto connection: connections){
            sendNotifications.send(connection.getUserId(), "Your connection "+postCreatedEvent.getCreatorId()+" has created a post.");
        }
    }
    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent){
        log.info("Sending notifications for the handlePostLiked:{}", postLikedEvent);
        String message = String.format("%d has liked your post with post id %d", postLikedEvent.getLikedByUserId(), postLikedEvent.getPostId());
        sendNotifications.send(postLikedEvent.getCreatorId(), message);
    }
}
