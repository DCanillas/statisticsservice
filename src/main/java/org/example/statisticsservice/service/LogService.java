package org.example.statisticsservice.service;

import org.example.modelproject.dto.LogMongoDTO;

import java.sql.Date;
import java.util.List;

public interface LogService {
    List<LogMongoDTO> getAllLogs();
    List<LogMongoDTO> getLogsByDate(Date from, Date to);
    LogMongoDTO getLogById(Long id);
    LogMongoDTO saveLog(LogMongoDTO logMongo);
}
