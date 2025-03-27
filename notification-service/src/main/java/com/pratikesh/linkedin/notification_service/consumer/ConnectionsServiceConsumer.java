package com.pratikesh.linkedin.notification_service.consumer;

import com.pratikesh.linkedin.connection_service.event.AcceptConnectionRequestEvent;
import com.pratikesh.linkedin.connection_service.event.SendConnectionRequestEvent;
import com.pratikesh.linkedin.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsServiceConsumer {

    private final SendNotification sendNotification;

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent){
        String message = "You have received a connection request from user with id: %d"+sendConnectionRequestEvent.getSenderId();
        sendNotification.send(sendConnectionRequestEvent.getReceiverId(), message);
    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void acceptSendConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent){
        String message = "Your connection request has been accepted by the user with id: %d"+acceptConnectionRequestEvent.getReceiverId();
        sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message);
    }

}
