package org.example.statisticsservice.controller;

import org.example.modelproject.dto.LogMongoDTO;
import org.example.statisticsservice.service.impl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LogMongoDTO>> getAllLogs(){
        return ResponseEntity.ok().body(consumerService.getAllLogs());
    }

    // get by id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LogMongoDTO> getLogById(@PathVariable(value = "id") long id){
        return ResponseEntity.ok().body(consumerService.getLogById(id));
    }

    // get by date
    @GetMapping("/date/{from}/{to}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LogMongoDTO>> getLogsByDate(@PathVariable(value = "from") Date from,
                                                  @PathVariable(value = "to") Date to){
        return ResponseEntity.ok().body(consumerService.getLogsByDate(from, to));
    }

    // create log
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LogMongoDTO> createLog(@RequestBody LogMongoDTO logMongoDTO){
        return ResponseEntity.ok().body(consumerService.saveLog(logMongoDTO));
    }
}
