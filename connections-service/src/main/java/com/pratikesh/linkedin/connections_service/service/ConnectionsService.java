package com.pratikesh.linkedin.connections_service.service;

import com.pratikesh.linkedin.connections_service.auth.UserContextHolder;
import com.pratikesh.linkedin.connections_service.entity.Person;
import com.pratikesh.linkedin.connections_service.event.AcceptConnectionRequestEvent;
import com.pratikesh.linkedin.connections_service.event.SendConnectionRequestEvent;
import com.pratikesh.linkedin.connections_service.repository.PersonRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsService {
    private final PersonRepo personRepo;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendConnectionRequestEventKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptConnectionRequestEventKafkaTemplate;

    public List<Person> getFirstDegreeConnections(){
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Getting first degree connections for user with id: {}", userId);
        return personRepo.getFirstDegreeConnections(userId);
    }

    public Boolean sendConnectionRequest(Long receiverId){
        Long senderId = UserContextHolder.getCurrentUserId();
        log.info("Trying to send connection request, sender:{}, receiver:{}", senderId, receiverId);

        if (senderId.equals(receiverId)){
            throw new RuntimeException("Sender and receiver are the same, cannot proceed with this request");
        }

        boolean alreadySentRequest = personRepo.connectionRequestExists(senderId, receiverId);
        if (alreadySentRequest){
            throw new RuntimeException("Connection request already exists, cannot sent again");
        }

        boolean alreadyConnected = personRepo.alreadyConnected(senderId, receiverId);
        if (alreadyConnected){
            throw new RuntimeException("Already connected users, cannot add same user again");
        }

        personRepo.addConnectionRequest(senderId, receiverId);
        log.info("Successfully sent the connection request, sender:{}, receiver:{}", senderId, receiverId);
        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        sendConnectionRequestEventKafkaTemplate.send("send-connection-request-topic", sendConnectionRequestEvent);
        return true;
    }

    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExists = personRepo.connectionRequestExists(senderId, receiverId);
        if (!connectionRequestExists){
            throw new RuntimeException("No connection requests exists to accept");
        }
        personRepo.acceptConnectionRequest(senderId, receiverId);
        log.info("Successfully accepted the connection request, sender:{}, receiver:{}",senderId, receiverId);
        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        acceptConnectionRequestEventKafkaTemplate.send("accept-connection-request-topic", acceptConnectionRequestEvent);
        return true;
    }

    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExists = personRepo.connectionRequestExists(senderId, receiverId);
        if (!connectionRequestExists){
            throw new RuntimeException("No connection requests exists to reject");
        }
        personRepo.rejectConnectionRequest(senderId, receiverId);
        log.info("Successfully rejected the connection request");
        return true;
    }
}
