package com.pratikesh.linkedin.connections_service.controller;

import com.pratikesh.linkedin.connections_service.entity.Person;
import com.pratikesh.linkedin.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @GetMapping("/firstConnections")
    public ResponseEntity<List<Person>> getFirstConnections(){
        return ResponseEntity.ok(connectionsService.getFirstDegreeConnections());
    }
}
