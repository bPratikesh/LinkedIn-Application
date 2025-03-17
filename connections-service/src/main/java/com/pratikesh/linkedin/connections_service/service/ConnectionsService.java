package com.pratikesh.linkedin.connections_service.service;

import com.pratikesh.linkedin.connections_service.auth.UserContextHolder;
import com.pratikesh.linkedin.connections_service.entity.Person;
import com.pratikesh.linkedin.connections_service.repository.PersonRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsService {

    private final PersonRepo personRepo;

    public List<Person> getFirstDegreeConnections(){
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Getting first degree connections for user with id: {}", userId);
        return personRepo.getFirstDegreeConnections(userId);
    }
}
