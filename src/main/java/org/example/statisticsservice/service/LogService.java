package org.example.statisticsservice.service;

import org.example.modelproject.dto.LogMongoDTO;

import java.util.List;

public interface LogService {
    List<LogMongoDTO> getAllLogs();
    LogMongoDTO getLogById(Long id);
    LogMongoDTO saveLog(LogMongoDTO logMongo);
}
