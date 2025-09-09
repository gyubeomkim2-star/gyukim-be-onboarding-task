package com.example.gyukimbeonboardingtask.repository.mongodb;

import com.example.gyukimbeonboardingtask.domain.mongodb.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByAuthorId(String authorId);
}
