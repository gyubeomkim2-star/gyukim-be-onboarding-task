package com.example.gyukimbeonboardingtask.service;

import com.example.gyukimbeonboardingtask.domain.mongodb.Post;
import com.example.gyukimbeonboardingtask.repository.mongodb.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MongoTemplate mongoTemplate;

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
        return false; // fail to delete
    }

    public boolean addLike(String postId, String userId) {
        Query query = Query.query(Criteria.where("id").is(postId));
        Update update = new Update().addToSet("likes", userId); // $addToSet은 중복 추가 방지

        Post updatedPost = mongoTemplate.findAndModify(query, update, Post.class);
        return updatedPost != null;
    }

    public boolean removeLike(String postId, String userId) {
        Query query = Query.query(Criteria.where("id").is(postId));
        Update update = new Update().pull("likes", userId); // $pull은 배열에서 특정 요소 제거

        Post updatedPost = mongoTemplate.findAndModify(query, update, Post.class);
        return updatedPost != null;
    }
}
