package org.example.statisticsservice.repository;

import org.example.modelproject.model.LogMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<LogMongo, Long> {

}
