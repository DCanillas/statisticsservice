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

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.modelproject.model.LogMongo.SEQUENCE_NAME;

@Slf4j
@Service
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final ModelMapper modelMapper;

    @Autowired
    public LogServiceImpl(LogRepository logRepository, SequenceGeneratorService sequenceGeneratorService, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.modelMapper = modelMapper;
    }

    @KafkaListener(topics = "requests", groupId= "requestGroup")
    public void consumeFromTopic(LogMongoDTO logMongoDTO){
        log.info("ConsumerServiceImpl - consumeFromTopic: "+logMongoDTO);
        LogMongo logMongo = modelMapper.map(logMongoDTO, LogMongo.class);
        //generate sequence
        logMongo.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        logRepository.save(logMongo);
    }

    @Override
    public List<LogMongoDTO> getAllLogs() {
        log.info("ConsumerServiceImpl - Method getAllLogs");
        List<LogMongoDTO> logsMongo = logRepository.findAll().stream()
                .map(logMongo -> modelMapper.map(logMongo, LogMongoDTO.class))
                .collect(Collectors.toList());
        log.info("ConsumerServiceImpl - Return getAllLogs: "+logsMongo);
        return logsMongo;
    }

    @Override
    public LogMongoDTO getLogById(Long id) {
        log.info("ConsumerServiceImpl - Method getById");
        LogMongo logMongo = logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Log not found for this id :: " + id));
        log.info("ConsumerServiceImpl - Return getById: "+logMongo);
        return modelMapper.map(logMongo, LogMongoDTO.class);
    }

    @Override
    public LogMongoDTO saveLog(LogMongoDTO logMongoDTO) {
        log.info("ConsumerServiceImpl - Method saveLog: "+logMongoDTO);
        LogMongo logMongo = modelMapper.map(logMongoDTO, LogMongo.class);
        //generate sequence
        logMongo.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        LogMongo logMongoCreated = logRepository.save(logMongo);
        log.info("ConsumerServiceImpl - Created saveLog: "+logMongoCreated);
        return modelMapper.map(logMongoCreated, LogMongoDTO.class);
    }

    @Override
    public List<LogMongoDTO> getLogsByDate(Date from, Date to) {
        log.info("ConsumerServiceImpl - Method getLogByDate");
        List<LogMongoDTO> logsMongo = logRepository.findAll().stream()
                .map(logMongo -> modelMapper.map(logMongo, LogMongoDTO.class))
                .collect(Collectors.toList());
        log.info("ConsumerServiceImpl - Logs found:  {}", logsMongo);

        List<LogMongoDTO> logsMongoInDate = new ArrayList<>();
        from = formatingDate(from, true);
        to = formatingDate(to, false);

        log.info("ConsumerServiceImpl - From: {}, - To: {}", from, to);
        for (LogMongoDTO logMongoDTO : logsMongo) {
            if (logMongoDTO.getTime().after(from) && logMongoDTO.getTime().before(to)){
                logsMongoInDate.add(logMongoDTO);
            }
        }
        log.info("ConsumerServiceImpl - Return getLogByDate: {}",logsMongoInDate);
        return logsMongoInDate;
    }

    private Date formatingDate(Date date, Boolean from) {
        if (date == null){
            date = new Date(System.currentTimeMillis());
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (from){
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
            } else {
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 59);
            }
            java.util.Date dateUtil = cal.getTime();
            date = new Date(dateUtil.getTime());
        }
        return date;
    }
}
