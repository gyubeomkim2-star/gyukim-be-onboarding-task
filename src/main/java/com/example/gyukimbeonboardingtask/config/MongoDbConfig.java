package com.example.gyukimbeonboardingtask.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.gyukimbeonboardingtask.repository.mongodb") // MongoDB 리포지토리 패키지
public class MongoDbConfig {

}
