package com.example.gyukimbeonboardingtask.domain.mongodb;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    private String authorId;
    private String title;
    private String content;
    private String imageUrl;
    private List<String> tags;

    private Long createdAt;

    private List<String> likes;
}
