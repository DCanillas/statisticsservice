package org.example.statisticsservice.controller;

import org.example.modelproject.dto.LogMongoDTO;
import org.example.statisticsservice.service.impl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<LogMongoDTO> getAllLogs(){ return consumerService.getAllLogs();}

    // get by id
    @GetMapping("/{id}")
    public LogMongoDTO getLogById(@PathVariable(value = "id") long id){
        return consumerService.getLogById(id);
    }

    // create log
    @PostMapping("")
    public LogMongoDTO createCustomer(@RequestBody LogMongoDTO logMongoDTO){
        return consumerService.saveLog(logMongoDTO);
    }
}
