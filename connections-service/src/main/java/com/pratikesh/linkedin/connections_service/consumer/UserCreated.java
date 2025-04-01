package com.pratikesh.linkedin.connections_service.consumer;

import com.pratikesh.linkedin.connections_service.entity.Person;
import com.pratikesh.linkedin.connections_service.repository.PersonRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCreated {
    private final PersonRepo personRepo;

    @KafkaListener(topics = "user-created-topic")
    public  void handleUserCreatedEvent(UserCreatedEvent userCreatedEvent){

        Person person = new Person();
        person.setUserId(userCreatedEvent.get);
    }
}
