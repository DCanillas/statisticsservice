package org.example.statisticsservice.controller;

import org.example.modelproject.dto.LogMongoDTO;
import org.example.statisticsservice.service.impl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.version}/log")
public class LogController {
    private final LogServiceImpl consumerService;

    @Autowired
    public LogController(LogServiceImpl consumerService) {
        this.consumerService = consumerService;
    }

    // get all logs
    @GetMapping("")
    public ResponseEntity<List<LogMongoDTO>> getAllLogs(){
        return ResponseEntity.ok().body(consumerService.getAllLogs());
    }

    // get by id
    @GetMapping("/{id}")
    public ResponseEntity<LogMongoDTO> getLogById(@PathVariable(value = "id") long id){
        return ResponseEntity.ok().body(consumerService.getLogById(id));
    }

    // create log
    @PostMapping("")
    public ResponseEntity<LogMongoDTO> createCustomer(@RequestBody LogMongoDTO logMongoDTO){
        return ResponseEntity.ok().body(consumerService.saveLog(logMongoDTO));
    }
}
