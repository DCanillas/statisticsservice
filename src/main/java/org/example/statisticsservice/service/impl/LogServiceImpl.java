package org.example.statisticsservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.example.modelproject.dto.LogMongoDTO;
import org.example.modelproject.model.LogMongo;
import org.example.statisticsservice.repository.LogRepository;
import org.example.statisticsservice.service.LogService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.modelproject.model.LogMongo.SEQUENCE_NAME;

@Slf4j
@Service
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public LogServiceImpl(LogRepository logRepository, SequenceGeneratorService sequenceGeneratorService) {
        this.logRepository = logRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @KafkaListener(topics = "orders", groupId= "orderGroup")
    public void consumeFromTopic(LogMongoDTO logMongoDTO){
        log.info("ConsumerServiceImpl - consumeFromTopic: "+logMongoDTO);
        LogMongo logMongo = new ModelMapper().map(logMongoDTO, LogMongo.class);
        //generate sequence
        logMongo.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        logRepository.save(logMongo);
    }

    @Override
    public List<LogMongoDTO> getAllLogs() {
        log.info("ConsumerServiceImpl - Method getAllLogs");
        /*
        List<LogMongoDTO> logsMongo = logRepository.findAll().stream()
                .map(logMongo -> new ModelMapper().map(logMongo, LogMongoDTO.class))
                .collect(Collectors.toList());

         */
        List<LogMongo> logsMongo = logRepository.findAll();
        List<LogMongoDTO> logsMongoDTO = new ArrayList<>();
        for (LogMongo logMongo : logsMongo) {
            log.info("LogMongo: "+logMongo);
            logsMongoDTO.add(new ModelMapper().map(logMongo, LogMongoDTO.class));
        }
        log.info("ConsumerServiceImpl - Return getAllLogs: "+logsMongo);
        return logsMongoDTO;
    }

    @Override
    public LogMongoDTO getLogById(Long id) {
        log.info("ConsumerServiceImpl - Method getById");
        LogMongo logMongo = logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Log not found for this id :: " + id));
        log.info("ConsumerServiceImpl - Return getById: "+logMongo);
        return new ModelMapper().map(logMongo, LogMongoDTO.class);
    }

    @Override
    public LogMongoDTO saveLog(LogMongoDTO logMongoDTO) {
        log.info("ConsumerServiceImpl - Method saveLog: "+logMongoDTO);
        LogMongo logMongo = new ModelMapper().map(logMongoDTO, LogMongo.class);
        //generate sequence
        logMongo.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        LogMongo logMongoCreated = logRepository.save(logMongo);
        log.info("ConsumerServiceImpl - Created saveLog: "+logMongoCreated);
        return new ModelMapper().map(logMongoCreated, LogMongoDTO.class);
    }
}
