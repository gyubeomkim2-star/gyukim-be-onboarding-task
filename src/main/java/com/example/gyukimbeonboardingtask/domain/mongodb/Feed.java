package com.example.gyukimbeonboardingtask.domain.mongodb;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "feeds")
public class Feed {
    @Id
    private String id;
    private Long userId;
    private String postId;
    private Long createdAt;
}
