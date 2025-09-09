package com.example.gyukimbeonboardingtask.service;

import com.example.gyukimbeonboardingtask.domain.mongodb.Post;
import com.example.gyukimbeonboardingtask.repository.mongodb.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByAuthorId(String authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    // update post is not supported

    public boolean deletePost(String id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true; // successful deletion
        }
        return false; // fail to dele
    }
}
