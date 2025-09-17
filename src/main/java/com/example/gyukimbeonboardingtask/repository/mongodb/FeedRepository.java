package com.example.gyukimbeonboardingtask.repository.mongodb;

import com.example.gyukimbeonboardingtask.domain.mongodb.Feed;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedRepository extends MongoRepository<Feed, String> {
}